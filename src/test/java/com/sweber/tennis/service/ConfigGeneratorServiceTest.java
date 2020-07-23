package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConfigGeneratorServiceTest {
    @Autowired
    private ConfigGeneratorService configGeneratorService;

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 140, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", gameConfigs.size(), (end - start)));
        // 525 - 51
        assertThat(gameConfigs).hasSize(525);
    }

    @Test
    public void testUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 0);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 1);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 2);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 3);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 4);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 5);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 6);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found in %d ms", (end - start)));
        // 23-147-408-684-820-852-858 in 844ms
    }
}