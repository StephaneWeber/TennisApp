package com.sweber.tennis.model.gear;

import java.util.Arrays;
import java.util.List;

public class OwnedGear {
    private static final List<GearItem> OWNED_GEAR = Arrays.asList(
            GearItem.BASIC_RACKET,
            GearItem.EAGLE_7,
            GearItem.PATRIOT_6,
            GearItem.PANTHER_4,
            GearItem.SAMURAI_3,
            GearItem.BASIC_GRIP,
            GearItem.WARRIOR_8,
            GearItem.TALON_5,
            GearItem.MACHETE_6,
            GearItem.KATANA_3,
            GearItem.BASIC_SHOES,
            GearItem.FEATHER_5,
            GearItem.RAPTOR_7,
            GearItem.HUNTER_3,
            GearItem.PIRANHA_5,
            GearItem.BASIC_WRIST,
            GearItem.TOMAHAWK_6,
            GearItem.MISSILE_3,
            GearItem.PIRATE_5,
            GearItem.ARA_1,
            GearItem.BASIC_NUTRITION,
            GearItem.PROTEIN_5,
            GearItem.HYDRATION_7,
            GearItem.MACROBIOTICS_5,
            GearItem.VEGAN_1,
            GearItem.BASIC_TRAINING,
            GearItem.ENDURANCE_8,
            GearItem.PLYOMETRICS_4,
            GearItem.WEIGHTLIFTING_1);

    private OwnedGear() {
    }

    public static int ownedLevel(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGripName = getGearItemGenericName(gearItem.name());
        return OWNED_GEAR.stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(GearItem::getLevel)
                .orElse(0);
    }

    public static boolean isNextLevel(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) == 1);
    }

    public static boolean isPossibleUpgrade(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) <= 1);
    }

    public static boolean isOwned(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) <= 0);
    }

    private static String getGearItemGenericName(String name) {
        int endIndex = name.indexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}