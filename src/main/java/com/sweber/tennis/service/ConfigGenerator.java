package com.sweber.tennis.service;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.config.OwnedGear;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<GearItem> leveledGearItems = GearItem.maxLevel(maxLevel);
        for (GearItem racket : potentialGearItems(leveledGearItems, RACKET)) {
            for (GearItem grip : potentialGearItems(leveledGearItems, GRIP)) {
                for (GearItem shoes : potentialGearItems(leveledGearItems, SHOES)) {
                    for (GearItem wristband : potentialGearItems(leveledGearItems, WRISTBAND)) {
                        for (GearItem nutrition : potentialGearItems(leveledGearItems, NUTRITION)) {
                            for (GearItem workout : potentialGearItems(leveledGearItems, WORKOUT)) {
                                FullConfig fullConfig = new FullConfig(player, racket, grip, shoes, wristband, nutrition, workout);
                                if (fullConfig.getValue() > (minTotalValue == null ? 0 : minTotalValue)
                                        && fullConfig.satisfies(minimumConfig)
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

    private static List<GearItem> potentialGearItems(List<GearItem> items, GearType gearType) {
        return items
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(item -> OwnedGear.isUpgradeableTo(item) != OwnedGear.UpgradeStatus.FORBIDDEN)
                .collect(Collectors.toList());
    }
}
