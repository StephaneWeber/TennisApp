package com.sweber.tennis.config;

import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;

import java.util.Arrays;
import java.util.List;

import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.FORBIDDEN;
import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.NO_UPGRADE;
import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.UPGRADE;

public class OwnedGear {
    public enum UpgradeStatus {
        UPGRADE,
        NO_UPGRADE,
        FORBIDDEN
    }

    private static final List<GearItem> ownedGear = Arrays.asList(
            GearItem.BASIC_RACKET_1,
            GearItem.EAGLE_7,
            GearItem.PANTHER_3,
            GearItem.PATRIOT_5,
            GearItem.BASIC_GRIP,
            GearItem.TALON_5,
            GearItem.MACHETE_3,
            GearItem.WARRIOR_8,
            GearItem.BASIC_SHOES_1,
            GearItem.FEATHER_4,
            GearItem.PIRANHA_4,
            GearItem.RAPTOR_6,
            GearItem.HUNTER_3,
            GearItem.BASIC_WRIST,
            GearItem.MISSILE_3,
            GearItem.PIRATE_5,
            GearItem.TOMAHAWK_5,
            GearItem.ARA_1,
            GearItem.BASIC_NUTRITION_1,
            GearItem.HYDRATION_5,
            GearItem.MACROBIOTICS,
            GearItem.PROTEIN_4,
            GearItem.VEGAN_1,
            GearItem.BASIC_TRAINING_1,
            GearItem.ENDURANCE_7,
            GearItem.PLIOMETRICS_3);

    public static UpgradeStatus isUpgradeableTo(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGripName = getGearItemGenericName(gearItem.name());
        int gearItemlevel = gearItem.getConfig().getLevel();
        Integer currentLevel = ownedGear.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .filter(item -> item.getGearType() == gearType)
                .findFirst()
                .map(GearItem::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gearItemlevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    private static String getGearItemGenericName(String name) {
        int endIndex = name.indexOf("_");
        String itemName = name;
        if (endIndex != -1) {
            itemName = itemName.substring(0, endIndex);
        }
        return itemName;
    }
}