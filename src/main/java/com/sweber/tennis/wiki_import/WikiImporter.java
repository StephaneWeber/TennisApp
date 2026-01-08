package com.sweber.tennis.wiki_import;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WikiImporter {
    private static final Logger LOG = LoggerFactory.getLogger(WikiImporter.class);

    public enum FetchFailureMode {
        FAIL_FAST,
        TOLERATE
    }

    private static final String RACKET = "RACKET";
    private static final String GRIP = "GRIP";
    private static final String SHOES = "SHOES";
    private static final String WRISTBAND = "WRISTBAND";
    private static final String NUTRITION = "NUTRITION";
    private static final String WORKOUT = "WORKOUT";

    private static final String GEAR_FILENAME = "src/main/resources/data/gear.csv";
    private static final String IMPORTED_GEAR_FILENAME = "src/main/resources/data/imported_gear.csv";
    private static final String DELTA_GEAR_FILENAME = "src/main/resources/data/changed_gear.csv";
    private static final String PLAYER_FILENAME = "src/main/resources/data/players.csv";
    private static final String IMPORTED_PLAYER_FILENAME = "src/main/resources/data/imported_players.csv";
    private static final String DELTA_PLAYER_FILENAME = "src/main/resources/data/changed_players.csv";

    private BufferedWriter bufferedWriter;
    private final WikiFetcher injectedFetcher;
    private final FetchFailureMode fetchFailureMode;

    // Package-visible constructor for tests to inject a fake fetcher
    public WikiImporter(WikiFetcher fetcher) {
        this.injectedFetcher = fetcher;
        this.fetchFailureMode = FetchFailureMode.TOLERATE;
    }

    // Public constructor to configure failure mode while using default fetcher
    public WikiImporter(FetchFailureMode mode) {
        this.injectedFetcher = null;
        this.fetchFailureMode = mode != null ? mode : FetchFailureMode.TOLERATE;
    }

    // Package-visible constructor to inject fetcher and configure failure mode (useful for tests)
    public WikiImporter(WikiFetcher fetcher, FetchFailureMode mode) {
        this.injectedFetcher = fetcher;
        this.fetchFailureMode = mode != null ? mode : FetchFailureMode.TOLERATE;
    }

    public void importPlayersData() throws IOException {
        LOG.info("Starting importing players data");

        Path tempPlayers = Paths.get(IMPORTED_PLAYER_FILENAME + ".tmp");
        // ensure previous temp removed
        Files.deleteIfExists(tempPlayers);
        bufferedWriter = Files.newBufferedWriter(tempPlayers);

        try {
            importPlayers();
            bufferedWriter.flush();
            bufferedWriter.close();
            // remove last newline from temp file then atomically move into place
            removeLastEmptyline(tempPlayers.toString());
            moveTempToFinal(tempPlayers, Paths.get(IMPORTED_PLAYER_FILENAME));
        } catch (IOException e) {
            // cleanup temp file on failure
            try {
                bufferedWriter.close();
            } catch (Exception ex) {
                LOG.debug("Error closing buffer during failure cleanup", ex);
            }
            Files.deleteIfExists(tempPlayers);
            throw e;
        }

        LOG.info("Imported players data to " + IMPORTED_PLAYER_FILENAME);

        LOG.info("Saving changes into " + DELTA_PLAYER_FILENAME);
        bufferedWriter = new BufferedWriter(new FileWriter(DELTA_PLAYER_FILENAME));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PLAYER_FILENAME));
             BufferedReader bufferedReaderImported = new BufferedReader(new FileReader(IMPORTED_PLAYER_FILENAME))) {
            bufferedReader.lines()
                    .forEach(line -> compareLine(bufferedWriter, bufferedReaderImported, line));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void compareLine(BufferedWriter bufferedWriter, BufferedReader bufferedReader, String line) {
        try {
            String importedLine = bufferedReader.readLine();
            if (importedLine == null || !Objects.equals(importedLine, line)) {
                bufferedWriter.write(line + "\n");
                bufferedWriter.write(Objects.toString(importedLine, "") + "\n");
                bufferedWriter.write("=====\n");
            }
        } catch (IOException e) {
            LOG.warn("Error comparing lines", e);
        }
    }

    public void importGearData() throws IOException {
        LOG.info("Starting importing gear data");

        Path tempGear = Paths.get(IMPORTED_GEAR_FILENAME + ".tmp");
        Files.deleteIfExists(tempGear);
        bufferedWriter = Files.newBufferedWriter(tempGear);
        bufferedWriter.write("Name,Type,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");

        // Aggregate all gear pages into one list so we can use a single shared WikiFetcher
        List<WikiPage> pages = new ArrayList<>();
        Arrays.stream(Rackets.values()).forEach(racket -> pages.add(new WikiPage(racket.getPage(), racket.name(), RACKET)));
        Arrays.stream(Grips.values()).forEach(grip -> pages.add(new WikiPage(grip.getPage(), grip.name(), GRIP)));
        Arrays.stream(Shoes.values()).forEach(shoes -> pages.add(new WikiPage(shoes.getPage(), shoes.name(), SHOES)));
        Arrays.stream(Wristbands.values()).forEach(wristband -> pages.add(new WikiPage(wristband.getPage(), wristband.name(), WRISTBAND)));
        Arrays.stream(Nutritions.values()).forEach(nutrition -> pages.add(new WikiPage(nutrition.getPage(), nutrition.name(), NUTRITION)));
        Arrays.stream(Workouts.values()).forEach(workout -> pages.add(new WikiPage(workout.getPage(), workout.name(), WORKOUT)));

        WikiFetcher fetcher = injectedFetcher != null ? injectedFetcher : new WikiFetcher(20, 4);
        boolean shutdownFetcher = injectedFetcher == null;

        // Collect results in a concurrent map keyed by page URL so we can write them in a deterministic order
        Map<String, String> results = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> errors = new ConcurrentLinkedQueue<>();
        try {
            fetcher.fetchAndProcessAll(pages, result -> {
                if (result.error != null) {
                    LOG.warn("Error fetching page {}", result.page.getUrl(), result.error);
                    errors.add(result.page.getUrl() + ": " + result.error.getMessage());
                }
                results.put(result.page.getUrl(), Objects.requireNonNullElse(result.content, ""));
            });

            // If configured to fail fast and we saw errors, abort and clean up
            if (fetchFailureMode == FetchFailureMode.FAIL_FAST && !errors.isEmpty()) {
                LOG.warn("Fetch failures detected and configured to FAIL_FAST: {}", errors.size());
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    LOG.debug("Error closing buffered writer during failure cleanup", e);
                }
                Files.deleteIfExists(tempGear);

                StringBuilder msg = new StringBuilder("Fetch failures:\n");
                int i = 0;
                for (String s : errors) {
                    if (i++ > 10) break;
                    msg.append(s).append('\n');
                }
                throw new IOException(msg.toString());
            }

            // Write results in the original deterministic pages order
            for (WikiPage p : pages) {
                String content = results.get(p.getUrl());
                if (content != null && !content.isEmpty()) {
                    bufferedWriter.write(content);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (shutdownFetcher) {
                fetcher.shutdown();
            }
        }

        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(tempGear.toString());
        moveTempToFinal(tempGear, Paths.get(IMPORTED_GEAR_FILENAME));
        LOG.info("Imported gear data to " + IMPORTED_GEAR_FILENAME);

        LOG.info("Saving changes into " + DELTA_GEAR_FILENAME);
        bufferedWriter = new BufferedWriter(new FileWriter(DELTA_GEAR_FILENAME));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(GEAR_FILENAME));
             BufferedReader bufferedReaderImported = new BufferedReader(new FileReader(IMPORTED_GEAR_FILENAME))) {
            bufferedReader.lines()
                    .forEach(line -> compareLine(bufferedWriter, bufferedReaderImported, line));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void moveTempToFinal(Path temp, Path target) throws IOException {
        try {
            // Try atomic move, fall back to replace if unsupported
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | UnsupportedOperationException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void removeLastEmptyline(String fileName) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(fileName, "rw")) {
            long length = f.length() - 2;
            if (length < 0) return;
            f.seek(length);
            f.readByte();
            f.setLength(length + 1);
        }
    }

    private void importPlayers() throws IOException {
        bufferedWriter.write("Player,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");
        List<WikiPage> pages = new ArrayList<>();
        Arrays.stream(Players.values()).forEach(player -> pages.add(new WikiPage(player.getPage(), player.name(), null)));
        fetchAndWritePages(pages);
    }

    // New method: fetch pages in parallel using WikiFetcher and collect results, then write in deterministic order
    private void fetchAndWritePages(List<WikiPage> pages) throws IOException {
        WikiFetcher fetcher = injectedFetcher != null ? injectedFetcher : new WikiFetcher(20, 4);
        boolean shutdownFetcher = injectedFetcher == null;
        Map<String, String> results = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> errors = new ConcurrentLinkedQueue<>();
        try {
            fetcher.fetchAndProcessAll(pages, result -> {
                if (result.error != null) {
                    LOG.warn("Error fetching page {}", result.page.getUrl(), result.error);
                    errors.add(result.page.getUrl() + ": " + result.error.getMessage());
                }
                results.put(result.page.getUrl(), Objects.requireNonNullElse(result.content, ""));
            });

            if (fetchFailureMode == FetchFailureMode.FAIL_FAST && !errors.isEmpty()) {
                LOG.warn("Fetch failures detected and configured to FAIL_FAST: {}", errors.size());
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    LOG.debug("Error closing buffered writer during failure cleanup", e);
                }
                Files.deleteIfExists(Paths.get(IMPORTED_PLAYER_FILENAME));
                StringBuilder msg = new StringBuilder("Fetch failures:\n");
                int i = 0;
                for (String s : errors) {
                    if (i++ > 10) break;
                    msg.append(s).append('\n');
                }
                throw new IOException(msg.toString());
            }

            // Write in pages order so output is deterministic
            for (WikiPage p : pages) {
                String content = results.get(p.getUrl());
                if (content != null && !content.isEmpty()) {
                    bufferedWriter.write(content);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (shutdownFetcher) {
                fetcher.shutdown();
            }
        }
    }

    public void replaceDatasetWithImport() {
        LOG.info("Updating data with import files");
        try {
            Files.move(Paths.get(IMPORTED_PLAYER_FILENAME), Paths.get(PLAYER_FILENAME), StandardCopyOption.REPLACE_EXISTING);
            Files.move(Paths.get(IMPORTED_GEAR_FILENAME), Paths.get(GEAR_FILENAME), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOG.warn("Error replacing dataset files", e);
        }
    }
}

