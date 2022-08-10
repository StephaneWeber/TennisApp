package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
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

    public ConfigGeneratorService(GearItemService gearItemService, PlayerService playerService) {
        this.gearItemService = gearItemService;
        this.playerService = playerService;
    }

    public List<GameConfig> generateFilteredGameConfigs(ConfigFilter configFilter) {
        String playerName = configFilter.getSelectedPlayer();
        Attributes minAttributes = configFilter.getMinAttributes();
        int minTotal = configFilter.getMinTotal();
        int upgradesAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        List<Player> players = Optional.ofNullable(playerService.getPlayer(playerName))
                .map(Collections::singletonList)
                .orElse(playerService.leveledPlayers(maxLevel));
        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        potentialRackets = potentialGearItems(leveledGearItems, RACKET);
        potentialGrips = potentialGearItems(leveledGearItems, GRIP);
        potentialShoes = potentialGearItems(leveledGearItems, SHOES);
        potentialWristbands = potentialGearItems(leveledGearItems, WRISTBAND);
        potentialNutritions = potentialGearItems(leveledGearItems, NUTRITION);
        potentialWorkouts = potentialGearItems(leveledGearItems, WORKOUT);
        return generateGameConfigs(players, minAttributes, minTotal, upgradesAllowed);
    }

    private Player createMaxPlayer(List<Player> players) {
        int minAgility = getMaxForProperty(players, Attributes::getAgility);
        int minEndurance = getMaxForProperty(players, Attributes::getEndurance);
        int minServe = getMaxForProperty(players, Attributes::getService);
        int minVolley = getMaxForProperty(players, Attributes::getVolley);
        int minForehand = getMaxForProperty(players, Attributes::getForehand);
        int minBackhand = getMaxForProperty(players, Attributes::getBackhand);
        return Player.dummy(Config.dummy(minAgility, minEndurance, minServe, minVolley, minForehand, minBackhand));
    }

    private int getMaxForProperty(List<Player> players, Function<Attributes, Integer> getProperty) {
        return players.stream()
                .map(Player::getAttributes)
                .max(Comparator.comparing(getProperty))
                .map(getProperty)
                .orElse(0);
    }

    private List<GameConfig> generateGameConfigs(List<Player> players, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed) {
        Player maxPlayer = createMaxPlayer(players);
        List<GameConfig> configs = generateReducedGameConfigs(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed);
        return players
                .stream()
                .flatMap(player -> generateGameConfigsForPlayer(player, configs, minimumAttributes, minTotalValue, upgradesAllowed))
                .sorted(Comparator.comparingInt(GameConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private List<GameConfig> generateReducedGameConfigs(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed) {
        int gameConfigs = 0;
        List<GameConfig> results = new ArrayList<>();
        Map<GearItem, Boolean> itemUpgrades = new HashMap<>();
        for (GearItem racket : potentialRackets) {
            gameConfigs = handleRacket(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, potentialGrips, potentialShoes, potentialWristbands, potentialNutritions, potentialWorkouts, itemUpgrades, racket);
        }
        String message = String.format("%d possible configurations, filtered only %d", gameConfigs, results.size());
        LOGGER.info(message);
        return results;
    }

    private int handleRacket(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, List<GearItem> potentialGrips, List<GearItem> potentialShoes, List<GearItem> potentialWristbands, List<GearItem> potentialNutritions, List<GearItem> potentialWorkouts, Map<GearItem, Boolean> itemUpgrades, GearItem racket) {
        if (maxUpgradesNotReached(itemUpgrades, racket, null, null, null, null, null, upgradesAllowed)) {
            for (GearItem grip : potentialGrips) {
                gameConfigs = handleGrip(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, potentialShoes, potentialWristbands, potentialNutritions, potentialWorkouts, itemUpgrades, racket, grip);
            }
        }
        return gameConfigs;
    }

    private int handleGrip(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, List<GearItem> potentialShoes, List<GearItem> potentialWristbands, List<GearItem> potentialNutritions, List<GearItem> potentialWorkouts, Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip) {
        if (maxUpgradesNotReached(itemUpgrades, racket, grip, null, null, null, null, upgradesAllowed)) {
            for (GearItem shoes : potentialShoes) {
                gameConfigs = handleShoes(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, potentialWristbands, potentialNutritions, potentialWorkouts, itemUpgrades, racket, grip, shoes);
            }
        }
        return gameConfigs;
    }

    private int handleShoes(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, List<GearItem> potentialWristbands, List<GearItem> potentialNutritions, List<GearItem> potentialWorkouts, Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes) {
        if (maxUpgradesNotReached(itemUpgrades, racket, grip, shoes, null, null, null, upgradesAllowed)) {
            for (GearItem wristband : potentialWristbands) {
                gameConfigs = handleWristband(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, potentialNutritions, potentialWorkouts, itemUpgrades, racket, grip, shoes, wristband);
            }
        }
        return gameConfigs;
    }

    private int handleWristband(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, List<GearItem> potentialNutritions, List<GearItem> potentialWorkouts, Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband) {
        if (maxUpgradesNotReached(itemUpgrades, racket, grip, shoes, wristband, null, null, upgradesAllowed)) {
            for (GearItem nutrition : potentialNutritions) {
                gameConfigs = handleNutrition(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, potentialWorkouts, itemUpgrades, racket, grip, shoes, wristband, nutrition);
            }
        }
        return gameConfigs;
    }

    private int handleNutrition(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, List<GearItem> potentialWorkouts, Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition) {
        if (maxUpgradesNotReached(itemUpgrades, racket, grip, shoes, wristband, nutrition, null, upgradesAllowed)) {
            for (GearItem workout : potentialWorkouts) {
                gameConfigs = handleWorkout(maxPlayer, minimumAttributes, minTotalValue, upgradesAllowed, gameConfigs, results, itemUpgrades, racket, grip, shoes, wristband, nutrition, workout);
            }
        }
        return gameConfigs;
    }

    private int handleWorkout(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, int gameConfigs, List<GameConfig> results, Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
        if (maxUpgradesNotReached(itemUpgrades, racket, grip, shoes, wristband, nutrition, workout, upgradesAllowed)) {
            GearConfig gearConfig = GearConfig.of(racket, grip, shoes, wristband, nutrition, workout);
            GameConfig gameConfig = new GameConfig(maxPlayer, gearConfig, upgradesAllowed > 0);
            gameConfigs++;
            if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, gameConfig)) {
                results.add(gameConfig);
            }
        }
        return gameConfigs;
    }

    private Stream<GameConfig> generateGameConfigsForPlayer(Player player, List<GameConfig> configs, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed) {
        List<GameConfig> results = new ArrayList<>();
        configs.forEach(config ->
                validateForPlayer(player, minimumAttributes, minTotalValue, upgradesAllowed, results, config)
        );
        if (!results.isEmpty()) {
            String message = String.format("%d configurations found for %s", results.size(), player.getName());
            LOGGER.info(message);
        }
        return results.stream();
    }

    private void validateForPlayer(Player player, Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, List<GameConfig> results, GameConfig config) {
        config.updatePlayer(player);
        GameConfig newConfig = config.cloneConfig();
        if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, newConfig)) {
            results.add(newConfig);
        }
    }

    private boolean maxUpgradesNotReached(Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout, int upgradesAllowed) {
        boolean racketUpgrade = isGearItemUpgrade(itemUpgrades, racket);
        boolean gripUpgrade = isGearItemUpgrade(itemUpgrades, grip);
        boolean shoesUpgrade = isGearItemUpgrade(itemUpgrades, shoes);
        boolean wristbandUpgrade = isGearItemUpgrade(itemUpgrades, wristband);
        boolean nutritionUpgrade = isGearItemUpgrade(itemUpgrades, nutrition);
        boolean workoutUpgrade = isGearItemUpgrade(itemUpgrades, workout);
        return Stream.of(racketUpgrade, gripUpgrade, shoesUpgrade, wristbandUpgrade, nutritionUpgrade, workoutUpgrade)
                .filter(aBoolean -> aBoolean)
                .count() <= upgradesAllowed;
    }

    private boolean isGearItemUpgrade(Map<GearItem, Boolean> itemUpgrades, GearItem gearItemNullable) {
        return Optional.ofNullable(gearItemNullable)
                .map(gearItem -> itemUpgrades.computeIfAbsent(gearItem, item -> item.getLevel() > gearItemService.ownedLevel(item)))
                .orElse(false);
    }

    boolean isSuitableConfig(Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, GameConfig gameConfig) {
        return gameConfig.getValue() >= minTotalValue
                && matchingAttributes(gameConfig, minimumAttributes)
                && upgradeAllowed(gameConfig, upgradesAllowed);
    }

    private boolean matchingAttributes(GameConfig gameConfig, Attributes minimumAttributes) {
        if (minimumAttributes != null) {
            Attributes gameConfigAttributes = gameConfig.getAttributes();
            return gameConfigAttributes.compareTo(minimumAttributes) > 0;
        }
        return true;
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
