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
    void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(3);
        assertThat(players).hasSize(7);
        players = playerService.leveledPlayers(6);
        assertThat(players).hasSize(7);
    }

    @Test
    void testCostsAreNullForOwned() {
        Attributes minimumAttributes = new Attributes(20, 0, 30, 0, 30, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("FLORENCE_4", minimumAttributes, 150, 6, 0);
        gameConfigs.forEach(gameConfig -> assertThat(gameConfig.getCost()).isZero());
    }

    @Test
    void testSimpleUpgrades() {
        Attributes minimumAttributes = new Attributes(40, 30, 40, 15, 40, 40);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_7", minimumAttributes, 240, 9, 1);
        assertThat(gameConfigs).hasSize(168);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 129 in 1877 ms
    }

    @Test
    void testUpgrades() {
        Attributes minimumAttributes = new Attributes(40, 30, 40, 15, 40, 30);
        List<GameConfig> gameConfigs;

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_7", minimumAttributes, 200, 9, 2);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        assertThat(gameConfigs).hasSize(4578);
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 31860 in 3717 ms
    }
}