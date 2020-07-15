package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.config.FullConfig;
import com.sweber.tennis.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigGeneratorServiceTest {
    @Test
    public void test() {
        long start = System.currentTimeMillis();
        ConfigGeneratorService configGeneratorService = new ConfigGeneratorService();
        List<FullConfig> fullConfigs = configGeneratorService.generateAllConfigs(Player.JONAH, new Config(20, 0, 20, 0, 20, 20, 0, 0), 140, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", fullConfigs.size(), (end - start)));
        // 1423 - 619
    }
}