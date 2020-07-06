package com.sweber.tennis.service;

import com.sweber.tennis.model.Config;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Training;
import com.sweber.tennis.model.gear.Wrist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.sweber.tennis.model.Player.ALL;

public class ConfigGenerator {
    public List<FullConfig> generateAllConfigs(Player targetPlayer, Config minimumConfig, Integer threshold, Integer upgradesAllowed) {
        List<FullConfig> results = new ArrayList<>();
        if (targetPlayer == null || targetPlayer == ALL) {
            for (Player player : Player.values()) {
                if (player != ALL) {
                    List<FullConfig> fullConfigs = generateAllConfigsForPlayer(player, minimumConfig, threshold, upgradesAllowed);
                    results.addAll(fullConfigs);
                }
            }
        } else {
            results = generateAllConfigsForPlayer(targetPlayer, minimumConfig, threshold, upgradesAllowed);
        }
        results.sort(Comparator.comparingInt(FullConfig::getValue).reversed());
        return results;
    }

    private List<FullConfig> generateAllConfigsForPlayer(Player player, Config minimumConfig, Integer threshold, Integer upgradesAllowed) {
        List<FullConfig> results = new ArrayList<>();
        for (Racket racket : Racket.values()) {
            for (Grip grip : Grip.values()) {
                for (Shoes shoes : Shoes.values()) {
                    for (Wrist wrist : Wrist.values()) {
                        for (Nutrition nutrition : Nutrition.values()) {
                            for (Training training : Training.values()) {
                                FullConfig fullConfig = new FullConfig(player, racket, grip, shoes, wrist, nutrition, training);
                                if (fullConfig.getValue() > (threshold == null ? 0 : threshold) && fullConfig.satisfies(minimumConfig) && fullConfig.upgradeAllowed(upgradesAllowed == null ? 0 : upgradesAllowed)) {
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
