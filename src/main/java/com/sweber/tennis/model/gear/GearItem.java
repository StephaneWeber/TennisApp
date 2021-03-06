package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;

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
    BASIC_RACKET(RACKET, new Config(new Attributes(0, 0, 0, 0, 3, 0), 0, 1)),
    EAGLE_5(RACKET, new Config(new Attributes(3, 0, 0, 0, 13, 0), 2500, 5)),
    EAGLE_6(RACKET, new Config(new Attributes(3, 0, 0, 0, 16, 0), 4600, 6)),
    EAGLE_7(RACKET, new Config(new Attributes(4, 0, 0, 0, 20, 0), 7200, 7)),
    EAGLE_8(RACKET, new Config(new Attributes(4, 0, 0, 0, 23, 0), 10900, 8)),
    EAGLE_9(RACKET, new Config(new Attributes(4, 0, 0, 0, 25, 0), 15000, 9)),
    EAGLE_10(RACKET, new Config(new Attributes(4, 0, 0, 0, 27, 0), 21700, 10)),
    EAGLE_11(RACKET, new Config(new Attributes(5, 0, 0, 0, 30, 0), 27500, 11)),
    PATRIOT_5(RACKET, new Config(new Attributes(0, 0, 0, 0, 15, 6), 0, 5)),
    PATRIOT_6(RACKET, new Config(new Attributes(0, 0, 0, 0, 19, 6), 7200, 6)),
    PATRIOT_7(RACKET, new Config(new Attributes(0, 0, 0, 0, 23, 6), 10900, 7)),
    PATRIOT_8(RACKET, new Config(new Attributes(0, 0, 0, 0, 25, 7), 15000, 8)),
    PATRIOT_9(RACKET, new Config(new Attributes(0, 0, 0, 0, 28, 7), 21700, 9)),
    PATRIOT_10(RACKET, new Config(new Attributes(0, 0, 0, 0, 30, 8), 0, 10)),
    PATRIOT_11(RACKET, new Config(new Attributes(0, 0, 0, 0, 33, 8), 0, 11)),
    OUTBACK_4(RACKET, new Config(new Attributes(0, 0, 0, 0, 5, 8), 0, 4)),
    OUTBACK_5(RACKET, new Config(new Attributes(0, 0, 0, 0, 8, 9), 10900, 5)),
    OUTBACK_6(RACKET, new Config(new Attributes(0, 0, 0, 0, 12, 9), 15000, 6)),
    OUTBACK_7(RACKET, new Config(new Attributes(0, 0, 0, 0, 16, 9), 21700, 7)),
    OUTBACK_8(RACKET, new Config(new Attributes(0, 0, 0, 0, 18, 10), 27500, 8)),
    OUTBACK_9(RACKET, new Config(new Attributes(0, 0, 0, 0, 20, 11), 35900, 9)),
    OUTBACK_11(RACKET, new Config(new Attributes(0, 0, 0, 0, 24, 12), 0, 11)),
    PANTHER_3(RACKET, new Config(new Attributes(0, 0, 0, 0, 11, 0), 0, 3)),
    PANTHER_4(RACKET, new Config(new Attributes(0, 0, 0, 0, 14, 0), 15000, 4)),
    PANTHER_5(RACKET, new Config(new Attributes(0, 0, 0, 0, 18, 0), 21700, 5)),
    PANTHER_6(RACKET, new Config(new Attributes(0, 0, 0, 0, 22, 0), 27500, 6)),
    PANTHER_7(RACKET, new Config(new Attributes(0, 0, 0, 0, 26, 0), 35900, 7)),
    PANTHER_8(RACKET, new Config(new Attributes(0, 0, 0, 0, 28, 0), 45900, 8)),
    PANTHER_9(RACKET, new Config(new Attributes(0, 0, 0, 0, 31, 0), 58400, 9)),
    PANTHER_10(RACKET, new Config(new Attributes(0, 0, 0, 0, 34, 0), 75000, 10)),
    PANTHER_11(RACKET, new Config(new Attributes(0, 0, 0, 0, 36, 0), 104200, 11)),
    SAMURAI_3(RACKET, new Config(new Attributes(3, 0, 0, 0, 6, 0), 0, 3)),
    SAMURAI_4(RACKET, new Config(new Attributes(4, 0, 0, 0, 9, 0), 15000, 4)),
    SAMURAI_5(RACKET, new Config(new Attributes(4, 0, 0, 0, 12, 0), 21700, 5)),
    SAMURAI_6(RACKET, new Config(new Attributes(4, 0, 0, 0, 16, 0), 27500, 6)),
    SAMURAI_7(RACKET, new Config(new Attributes(4, 0, 0, 0, 20, 0), 35900, 7)),
    SAMURAI_8(RACKET, new Config(new Attributes(4, 0, 0, 0, 22, 0), 49500, 8)),
    SAMURAI_9(RACKET, new Config(new Attributes(5, 0, 0, 0, 24, 0), 58400, 9)),
    SAMURAI_10(RACKET, new Config(new Attributes(5, 0, 0, 0, 27, 0), 75000, 10)),
    SAMURAI_11(RACKET, new Config(new Attributes(5, 0, 0, 0, 29, 0), 87500, 11)),
    SAMURAI_12(RACKET, new Config(new Attributes(6, 0, 0, 0, 31, 0), 0, 12)),
    SAMURAI_13(RACKET, new Config(new Attributes(6, 0, 0, 0, 33, 0), 183400, 13)),
    HAMMER_4(RACKET, new Config(new Attributes(0, 0, 0, 0, 12, 6), 0, 4)),
    HAMMER_5(RACKET, new Config(new Attributes(0, 0, 0, 0, 15, 7), 45900, 5)),
    HAMMER_6(RACKET, new Config(new Attributes(0, 0, 0, 0, 19, 7), 58400, 6)),
    HAMMER_7(RACKET, new Config(new Attributes(0, 0, 0, 0, 23, 7), 75000, 7)),
    HAMMER_8(RACKET, new Config(new Attributes(0, 0, 0, 0, 26, 7), 87500, 8)),
    HAMMER_9(RACKET, new Config(new Attributes(0, 0, 0, 0, 28, 8), 104200, 9)),
    HAMMER_10(RACKET, new Config(new Attributes(0, 0, 0, 0, 31, 9), 116700, 10)),
    HAMMER_11(RACKET, new Config(new Attributes(0, 0, 0, 0, 33, 9), 150000, 11)),
    HAMMER_12(RACKET, new Config(new Attributes(0, 0, 0, 0, 36, 10), 183400, 12)),
    HAMMER_13(RACKET, new Config(new Attributes(0, 0, 0, 0, 38, 10), 216700, 13)),
    BULLS_EYE_4(RACKET, new Config(new Attributes(0, 0, 0, 0, 15, 0), 0, 4)),
    BULLS_EYE_5(RACKET, new Config(new Attributes(0, 0, 0, 0, 18, 0), 0, 5)),
    BULLS_EYE_7(RACKET, new Config(new Attributes(0, 0, 0, 0, 27, 0), 0, 7)),
    BULLS_EYE_8(RACKET, new Config(new Attributes(0, 0, 0, 0, 29, 0), 183400, 8)),
    BULLS_EYE_9(RACKET, new Config(new Attributes(0, 0, 0, 0, 32, 0), 0, 9)),
    BULLS_EYE_13(RACKET, new Config(new Attributes(0, 0, 0, 0, 43, 0), 0, 13)),

    BASIC_GRIP(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 3), 0, 1)),
    WARRIOR_5(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 13), 0, 5)),
    WARRIOR_6(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 16), 0, 6)),
    WARRIOR_7(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 20), 0, 7)),
    WARRIOR_8(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 23), 0, 8)),
    WARRIOR_9(GRIP, new Config(new Attributes(0, 0, 0, 6, 0, 25), 0, 9)),
    WARRIOR_10(GRIP, new Config(new Attributes(0, 0, 0, 6, 0, 27), 0, 10)),
    WARRIOR_11(GRIP, new Config(new Attributes(0, 0, 0, 7, 0, 30), 0, 11)),
    TALON_5(GRIP, new Config(new Attributes(0, 0, 0, 0, 6, 15), 0, 5)),
    TALON_6(GRIP, new Config(new Attributes(0, 0, 0, 0, 6, 19), 0, 6)),
    TALON_7(GRIP, new Config(new Attributes(0, 0, 0, 0, 6, 23), 0, 7)),
    TALON_8(GRIP, new Config(new Attributes(0, 0, 0, 0, 7, 25), 0, 8)),
    TALON_9(GRIP, new Config(new Attributes(0, 0, 0, 0, 7, 28), 0, 9)),
    TALON_10(GRIP, new Config(new Attributes(0, 0, 0, 0, 7, 30), 0, 10)),
    TALON_11(GRIP, new Config(new Attributes(0, 0, 0, 0, 8, 33), 0, 11)),
    MACHETE_3(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 11), 0, 3)),
    MACHETE_4(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 14), 7200, 4)),
    MACHETE_5(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 18), 7200, 5)),
    MACHETE_6(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 22), 7200, 6)),
    MACHETE_7(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 26), 7200, 7)),
    MACHETE_8(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 28), 7200, 8)),
    MACHETE_9(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 31), 7200, 9)),
    MACHETE_10(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 34), 7200, 10)),
    MACHETE_11(GRIP, new Config(new Attributes(0, 0, 0, 0, 0, 36), 7200, 11)),
    KATANA_3(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 6), 0, 3)),
    KATANA_4(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 9), 15000, 4)),
    KATANA_5(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 12), 0, 5)),
    KATANA_6(GRIP, new Config(new Attributes(0, 0, 0, 5, 0, 16), 27500, 6)),
    KATANA_7(GRIP, new Config(new Attributes(0, 0, 0, 6, 0, 20), 35900, 7)),
    KATANA_8(GRIP, new Config(new Attributes(0, 0, 0, 6, 0, 22), 45900, 8)),
    KATANA_9(GRIP, new Config(new Attributes(0, 0, 0, 6, 0, 24), 58400, 9)),
    KATANA_10(GRIP, new Config(new Attributes(0, 0, 0, 7, 0, 27), 75000, 10)),
    KATANA_11(GRIP, new Config(new Attributes(0, 0, 0, 7, 0, 29), 87500, 11)),
    KATANA_12(GRIP, new Config(new Attributes(0, 0, 0, 8, 0, 31), 104200, 12)),
    KATANA_13(GRIP, new Config(new Attributes(0, 0, 0, 8, 0, 33), 116700, 13)),

    BASIC_SHOES(SHOES, new Config(new Attributes(3, 0, 0, 0, 0, 0), 0, 1)),
    FEATHER_4(SHOES, new Config(new Attributes(18, 0, 0, 0, 0, 0), 0, 4)),
    FEATHER_5(SHOES, new Config(new Attributes(23, 0, 0, 0, 0, 0), 2500, 5)),
    FEATHER_6(SHOES, new Config(new Attributes(28, 0, 0, 0, 0, 0), 2500, 6)),
    FEATHER_7(SHOES, new Config(new Attributes(34, 0, 0, 0, 0, 0), 2500, 7)),
    FEATHER_8(SHOES, new Config(new Attributes(38, 0, 0, 0, 0, 0), 2500, 8)),
    FEATHER_11(SHOES, new Config(new Attributes(50, 0, 0, 0, 0, 0), 2500, 11)),
    RAPTOR_5(SHOES, new Config(new Attributes(19, 6, 0, 0, 0, 0), 0, 5)),
    RAPTOR_6(SHOES, new Config(new Attributes(24, 6, 0, 0, 0, 0), 0, 6)),
    RAPTOR_7(SHOES, new Config(new Attributes(29, 6, 0, 0, 0, 0), 10900, 7)),
    RAPTOR_8(SHOES, new Config(new Attributes(33, 7, 0, 0, 0, 0), 10900, 8)),
    RAPTOR_9(SHOES, new Config(new Attributes(36, 7, 0, 0, 0, 0), 10900, 9)),
    RAPTOR_10(SHOES, new Config(new Attributes(40, 8, 0, 0, 0, 0), 10900, 10)),
    RAPTOR_11(SHOES, new Config(new Attributes(44, 8, 0, 0, 0, 0), 10900, 11)),
    HUNTER_3(SHOES, new Config(new Attributes(9, 0, 4, 0, 0, 0), 0, 3)),
    HUNTER_4(SHOES, new Config(new Attributes(14, 0, 5, 0, 0, 0), 7200, 4)),
    HUNTER_5(SHOES, new Config(new Attributes(19, 0, 5, 0, 0, 0), 7200, 5)),
    HUNTER_6(SHOES, new Config(new Attributes(24, 0, 5, 0, 0, 0), 7200, 6)),
    HUNTER_7(SHOES, new Config(new Attributes(29, 0, 5, 0, 0, 0), 7200, 7)),
    HUNTER_8(SHOES, new Config(new Attributes(33, 0, 5, 0, 0, 0), 7200, 8)),
    HUNTER_9(SHOES, new Config(new Attributes(36, 0, 6, 0, 0, 0), 7200, 9)),
    HUNTER_10(SHOES, new Config(new Attributes(40, 0, 6, 0, 0, 0), 7200, 10)),
    HUNTER_11(SHOES, new Config(new Attributes(44, 0, 7, 0, 0, 0), 7200, 11)),
    PIRANHA_4(SHOES, new Config(new Attributes(10, 12, 0, 0, 0, 0), 0, 4)),
    PIRANHA_5(SHOES, new Config(new Attributes(15, 13, 0, 0, 0, 0), 21700, 5)),
    PIRANHA_6(SHOES, new Config(new Attributes(20, 14, 0, 0, 0, 0), 21700, 6)),
    PIRANHA_7(SHOES, new Config(new Attributes(25, 15, 0, 0, 0, 0), 21700, 7)),
    PIRANHA_8(SHOES, new Config(new Attributes(28, 15, 0, 0, 0, 0), 21700, 8)),
    PIRANHA_11(SHOES, new Config(new Attributes(39, 18, 0, 0, 0, 0), 21700, 11)),

    BASIC_WRIST(WRISTBAND, new Config(new Attributes(0, 0, 0, 0, 0, 0), 0, 0)),
    TOMAHAWK_5(WRISTBAND, new Config(new Attributes(0, 0, 0, 16, 0, 0), 0, 5)),
    TOMAHAWK_6(WRISTBAND, new Config(new Attributes(0, 0, 0, 21, 0, 0), 0, 6)),
    TOMAHAWK_7(WRISTBAND, new Config(new Attributes(0, 0, 0, 25, 0, 0), 0, 7)),
    TOMAHAWK_8(WRISTBAND, new Config(new Attributes(0, 0, 0, 28, 0, 0), 0, 8)),
    TOMAHAWK_9(WRISTBAND, new Config(new Attributes(0, 0, 0, 31, 0, 0), 0, 9)),
    TOMAHAWK_11(WRISTBAND, new Config(new Attributes(0, 0, 0, 37, 0, 0), 0, 11)),
    MISSILE_3(WRISTBAND, new Config(new Attributes(3, 0, 0, 3, 0, 0), 0, 3)),
    MISSILE_4(WRISTBAND, new Config(new Attributes(3, 0, 0, 6, 0, 0), 2500, 4)),
    MISSILE_5(WRISTBAND, new Config(new Attributes(3, 0, 0, 10, 0, 0), 2500, 5)),
    MISSILE_6(WRISTBAND, new Config(new Attributes(3, 0, 0, 14, 0, 0), 2500, 6)),
    MISSILE_7(WRISTBAND, new Config(new Attributes(4, 0, 0, 19, 0, 0), 2500, 7)),
    MISSILE_8(WRISTBAND, new Config(new Attributes(4, 0, 0, 21, 0, 0), 2500, 8)),
    MISSILE_9(WRISTBAND, new Config(new Attributes(4, 0, 0, 24, 0, 0), 2500, 9)),
    MISSILE_10(WRISTBAND, new Config(new Attributes(4, 0, 0, 26, 0, 0), 2500, 10)),
    MISSILE_11(WRISTBAND, new Config(new Attributes(5, 0, 0, 28, 0, 0), 2500, 11)),
    PIRATE_5(WRISTBAND, new Config(new Attributes(0, 0, 11, 5, 0, 0), 0, 5)),
    PIRATE_6(WRISTBAND, new Config(new Attributes(0, 0, 12, 9, 0, 0), 0, 6)),
    PIRATE_7(WRISTBAND, new Config(new Attributes(0, 0, 12, 13, 0, 0), 0, 7)),
    PIRATE_8(WRISTBAND, new Config(new Attributes(0, 0, 13, 15, 0, 0), 0, 8)),
    PIRATE_9(WRISTBAND, new Config(new Attributes(0, 0, 14, 17, 0, 0), 0, 9)),
    PIRATE_11(WRISTBAND, new Config(new Attributes(0, 0, 15, 21, 0, 0), 0, 11)),
    ARA_1(WRISTBAND, new Config(new Attributes(0, 0, 3, 0, 0, 0), 0, 1)),
    ARA_2(WRISTBAND, new Config(new Attributes(0, 0, 4, 1, 0, 0), 7200, 2)),
    ARA_3(WRISTBAND, new Config(new Attributes(0, 0, 4, 3, 0, 0), 7200, 3)),
    ARA_4(WRISTBAND, new Config(new Attributes(0, 0, 5, 6, 0, 0), 7200, 4)),
    ARA_5(WRISTBAND, new Config(new Attributes(0, 0, 5, 10, 0, 0), 7200, 5)),
    ARA_6(WRISTBAND, new Config(new Attributes(0, 0, 5, 14, 0, 0), 7200, 6)),
    ARA_7(WRISTBAND, new Config(new Attributes(0, 0, 5, 19, 0, 0), 7200, 7)),
    ARA_8(WRISTBAND, new Config(new Attributes(0, 0, 5, 21, 0, 0), 7200, 8)),
    ARA_9(WRISTBAND, new Config(new Attributes(0, 0, 6, 24, 0, 0), 7200, 9)),
    ARA_10(WRISTBAND, new Config(new Attributes(0, 0, 6, 26, 0, 0), 7200, 10)),
    ARA_11(WRISTBAND, new Config(new Attributes(0, 0, 7, 28, 0, 0), 7200, 11)),

    BASIC_NUTRITION(NUTRITION, new Config(new Attributes(0, 0, 0, 0, 0, 0), 0, 1)),
    PROTEIN_4(NUTRITION, new Config(new Attributes(0, 13, 0, 0, 0, 0), 0, 4)),
    PROTEIN_5(NUTRITION, new Config(new Attributes(0, 17, 0, 0, 0, 0), 0, 5)),
    PROTEIN_6(NUTRITION, new Config(new Attributes(0, 22, 0, 0, 0, 0), 0, 6)),
    PROTEIN_7(NUTRITION, new Config(new Attributes(0, 26, 0, 0, 0, 0), 0, 7)),
    PROTEIN_8(NUTRITION, new Config(new Attributes(0, 29, 0, 0, 0, 0), 0, 8)),
    PROTEIN_11(NUTRITION, new Config(new Attributes(0, 38, 0, 0, 0, 0), 0, 11)),
    HYDRATION_5(NUTRITION, new Config(new Attributes(0, 10, 0, 0, 4, 0), 0, 5)),
    HYDRATION_6(NUTRITION, new Config(new Attributes(0, 14, 0, 0, 4, 0), 7200, 6)),
    HYDRATION_7(NUTRITION, new Config(new Attributes(0, 18, 0, 0, 4, 0), 7200, 7)),
    HYDRATION_8(NUTRITION, new Config(new Attributes(0, 21, 0, 0, 4, 0), 7200, 8)),
    HYDRATION_9(NUTRITION, new Config(new Attributes(0, 23, 0, 0, 5, 0), 7200, 9)),
    HYDRATION_10(NUTRITION, new Config(new Attributes(0, 25, 0, 0, 5, 0), 7200, 10)),
    HYDRATION_11(NUTRITION, new Config(new Attributes(0, 28, 0, 0, 5, 0), 7200, 11)),
    MACROBIOTICS_4(NUTRITION, new Config(new Attributes(0, 0, 0, 11, 0, 0), 0, 4)),
    MACROBIOTICS_5(NUTRITION, new Config(new Attributes(0, 4, 0, 11, 0, 0), 10900, 5)),
    MACROBIOTICS_6(NUTRITION, new Config(new Attributes(0, 8, 0, 11, 0, 0), 10900, 6)),
    MACROBIOTICS_7(NUTRITION, new Config(new Attributes(0, 12, 0, 12, 0, 0), 10900, 7)),
    MACROBIOTICS_8(NUTRITION, new Config(new Attributes(0, 14, 0, 12, 0, 0), 10900, 8)),
    MACROBIOTICS_9(NUTRITION, new Config(new Attributes(0, 15, 0, 13, 0, 0), 10900, 9)),
    MACROBIOTICS_10(NUTRITION, new Config(new Attributes(0, 17, 0, 14, 0, 0), 10900, 10)),
    MACROBIOTICS_11(NUTRITION, new Config(new Attributes(0, 19, 0, 15, 0, 0), 10900, 11)),
    VEGAN_1(NUTRITION, new Config(new Attributes(0, 0, 0, 3, 0, 0), 0, 1)),
    VEGAN_2(NUTRITION, new Config(new Attributes(0, 1, 0, 4, 0, 0), 7200, 2)),
    VEGAN_3(NUTRITION, new Config(new Attributes(0, 3, 0, 4, 0, 0), 7200, 3)),
    VEGAN_4(NUTRITION, new Config(new Attributes(0, 6, 0, 5, 0, 0), 7200, 4)),
    VEGAN_5(NUTRITION, new Config(new Attributes(0, 10, 0, 5, 0, 0), 7200, 5)),
    VEGAN_6(NUTRITION, new Config(new Attributes(0, 14, 0, 5, 0, 0), 7200, 6)),
    VEGAN_7(NUTRITION, new Config(new Attributes(0, 18, 0, 5, 0, 0), 7200, 7)),
    VEGAN_8(NUTRITION, new Config(new Attributes(0, 21, 0, 5, 0, 0), 7200, 8)),
    VEGAN_9(NUTRITION, new Config(new Attributes(0, 23, 0, 6, 0, 0), 7200, 9)),
    VEGAN_10(NUTRITION, new Config(new Attributes(0, 25, 0, 6, 0, 0), 7200, 10)),
    VEGAN_11(NUTRITION, new Config(new Attributes(0, 28, 0, 7, 0, 0), 7200, 11)),

    BASIC_TRAINING(WORKOUT, new Config(new Attributes(0, 0, 1, 0, 0, 0), 0, 1)),
    ENDURANCE_5(WORKOUT, new Config(new Attributes(0, 6, 11, 0, 0, 0), 0, 5)),
    ENDURANCE_6(WORKOUT, new Config(new Attributes(0, 6, 16, 0, 0, 0), 0, 6)),
    ENDURANCE_7(WORKOUT, new Config(new Attributes(0, 6, 20, 0, 0, 0), 0, 7)),
    ENDURANCE_8(WORKOUT, new Config(new Attributes(0, 7, 23, 0, 0, 0), 0, 8)),
    ENDURANCE_9(WORKOUT, new Config(new Attributes(0, 7, 25, 0, 0, 0), 0, 9)),
    ENDURANCE_10(WORKOUT, new Config(new Attributes(0, 8, 27, 0, 0, 0), 0, 10)),
    ENDURANCE_11(WORKOUT, new Config(new Attributes(0, 8, 30, 0, 0, 0), 0, 11)),
    PLYOMETRICS_3(WORKOUT, new Config(new Attributes(0, 0, 4, 0, 0, 3), 0, 3)),
    PLYOMETRICS_4(WORKOUT, new Config(new Attributes(0, 0, 7, 0, 0, 4), 7200, 4)),
    PLYOMETRICS_5(WORKOUT, new Config(new Attributes(0, 0, 11, 0, 0, 4), 7200, 5)),
    PLYOMETRICS_6(WORKOUT, new Config(new Attributes(0, 0, 16, 0, 0, 4), 7200, 6)),
    PLYOMETRICS_7(WORKOUT, new Config(new Attributes(0, 0, 20, 0, 0, 4), 7200, 7)),
    PLYOMETRICS_8(WORKOUT, new Config(new Attributes(0, 0, 23, 0, 0, 4), 7200, 8)),
    PLYOMETRICS_9(WORKOUT, new Config(new Attributes(0, 0, 25, 0, 0, 5), 7200, 9)),
    PLYOMETRICS_10(WORKOUT, new Config(new Attributes(0, 0, 27, 0, 0, 5), 7200, 10)),
    PLYOMETRICS_11(WORKOUT, new Config(new Attributes(0, 0, 30, 0, 0, 5), 7200, 11)),
    WEIGHTLIFTING_1(WORKOUT, new Config(new Attributes(0, 4, 0, 0, 0, 0), 0, 1)),
    WEIGHTLIFTING_2(WORKOUT, new Config(new Attributes(0, 5, 2, 0, 0, 0), 10900, 2)),
    WEIGHTLIFTING_3(WORKOUT, new Config(new Attributes(0, 6, 3, 0, 0, 0), 0, 3)),
    WEIGHTLIFTING_4(WORKOUT, new Config(new Attributes(0, 6, 7, 0, 0, 0), 15000, 4)),
    WEIGHTLIFTING_5(WORKOUT, new Config(new Attributes(0, 6, 11, 0, 0, 0), 21700, 5)),
    WEIGHTLIFTING_6(WORKOUT, new Config(new Attributes(0, 7, 15, 0, 0, 0), 27500, 6)),
    WEIGHTLIFTING_7(WORKOUT, new Config(new Attributes(0, 7, 20, 0, 0, 0), 35900, 7)),
    WEIGHTLIFTING_8(WORKOUT, new Config(new Attributes(0, 7, 22, 0, 0, 0), 45900, 8)),
    WEIGHTLIFTING_9(WORKOUT, new Config(new Attributes(0, 8, 24, 0, 0, 0), 58400, 9)),
    WEIGHTLIFTING_10(WORKOUT, new Config(new Attributes(0, 8, 27, 0, 0, 0), 75000, 10)),
    WEIGHTLIFTING_11(WORKOUT, new Config(new Attributes(0, 9, 29, 0, 0, 0), 87500, 11)),
    WEIGHTLIFTING_12(WORKOUT, new Config(new Attributes(0, 10, 32, 0, 0, 0), 104200, 13)),
    WEIGHTLIFTING_13(WORKOUT, new Config(new Attributes(0, 10, 34, 0, 0, 0), 183400, 13));

    private final Config config;
    private final GearType gearType;

    GearItem(GearType gearType, Config config) {
        this.gearType = gearType;
        this.config = config;
    }

    public Attributes getAttributes() {
        return config.getAttributes();
    }

    public int getCost() {
        return config.getCost();
    }

    public int getLevel() {
        return config.getLevel();
    }

    public GearType getGearType() {
        return gearType;
    }

    public static List<GearItem> leveledGearItems(int maxLevel, int upgradesAllowed) {
        return Arrays.stream(GearItem.values())
                .filter(item -> item.getLevel() <= maxLevel)
                .filter(item -> item.getLevel() >= Math.min(maxLevel, OwnedGear.ownedLevel(item)))
                .filter(item -> upgradesAllowed == 0 ? OwnedGear.isOwned(item) : OwnedGear.isPossibleUpgrade(item))
                .collect(Collectors.toList());
    }
}
