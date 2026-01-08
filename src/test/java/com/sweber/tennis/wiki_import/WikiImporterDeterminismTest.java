package com.sweber.tennis.wiki_import;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WikiImporterDeterminismTest {
    static class FakeFetcher extends WikiFetcher {
        public FakeFetcher() {
            super(1, 1);
        }

        @Override
        public void fetchAndProcessAll(List<WikiPage> pages, java.util.function.Consumer<WikiFetcher.WikiPageResult> resultConsumer) {
            for (WikiPage p : pages) {
                // Generate deterministic content based on the page suffix
                String content = "#FAKE#" + p.getUrl() + "\n";
                resultConsumer.accept(new WikiFetcher.WikiPageResult(p, content, null));
            }
        }
    }

    @Test
    public void importerProducesDeterministicFiles() throws IOException {
        // Create temporary data directory and set system property so importer uses it
        Path tempDir = Files.createTempDirectory("tennis-data-");
        System.setProperty("tennis.data.dir", tempDir.toString());

        // Copy baseline source files into temp dir so importer can read them
        Files.copy(Path.of("src/main/resources/data/players.csv"), tempDir.resolve("players.csv"));
        Files.copy(Path.of("src/main/resources/data/gear.csv"), tempDir.resolve("gear.csv"));

        // Inject fake fetcher to avoid network in tests
        WikiImporter importer = new WikiImporter(new FakeFetcher());

        // Run import once
        importer.importPlayersData();
        importer.importGearData();

        Path importedPlayers = tempDir.resolve("imported_players.csv");
        Path importedGear = tempDir.resolve("imported_gear.csv");

        Path tempPlayers = Files.createTempFile("imported_players", ".csv");
        Path tempGear = Files.createTempFile("imported_gear", ".csv");

        Files.copy(importedPlayers, tempPlayers, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(importedGear, tempGear, StandardCopyOption.REPLACE_EXISTING);

        // Run import again
        importer.importPlayersData();
        importer.importGearData();

        String firstPlayers = Files.readString(tempPlayers);
        String secondPlayers = Files.readString(importedPlayers);

        String firstGear = Files.readString(tempGear);
        String secondGear = Files.readString(importedGear);

        assertEquals(firstPlayers, secondPlayers, "Imported players file should be identical across runs");
        assertEquals(firstGear, secondGear, "Imported gear file should be identical across runs");

        // Cleanup system property
        System.clearProperty("tennis.data.dir");
    }
}
