package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.player.Player;
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
    @Autowired
    private PlayerService playerService;

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 140, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", gameConfigs.size(), (end - start)));
        // 553 - 61ms
        assertThat(gameConfigs).hasSize(553);
    }

    @Test
    public void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(3);
        assertThat(players).hasSize(3);
        players = playerService.leveledPlayers(4);
        assertThat(players).hasSize(3);
    }

    @Test
    public void testUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 0);
        assertThat(gameConfigs).hasSize(73);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 1);
        assertThat(gameConfigs).hasSize(392);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 2);
        assertThat(gameConfigs).hasSize(975);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 3);
        assertThat(gameConfigs).hasSize(1487);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 4);
        assertThat(gameConfigs).hasSize(1746);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 5);
        assertThat(gameConfigs).hasSize(1822);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 6);
        assertThat(gameConfigs).hasSize(1833);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found in %d ms", (end - start)));
        // 73-392-975-1487-1746-1822-1833 in 1416ms
    }
}