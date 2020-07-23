package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import com.sweber.tennis.model.player.Player;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sweber.tennis.model.gear.GearType.*;

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
                gearItemService.isNextLevel(gameConfig.getRacket()), gearItemService.isNextLevel(gameConfig.getGrip()),
                gearItemService.isNextLevel(gameConfig.getShoes()), gearItemService.isNextLevel(gameConfig.getWristband()),
                gearItemService.isNextLevel(gameConfig.getNutrition()), gearItemService.isNextLevel(gameConfig.getWorkout()))
                .filter(check -> check)
                .count();
        return numberOfUpgrades <= maxUpgradesAllowed;
    }

    private List<GearItem> potentialGearItems(List<GearItem> items, GearType gearType) {
        return items
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(gearItemService::isPossibleUpgrade)
                .collect(Collectors.toList());
    }
}
