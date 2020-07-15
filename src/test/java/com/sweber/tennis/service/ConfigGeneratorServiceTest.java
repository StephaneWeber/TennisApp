package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigGeneratorServiceTest {
    @Test
    public void test() {
        long start = System.currentTimeMillis();
        ConfigGeneratorService configGeneratorService = new ConfigGeneratorService();
        Attributes minimumAttributes = new Attributes(20, 0, 20, 0, 20, 20);
        List<GameConfig> gameConfigs = configGeneratorService.generateAllConfigs(Player.JONAH, minimumAttributes, 140, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", gameConfigs.size(), (end - start)));
        // 1423 - 619
    }
}