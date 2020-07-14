package com.sweber.tennis.service;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.config.OwnedGear;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sweber.tennis.model.gear.GearType.GRIP;
import static com.sweber.tennis.model.gear.GearType.NUTRITION;
import static com.sweber.tennis.model.gear.GearType.RACKET;
import static com.sweber.tennis.model.gear.GearType.SHOES;
import static com.sweber.tennis.model.gear.GearType.WORKOUT;
import static com.sweber.tennis.model.gear.GearType.WRISTBAND;

public class ConfigGenerator {
    public List<FullConfig> generateAllConfigs(Player targetPlayer, Config minimumConfig, int minTotalValue, int maxLevel, int upgradesAllowed) {
        return Optional.ofNullable(targetPlayer)
                .map(Collections::singletonList)
                .orElse(Player.maxLevel(maxLevel))
                .stream()
                .flatMap(player -> generateAllConfigsForPlayer(player, minimumConfig, minTotalValue, maxLevel, upgradesAllowed))
                .sorted(Comparator.comparingInt(FullConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private Stream<FullConfig> generateAllConfigsForPlayer(Player player, Config minimumConfig, int minTotalValue, int maxLevel, int upgradesAllowed) {
        List<FullConfig> results = new ArrayList<>();
        List<GearItem> leveledGearItems = GearItem.maxLevel(maxLevel, upgradesAllowed);
        for (GearItem racket : potentialGearItems(leveledGearItems, RACKET)) {
            for (GearItem grip : potentialGearItems(leveledGearItems, GRIP)) {
                for (GearItem shoes : potentialGearItems(leveledGearItems, SHOES)) {
                    for (GearItem wristband : potentialGearItems(leveledGearItems, WRISTBAND)) {
                        for (GearItem nutrition : potentialGearItems(leveledGearItems, NUTRITION)) {
                            for (GearItem workout : potentialGearItems(leveledGearItems, WORKOUT)) {
                                FullConfig fullConfig = new FullConfig(player, racket, grip, shoes, wristband, nutrition, workout);
                                if (isSuitableConfig(minimumConfig, minTotalValue, upgradesAllowed, fullConfig)) {
                                    results.add(fullConfig);
                                }
                            }
                        }
                    }
                }
            }
        }
        return results.stream();
    }

    private boolean isSuitableConfig(Config minimumConfig, int minTotalValue, int upgradesAllowed, FullConfig fullConfig) {
        return fullConfig.getValue() >= minTotalValue
                && fullConfig.satisfies(minimumConfig)
                && fullConfig.upgradeAllowed(upgradesAllowed);
    }

    private List<GearItem> potentialGearItems(List<GearItem> items, GearType gearType) {
        return items
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(OwnedGear::isPossibleUpgrade)
                .collect(Collectors.toList());
    }
}
