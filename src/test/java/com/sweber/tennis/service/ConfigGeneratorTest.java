package com.sweber.tennis.service;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigGeneratorTest {
    @Test
    public void test() {
        long start = System.currentTimeMillis();
        ConfigGenerator configGenerator = new ConfigGenerator();
        List<FullConfig> fullConfigs = configGenerator.generateAllConfigs(Player.JONAH, new Config(20, 0, 20, 0, 20, 20, 0, 0), 140, 6, 0);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Found %d configs in %d ms", fullConfigs.size(), (end - start)));
        // 682 - 571
    }
}