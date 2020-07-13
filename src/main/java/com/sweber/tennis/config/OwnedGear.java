package com.sweber.tennis.config;

import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            GearItem.BASIC_RACKET,
            GearItem.EAGLE_7,
            GearItem.PATRIOT_5,
            GearItem.PANTHER_3,
            GearItem.BASIC_GRIP,
            GearItem.WARRIOR_8,
            GearItem.TALON_5,
            GearItem.MACHETE_3,
            GearItem.BASIC_SHOES,
            GearItem.FEATHER_5,
            GearItem.RAPTOR_7,
            GearItem.HUNTER_3,
            GearItem.PIRANHA_4,
            GearItem.BASIC_WRIST,
            GearItem.TOMAHAWK_5,
            GearItem.MISSILE_3,
            GearItem.PIRATE_5,
            GearItem.ARA_1,
            GearItem.BASIC_NUTRITION,
            GearItem.PROTEIN_4,
            GearItem.HYDRATION_6,
            GearItem.MACROBIOTICS_4,
            GearItem.VEGAN_1,
            GearItem.BASIC_TRAINING,
            GearItem.ENDURANCE_7,
            GearItem.PLYOMETRICS_3);

    /* Returns -1 in case the item is not owned yet, to avoid getting configurations with it if update is allowed */
    public static int ownedLevel(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGripName = getGearItemGenericName(gearItem.name());
        return ownedGear.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .filter(item -> item.getGearType() == gearType)
                .findFirst()
                .map(GearItem::getConfig)
                .map(Config::getLevel)
                .orElse(-1); //TODO examine this -1 / 0 case. Seems working right, but not intuitive.
    }

    public static UpgradeStatus isUpgradeableTo(GearItem gearItem) {
        int currentLevel = ownedLevel(gearItem);
        int i = gearItem.getConfig().getLevel() - currentLevel;
        if (i == 1) {
            return UPGRADE;
        } else if (i <= 0) {
            return NO_UPGRADE;
        }
        return FORBIDDEN;
    }

    private static String getGearItemGenericName(String name) {
        int endIndex = name.indexOf("_");
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}