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
    void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(10);
        assertThat(players).hasSize(10);

        int minLevel = 1;
        long count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel).count();
        assertThat(count).isEqualTo(10);
        int minLevel2 = 9;
        count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel2).count();
        assertThat(count).isEqualTo(6);
        int minLevel3 = 10;
        count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel3).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testSingleUpgrade() {
        Attributes minimumAttributes = new Attributes(45, 30, 55, 15, 55, 40);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("LEO_9");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(300);
        configFilter.setMaxLevel(11);
        configFilter.setUpgradeAllowed(1);

        long start = System.currentTimeMillis();
        List<GameConfig> gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs%n", gameConfigs.size());
        System.out.printf("Found in %d ms%n", (end - start));
        assertThat(gameConfigs).hasSize(807);
        // 807 in 13638 ms
    }

    @Test
    void testMultipleUpgrades() {
        Attributes minimumAttributes = new Attributes(45, 30, 60, 15, 60, 40);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("LEO_9");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(300);
        configFilter.setMaxLevel(11);
        configFilter.setUpgradeAllowed(2);

        List<GameConfig> gameConfigs;

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs%n", gameConfigs.size());
        System.out.printf("Found in %d ms%n", (end - start));
        assertThat(gameConfigs).hasSize(539);
        // 539 in 29343 ms
    }
}