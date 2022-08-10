package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.ConfigValues;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.config.GearConfig;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import com.sweber.tennis.model.player.Player;
import com.sweber.tennis.web.model.ConfigFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigGeneratorService.class);
    private final GearItemService gearItemService;
    private final PlayerService playerService;

    private List<GearItem> potentialRackets;
    private List<GearItem> potentialGrips;
    private List<GearItem> potentialShoes;
    private List<GearItem> potentialWristbands;
    private List<GearItem> potentialNutritions;
    private List<GearItem> potentialWorkouts;
    private List<Player> players;
    private Player maxPlayer;
    private ConfigFilter configFilter;
    private List<GameConfig> candidateGameConfigs;
    private int numberOfExaminedGameConfigs;

    public ConfigGeneratorService(GearItemService gearItemService, PlayerService playerService) {
        this.gearItemService = gearItemService;
        this.playerService = playerService;
    }

    public synchronized List<GameConfig> generateFilteredGameConfigs(ConfigFilter configFilter) {
        this.configFilter = configFilter;
        this.players = generateFilteredPlayers();
        generatePotentialGearItems();
        this.maxPlayer = createMaxPlayer();
        this.numberOfExaminedGameConfigs = 0;
        this.candidateGameConfigs = generateReducedGameConfigs();
        return generateGameConfigsForPlayers();
    }

    private List<Player> generateFilteredPlayers() {
        String playerName = configFilter.getSelectedPlayer();
        int maxLevel = configFilter.getMaxLevel();
        return Optional.ofNullable(playerService.getPlayer(playerName))
                .map(Collections::singletonList)
                .orElse(playerService.leveledPlayers(maxLevel));
    }

    private void generatePotentialGearItems() {
        int upgradesAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        potentialRackets = gearItemsOfType(leveledGearItems, RACKET);
        potentialGrips = gearItemsOfType(leveledGearItems, GRIP);
        potentialShoes = gearItemsOfType(leveledGearItems, SHOES);
        potentialWristbands = gearItemsOfType(leveledGearItems, WRISTBAND);
        potentialNutritions = gearItemsOfType(leveledGearItems, NUTRITION);
        potentialWorkouts = gearItemsOfType(leveledGearItems, WORKOUT);
    }

    private Player createMaxPlayer() {
        int minAgility = getMaxForProperty(Attributes::getAgility);
        int minEndurance = getMaxForProperty(Attributes::getEndurance);
        int minServe = getMaxForProperty(Attributes::getService);
        int minVolley = getMaxForProperty(Attributes::getVolley);
        int minForehand = getMaxForProperty(Attributes::getForehand);
        int minBackhand = getMaxForProperty(Attributes::getBackhand);
        return Player.dummy(ConfigValues.dummy(minAgility, minEndurance, minServe, minVolley, minForehand, minBackhand));
    }

    private int getMaxForProperty(Function<Attributes, Integer> getProperty) {
        return players.stream()
                .map(Player::getAttributes)
                .max(Comparator.comparing(getProperty))
                .map(getProperty)
                .orElse(0);
    }

    private List<GameConfig> generateGameConfigsForPlayers() {
        return players
                .stream()
                .flatMap(player -> generateGameConfigsForPlayer(player, candidateGameConfigs))
                .sorted(Comparator.comparingInt(GameConfig::getTotalValue).reversed())
                .collect(Collectors.toList());
    }

    private List<GameConfig> generateReducedGameConfigs() {
        List<GameConfig> results = new ArrayList<>();
        Map<GearItem, Boolean> itemUpgrades = new HashMap<>();
        GearConfig gearConfig = new GearConfig();
        for (GearItem racket : potentialRackets) {
            handleRacket(results, itemUpgrades, gearConfig, racket);
        }
        String message = String.format("%d possible configurations, filtered only %d", numberOfExaminedGameConfigs, results.size());
        LOGGER.info(message);
        return results;
    }

    private void handleRacket(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem racket) {
        gearConfig.setRacket(racket);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            for (GearItem grip : potentialGrips) {
                handleGrip(results, itemUpgrades, gearConfig, grip);
            }
            gearConfig.setGrip(null);
        }
    }

    private void handleGrip(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem grip) {
        gearConfig.setGrip(grip);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            for (GearItem shoes : potentialShoes) {
                handleShoes(results, itemUpgrades, gearConfig, shoes);
            }
            gearConfig.setShoes(null);
        }
    }

    private void handleShoes(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem shoes) {
        gearConfig.setShoes(shoes);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            for (GearItem wristband : potentialWristbands) {
                handleWristband(results, itemUpgrades, gearConfig, wristband);
            }
            gearConfig.setWristband(null);
        }
    }

    private void handleWristband(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem wristband) {
        gearConfig.setWristband(wristband);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            for (GearItem nutrition : potentialNutritions) {
                handleNutrition(results, itemUpgrades, gearConfig, nutrition);
            }
            gearConfig.setNutrition(null);
        }
    }

    private void handleNutrition(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem nutrition) {
        gearConfig.setNutrition(nutrition);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            for (GearItem workout : potentialWorkouts) {
                handleWorkout(results, itemUpgrades, gearConfig, workout);
            }
            gearConfig.setWorkout(null);
        }
    }

    private void handleWorkout(List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig, GearItem workout) {
        gearConfig.setWorkout(workout);
        if (maxUpgradesNotReached(itemUpgrades, gearConfig)) {
            checkGameConfig(maxPlayer, results, gearConfig);
        }
    }

    private void checkGameConfig(Player maxPlayer, List<GameConfig> results, GearConfig gearConfig) {
        GameConfig gameConfig = new GameConfig(maxPlayer, gearConfig, configFilter.getUpgradeAllowed() > 0);
        numberOfExaminedGameConfigs++;
        if (isSuitableConfig(gameConfig)) {
            gameConfig.updateGearConfig(gearConfig.cloneConfig());
            results.add(gameConfig);
        }
    }

    private Stream<GameConfig> generateGameConfigsForPlayer(Player player, List<GameConfig> configs) {
        List<GameConfig> results = new ArrayList<>();
        configs.forEach(config ->
                validateForPlayer(player, results, config)
        );
        if (!results.isEmpty()) {
            String message = String.format("%d configurations found for %s", results.size(), player.getName());
            LOGGER.info(message);
        }
        return results.stream();
    }

    private void validateForPlayer(Player player, List<GameConfig> results, GameConfig config) {
        config.updatePlayer(player);
        GameConfig newConfig = config.cloneConfig();
        if (isSuitableConfig(newConfig)) {
            results.add(newConfig);
        }
    }

    private boolean maxUpgradesNotReached(Map<GearItem, Boolean> itemUpgrades, GearConfig gearConfig) {
        boolean racketUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getRacket());
        boolean gripUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getGrip());
        boolean shoesUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getShoes());
        boolean wristbandUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getWristband());
        boolean nutritionUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getNutrition());
        boolean workoutUpgrade = isGearItemUpgrade(itemUpgrades, gearConfig.getWorkout());
        return Stream.of(racketUpgrade, gripUpgrade, shoesUpgrade, wristbandUpgrade, nutritionUpgrade, workoutUpgrade)
                .filter(aBoolean -> aBoolean)
                .count() <= configFilter.getUpgradeAllowed();
    }

    private boolean isGearItemUpgrade(Map<GearItem, Boolean> itemUpgrades, GearItem gearItemNullable) {
        return Optional.ofNullable(gearItemNullable)
                .map(gearItem -> itemUpgrades.computeIfAbsent(gearItem, item -> item.getLevel() > gearItemService.ownedLevel(item)))
                .orElse(false);
    }

    private boolean isSuitableConfig(GameConfig gameConfig) {
        return gameConfig.getTotalValue() >= configFilter.getMinTotal()
                && gameConfig.getAttributes().compareTo(configFilter.getMinAttributes()) > 0;
    }

    private List<GearItem> gearItemsOfType(List<GearItem> allGearItems, GearType gearType) {
        return allGearItems
                .stream()
                .filter(item -> item.getGearType() == gearType)
                .collect(Collectors.toList());
    }
}
