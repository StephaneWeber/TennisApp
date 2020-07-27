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
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", gameConfigs.size(), (end - start)));
        // 140 - 79ms
        assertThat(gameConfigs).hasSize(140);
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
        assertThat(gameConfigs).hasSize(140);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 1);
        assertThat(gameConfigs).hasSize(759);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 2);
        assertThat(gameConfigs).hasSize(1997);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 3);
        assertThat(gameConfigs).hasSize(3229);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 4);
        assertThat(gameConfigs).hasSize(3915);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 5);
        assertThat(gameConfigs).hasSize(4125);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        gameConfigs = configGeneratorService.generateAllConfigs("JONAH_4", minimumAttributes, 160, 6, 6);
        assertThat(gameConfigs).hasSize(4152);
        System.out.println(String.format("Found %d configs", gameConfigs.size()));
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found in %d ms", (end - start)));
        // in 1912ms
    }
}