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
import java.util.function.BiFunction;
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

    public ConfigGeneratorService(GearItemService gearItemService, PlayerService playerService) {
        this.gearItemService = gearItemService;
        this.playerService = playerService;
    }

    public List<GameConfig> generateFilteredGameConfigs(ConfigFilter configFilter) {
        String playerName = configFilter.getSelectedPlayer();
        Attributes minAttributes = configFilter.getMinAttributes();
        int minTotal = configFilter.getMinTotal();
        int upgradeAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        List<Player> players = Optional.ofNullable(playerService.getPlayer(playerName))
                .map(Collections::singletonList)
                .orElse(playerService.leveledPlayers(maxLevel));
        return generateGameConfigs(players, minAttributes, minTotal, maxLevel, upgradeAllowed);
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

    private List<GameConfig> generateGameConfigs(List<Player> players, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        Player maxPlayer = createMaxPlayer(players);
        List<GameConfig> configs = generateReducedGameConfigs(maxPlayer, minimumAttributes, minTotalValue, maxLevel, upgradesAllowed);
        return players
                .stream()
                .flatMap(player -> generateGameConfigsForPlayer(player, configs, minimumAttributes, minTotalValue, upgradesAllowed))
                .sorted(Comparator.comparingInt(GameConfig::getValue).reversed())
                .collect(Collectors.toList());
    }

    private List<GameConfig> generateReducedGameConfigs(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        // Allow running a legacy (baseline) implementation for benchmarking by setting system property -Dtennis.generator.legacy=true
        if (Boolean.getBoolean("tennis.generator.legacy")) {
            return generateReducedGameConfigsLegacy(maxPlayer, minimumAttributes, minTotalValue, maxLevel, upgradesAllowed);
        }
        int gameConfigsCount;
        // thread-safe result collection and counter for parallel execution
        java.util.concurrent.ConcurrentLinkedQueue<GameConfig> concurrentResults = new java.util.concurrent.ConcurrentLinkedQueue<>();
        java.util.concurrent.atomic.AtomicInteger gameConfigsCounter = new java.util.concurrent.atomic.AtomicInteger(0);

        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        // Pre-partition items by type to avoid repeated filtering in nested loops
        Map<GearType, List<GearItem>> itemsByType = leveledGearItems.stream().collect(Collectors.groupingBy(GearItem::getGearType));

        // Precompute per-type maxima separately for non-upgrade (owned) and upgrade items to respect upgrade budget
        Map<GearType, Attributes> maxNonUpgradeAttrsByType = new HashMap<>();
        Map<GearType, Attributes> maxUpgradeAttrsByType = new HashMap<>();
        Map<GearType, Integer> maxNonUpgradeTotalByType = new HashMap<>();
        Map<GearType, Integer> maxUpgradeTotalByType = new HashMap<>();

        for (Map.Entry<GearType, List<GearItem>> e : itemsByType.entrySet()) {
            Attributes maxNon = new Attributes();
            Attributes maxUp = new Attributes();
            int maxNonTotal = 0;
            int maxUpTotal = 0;
            for (GearItem gi : e.getValue()) {
                boolean isUpgrade = gi.getLevel() > gearItemService.ownedLevel(gi);
                Attributes a = gi.getAttributes();
                int total = a.getAgility() + a.getEndurance() + a.getService() + a.getVolley() + a.getForehand() + a.getBackhand();
                if (isUpgrade) {
                    if (a.getAgility() > maxUp.getAgility()) maxUp.setAgility(a.getAgility());
                    if (a.getEndurance() > maxUp.getEndurance()) maxUp.setEndurance(a.getEndurance());
                    if (a.getService() > maxUp.getService()) maxUp.setService(a.getService());
                    if (a.getVolley() > maxUp.getVolley()) maxUp.setVolley(a.getVolley());
                    if (a.getForehand() > maxUp.getForehand()) maxUp.setForehand(a.getForehand());
                    if (a.getBackhand() > maxUp.getBackhand()) maxUp.setBackhand(a.getBackhand());
                    if (total > maxUpTotal) maxUpTotal = total;
                } else {
                    if (a.getAgility() > maxNon.getAgility()) maxNon.setAgility(a.getAgility());
                    if (a.getEndurance() > maxNon.getEndurance()) maxNon.setEndurance(a.getEndurance());
                    if (a.getService() > maxNon.getService()) maxNon.setService(a.getService());
                    if (a.getVolley() > maxNon.getVolley()) maxNon.setVolley(a.getVolley());
                    if (a.getForehand() > maxNon.getForehand()) maxNon.setForehand(a.getForehand());
                    if (a.getBackhand() > maxNon.getBackhand()) maxNon.setBackhand(a.getBackhand());
                    if (total > maxNonTotal) maxNonTotal = total;
                }
            }
            maxNonUpgradeAttrsByType.put(e.getKey(), maxNon);
            maxUpgradeAttrsByType.put(e.getKey(), maxUp);
            maxNonUpgradeTotalByType.put(e.getKey(), maxNonTotal);
            maxUpgradeTotalByType.put(e.getKey(), maxUpTotal);
        }

        // Helpers to compute optimistic remaining contributions honoring remaining upgrade budget.
        // For totals: baseline = sum of non-upgrade maxima; deltas = upgradeMax - nonUpgradeMax per type; pick top k deltas.
        Function<GearType[], Integer> optimisticRemainingTotal = remaining -> {
            int baseline = 0;
            List<Integer> deltas = new ArrayList<>();
            for (GearType t : remaining) {
                int nonMax = maxNonUpgradeTotalByType.getOrDefault(t, 0);
                int upMax = maxUpgradeTotalByType.getOrDefault(t, 0);
                baseline += nonMax;
                deltas.add(Math.max(0, upMax - nonMax));
            }
            deltas.sort(Comparator.reverseOrder());
            int allowed = Math.max(0, upgradesAllowed);
            int take = Math.min(allowed, deltas.size());
            int add = 0;
            for (int i = 0; i < take; i++) add += deltas.get(i);
            return baseline + add;
        };

        // For per-attribute optimistic bounds do similar: baseline non-upgrade attrs + best k deltas for that attribute.
        BiFunction<GearType[], Integer, Attributes> optimisticRemainingAttrs = (remaining, remainingUpgrades) -> {
            Attributes baseline = new Attributes();
            // collect deltas per type for each attribute
            List<Integer> agilityDeltas = new ArrayList<>();
            List<Integer> enduranceDeltas = new ArrayList<>();
            List<Integer> serviceDeltas = new ArrayList<>();
            List<Integer> volleyDeltas = new ArrayList<>();
            List<Integer> forehandDeltas = new ArrayList<>();
            List<Integer> backhandDeltas = new ArrayList<>();
            for (GearType t : remaining) {
                Attributes nonA = maxNonUpgradeAttrsByType.getOrDefault(t, new Attributes());
                Attributes upA = maxUpgradeAttrsByType.getOrDefault(t, new Attributes());
                baseline.setAgility(baseline.getAgility() + nonA.getAgility());
                baseline.setEndurance(baseline.getEndurance() + nonA.getEndurance());
                baseline.setService(baseline.getService() + nonA.getService());
                baseline.setVolley(baseline.getVolley() + nonA.getVolley());
                baseline.setForehand(baseline.getForehand() + nonA.getForehand());
                baseline.setBackhand(baseline.getBackhand() + nonA.getBackhand());
                agilityDeltas.add(Math.max(0, upA.getAgility() - nonA.getAgility()));
                enduranceDeltas.add(Math.max(0, upA.getEndurance() - nonA.getEndurance()));
                serviceDeltas.add(Math.max(0, upA.getService() - nonA.getService()));
                volleyDeltas.add(Math.max(0, upA.getVolley() - nonA.getVolley()));
                forehandDeltas.add(Math.max(0, upA.getForehand() - nonA.getForehand()));
                backhandDeltas.add(Math.max(0, upA.getBackhand() - nonA.getBackhand()));
            }
            Comparator<Integer> desc = Comparator.reverseOrder();
            agilityDeltas.sort(desc);
            enduranceDeltas.sort(desc);
            serviceDeltas.sort(desc);
            volleyDeltas.sort(desc);
            forehandDeltas.sort(desc);
            backhandDeltas.sort(desc);
            int take = Math.max(0, Math.min(remainingUpgrades, agilityDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setAgility(baseline.getAgility() + agilityDeltas.get(i));
            take = Math.max(0, Math.min(remainingUpgrades, enduranceDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setEndurance(baseline.getEndurance() + enduranceDeltas.get(i));
            take = Math.max(0, Math.min(remainingUpgrades, serviceDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setService(baseline.getService() + serviceDeltas.get(i));
            take = Math.max(0, Math.min(remainingUpgrades, volleyDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setVolley(baseline.getVolley() + volleyDeltas.get(i));
            take = Math.max(0, Math.min(remainingUpgrades, forehandDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setForehand(baseline.getForehand() + forehandDeltas.get(i));
            take = Math.max(0, Math.min(remainingUpgrades, backhandDeltas.size()));
            for (int i = 0; i < take; i++) baseline.setBackhand(baseline.getBackhand() + backhandDeltas.get(i));
            return baseline;
        };

        // Base player attributes (from maxPlayer template)
        Attributes basePlayerAttrs = maxPlayer.getAttributes();
        int basePlayerValue = basePlayerAttrs.getAgility() + basePlayerAttrs.getEndurance() + basePlayerAttrs.getService() + basePlayerAttrs.getVolley() + basePlayerAttrs.getForehand() + basePlayerAttrs.getBackhand();

        // Parallelize outermost loop (rackets) to utilize CPU cores. Each parallel task uses its own local cache for itemUpgrades and pushes results into concurrentResults.
        itemsByType.getOrDefault(RACKET, Collections.emptyList()).parallelStream().forEach(racket -> {
            Map<GearItem, Boolean> itemUpgradesLocal = new HashMap<>();
            // compute current attributes/value including racket
            Attributes curAttrsR = new Attributes(basePlayerAttrs.getAgility() + racket.getAttributes().getAgility(), basePlayerAttrs.getEndurance() + racket.getAttributes().getEndurance(), basePlayerAttrs.getService() + racket.getAttributes().getService(), basePlayerAttrs.getVolley() + racket.getAttributes().getVolley(), basePlayerAttrs.getForehand() + racket.getAttributes().getForehand(), basePlayerAttrs.getBackhand() + racket.getAttributes().getBackhand());
            int curValR = basePlayerValue + (racket.getAttributes().getAgility() + racket.getAttributes().getEndurance() + racket.getAttributes().getService() + racket.getAttributes().getVolley() + racket.getAttributes().getForehand() + racket.getAttributes().getBackhand());
            GearType[] remainAfterR = new GearType[]{GRIP, SHOES, WRISTBAND, NUTRITION, WORKOUT};
            // remaining upgrades allowed after selecting racket
            int usedUpgradesR = (int) numberOfUpgrades(itemUpgradesLocal, racket, null, null, null, null, null);
            int remainingUpgradesAfterR = Math.max(0, upgradesAllowed - usedUpgradesR);
            Attributes optimisticAttrsAfterR = optimisticRemainingAttrs.apply(remainAfterR, remainingUpgradesAfterR);
            int optimisticTotalAfterR = optimisticRemainingTotal.apply(remainAfterR);
            if (minimumAttributes != null) {
                if (curAttrsR.getAgility() + optimisticAttrsAfterR.getAgility() < minimumAttributes.getAgility()
                        || curAttrsR.getEndurance() + optimisticAttrsAfterR.getEndurance() < minimumAttributes.getEndurance()
                        || curAttrsR.getService() + optimisticAttrsAfterR.getService() < minimumAttributes.getService()
                        || curAttrsR.getVolley() + optimisticAttrsAfterR.getVolley() < minimumAttributes.getVolley()
                        || curAttrsR.getForehand() + optimisticAttrsAfterR.getForehand() < minimumAttributes.getForehand()
                        || curAttrsR.getBackhand() + optimisticAttrsAfterR.getBackhand() < minimumAttributes.getBackhand()) {
                    return; // prune
                }
            }
            if (curValR + optimisticTotalAfterR < minTotalValue) return;
            if (numberOfUpgrades(itemUpgradesLocal, racket, null, null, null, null, null) > upgradesAllowed) return;

            for (GearItem grip : itemsByType.getOrDefault(GRIP, Collections.emptyList())) {
                Attributes curAttrsG = new Attributes(curAttrsR.getAgility() + grip.getAttributes().getAgility(), curAttrsR.getEndurance() + grip.getAttributes().getEndurance(), curAttrsR.getService() + grip.getAttributes().getService(), curAttrsR.getVolley() + grip.getAttributes().getVolley(), curAttrsR.getForehand() + grip.getAttributes().getForehand(), curAttrsR.getBackhand() + grip.getAttributes().getBackhand());
                int curValG = curValR + (grip.getAttributes().getAgility() + grip.getAttributes().getEndurance() + grip.getAttributes().getService() + grip.getAttributes().getVolley() + grip.getAttributes().getForehand() + grip.getAttributes().getBackhand());
                GearType[] remainAfterG = new GearType[]{SHOES, WRISTBAND, NUTRITION, WORKOUT};
                int usedUpgradesG = (int) numberOfUpgrades(itemUpgradesLocal, racket, grip, null, null, null, null);
                int remainingUpgradesAfterG = Math.max(0, upgradesAllowed - usedUpgradesG);
                Attributes optimisticAttrsAfterG = optimisticRemainingAttrs.apply(remainAfterG, remainingUpgradesAfterG);
                int optimisticTotalAfterG = optimisticRemainingTotal.apply(remainAfterG);
                if (minimumAttributes != null) {
                    if (curAttrsG.getAgility() + optimisticAttrsAfterG.getAgility() < minimumAttributes.getAgility()
                            || curAttrsG.getEndurance() + optimisticAttrsAfterG.getEndurance() < minimumAttributes.getEndurance()
                            || curAttrsG.getService() + optimisticAttrsAfterG.getService() < minimumAttributes.getService()
                            || curAttrsG.getVolley() + optimisticAttrsAfterG.getVolley() < minimumAttributes.getVolley()
                            || curAttrsG.getForehand() + optimisticAttrsAfterG.getForehand() < minimumAttributes.getForehand()
                            || curAttrsG.getBackhand() + optimisticAttrsAfterG.getBackhand() < minimumAttributes.getBackhand()) {
                        continue;
                    }
                }
                if (curValG + optimisticTotalAfterG < minTotalValue) continue;
                if (numberOfUpgrades(itemUpgradesLocal, racket, grip, null, null, null, null) > upgradesAllowed) continue;

                for (GearItem shoes : itemsByType.getOrDefault(SHOES, Collections.emptyList())) {
                    Attributes curAttrsS = new Attributes(curAttrsG.getAgility() + shoes.getAttributes().getAgility(), curAttrsG.getEndurance() + shoes.getAttributes().getEndurance(), curAttrsG.getService() + shoes.getAttributes().getService(), curAttrsG.getVolley() + shoes.getAttributes().getVolley(), curAttrsG.getForehand() + shoes.getAttributes().getForehand(), curAttrsG.getBackhand() + shoes.getAttributes().getBackhand());
                    int curValS = curValG + (shoes.getAttributes().getAgility() + shoes.getAttributes().getEndurance() + shoes.getAttributes().getService() + shoes.getAttributes().getVolley() + shoes.getAttributes().getForehand() + shoes.getAttributes().getBackhand());
                    GearType[] remainAfterS = new GearType[]{WRISTBAND, NUTRITION, WORKOUT};
                    int usedUpgradesS = (int) numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, null, null, null);
                    int remainingUpgradesAfterS = Math.max(0, upgradesAllowed - usedUpgradesS);
                    Attributes optimisticAttrsAfterS = optimisticRemainingAttrs.apply(remainAfterS, remainingUpgradesAfterS);
                    int optimisticTotalAfterS = optimisticRemainingTotal.apply(remainAfterS);
                    if (minimumAttributes != null) {
                        if (curAttrsS.getAgility() + optimisticAttrsAfterS.getAgility() < minimumAttributes.getAgility()
                                || curAttrsS.getEndurance() + optimisticAttrsAfterS.getEndurance() < minimumAttributes.getEndurance()
                                || curAttrsS.getService() + optimisticAttrsAfterS.getService() < minimumAttributes.getService()
                                || curAttrsS.getVolley() + optimisticAttrsAfterS.getVolley() < minimumAttributes.getVolley()
                                || curAttrsS.getForehand() + optimisticAttrsAfterS.getForehand() < minimumAttributes.getForehand()
                                || curAttrsS.getBackhand() + optimisticAttrsAfterS.getBackhand() < minimumAttributes.getBackhand()) {
                            continue;
                        }
                    }
                    if (curValS + optimisticTotalAfterS < minTotalValue) continue;
                    if (numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, null, null, null) > upgradesAllowed) continue;

                    for (GearItem wristband : itemsByType.getOrDefault(WRISTBAND, Collections.emptyList())) {
                        Attributes curAttrsW = new Attributes(curAttrsS.getAgility() + wristband.getAttributes().getAgility(), curAttrsS.getEndurance() + wristband.getAttributes().getEndurance(), curAttrsS.getService() + wristband.getAttributes().getService(), curAttrsS.getVolley() + wristband.getAttributes().getVolley(), curAttrsS.getForehand() + wristband.getAttributes().getForehand(), curAttrsS.getBackhand() + wristband.getAttributes().getBackhand());
                        int curValW = curValS + (wristband.getAttributes().getAgility() + wristband.getAttributes().getEndurance() + wristband.getAttributes().getService() + wristband.getAttributes().getVolley() + wristband.getAttributes().getForehand() + wristband.getAttributes().getBackhand());
                        GearType[] remainAfterW = new GearType[]{NUTRITION, WORKOUT};
                        int usedUpgradesW = (int) numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, wristband, null, null);
                        int remainingUpgradesAfterW = Math.max(0, upgradesAllowed - usedUpgradesW);
                        Attributes optimisticAttrsAfterW = optimisticRemainingAttrs.apply(remainAfterW, remainingUpgradesAfterW);
                        int optimisticTotalAfterW = optimisticRemainingTotal.apply(remainAfterW);
                        if (minimumAttributes != null) {
                            if (curAttrsW.getAgility() + optimisticAttrsAfterW.getAgility() < minimumAttributes.getAgility()
                                    || curAttrsW.getEndurance() + optimisticAttrsAfterW.getEndurance() < minimumAttributes.getEndurance()
                                    || curAttrsW.getService() + optimisticAttrsAfterW.getService() < minimumAttributes.getService()
                                    || curAttrsW.getVolley() + optimisticAttrsAfterW.getVolley() < minimumAttributes.getVolley()
                                    || curAttrsW.getForehand() + optimisticAttrsAfterW.getForehand() < minimumAttributes.getForehand()
                                    || curAttrsW.getBackhand() + optimisticAttrsAfterW.getBackhand() < minimumAttributes.getBackhand()) {
                                continue;
                            }
                        }
                        if (curValW + optimisticTotalAfterW < minTotalValue) continue;
                        if (numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, wristband, null, null) > upgradesAllowed) continue;

                        for (GearItem nutrition : itemsByType.getOrDefault(NUTRITION, Collections.emptyList())) {
                            Attributes curAttrsN = new Attributes(curAttrsW.getAgility() + nutrition.getAttributes().getAgility(), curAttrsW.getEndurance() + nutrition.getAttributes().getEndurance(), curAttrsW.getService() + nutrition.getAttributes().getService(), curAttrsW.getVolley() + nutrition.getAttributes().getVolley(), curAttrsW.getForehand() + nutrition.getAttributes().getForehand(), curAttrsW.getBackhand() + nutrition.getAttributes().getBackhand());
                            int curValN = curValW + (nutrition.getAttributes().getAgility() + nutrition.getAttributes().getEndurance() + nutrition.getAttributes().getService() + nutrition.getAttributes().getVolley() + nutrition.getAttributes().getForehand() + nutrition.getAttributes().getBackhand());
                            GearType[] remainAfterN = new GearType[]{WORKOUT};
                            int usedUpgradesN = (int) numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, wristband, nutrition, null);
                            int remainingUpgradesAfterN = Math.max(0, upgradesAllowed - usedUpgradesN);
                            Attributes optimisticAttrsAfterN = optimisticRemainingAttrs.apply(remainAfterN, remainingUpgradesAfterN);
                            int optimisticTotalAfterN = optimisticRemainingTotal.apply(remainAfterN);
                            if (minimumAttributes != null) {
                                if (curAttrsN.getAgility() + optimisticAttrsAfterN.getAgility() < minimumAttributes.getAgility()
                                        || curAttrsN.getEndurance() + optimisticAttrsAfterN.getEndurance() < minimumAttributes.getEndurance()
                                        || curAttrsN.getService() + optimisticAttrsAfterN.getService() < minimumAttributes.getService()
                                        || curAttrsN.getVolley() + optimisticAttrsAfterN.getVolley() < minimumAttributes.getVolley()
                                        || curAttrsN.getForehand() + optimisticAttrsAfterN.getForehand() < minimumAttributes.getForehand()
                                        || curAttrsN.getBackhand() + optimisticAttrsAfterN.getBackhand() < minimumAttributes.getBackhand()) {
                                    continue;
                                }
                            }
                            if (curValN + optimisticTotalAfterN < minTotalValue) continue;
                            if (numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, wristband, nutrition, null) > upgradesAllowed) continue;

                            for (GearItem workout : itemsByType.getOrDefault(WORKOUT, Collections.emptyList())) {
                                // final pruning: check attributes and totals with workout added
                                Attributes curAttrsFinal = new Attributes(curAttrsN.getAgility() + workout.getAttributes().getAgility(), curAttrsN.getEndurance() + workout.getAttributes().getEndurance(), curAttrsN.getService() + workout.getAttributes().getService(), curAttrsN.getVolley() + workout.getAttributes().getVolley(), curAttrsN.getForehand() + workout.getAttributes().getForehand(), curAttrsN.getBackhand() + workout.getAttributes().getBackhand());
                                int curValFinal = curValN + (workout.getAttributes().getAgility() + workout.getAttributes().getEndurance() + workout.getAttributes().getService() + workout.getAttributes().getVolley() + workout.getAttributes().getForehand() + workout.getAttributes().getBackhand());
                                if (minimumAttributes != null) {
                                    if (curAttrsFinal.getAgility() < minimumAttributes.getAgility()
                                            || curAttrsFinal.getEndurance() < minimumAttributes.getEndurance()
                                            || curAttrsFinal.getService() < minimumAttributes.getService()
                                            || curAttrsFinal.getVolley() < minimumAttributes.getVolley()
                                            || curAttrsFinal.getForehand() < minimumAttributes.getForehand()
                                            || curAttrsFinal.getBackhand() < minimumAttributes.getBackhand()) {
                                        continue;
                                    }
                                }
                                if (curValFinal < minTotalValue) continue;
                                if (numberOfUpgrades(itemUpgradesLocal, racket, grip, shoes, wristband, nutrition, workout) > upgradesAllowed) continue;

                                GearConfig gearConfig = GearConfig.of(racket, grip, shoes, wristband, nutrition, workout);
                                GameConfig gameConfig = new GameConfig(maxPlayer, gearConfig, upgradesAllowed > 0);
                                gameConfigsCounter.incrementAndGet();
                                if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, gameConfig)) {
                                    concurrentResults.add(gameConfig);
                                }
                            }
                        }
                    }
                }
            }
        });

        gameConfigsCount = gameConfigsCounter.get();
        List<GameConfig> results = new ArrayList<>(concurrentResults);
         String message = String.format("%d possible configurations, filtered only %d", gameConfigsCount, results.size());
         LOGGER.info(message);
        return results;
     }

     // Baseline sequential generator for benchmarking (no optimistic pruning, no parallelization)
     private List<GameConfig> generateReducedGameConfigsLegacy(Player maxPlayer, Attributes minimumAttributes, int minTotalValue, int maxLevel, int upgradesAllowed) {
        int gameConfigs = 0;
        List<GameConfig> results = new ArrayList<>();
        List<GearItem> leveledGearItems = gearItemService.leveledGearItems(maxLevel, upgradesAllowed);
        Map<GearType, List<GearItem>> itemsByType = leveledGearItems.stream().collect(Collectors.groupingBy(GearItem::getGearType));
        for (GearItem racket : itemsByType.getOrDefault(RACKET, Collections.emptyList())) {
            for (GearItem grip : itemsByType.getOrDefault(GRIP, Collections.emptyList())) {
                for (GearItem shoes : itemsByType.getOrDefault(SHOES, Collections.emptyList())) {
                    for (GearItem wristband : itemsByType.getOrDefault(WRISTBAND, Collections.emptyList())) {
                        for (GearItem nutrition : itemsByType.getOrDefault(NUTRITION, Collections.emptyList())) {
                            for (GearItem workout : itemsByType.getOrDefault(WORKOUT, Collections.emptyList())) {
                                GearConfig gearConfig = GearConfig.of(racket, grip, shoes, wristband, nutrition, workout);
                                GameConfig gameConfig = new GameConfig(maxPlayer, gearConfig, upgradesAllowed > 0);
                                gameConfigs++;
                                if (isSuitableConfig(minimumAttributes, minTotalValue, upgradesAllowed, gameConfig)) {
                                    results.add(gameConfig);
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("[LEGACY] {} possible configurations, filtered only {}", gameConfigs, results.size());
        return results;
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

    private long numberOfUpgrades(Map<GearItem, Boolean> itemUpgrades, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
        boolean racketUpgrade = isGearItemUpgrade(itemUpgrades, racket);
        boolean gripUpgrade = isGearItemUpgrade(itemUpgrades,grip);
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

    boolean isSuitableConfig(Attributes minimumAttributes, int minTotalValue, int upgradesAllowed, GameConfig gameConfig) {
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
