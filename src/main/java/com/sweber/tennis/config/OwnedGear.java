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
            GearItem.RAPTOR_6,
            GearItem.HUNTER_3,
            GearItem.PIRANHA_4,
            GearItem.BASIC_WRIST,
            GearItem.TOMAHAWK_5,
            GearItem.MISSILE_3,
            GearItem.PIRATE_5,
            GearItem.ARA_1,
            GearItem.BASIC_NUTRITION,
            GearItem.PROTEIN_4,
            GearItem.HYDRATION_5,
            GearItem.MACROBIOTICS_4,
            GearItem.VEGAN_1,
            GearItem.BASIC_TRAINING,
            GearItem.ENDURANCE_7,
            GearItem.PLYOMETRICS_3);

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
        if (i > 1 || i < 0) {
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

    public static List<GearItem> maxLevel(int maxLevel) {
        return ownedGear
                .stream()
                .filter(item -> item.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}