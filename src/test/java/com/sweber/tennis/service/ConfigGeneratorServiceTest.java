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
        configFilter.setSelectedPlayer("FLORENCE_11");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(230);
        configFilter.setMaxLevel(6);
        List<GameConfig> gameConfigs = configGeneratorService.generateFilteredGameConfigs(configFilter);
        gameConfigs.forEach(gameConfig -> assertThat(gameConfig.getCost()).isZero());
    }

    @Test
    void testPlayerService() {
        List<Player> players = playerService.leveledPlayers(15);
        assertThat(players).hasSize(17);

        int minLevel = 12;
        long count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel).count();
        assertThat(count).isEqualTo(13);
        int minLevel2 = 14;
        count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel2).count();
        assertThat(count).isEqualTo(8);
        int minLevel3 = 15;
        count = players.stream().map(playerService::ownedLevel).filter(ownedLevel -> ownedLevel >= minLevel3).count();
        assertThat(count).isEqualTo(5);
    }

    @Test
    void testSingleUpgrade() {
        Attributes minimumAttributes = new Attributes(60, 30, 25, 25, 65, 65);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("KAITO_11");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(320);
        configFilter.setMaxLevel(12);
        configFilter.setUpgradeAllowed(1);

        long start = System.currentTimeMillis();
        List<GameConfig> gameConfigs = configGeneratorService.generateFilteredGameConfigs(configFilter);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs%n", gameConfigs.size());
        System.out.printf("Found in %d ms%n", (end - start));
        assertThat(gameConfigs).hasSize(10970);
        // 3333 in 8148 ms
    }

    @Test
    void testMultipleUpgrades() {
        Attributes minimumAttributes = new Attributes(60, 30, 25, 25, 65, 65);
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setSelectedPlayer("KAITO_11");
        configFilter.setMinAttributes(minimumAttributes);
        configFilter.setMinTotal(320);
        configFilter.setMaxLevel(11);
        configFilter.setUpgradeAllowed(2);

        List<GameConfig> gameConfigs;

        long start = System.currentTimeMillis();
        gameConfigs = configGeneratorService.generateFilteredGameConfigs(configFilter);
        long end = System.currentTimeMillis();
        System.out.printf("Found %d configs%n", gameConfigs.size());
        System.out.printf("Found in %d ms%n", (end - start));
        assertThat(gameConfigs).hasSize(1773);
        // 640 in 11599 ms
    }
}