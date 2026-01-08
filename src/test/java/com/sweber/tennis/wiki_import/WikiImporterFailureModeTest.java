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
        // Inject fetcher which fails for first page
        WikiFetcher f = new PartialFailFetcher(List.of("https://tennis-clash.fandom.com/wiki/SomePlayer"));
        WikiImporter importer = new WikiImporter(f, WikiImporter.FetchFailureMode.FAIL_FAST);

        // Run importPlayersData and expect IOException
        Path tempPlayers = Path.of("src/main/resources/data/imported_players.csv.tmp");
        Files.deleteIfExists(tempPlayers);

        assertThrows(IOException.class, importer::importPlayersData);
        // ensure temp file cleaned
        assertFalse(Files.exists(tempPlayers), "Temp players file should be deleted on FAIL_FAST");
    }

    @Test
    public void testTolerateWritesFiles() throws IOException {
        WikiFetcher f = new PartialFailFetcher(List.of("https://tennis-clash.fandom.com/wiki/NonExisting"));
        WikiImporter importer = new WikiImporter(f, WikiImporter.FetchFailureMode.TOLERATE);

        // Run both imports; should not throw
        importer.importPlayersData();
        importer.importGearData();

        Path players = Path.of("src/main/resources/data/imported_players.csv");
        Path gear = Path.of("src/main/resources/data/imported_gear.csv");

        assertTrue(Files.exists(players), "Imported players file should exist when TOLERATE");
        assertTrue(Files.exists(gear), "Imported gear file should exist when TOLERATE");

        String playersContent = Files.readString(players);
        assertTrue(playersContent.contains("#OK#"), "Players content should include OK markers for successful pages");
    }
}
