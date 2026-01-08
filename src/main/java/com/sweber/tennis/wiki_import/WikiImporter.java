package com.sweber.tennis.wiki_import;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WikiImporter {
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

    public void importPlayersData() throws IOException {
        System.out.println("Starting importing players data");
        bufferedWriter = new BufferedWriter(new FileWriter(IMPORTED_PLAYER_FILENAME));
        importPlayers();
        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(IMPORTED_PLAYER_FILENAME);
        System.out.println("Imported players data to " + IMPORTED_PLAYER_FILENAME);

        System.out.println("Saving changes into " + DELTA_PLAYER_FILENAME);
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
        String importedLine = null;
        try {
            importedLine = bufferedReader.readLine();
            if (!importedLine.equals(line)) {
                bufferedWriter.write(line + "\n");
                bufferedWriter.write(importedLine + "\n");
                bufferedWriter.write("=====\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importGearData() throws IOException {
        System.out.println("Starting importing gear data");
        bufferedWriter = new BufferedWriter(new FileWriter(IMPORTED_GEAR_FILENAME));
        bufferedWriter.write("Name,Type,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");

        // Aggregate all gear pages into one list so we can use a single shared WikiFetcher
        List<WikiPage> pages = new ArrayList<>();
        Arrays.stream(Rackets.values()).forEach(racket -> pages.add(new WikiPage(racket.getPage(), racket.name(), RACKET)));
        Arrays.stream(Grips.values()).forEach(grip -> pages.add(new WikiPage(grip.getPage(), grip.name(), GRIP)));
        Arrays.stream(Shoes.values()).forEach(shoes -> pages.add(new WikiPage(shoes.getPage(), shoes.name(), SHOES)));
        Arrays.stream(Wristbands.values()).forEach(wristband -> pages.add(new WikiPage(wristband.getPage(), wristband.name(), WRISTBAND)));
        Arrays.stream(Nutritions.values()).forEach(nutrition -> pages.add(new WikiPage(nutrition.getPage(), nutrition.name(), NUTRITION)));
        Arrays.stream(Workouts.values()).forEach(workout -> pages.add(new WikiPage(workout.getPage(), workout.name(), WORKOUT)));

        // Use a single shared fetcher for all gear pages to warm the connection pool and improve performance
        WikiFetcher fetcher = new WikiFetcher(20, 4);
        // Collect results in a concurrent map keyed by page URL so we can write them in a deterministic order
        Map<String, String> results = new ConcurrentHashMap<>();
        try {
            fetcher.fetchAndProcessAll(pages, result -> {
                if (result.error != null) {
                    System.err.println("Error fetching page " + result.page.getUrl() + ": " + result.error.getMessage());
                }
                if (result.content != null) {
                    results.put(result.page.getUrl(), result.content);
                } else {
                    // Put empty string to preserve ordering even if fetch failed
                    results.put(result.page.getUrl(), "");
                }
            });

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
            fetcher.shutdown();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(IMPORTED_GEAR_FILENAME);
        System.out.println("Imported gear data to " + IMPORTED_GEAR_FILENAME);

        System.out.println("Saving changes into " + DELTA_GEAR_FILENAME);
        bufferedWriter = new BufferedWriter(new FileWriter(DELTA_GEAR_FILENAME));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(GEAR_FILENAME));
             BufferedReader bufferedReaderImported = new BufferedReader(new FileReader(IMPORTED_GEAR_FILENAME))) {
            bufferedReader.lines()
                    .forEach(line -> compareLine(bufferedWriter, bufferedReaderImported, line));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void removeLastEmptyline(String fileName) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(fileName, "rw")) {
            long length = f.length() - 2;
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
        WikiFetcher fetcher = new WikiFetcher(20, 4);
        Map<String, String> results = new ConcurrentHashMap<>();
        try {
            fetcher.fetchAndProcessAll(pages, result -> {
                if (result.error != null) {
                    System.err.println("Error fetching page " + result.page.getUrl() + ": " + result.error.getMessage());
                }
                if (result.content != null) {
                    results.put(result.page.getUrl(), result.content);
                } else {
                    results.put(result.page.getUrl(), "");
                }
            });

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
            fetcher.shutdown();
        }
    }

    private void executeAndWriteTasks(List<Callable<String>> tasks) {
        ExecutorService executor = null;
        try {
            // Use virtual threads if available (Java 21+). Fall back to cached thread pool otherwise.
            try {
                // Use reflection so we can compile with older JDKs (e.g. 17) while still
                // taking advantage of virtual threads when running on newer JDKs.
                java.lang.reflect.Method m = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
                executor = (ExecutorService) m.invoke(null);
            } catch (Throwable t) {
                // Method not available or invocation failed -> fall back to cached pool
                executor = Executors.newCachedThreadPool();
            }
            List<Future<String>> futures = executor.invokeAll(tasks);
            for (Future<String> future : futures) {
                try {
                    String content = future.get();
                    synchronized (bufferedWriter) {
                        bufferedWriter.write(content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    private void processWikiPage(String page, String itemName, String itemType) {
        try {
            WikiPage wikiPage = new WikiPage(page, itemName, itemType);
            StringBuilder stringBuffer = wikiPage.processWikiPage();
            //write contents of StringBuffer to a file
            bufferedWriter.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replaceDatasetWithImport() {
        System.out.println("Updating data with import files");
        try {
            Files.move(Paths.get(IMPORTED_PLAYER_FILENAME), Paths.get(PLAYER_FILENAME), StandardCopyOption.REPLACE_EXISTING);
            Files.move(Paths.get(IMPORTED_GEAR_FILENAME), Paths.get(GEAR_FILENAME), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}