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
    public void testCostsAreNullForOwned() {
        long start = System.currentTimeMillis();
        Attributes minimumAttributes = new Attributes(20, 0, 30, 0, 30, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("FLORENCE_4", minimumAttributes, 150, 6, 0);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs in %d ms%n", gameConfigs.size(), (end - start));
        // 48 - 58 ms
        assertThat(gameConfigs).hasSize(48);
        gameConfigs.forEach(gameConfig -> assertThat(gameConfig.getCost()).isZero());
    }

    @Test
    public void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(3);
        assertThat(players).hasSize(4);
        players = playerService.leveledPlayers(4);
        assertThat(players).hasSize(4);
    }

    @Test
    public void testSimpleUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 170, 6, 1);
        assertThat(gameConfigs).hasSize(416);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 416 in 1106 ms
    }

    @Test
    public void testUpgrades() {
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = new ArrayList<>();

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 0);
//        assertThat(gameConfigs).hasSize(288);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 1);
//        assertThat(gameConfigs).hasSize(1539);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 2);
//        assertThat(gameConfigs).hasSize(3648);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 3);
//        assertThat(gameConfigs).hasSize(5396);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 4);
//        assertThat(gameConfigs).hasSize(6103);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 5);
//        assertThat(gameConfigs).hasSize(6232);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 6);
//        assertThat(gameConfigs).hasSize(6239);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // in 6166ms
    }
}