package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.player.Player;
import com.sweber.tennis.web.model.ConfigFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        assertThat(players).hasSize(9);
        players = playerService.leveledPlayers(6);
        assertThat(players).hasSize(9);
    }

    @Test
    void testCostsAreNullForOwned() {
        Attributes minimumAttributes = new Attributes(20, 0, 30, 0, 30, 20);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("FLORENCE_4");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(150);
        configFilter.setMaxLevel(6);
        List<GameConfig> gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        gameConfigs.forEach(gameConfig -> assertThat(gameConfig.getCost()).isZero());
    }

    @Test
    void testSimpleUpgrades() {
        Attributes minimumAttributes = new Attributes(40, 30, 55, 15, 40, 40);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("LEO_8");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(300);
        configFilter.setMaxLevel(11);
        configFilter.setUpgradeAllowed(1);

        long start = System.currentTimeMillis();
        List<GameConfig> gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        assertThat(gameConfigs).hasSize(23);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 23 in 5594 ms
    }

    @Test
    void testUpgrades() {
        Attributes minimumAttributes = new Attributes(40, 30, 40, 15, 40, 30);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("LEO_8");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(300);
        configFilter.setMaxLevel(11);
        configFilter.setUpgradeAllowed(2);

        List<GameConfig> gameConfigs;

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        System.out.printf("Found %d configs%n", gameConfigs.size());
        assertThat(gameConfigs).hasSize(91);
        long end = System.currentTimeMillis();
        System.out.printf("Found in %d ms%n", (end - start));
        // 68 in 11228 ms
    }
}