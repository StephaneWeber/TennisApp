package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import com.sweber.tennis.model.gear.OwnedGear;
import com.sweber.tennis.model.player.Player;
import org.springframework.stereotype.Component;

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

@Component
public class ConfigGeneratorService {
    public List<GameConfig> generateAllConfigs(Player targetPlayer, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        return Optional.ofNullable(targetPlayer)
                .map(Collections::singletonList)
                .orElse(Player.maxLevel(maxLevel))
                .stream()
                .flatMap(player -> generateAllConfigsForPlayer(player, minimumAttributes, minTotalValue, maxLevel, upgradesAllowed))
                .sorted(Comparator.comparingInt(GameConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private Stream<GameConfig> generateAllConfigsForPlayer(Player player, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        List<GameConfig> results = new ArrayList<>();
        List<GearItem> leveledGearItems = GearItem.leveledGearItems(maxLevel, upgradesAllowed);
        for (GearItem racket : potentialGearItems(leveledGearItems, RACKET)) {
            for (GearItem grip : potentialGearItems(leveledGearItems, GRIP)) {
                for (GearItem shoes : potentialGearItems(leveledGearItems, SHOES)) {
                    for (GearItem wristband : potentialGearItems(leveledGearItems, WRISTBAND)) {
                        for (GearItem nutrition : potentialGearItems(leveledGearItems, NUTRITION)) {
                            for (GearItem workout : potentialGearItems(leveledGearItems, WORKOUT)) {
                                GameConfig gameConfig = new GameConfig(player, racket, grip, shoes, wristband, nutrition, workout);
                                if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, gameConfig)) {
                                    results.add(gameConfig);
                                }
                            }
                        }
                    }
                }
            }
        }
        return results.stream();
    }

    private boolean isSuitableConfig(Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, GameConfig gameConfig) {
        return gameConfig.getValue() >= minTotalValue
                && gameConfig.matchingAttributes(minimumAttributes)
                && gameConfig.upgradeAllowed(upgradesAllowed);
    }

    private List<GearItem> potentialGearItems(List<GearItem> items, GearType gearType) {
        return items
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(OwnedGear::isPossibleUpgrade)
                .collect(Collectors.toList());
    }
}
