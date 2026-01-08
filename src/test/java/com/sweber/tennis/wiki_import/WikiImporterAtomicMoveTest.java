package com.sweber.tennis.wiki_import;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WikiImporterAtomicMoveTest {
    static class SimpleFetcher extends WikiFetcher {
        public SimpleFetcher() { super(1,1); }
        @Override
        public void fetchAndProcessAll(List<WikiPage> pages, java.util.function.Consumer<WikiFetcher.WikiPageResult> resultConsumer) {
            for (WikiPage p : pages) {
                resultConsumer.accept(new WikiFetcher.WikiPageResult(p, "#OK#" + p.getUrl() + "\n", null));
            }
        }
    }

    @Test
    public void testAtomicMoveLeavesFinalAndRemovesTemp() throws IOException {
        // Create temporary data directory and set system property so importer uses it
        Path tempDir = Files.createTempDirectory("tennis-data-");
        System.setProperty("tennis.data.dir", tempDir.toString());

        // Copy baseline source files into temp dir so importer can read them
        Files.copy(Path.of("src/main/resources/data/players.csv"), tempDir.resolve("players.csv"));
        Files.copy(Path.of("src/main/resources/data/gear.csv"), tempDir.resolve("gear.csv"));

        // Run importer with fake fetcher to avoid network and ensure success
        WikiFetcher f = new SimpleFetcher();
        WikiImporter importer = new WikiImporter(f, WikiImporter.FetchFailureMode.TOLERATE);

        // Ensure no temp files exist before
        Path tempPlayers = tempDir.resolve("imported_players.csv.tmp");
        Path tempGear = tempDir.resolve("imported_gear.csv.tmp");
        Files.deleteIfExists(tempPlayers);
        Files.deleteIfExists(tempGear);

        // Run imports
        importer.importPlayersData();
        importer.importGearData();

        // Temp files should not exist, final files should
        assertFalse(Files.exists(tempPlayers));
        assertFalse(Files.exists(tempGear));

        Path players = tempDir.resolve("imported_players.csv");
        Path gear = tempDir.resolve("imported_gear.csv");

        assertTrue(Files.exists(players));
        assertTrue(Files.exists(gear));

        // Verify contents contain the success marker written by our SimpleFetcher
        String playersContent = Files.readString(players);
        String gearContent = Files.readString(gear);
        assertTrue(playersContent.contains("#OK#"), "Players file should contain OK markers");
        assertTrue(gearContent.contains("#OK#"), "Gear file should contain OK markers");

        // Cleanup system property
        System.clearProperty("tennis.data.dir");
    }
}
