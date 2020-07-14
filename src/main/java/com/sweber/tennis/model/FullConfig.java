package com.sweber.tennis.model;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.config.OwnedGear;
import com.sweber.tennis.model.gear.GearItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FullConfig {
    protected Player player;
    protected GearItem racket;
    protected GearItem grip;
    protected GearItem shoes;
    protected GearItem wristband;
    protected GearItem nutrition;
    protected GearItem workout;
    protected Config config;
    protected int value;

    public FullConfig(Player player, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
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

    private int computeMaxLevel() {
        return Stream.of(player.getConfig().getLevel(), racket.getConfig().getLevel(), grip.getConfig().getLevel(), shoes.getConfig().getLevel(), wristband.getConfig().getLevel(), nutrition.getConfig().getCost(), workout.getConfig().getLevel())
                .mapToInt(v -> v)
                .max()
                .orElse(11); // MaxLevel by default if we cannot compute one ...
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

    public boolean upgradeAllowed(int maxUpgradesAllowed) {
        List<Boolean> simpleUpgradesCheck = Arrays.asList(
                OwnedGear.isUpgrade(racket), OwnedGear.isUpgrade(grip),
                OwnedGear.isUpgrade(shoes), OwnedGear.isUpgrade(wristband),
                OwnedGear.isUpgrade(nutrition), OwnedGear.isUpgrade(workout));
        long hitCount = simpleUpgradesCheck.stream()
                .filter(check -> check)
                .count();
        return hitCount == maxUpgradesAllowed;
    }
}
