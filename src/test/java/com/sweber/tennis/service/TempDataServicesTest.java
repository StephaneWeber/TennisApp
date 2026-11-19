package com.sweber.tennis.service;

import com.sweber.tennis.config.CsvProperties;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

class TempDataServicesTest {
    @Test
    void servicesLoadFromTempDir() throws IOException {
        Path tempDir = Files.createTempDirectory("tennis-data-test");
        try {
            // copy data files from src/main/resources/data
            Path repoData = Path.of("src/main/resources/data");
            for (String f : Arrays.asList("players.csv", "owned_players.csv", "gear.csv", "owned_gear.csv")) {
                Files.copy(repoData.resolve(f), tempDir.resolve(f), StandardCopyOption.REPLACE_EXISTING);
            }

            CsvProperties csvProps = TestCsvPropertiesFactory.fromPaths(tempDir.resolve("players.csv"), tempDir.resolve("owned_players.csv"), tempDir.resolve("gear.csv"), tempDir.resolve("owned_gear.csv"));

            PlayerService ps = new PlayerService(csvProps);
            GearItemService gs = new GearItemService(csvProps);

            assertThat(ps.leveledPlayers(15)).isNotEmpty();
            assertThat(gs.leveledGearItems(1, 0)).isNotEmpty();
        } finally {
            // recursive delete the temp dir
            try {
                Files.walk(tempDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (Exception ignored) {
                            }
                        });
            } catch (Exception ignored) {
            }
        }
    }
}
