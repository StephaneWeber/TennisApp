package com.sweber.tennis.service;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Workout;
import com.sweber.tennis.model.gear.Wristband;
import com.sweber.tennis.web.model.FullConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.sweber.tennis.model.Player.ALL;

public class ConfigGenerator {
    public List<FullConfig> generateAllConfigs(Player targetPlayer, Config minimumConfig, Integer minTotalValue, Integer maxLevel, Integer upgradesAllowed) {
        List<FullConfig> results = new ArrayList<>();
        if (targetPlayer == null || targetPlayer == ALL) {
            for (Player player : Player.values()) {
                if (player != ALL) {
                    List<FullConfig> fullConfigs = generateAllConfigsForPlayer(player, minimumConfig, minTotalValue, maxLevel, upgradesAllowed);
                    results.addAll(fullConfigs);
                }
            }
        } else {
            results = generateAllConfigsForPlayer(targetPlayer, minimumConfig, minTotalValue, maxLevel, upgradesAllowed);
        }
        results.sort(Comparator.comparingInt(FullConfig::getValue).reversed());
        return results;
    }

    private List<FullConfig> generateAllConfigsForPlayer(Player player, Config minimumConfig, Integer minTotalValue, Integer maxLevel, Integer upgradesAllowed) {
        List<FullConfig> results = new ArrayList<>();
        for (Racket racket : Racket.maxLevel(maxLevel)) {
            for (Grip grip : Grip.maxLevel(maxLevel)) {
                for (Shoes shoes : Shoes.maxLevel(maxLevel)) {
                    for (Wristband wristband : Wristband.maxLevel(maxLevel)) {
                        for (Nutrition nutrition : Nutrition.maxLevel(maxLevel)) {
                            for (Workout workout : Workout.maxLevel(maxLevel)) {
                                FullConfig fullConfig = new FullConfig(player, racket, grip, shoes, wristband, nutrition, workout);
                                if (fullConfig.getValue() > (minTotalValue == null ? 0 : minTotalValue)
                                        && fullConfig.satisfies(minimumConfig)
                                        && fullConfig.maxLevelRespected(maxLevel)
                                        && fullConfig.upgradeAllowed(upgradesAllowed == null ? 0 : upgradesAllowed)) {
                                    results.add(fullConfig);
                                }
                            }
                        }
                    }
                }
            }
        }
        results.sort(Comparator.comparingInt(FullConfig::getValue).reversed());
        return results;
    }
}
