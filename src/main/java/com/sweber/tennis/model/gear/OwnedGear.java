package com.sweber.tennis.model.gear;

import java.util.Arrays;
import java.util.List;

public class OwnedGear {
    private static final List<GearItem> OWNED_GEAR = Arrays.asList(
            GearItem.BASIC_RACKET,
            GearItem.EAGLE_7,
            GearItem.PATRIOT_5,
            GearItem.PANTHER_3,
            GearItem.BASIC_GRIP,
            GearItem.WARRIOR_8,
            GearItem.TALON_5,
            GearItem.MACHETE_5,
            GearItem.BASIC_SHOES,
            GearItem.FEATHER_5,
            GearItem.RAPTOR_7,
            GearItem.HUNTER_3,
            GearItem.PIRANHA_4,
            GearItem.BASIC_WRIST,
            GearItem.TOMAHAWK_6,
            GearItem.MISSILE_3,
            GearItem.PIRATE_5,
            GearItem.ARA_1,
            GearItem.BASIC_NUTRITION,
            GearItem.PROTEIN_4,
            GearItem.HYDRATION_6,
            GearItem.MACROBIOTICS_5,
            GearItem.VEGAN_1,
            GearItem.BASIC_TRAINING,
            GearItem.ENDURANCE_7,
            GearItem.PLYOMETRICS_3);

    private OwnedGear() {
    }

    private static int ownedLevel(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGripName = getGearItemGenericName(gearItem.name());
        return OWNED_GEAR.stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(GearItem::getLevel)
                .orElse(0);
    }

    public static boolean isUpgrade(GearItem gearItem) {
        int currentLevel = ownedLevel(gearItem);
        int i = gearItem.getLevel() - currentLevel;
        return (i == 1);
    }

    public static boolean isPossibleUpgrade(GearItem gearItem) {
        int currentLevel = ownedLevel(gearItem);
        int i = gearItem.getLevel() - currentLevel;
        return i <= 1;
    }

    private static String getGearItemGenericName(String name) {
        int endIndex = name.indexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}