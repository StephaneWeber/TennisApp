package com.sweber.tennis.config;

import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Workout;
import com.sweber.tennis.model.gear.Wristband;

import java.util.Arrays;
import java.util.List;

import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.FORBIDDEN;
import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.NO_UPGRADE;
import static com.sweber.tennis.config.OwnedGear.UpgradeStatus.UPGRADE;

//TODO Cleanup + refactor !
public class OwnedGear {
    public enum UpgradeStatus {
        UPGRADE,
        NO_UPGRADE,
        FORBIDDEN
    }

    private static final List<Racket> rackets = Arrays.asList(
            Racket.BASIC_RACKET_1,
            Racket.EAGLE_7,
            Racket.PANTHER_3,
            Racket.PATRIOT_5);
    private static final List<Grip> grips = Arrays.asList(
            Grip.BASIC_GRIP,
            Grip.TALON_5,
            Grip.MACHETE_3,
            Grip.WARRIOR_8);
    private static final List<Shoes> shoesList = Arrays.asList(
            Shoes.BASIC_SHOES_1,
            Shoes.FEATHER_4,
            Shoes.PIRANHA_4,
            Shoes.RAPTOR_6,
            Shoes.HUNTER_3);
    private static final List<Wristband> WRISTBANDS = Arrays.asList(
            Wristband.BASIC_WRIST,
            Wristband.MISSILE_3,
            Wristband.PIRATE_5,
            Wristband.TOMAHAWK_5,
            Wristband.ARA_1);
    private static final List<Nutrition> nutritions = Arrays.asList(
            Nutrition.BASIC_NUTRITION_1,
            Nutrition.HYDRATION_5,
            Nutrition.MACROBIOTICS,
            Nutrition.PROTEIN_4,
            Nutrition.VEGAN_1);
    private static final List<Workout> WORKOUTS = Arrays.asList(
            Workout.BASIC_TRAINING_1,
            Workout.ENDURANCE_7,
            Workout.PLIOMETRICS_3);

    public static UpgradeStatus isUpgradeableTo(Racket racket) {
        String configGripName = getGearItemGenericName(racket.name());
        int racketLevel = racket.getConfig().getLevel();
        Integer currentLevel = rackets.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Racket::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = racketLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    private static String getGearItemGenericName(String name) {
        int endIndex = name.indexOf("_");
        String configGripName = name;
        if (endIndex != -1) {
            configGripName = configGripName.substring(0, endIndex);
        }
        return configGripName;
    }

    public static UpgradeStatus isUpgradeableTo(Grip grip) {
        String configGripName = getGearItemGenericName(grip.name());
        int gripLevel = grip.getConfig().getLevel();
        Integer currentLevel = grips.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Grip::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gripLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    public static UpgradeStatus isUpgradeableTo(Shoes shoes) {
        String configGripName = getGearItemGenericName(shoes.name());
        int gripLevel = shoes.getConfig().getLevel();
        Integer currentLevel = shoesList.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Shoes::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gripLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    public static UpgradeStatus isUpgradeableTo(Wristband wristband) {
        String configGripName = getGearItemGenericName(wristband.name());
        int gripLevel = wristband.getConfig().getLevel();
        Integer currentLevel = WRISTBANDS.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Wristband::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gripLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    public static UpgradeStatus isUpgradeableTo(Nutrition nutrition) {
        String configGripName = getGearItemGenericName(nutrition.name());
        int gripLevel = nutrition.getConfig().getLevel();
        Integer currentLevel = nutritions.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Nutrition::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gripLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }

    public static UpgradeStatus isUpgradeableTo(Workout workout) {
        String configGripName = getGearItemGenericName(workout.name());
        int gripLevel = workout.getConfig().getLevel();
        Integer currentLevel = WORKOUTS.stream()
                .filter(item -> item.name().startsWith(configGripName))
                .findFirst()
                .map(Workout::getConfig)
                .map(Config::getLevel)
                .orElse(-1);

        int i = gripLevel - currentLevel;
        if (i > 1) {
            return FORBIDDEN;
        } else if (i == 1) {
            return UPGRADE;
        }
        return NO_UPGRADE;
    }
}