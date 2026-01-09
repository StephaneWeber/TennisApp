package com.sweber.tennis.service;

import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.web.model.ConfigFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConfigGeneratorBenchmarkTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigGeneratorBenchmarkTest.class);

    @Autowired
    private ConfigGeneratorService configGeneratorService;

    @BeforeAll
    static void beforeAll() {
        // Ensure tests run deterministically re: generator mode by clearing the property
        System.clearProperty("tennis.generator.legacy");
    }

    @Test
    public void compareLegacyAndOptimizedGenerators() {
        ConfigFilter filter = new ConfigFilter();
        // choose parameters that exercise the generator reasonably
        filter.setMaxLevel(15);
        filter.setUpgradeAllowed(1);
        filter.setMinTotal(100);

        int warmups = 2;
        int runs = 4;

        // Warm up optimized
        System.setProperty("tennis.generator.legacy", "false");
        for (int i = 0; i < warmups; i++) {
            configGeneratorService.generateFilteredGameConfigs(filter);
        }

        // Measure optimized
        List<Long> optimizedTimes = new ArrayList<>();
        int optimizedCount = -1;
        for (int i = 0; i < runs; i++) {
            System.gc();
            System.setProperty("tennis.generator.legacy", "false");
            long start = System.nanoTime();
            List<GameConfig> res = configGeneratorService.generateFilteredGameConfigs(filter);
            long end = System.nanoTime();
            optimizedTimes.add(end - start);
            optimizedCount = res.size();
        }

        // Warm up legacy
        System.setProperty("tennis.generator.legacy", "true");
        for (int i = 0; i < warmups; i++) {
            configGeneratorService.generateFilteredGameConfigs(filter);
        }

        // Measure legacy
        List<Long> legacyTimes = new ArrayList<>();
        int legacyCount = -1;
        for (int i = 0; i < runs; i++) {
            System.gc();
            System.setProperty("tennis.generator.legacy", "true");
            long start = System.nanoTime();
            List<GameConfig> res = configGeneratorService.generateFilteredGameConfigs(filter);
            long end = System.nanoTime();
            legacyTimes.add(end - start);
            legacyCount = res.size();
        }

        // Basic correctness: both implementations produce the same number of results
        assertThat(optimizedCount).isEqualTo(legacyCount);

        double optAvgMs = optimizedTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0;
        double legAvgMs = legacyTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0;

        LOG.info("Benchmark results (avg over {} runs): optimized={} ms, legacy={} ms, resultsCount={}", runs, String.format("%.2f", optAvgMs), String.format("%.2f", legAvgMs), optimizedCount);

        System.out.println("Benchmark summary:");
        System.out.println("  optimized avg (ms): " + String.format("%.2f", optAvgMs));
        System.out.println("  legacy    avg (ms): " + String.format("%.2f", legAvgMs));
        System.out.println("  results count: " + optimizedCount);

        // No strict assertion on speedup because CI machines vary; we at least assert both ran successfully and returned same content size
    }
}
