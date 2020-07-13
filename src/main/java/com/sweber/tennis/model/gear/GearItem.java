package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.config.OwnedGear;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sweber.tennis.model.gear.GearType.GRIP;
import static com.sweber.tennis.model.gear.GearType.NUTRITION;
import static com.sweber.tennis.model.gear.GearType.RACKET;
import static com.sweber.tennis.model.gear.GearType.SHOES;
import static com.sweber.tennis.model.gear.GearType.WORKOUT;
import static com.sweber.tennis.model.gear.GearType.WRISTBAND;

public enum GearItem {
    BASIC_RACKET(RACKET, new Config(0, 0, 0, 0, 3, 0, 0, 1)),
    EAGLE_5(RACKET, new Config(3, 0, 0, 0, 13, 0, 2500, 5)),
    EAGLE_6(RACKET, new Config(3, 0, 0, 0, 16, 0, 4600, 6)),
    EAGLE_7(RACKET, new Config(4, 0, 0, 0, 20, 0, 7200, 7)),
    EAGLE_8(RACKET, new Config(4, 0, 0, 0, 23, 0, 10900, 8)),
    EAGLE_9(RACKET, new Config(4, 0, 0, 0, 25, 0, 15000, 9)),
    EAGLE_10(RACKET, new Config(4, 0, 0, 0, 27, 0, 21700, 10)),
    EAGLE_11(RACKET, new Config(5, 0, 0, 0, 30, 0, 27500, 11)),
    PATRIOT_5(RACKET, new Config(0, 0, 0, 0, 15, 6, 0, 5)),
    PATRIOT_6(RACKET, new Config(0, 0, 0, 0, 19, 6, 0, 6)),
    PATRIOT_7(RACKET, new Config(0, 0, 0, 0, 23, 6, 0, 7)),
    PATRIOT_8(RACKET, new Config(0, 0, 0, 0, 25, 7, 0, 8)),
    PATRIOT_10(RACKET, new Config(0, 0, 0, 0, 30, 8, 0, 10)),
    PATRIOT_11(RACKET, new Config(0, 0, 0, 0, 33, 8, 0, 11)),
    PANTHER_3(RACKET, new Config(0, 0, 0, 0, 11, 0, 0, 3)),
    PANTHER_4(RACKET, new Config(0, 0, 0, 0, 14, 0, 15000, 4)),
    PANTHER_5(RACKET, new Config(0, 0, 0, 0, 18, 0, 15000, 5)),
    PANTHER_6(RACKET, new Config(0, 0, 0, 0, 22, 0, 15000, 6)),
    PANTHER_7(RACKET, new Config(0, 0, 0, 0, 26, 0, 15000, 7)),
    PANTHER_8(RACKET, new Config(0, 0, 0, 0, 28, 0, 15000, 8)),
    PANTHER_9(RACKET, new Config(0, 0, 0, 0, 31, 0, 15000, 9)),
    PANTHER_10(RACKET, new Config(0, 0, 0, 0, 34, 0, 15000, 10)),
    PANTHER_11(RACKET, new Config(0, 0, 0, 0, 36, 0, 15000, 11)),

    BASIC_GRIP(GRIP, new Config(0, 0, 0, 5, 0, 3, 0, 1)),
    WARRIOR_5(GRIP, new Config(0, 0, 0, 5, 0, 13, 0, 5)),
    WARRIOR_6(GRIP, new Config(0, 0, 0, 5, 0, 16, 0, 6)),
    WARRIOR_7(GRIP, new Config(0, 0, 0, 5, 0, 20, 0, 7)),
    WARRIOR_8(GRIP, new Config(0, 0, 0, 5, 0, 23, 0, 8)),
    WARRIOR_9(GRIP, new Config(0, 0, 0, 6, 0, 25, 0, 9)),
    WARRIOR_10(GRIP, new Config(0, 0, 0, 6, 0, 27, 0, 10)),
    WARRIOR_11(GRIP, new Config(0, 0, 0, 7, 0, 30, 0, 11)),
    TALON_5(GRIP, new Config(0, 0, 0, 0, 6, 15, 0, 5)),
    TALON_6(GRIP, new Config(0, 0, 0, 0, 6, 19, 0, 6)),
    TALON_7(GRIP, new Config(0, 0, 0, 0, 6, 23, 0, 7)),
    TALON_8(GRIP, new Config(0, 0, 0, 0, 7, 25, 0, 8)),
    TALON_9(GRIP, new Config(0, 0, 0, 0, 7, 28, 0, 9)),
    TALON_10(GRIP, new Config(0, 0, 0, 0, 7, 30, 0, 10)),
    TALON_11(GRIP, new Config(0, 0, 0, 0, 8, 33, 0, 11)),
    MACHETE_3(GRIP, new Config(0, 0, 0, 0, 0, 11, 0, 3)),
    MACHETE_4(GRIP, new Config(0, 0, 0, 0, 0, 14, 7200, 4)),
    MACHETE_5(GRIP, new Config(0, 0, 0, 0, 0, 18, 7200, 5)),
    MACHETE_6(GRIP, new Config(0, 0, 0, 0, 0, 22, 7200, 6)),
    MACHETE_7(GRIP, new Config(0, 0, 0, 0, 0, 26, 7200, 7)),
    MACHETE_8(GRIP, new Config(0, 0, 0, 0, 0, 28, 7200, 8)),
    MACHETE_9(GRIP, new Config(0, 0, 0, 0, 0, 31, 7200, 9)),
    MACHETE_10(GRIP, new Config(0, 0, 0, 0, 0, 34, 7200, 10)),
    MACHETE_11(GRIP, new Config(0, 0, 0, 0, 0, 36, 7200, 11)),

    BASIC_SHOES(SHOES, new Config(3, 0, 0, 0, 0, 0, 0, 1)),
    FEATHER_4(SHOES, new Config(18, 0, 0, 0, 0, 0, 0, 4)),
    FEATHER_5(SHOES, new Config(23, 0, 0, 0, 0, 0, 2500, 5)),
    FEATHER_6(SHOES, new Config(28, 0, 0, 0, 0, 0, 2500, 6)),
    FEATHER_7(SHOES, new Config(34, 0, 0, 0, 0, 0, 2500, 7)),
    FEATHER_8(SHOES, new Config(38, 0, 0, 0, 0, 0, 2500, 8)),
    FEATHER_11(SHOES, new Config(50, 0, 0, 0, 0, 0, 2500, 11)),
    RAPTOR_5(SHOES, new Config(19, 6, 0, 0, 0, 0, 0, 5)),
    RAPTOR_6(SHOES, new Config(24, 6, 0, 0, 0, 0, 0, 6)),
    RAPTOR_7(SHOES, new Config(29, 6, 0, 0, 0, 0, 10900, 7)),
    RAPTOR_8(SHOES, new Config(33, 7, 0, 0, 0, 0, 10900, 8)),
    RAPTOR_9(SHOES, new Config(36, 7, 0, 0, 0, 0, 10900, 9)),
    RAPTOR_10(SHOES, new Config(40, 8, 0, 0, 0, 0, 10900, 10)),
    RAPTOR_11(SHOES, new Config(44, 8, 0, 0, 0, 0, 10900, 11)),
    HUNTER_3(SHOES, new Config(9, 0, 4, 0, 0, 0, 0, 3)),
    HUNTER_4(SHOES, new Config(14, 0, 5, 0, 0, 0, 7200, 4)),
    HUNTER_5(SHOES, new Config(19, 0, 5, 0, 0, 0, 7200, 5)),
    HUNTER_6(SHOES, new Config(24, 0, 5, 0, 0, 0, 7200, 6)),
    HUNTER_7(SHOES, new Config(29, 0, 5, 0, 0, 0, 7200, 7)),
    HUNTER_8(SHOES, new Config(33, 0, 5, 0, 0, 0, 7200, 8)),
    HUNTER_9(SHOES, new Config(36, 0, 6, 0, 0, 0, 7200, 9)),
    HUNTER_10(SHOES, new Config(40, 0, 6, 0, 0, 0, 7200, 10)),
    HUNTER_11(SHOES, new Config(44, 0, 7, 0, 0, 0, 7200, 11)),
    PIRANHA_4(SHOES, new Config(10, 12, 0, 0, 0, 0, 0, 4)),
    PIRANHA_5(SHOES, new Config(15, 13, 0, 0, 0, 0, 21700, 5)),
    PIRANHA_6(SHOES, new Config(20, 14, 0, 0, 0, 0, 21700, 6)),
    PIRANHA_7(SHOES, new Config(25, 15, 0, 0, 0, 0, 21700, 7)),
    PIRANHA_8(SHOES, new Config(28, 15, 0, 0, 0, 0, 21700, 8)),
    PIRANHA_11(SHOES, new Config(39, 18, 0, 0, 0, 0, 21700, 11)),

    BASIC_WRIST(WRISTBAND, new Config(0, 0, 0, 0, 0, 0, 0, 0)),
    TOMAHAWK_5(WRISTBAND, new Config(0, 0, 0, 16, 0, 0, 0, 5)),
    TOMAHAWK_6(WRISTBAND, new Config(0, 0, 0, 21, 0, 0, 0, 6)),
    TOMAHAWK_7(WRISTBAND, new Config(0, 0, 0, 25, 0, 0, 0, 7)),
    TOMAHAWK_8(WRISTBAND, new Config(0, 0, 0, 28, 0, 0, 0, 8)),
    TOMAHAWK_9(WRISTBAND, new Config(0, 0, 0, 31, 0, 0, 0, 9)),
    TOMAHAWK_11(WRISTBAND, new Config(0, 0, 0, 37, 0, 0, 0, 11)),
    MISSILE_3(WRISTBAND, new Config(3, 0, 0, 3, 0, 0, 0, 3)),
    MISSILE_4(WRISTBAND, new Config(3, 0, 0, 6, 0, 0, 2500, 4)),
    MISSILE_5(WRISTBAND, new Config(3, 0, 0, 10, 0, 0, 2500, 5)),
    MISSILE_6(WRISTBAND, new Config(3, 0, 0, 14, 0, 0, 2500, 6)),
    MISSILE_7(WRISTBAND, new Config(4, 0, 0, 19, 0, 0, 2500, 7)),
    MISSILE_8(WRISTBAND, new Config(4, 0, 0, 21, 0, 0, 2500, 8)),
    MISSILE_9(WRISTBAND, new Config(4, 0, 0, 24, 0, 0, 2500, 9)),
    MISSILE_10(WRISTBAND, new Config(4, 0, 0, 26, 0, 0, 2500, 10)),
    MISSILE_11(WRISTBAND, new Config(5, 0, 0, 28, 0, 0, 2500, 11)),
    PIRATE_5(WRISTBAND, new Config(0, 0, 11, 5, 0, 0, 0, 5)),
    PIRATE_6(WRISTBAND, new Config(0, 0, 12, 9, 0, 0, 0, 6)),
    PIRATE_7(WRISTBAND, new Config(0, 0, 12, 13, 0, 0, 0, 7)),
    PIRATE_8(WRISTBAND, new Config(0, 0, 13, 15, 0, 0, 0, 8)),
    PIRATE_9(WRISTBAND, new Config(0, 0, 14, 17, 0, 0, 0, 9)),
    PIRATE_11(WRISTBAND, new Config(0, 0, 15, 21, 0, 0, 0, 11)),
    ARA_1(WRISTBAND, new Config(0, 0, 3, 0, 0, 0, 0, 1)),
    ARA_2(WRISTBAND, new Config(0, 0, 4, 1, 0, 0, 7200, 2)),
    ARA_3(WRISTBAND, new Config(0, 0, 4, 3, 0, 0, 7200, 3)),
    ARA_4(WRISTBAND, new Config(0, 0, 5, 6, 0, 0, 7200, 4)),
    ARA_5(WRISTBAND, new Config(0, 0, 5, 10, 0, 0, 7200, 5)),
    ARA_6(WRISTBAND, new Config(0, 0, 5, 14, 0, 0, 7200, 6)),
    ARA_7(WRISTBAND, new Config(0, 0, 5, 19, 0, 0, 7200, 7)),
    ARA_8(WRISTBAND, new Config(0, 0, 5, 21, 0, 0, 7200, 8)),
    ARA_9(WRISTBAND, new Config(0, 0, 6, 24, 0, 0, 7200, 9)),
    ARA_10(WRISTBAND, new Config(0, 0, 6, 26, 0, 0, 7200, 10)),
    ARA_11(WRISTBAND, new Config(0, 0, 7, 28, 0, 0, 7200, 11)),

    BASIC_NUTRITION(NUTRITION, new Config(0, 0, 0, 0, 0, 0, 0, 1)),
    PROTEIN_4(NUTRITION, new Config(0, 13, 0, 0, 0, 0, 0, 4)),
    PROTEIN_5(NUTRITION, new Config(0, 17, 0, 0, 0, 0, 0, 5)),
    PROTEIN_6(NUTRITION, new Config(0, 22, 0, 0, 0, 0, 0, 6)),
    PROTEIN_7(NUTRITION, new Config(0, 26, 0, 0, 0, 0, 0, 7)),
    PROTEIN_8(NUTRITION, new Config(0, 29, 0, 0, 0, 0, 0, 8)),
    PROTEIN_11(NUTRITION, new Config(0, 38, 0, 0, 0, 0, 0, 11)),
    HYDRATION_5(NUTRITION, new Config(0, 10, 0, 0, 4, 0, 0, 5)),
    HYDRATION_6(NUTRITION, new Config(0, 14, 0, 0, 4, 0, 7200, 6)),
    HYDRATION_7(NUTRITION, new Config(0, 18, 0, 0, 4, 0, 7200, 7)),
    HYDRATION_8(NUTRITION, new Config(0, 21, 0, 0, 4, 0, 7200, 8)),
    HYDRATION_9(NUTRITION, new Config(0, 23, 0, 0, 5, 0, 7200, 9)),
    HYDRATION_10(NUTRITION, new Config(0, 25, 0, 0, 5, 0, 7200, 10)),
    HYDRATION_11(NUTRITION, new Config(0, 28, 0, 0, 5, 0, 7200, 11)),
    MACROBIOTICS_4(NUTRITION, new Config(0, 0, 0, 11, 0, 0, 0, 4)),
    MACROBIOTICS_5(NUTRITION, new Config(0, 4, 0, 11, 0, 0, 10900, 5)),
    MACROBIOTICS_6(NUTRITION, new Config(0, 8, 0, 11, 0, 0, 10900, 6)),
    MACROBIOTICS_7(NUTRITION, new Config(0, 12, 0, 12, 0, 0, 10900, 7)),
    MACROBIOTICS_8(NUTRITION, new Config(0, 14, 0, 12, 0, 0, 10900, 8)),
    MACROBIOTICS_9(NUTRITION, new Config(0, 15, 0, 13, 0, 0, 10900, 9)),
    MACROBIOTICS_10(NUTRITION, new Config(0, 17, 0, 14, 0, 0, 10900, 10)),
    MACROBIOTICS_11(NUTRITION, new Config(0, 19, 0, 15, 0, 0, 10900, 11)),
    VEGAN_1(NUTRITION, new Config(0, 0, 0, 3, 0, 0, 0, 1)),
    VEGAN_2(NUTRITION, new Config(0, 1, 0, 4, 0, 0, 7200, 2)),
    VEGAN_3(NUTRITION, new Config(0, 3, 0, 4, 0, 0, 7200, 3)),
    VEGAN_4(NUTRITION, new Config(0, 6, 0, 5, 0, 0, 7200, 4)),
    VEGAN_5(NUTRITION, new Config(0, 10, 0, 5, 0, 0, 7200, 5)),
    VEGAN_6(NUTRITION, new Config(0, 14, 0, 5, 0, 0, 7200, 6)),
    VEGAN_7(NUTRITION, new Config(0, 18, 0, 5, 0, 0, 7200, 7)),
    VEGAN_8(NUTRITION, new Config(0, 21, 0, 5, 0, 0, 7200, 8)),
    VEGAN_9(NUTRITION, new Config(0, 23, 0, 6, 0, 0, 7200, 9)),
    VEGAN_10(NUTRITION, new Config(0, 25, 0, 6, 0, 0, 7200, 10)),
    VEGAN_11(NUTRITION, new Config(0, 28, 0, 7, 0, 0, 7200, 11)),

    BASIC_TRAINING(WORKOUT, new Config(0, 0, 1, 0, 0, 0, 0, 1)),
    ENDURANCE_5(WORKOUT, new Config(0, 6, 11, 0, 0, 0, 0, 5)),
    ENDURANCE_6(WORKOUT, new Config(0, 6, 16, 0, 0, 0, 0, 6)),
    ENDURANCE_7(WORKOUT, new Config(0, 6, 20, 0, 0, 0, 0, 7)),
    ENDURANCE_8(WORKOUT, new Config(0, 7, 23, 0, 0, 0, 0, 8)),
    ENDURANCE_9(WORKOUT, new Config(0, 7, 25, 0, 0, 0, 0, 9)),
    ENDURANCE_10(WORKOUT, new Config(0, 8, 27, 0, 0, 0, 0, 10)),
    ENDURANCE_11(WORKOUT, new Config(0, 8, 30, 0, 0, 0, 0, 11)),
    PLYOMETRICS_3(WORKOUT, new Config(0, 0, 4, 0, 0, 3, 0, 3)),
    PLYOMETRICS_4(WORKOUT, new Config(0, 0, 7, 0, 0, 4, 7200, 4)),
    PLYOMETRICS_5(WORKOUT, new Config(0, 0, 11, 0, 0, 4, 7200, 5)),
    PLYOMETRICS_6(WORKOUT, new Config(0, 0, 16, 0, 0, 4, 7200, 6)),
    PLYOMETRICS_7(WORKOUT, new Config(0, 0, 20, 0, 0, 4, 7200, 7)),
    PLYOMETRICS_8(WORKOUT, new Config(0, 0, 23, 0, 0, 4, 7200, 8)),
    PLYOMETRICS_9(WORKOUT, new Config(0, 0, 25, 0, 0, 5, 7200, 9)),
    PLYOMETRICS_10(WORKOUT, new Config(0, 0, 27, 0, 0, 5, 7200, 10)),
    PLYOMETRICS_11(WORKOUT, new Config(0, 0, 30, 0, 0, 5, 7200, 11));

    private final Config config;
    private final GearType gearType;

    GearItem(GearType gearType, Config config) {
        this.gearType = gearType;
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public GearType getGearType() {
        return gearType;
    }

    public static List<GearItem> maxLevel(int maxLevel, int upgradesAllowed) {
        return Arrays.stream(GearItem.values())
                .filter(item -> item.getConfig().getLevel() <= maxLevel)
                .filter(item -> upgradesAllowed == 0 || OwnedGear.isPossibleUpgrade(item))
                .collect(Collectors.toList());
    }
}
