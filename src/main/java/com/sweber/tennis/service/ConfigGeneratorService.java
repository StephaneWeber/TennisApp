package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import com.sweber.tennis.model.player.Player;
import com.sweber.tennis.web.model.ConfigFilter;
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

    public List<GameConfig> generateGameConfigs(ConfigFilter configFilter) {
        String player = configFilter.getSelectedPlayer();
        Attributes minAttributes = configFilter.getMinAttributes();
        int minTotal = configFilter.getMinTotal();
        int upgradeAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        return generateGameConfigs(player, minAttributes, minTotal, maxLevel, upgradeAllowed);
    }

    private List<GameConfig> generateGameConfigs(String playerName, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        return Optional.ofNullable(playerService.getPlayer(playerName))
                .map(Collections::singletonList)
                .orElse(playerService.leveledPlayers(maxLevel))
                .stream()
                .flatMap(player -> generateGameConfigsForPlayer(player, minimumAttributes, minTotalValue, maxLevel, upgradesAllowed))
                .sorted(Comparator.comparingInt(GameConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private Stream<GameConfig> generateGameConfigsForPlayer(Player player, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        List<GameConfig> results = new ArrayList<>();
        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        // Pre-partition items by type to avoid repeated filtering in nested loops
        Map<GearType, List<GearItem>> itemsByType = leveledGearItems.stream().collect(Collectors.groupingBy(GearItem::getGearType));
        Map<GearItem, Boolean> itemUpgrades = new HashMap<>();
        for (GearItem racket : itemsByType.getOrDefault(RACKET, Collections.emptyList())) {
            if (numberOfUpgrades(itemUpgrades, racket, null, null, null, null, null) > upgradesAllowed) {
                continue;
            }
            for (GearItem grip : itemsByType.getOrDefault(GRIP, Collections.emptyList())) {
                if (numberOfUpgrades(itemUpgrades, racket, grip, null, null, null, null) > upgradesAllowed) {
                    continue;
                }
                for (GearItem shoes : itemsByType.getOrDefault(SHOES, Collections.emptyList())) {
                    if (numberOfUpgrades(itemUpgrades, racket, grip, shoes, null, null, null) > upgradesAllowed) {
                        continue;
                    }
                    for (GearItem wristband : itemsByType.getOrDefault(WRISTBAND, Collections.emptyList())) {
                        if (numberOfUpgrades(itemUpgrades, racket, grip, shoes, wristband, null, null) > upgradesAllowed) {
                            continue;
                        }
                        for (GearItem nutrition : itemsByType.getOrDefault(NUTRITION, Collections.emptyList())) {
                            if (numberOfUpgrades(itemUpgrades, racket, grip, shoes, wristband, nutrition, null) > upgradesAllowed) {
                                continue;
                            }
                            for (GearItem workout : itemsByType.getOrDefault(WORKOUT, Collections.emptyList())) {
                                if (numberOfUpgrades(itemUpgrades, racket, grip, shoes, wristband, nutrition, workout) > upgradesAllowed) {
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

    private long numberOfUpgrades(Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
        boolean racketUpgrade = isGearItemUpgrade(itemUpgrades, racket);
        boolean gripUpgrade = isGearItemUpgrade(itemUpgrades, grip);
        boolean shoesUpgrade = isGearItemUpgrade(itemUpgrades, shoes);
        boolean wristbandUpgrade = isGearItemUpgrade(itemUpgrades, wristband);
        boolean nutritionUpgrade = isGearItemUpgrade(itemUpgrades, nutrition);
        boolean workoutUpgrade = isGearItemUpgrade(itemUpgrades, workout);
        return Stream.of(racketUpgrade, gripUpgrade, shoesUpgrade, wristbandUpgrade, nutritionUpgrade, workoutUpgrade)
                .filter(aBoolean -> aBoolean)
                .count();
    }

    private boolean isGearItemUpgrade(Map<GearItem, Boolean> itemUpgrades, GearItem gearItemNullable) {
        return Optional.ofNullable(gearItemNullable)
                .map(gearItem -> itemUpgrades.computeIfAbsent(gearItem, item -> item.getLevel() > gearItemService.ownedLevel(item)))
                .orElse(false);
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
}
