package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
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
    private final GearItemService gearItemService;
    private final PlayerService playerService;

    public ConfigGeneratorService(GearItemService gearItemService, PlayerService playerService) {
        this.gearItemService = gearItemService;
        this.playerService = playerService;
    }

    public List<GameConfig> generateAllConfigs(String playerName, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        return Optional.ofNullable(playerService.getPlayer(playerName))
                .map(Collections::singletonList)
                .orElse(playerService.leveledPlayers(maxLevel))
                .stream()
                .flatMap(player -> generateAllConfigsForPlayer(player, minimumAttributes, minTotalValue, maxLevel, upgradesAllowed))
                .sorted(Comparator.comparingInt(GameConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private Stream<GameConfig> generateAllConfigsForPlayer(Player player, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        List<GameConfig> results = new ArrayList<>();
        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        int examinedConfigs = 0;
        for (GearItem racket : potentialGearItems(leveledGearItems, RACKET)) {
            int upgrades = 0;
            if (racket.getLevel() > gearItemService.ownedLevel(racket)) {
                if (upgrades + 1 > upgradesAllowed) {
                    continue;
                } else {
                    upgrades = upgrades + 1;
                }
            }
            for (GearItem grip : potentialGearItems(leveledGearItems, GRIP)) {
                if (grip.getLevel() > gearItemService.ownedLevel(grip)) {
                    if (upgrades + 1 > upgradesAllowed) {
                        continue;
                    } else {
                        upgrades = upgrades + 1;
                    }
                }
                for (GearItem shoes : potentialGearItems(leveledGearItems, SHOES)) {
                    if (shoes.getLevel() > gearItemService.ownedLevel(shoes)) {
                        if (upgrades + 1 > upgradesAllowed) {
                            continue;
                        } else {
                            upgrades = upgrades + 1;
                        }
                    }
                    for (GearItem wristband : potentialGearItems(leveledGearItems, WRISTBAND)) {
                        if (wristband.getLevel() > gearItemService.ownedLevel(wristband)) {
                            if (upgrades + 1 > upgradesAllowed) {
                                continue;
                            } else {
                                upgrades = upgrades + 1;
                            }
                        }
                        for (GearItem nutrition : potentialGearItems(leveledGearItems, NUTRITION)) {
                            if (nutrition.getLevel() > gearItemService.ownedLevel(nutrition)) {
                                if (upgrades + 1 > upgradesAllowed) {
                                    continue;
                                } else {
                                    upgrades = upgrades + 1;
                                }
                            }
                            for (GearItem workout : potentialGearItems(leveledGearItems, WORKOUT)) {
                                if (workout.getLevel() > gearItemService.ownedLevel(workout)) {
                                    if (upgrades + 1 > upgradesAllowed) {
                                        continue;
                                    } else {
                                        upgrades = upgrades + 1;
                                    }
                                }
                                GameConfig gameConfig = new GameConfig(player, racket, grip, shoes, wristband, nutrition, workout, upgradesAllowed > 0);
                                examinedConfigs++;
                                if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, gameConfig)) {
                                    results.add(gameConfig);
                                }
                                upgrades = 0;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Examined " + examinedConfigs); //290304 - 526 - (1318) 1655 ms
        return results.stream();
    }

    private boolean isSuitableConfig(Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, GameConfig gameConfig) {
        return gameConfig.getValue() >= minTotalValue
                && matchingAttributes(gameConfig, minimumAttributes)
                && upgradeAllowed(gameConfig, upgradesAllowed);
    }

    private boolean matchingAttributes(GameConfig gameConfig, Attributes minimumAttributes) {
        if (minimumAttributes == null) return true;
        Attributes gameConfigAttributes = gameConfig.getAttributes();
        return gameConfigAttributes.getAgility() >= minimumAttributes.getAgility()
                && gameConfigAttributes.getEndurance() >= minimumAttributes.getEndurance()
                && gameConfigAttributes.getService() >= minimumAttributes.getService()
                && gameConfigAttributes.getVolley() >= minimumAttributes.getVolley()
                && gameConfigAttributes.getForehand() >= minimumAttributes.getForehand()
                && gameConfigAttributes.getBackhand() >= minimumAttributes.getBackhand();
    }

    private boolean upgradeAllowed(GameConfig gameConfig, int maxUpgradesAllowed) {
        long numberOfUpgrades = Stream.of(
                isSimpleUpgrade(gameConfig.getRacket()), isSimpleUpgrade(gameConfig.getGrip()),
                isSimpleUpgrade(gameConfig.getShoes()), isSimpleUpgrade(gameConfig.getWristband()),
                isSimpleUpgrade(gameConfig.getNutrition()), isSimpleUpgrade(gameConfig.getWorkout()))
                .filter(check -> check)
                .count();
        return numberOfUpgrades <= maxUpgradesAllowed;
    }

    private boolean isSimpleUpgrade(GearItem gearItem) {
        return gearItem.getLevel() == gearItemService.ownedLevel(gearItem) + 1;
    }

    private List<GearItem> potentialGearItems(List<GearItem> items, GearType gearType) {
        return items
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .collect(Collectors.toList());
    }
}
