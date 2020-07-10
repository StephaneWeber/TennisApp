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
        List<FullConfig> fullConfigs = configGenerator.generateAllConfigs(Player.JONAH, new Config(20, 0, 15, 0, 20, 25, 0, 0), 140, 6, 0);
        fullConfigs.forEach(System.out::println);
        System.out.println(fullConfigs.size());
//        assertThat(fullConfigs).hasSize(3);
        long end = System.currentTimeMillis();
        System.out.println((end - start)); // 14125 configs - 30,151 sec
        // branch -> 926 - 80154
    }
}