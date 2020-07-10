package com.sweber.tennis.web.model;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.config.OwnedGear;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Workout;
import com.sweber.tennis.model.gear.Wristband;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class FullConfig {
    private Player player;
    private Racket racket;
    private Grip grip;
    private Shoes shoes;
    private Wristband wristband;
    private Nutrition nutrition;
    private Workout workout;
    private Config config;
    private int value;

    public FullConfig(Player player, Racket racket, Grip grip, Shoes shoes, Wristband wristband, Nutrition nutrition, Workout workout) {
        this.player = player;
        this.racket = racket;
        this.grip = grip;
        this.shoes = shoes;
        this.wristband = wristband;
        this.nutrition = nutrition;
        this.workout = workout;
        computeConfig();
    }

    public String getPlayer() {
        return player.name();
    }

    public String getRacket() {
        return racket.name();
    }

    public String getGrip() {
        return grip.name();
    }

    public String getShoes() {
        return shoes.name();
    }

    public String getWristband() {
        return wristband.name();
    }

    public String getNutrition() {
        return nutrition.name();
    }

    public String getWorkout() {
        return workout.name();
    }

    public Config getConfig() {
        return config;
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return config.getCost();
    }

    public int getLevel() {
        return config.getLevel();
    }

    private void computeConfig() {
        int agility = player.getConfig().getAgility() + racket.getConfig().getAgility() + grip.getConfig().getAgility() + shoes.getConfig().getAgility() + wristband.getConfig().getAgility() + nutrition.getConfig().getAgility() + workout.getConfig().getAgility();
        int endurance = player.getConfig().getEndurance() + racket.getConfig().getEndurance() + grip.getConfig().getEndurance() + shoes.getConfig().getEndurance() + wristband.getConfig().getEndurance() + nutrition.getConfig().getEndurance() + workout.getConfig().getEndurance();
        int service = player.getConfig().getService() + racket.getConfig().getService() + grip.getConfig().getService() + shoes.getConfig().getService() + wristband.getConfig().getService() + nutrition.getConfig().getService() + workout.getConfig().getService();
        int volley = player.getConfig().getVolley() + racket.getConfig().getVolley() + grip.getConfig().getVolley() + shoes.getConfig().getVolley() + wristband.getConfig().getVolley() + nutrition.getConfig().getVolley() + workout.getConfig().getVolley();
        int forehand = player.getConfig().getForehand() + racket.getConfig().getForehand() + grip.getConfig().getForehand() + shoes.getConfig().getForehand() + wristband.getConfig().getForehand() + nutrition.getConfig().getForehand() + workout.getConfig().getForehand();
        int backhand = player.getConfig().getBackhand() + racket.getConfig().getBackhand() + grip.getConfig().getBackhand() + shoes.getConfig().getBackhand() + wristband.getConfig().getBackhand() + nutrition.getConfig().getBackhand() + workout.getConfig().getBackhand();
        int cost = player.getConfig().getCost() + racket.getConfig().getCost() + grip.getConfig().getCost() + shoes.getConfig().getCost() + wristband.getConfig().getCost() + nutrition.getConfig().getCost() + workout.getConfig().getCost();
        config = new Config(agility, endurance, service, volley, forehand, backhand, cost, computeMaxLevel());
        value = agility + endurance + service + volley + forehand + backhand;
    }

    public int computeMaxLevel() {
        List<Integer> listOfLevels = Arrays.asList(player.getConfig().getLevel(), racket.getConfig().getLevel(), grip.getConfig().getLevel(), shoes.getConfig().getLevel(), wristband.getConfig().getLevel(), nutrition.getConfig().getCost(), workout.getConfig().getLevel());
        return listOfLevels
                .stream()
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
    }

    @Override
    public String toString() {
        return "FullConfig{" +
                "player=" + player +
                ", racket=" + racket +
                ", grip=" + grip +
                ", shoes=" + shoes +
                ", wristband=" + wristband +
                ", nutrition=" + nutrition +
                ", workout=" + workout +
                ", config=" + config +
                ", value=" + value +
                '}';
    }

    public boolean satisfies(Config minimumConfig) {
        if (minimumConfig == null) return true;
        return config.getAgility() >= minimumConfig.getAgility()
                && config.getEndurance() >= minimumConfig.getEndurance()
                && config.getService() >= minimumConfig.getService()
                && config.getVolley() >= minimumConfig.getVolley()
                && config.getForehand() >= minimumConfig.getForehand()
                && config.getBackhand() >= minimumConfig.getBackhand();
    }

    //TODO cleanup
    public boolean upgradeAllowed(int maxUpgradesAllowed) {
        int numberOfUpgrades = 0;
        OwnedGear.UpgradeStatus upgradeableToRacket = OwnedGear.isUpgradeableTo(racket);
        if (upgradeableToRacket == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToRacket == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        OwnedGear.UpgradeStatus upgradeableToGrip = OwnedGear.isUpgradeableTo(grip);
        if (upgradeableToGrip == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToGrip == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        OwnedGear.UpgradeStatus upgradeableToShoes = OwnedGear.isUpgradeableTo(shoes);
        if (upgradeableToShoes == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToShoes == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        OwnedGear.UpgradeStatus upgradeableToWrist = OwnedGear.isUpgradeableTo(wristband);
        if (upgradeableToWrist == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToWrist == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        OwnedGear.UpgradeStatus upgradeableToNutrition = OwnedGear.isUpgradeableTo(nutrition);
        if (upgradeableToNutrition == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToNutrition == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        OwnedGear.UpgradeStatus upgradeableToTraining = OwnedGear.isUpgradeableTo(workout);
        if (upgradeableToTraining == OwnedGear.UpgradeStatus.UPGRADE) {
            numberOfUpgrades++;
        } else if (upgradeableToTraining == OwnedGear.UpgradeStatus.FORBIDDEN) {
            return false;
        }
        return numberOfUpgrades <= maxUpgradesAllowed;
    }

    public boolean maxLevelRespected(Integer maxLevel) {
        return config.getLevel() <= maxLevel;
    }
}
