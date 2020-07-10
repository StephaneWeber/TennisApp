package com.sweber.tennis.service;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.GearItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.sweber.tennis.model.Player.ALL;
import static com.sweber.tennis.model.gear.GearType.GRIP;
import static com.sweber.tennis.model.gear.GearType.NUTRITION;
import static com.sweber.tennis.model.gear.GearType.RACKET;
import static com.sweber.tennis.model.gear.GearType.SHOES;
import static com.sweber.tennis.model.gear.GearType.WORKOUT;
import static com.sweber.tennis.model.gear.GearType.WRISTBAND;

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
        for (GearItem racket : GearItem.maxLevel(RACKET, maxLevel)) {
            for (GearItem grip : GearItem.maxLevel(GRIP, maxLevel)) {
                for (GearItem shoes : GearItem.maxLevel(SHOES, maxLevel)) {
                    for (GearItem wristband : GearItem.maxLevel(WRISTBAND, maxLevel)) {
                        for (GearItem nutrition : GearItem.maxLevel(NUTRITION, maxLevel)) {
                            for (GearItem workout : GearItem.maxLevel(WORKOUT, maxLevel)) {
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
