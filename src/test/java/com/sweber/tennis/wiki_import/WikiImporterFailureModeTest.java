package com.sweber.tennis.wiki_import;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WikiImporterFailureModeTest {
    static class PartialFailFetcher extends WikiFetcher {
        private final List<String> failUrls;
        public PartialFailFetcher(List<String> failUrls) {
            super(1,1);
            this.failUrls = failUrls;
        }

        @Override
        public void fetchAndProcessAll(List<WikiPage> pages, java.util.function.Consumer<WikiFetcher.WikiPageResult> resultConsumer) {
            for (WikiPage p : pages) {
                if (failUrls.contains(p.getUrl())) {
                    resultConsumer.accept(new WikiFetcher.WikiPageResult(p, null, new IOException("simulated")));
                } else {
                    resultConsumer.accept(new WikiFetcher.WikiPageResult(p, "#OK#" + p.getUrl() + "\n", null));
                }
            }
        }
    }

    @Test
    public void testFailFastDeletesPartial() throws IOException {
        // Create temporary data directory and set system property so importer uses it
        Path tempDir = Files.createTempDirectory("tennis-data-");
        System.setProperty("tennis.data.dir", tempDir.toString());
        // Copy baseline source files into temp dir so importer can read them
        Files.copy(Path.of("src/main/resources/data/players.csv"), tempDir.resolve("players.csv"));
        Files.copy(Path.of("src/main/resources/data/gear.csv"), tempDir.resolve("gear.csv"));

        // Inject fetcher which fails for first player (Jonah)
        String jonahUrl = "https://tennis-clash.fandom.com/wiki/" + Players.JONAH.getPage();
        WikiFetcher f = new PartialFailFetcher(List.of(jonahUrl));
        WikiImporter importer = new WikiImporter(f, WikiImporter.FetchFailureMode.FAIL_FAST);

        // Run importPlayersData and expect IOException
        Path tempPlayers = tempDir.resolve("imported_players.csv.tmp");
        Files.deleteIfExists(tempPlayers);

        assertThrows(IOException.class, importer::importPlayersData);
        // ensure temp file cleaned
        assertFalse(Files.exists(tempPlayers), "Temp players file should be deleted on FAIL_FAST");

        // Cleanup system property
        System.clearProperty("tennis.data.dir");
    }

    @Test
    public void testTolerateWritesFiles() throws IOException {
        // Create temporary data directory and set system property so importer uses it
        Path tempDir = Files.createTempDirectory("tennis-data-");
        System.setProperty("tennis.data.dir", tempDir.toString());
        // Copy baseline source files into temp dir so importer can read them
        Files.copy(Path.of("src/main/resources/data/players.csv"), tempDir.resolve("players.csv"));
        Files.copy(Path.of("src/main/resources/data/gear.csv"), tempDir.resolve("gear.csv"));

        WikiFetcher f = new PartialFailFetcher(List.of("https://tennis-clash.fandom.com/wiki/NonExisting"));
        WikiImporter importer = new WikiImporter(f, WikiImporter.FetchFailureMode.TOLERATE);

        // Run both imports; should not throw
        importer.importPlayersData();
        importer.importGearData();

        Path players = tempDir.resolve("imported_players.csv");
        Path gear = tempDir.resolve("imported_gear.csv");

        assertTrue(Files.exists(players), "Imported players file should exist when TOLERATE");
        assertTrue(Files.exists(gear), "Imported gear file should exist when TOLERATE");

        String playersContent = Files.readString(players);
        assertTrue(playersContent.contains("#OK#") || playersContent.contains("#FAKE#"), "Players content should include OK markers for successful pages");

        // Cleanup system property
        System.clearProperty("tennis.data.dir");
    }
}
