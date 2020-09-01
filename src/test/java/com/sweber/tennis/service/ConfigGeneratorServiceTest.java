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
    void testCostsAreNullForOwned() {
        long start = System.currentTimeMillis();
        Attributes minimumAttributes = new Attributes(20, 0, 30, 0, 30, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("FLORENCE_4", minimumAttributes, 150, 6, 0);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs in %d ms%n", gameConfigs.size(), (end - start));
        // 127 - 118 ms
        assertThat(gameConfigs).hasSize(127);
        gameConfigs.forEach(gameConfig -> assertThat(gameConfig.getCost()).isZero());
    }

    @Test
    void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(3);
        assertThat(players).hasSize(5);
        players = playerService.leveledPlayers(4);
        assertThat(players).hasSize(5);
    }

    @Test
    void testSimpleUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 20, 20, 20, 20, 20);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_5", minimumAttributes, 185, 6, 1);
        assertThat(gameConfigs).hasSize(50);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 50 in 666 ms
    }

    @Test
    void testUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 0, 20, 15, 30, 20);
        List<GameConfig> gameConfigs;

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_5", minimumAttributes, 160, 9, 0);
        System.out.printf("Found %d configs%n", gameConfigs.size());
//        assertThat(gameConfigs).hasSize(288);
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_5", minimumAttributes, 160, 9, 1);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        assertThat(gameConfigs).hasSize(10271);
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 10271 in 828 ms
    }
}