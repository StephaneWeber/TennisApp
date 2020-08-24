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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Map<GearItem, Boolean> itemUpgrades = new HashMap<>();
        for (GearItem racket : potentialGearItems(leveledGearItems, RACKET)) {
            if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, null, null, null, null, null)) {
                continue;
            }
            for (GearItem grip : potentialGearItems(leveledGearItems, GRIP)) {
                if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, grip, null, null, null, null)) {
                    continue;
                }
                for (GearItem shoes : potentialGearItems(leveledGearItems, SHOES)) {
                    if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, grip, shoes, null, null, null)) {
                        continue;
                    }
                    for (GearItem wristband : potentialGearItems(leveledGearItems, WRISTBAND)) {
                        if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, grip, shoes, wristband, null, null)) {
                            continue;
                        }
                        for (GearItem nutrition : potentialGearItems(leveledGearItems, NUTRITION)) {
                            if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, grip, shoes, wristband, nutrition, null)) {
                                continue;
                            }
                            for (GearItem workout : potentialGearItems(leveledGearItems, WORKOUT)) {
                                if (tooManyUpdates(itemUpgrades, upgradesAllowed, racket, grip, shoes, wristband, nutrition, workout)) {
                                    continue;
                                }
                                GameConfig gameConfig = new GameConfig(player, racket, grip, shoes, wristband, nutrition, workout, upgradesAllowed > 0);
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

    private boolean tooManyUpdates(Map<GearItem, Boolean> itemUpgrades, int upgradesAllowed, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
        boolean racketUpgrade = isGearItemUpgrade(itemUpgrades, racket);
        boolean gripUpgrade = isGearItemUpgrade(itemUpgrades, grip);
        boolean shoesUpgrade = isGearItemUpgrade(itemUpgrades, shoes);
        boolean wristbandUpgrade = isGearItemUpgrade(itemUpgrades, wristband);
        boolean nutritionUpgrade = isGearItemUpgrade(itemUpgrades, nutrition);
        boolean workoutUpgrade = isGearItemUpgrade(itemUpgrades, workout);
        return Stream.of(racketUpgrade, gripUpgrade, shoesUpgrade, wristbandUpgrade, nutritionUpgrade, workoutUpgrade)
                .filter(aBoolean -> aBoolean)
                .count()
                > upgradesAllowed;
    }

    private boolean isGearItemUpgrade(Map<GearItem, Boolean> itemUpgrades, GearItem gearItem) {
        if (gearItem == null) {
            return false;
        }

        return itemUpgrades.computeIfAbsent(gearItem, item -> item.getLevel() > gearItemService.ownedLevel(item));
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
