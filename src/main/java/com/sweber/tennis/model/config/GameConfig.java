package com.sweber.tennis.model.config;

import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.OwnedGear;
import com.sweber.tennis.model.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameConfig {
    protected Player player;
    protected GearItem racket;
    protected GearItem grip;
    protected GearItem shoes;
    protected GearItem wristband;
    protected GearItem nutrition;
    protected GearItem workout;
    protected Config config;
    protected int value;

    public GameConfig(Player player, GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
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

    public Attributes getAttributes() {
        return config.getAttributes();
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
        int agility = player.getAttributes().getAgility() + racket.getAttributes().getAgility() + grip.getAttributes().getAgility() + shoes.getAttributes().getAgility() + wristband.getAttributes().getAgility() + nutrition.getAttributes().getAgility() + workout.getAttributes().getAgility();
        int endurance = player.getAttributes().getEndurance() + racket.getAttributes().getEndurance() + grip.getAttributes().getEndurance() + shoes.getAttributes().getEndurance() + wristband.getAttributes().getEndurance() + nutrition.getAttributes().getEndurance() + workout.getAttributes().getEndurance();
        int service = player.getAttributes().getService() + racket.getAttributes().getService() + grip.getAttributes().getService() + shoes.getAttributes().getService() + wristband.getAttributes().getService() + nutrition.getAttributes().getService() + workout.getAttributes().getService();
        int volley = player.getAttributes().getVolley() + racket.getAttributes().getVolley() + grip.getAttributes().getVolley() + shoes.getAttributes().getVolley() + wristband.getAttributes().getVolley() + nutrition.getAttributes().getVolley() + workout.getAttributes().getVolley();
        int forehand = player.getAttributes().getForehand() + racket.getAttributes().getForehand() + grip.getAttributes().getForehand() + shoes.getAttributes().getForehand() + wristband.getAttributes().getForehand() + nutrition.getAttributes().getForehand() + workout.getAttributes().getForehand();
        int backhand = player.getAttributes().getBackhand() + racket.getAttributes().getBackhand() + grip.getAttributes().getBackhand() + shoes.getAttributes().getBackhand() + wristband.getAttributes().getBackhand() + nutrition.getAttributes().getBackhand() + workout.getAttributes().getBackhand();
        int cost = player.getCost() + racket.getCost() + grip.getCost() + shoes.getCost() + wristband.getCost() + nutrition.getCost() + workout.getCost();
        config = new Config(new Attributes(agility, endurance, service, volley, forehand, backhand), cost, computeMaxLevel());
        value = agility + endurance + service + volley + forehand + backhand;
    }

    private int computeMaxLevel() {
        return Stream.of(player.getLevel(), racket.getLevel(), grip.getLevel(), shoes.getLevel(), wristband.getLevel(), nutrition.getCost(), workout.getLevel())
                .mapToInt(v -> v)
                .max()
                .orElse(11); // MaxLevel by default if we cannot compute one ...
    }

    @Override
    public String toString() {
        return "GameConfig{" +
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

    public boolean satisfies(Attributes minimumAttributes) {
        if (minimumAttributes == null) return true;
        return getAttributes().getAgility() >= minimumAttributes.getAgility()
                && getAttributes().getEndurance() >= minimumAttributes.getEndurance()
                && getAttributes().getService() >= minimumAttributes.getService()
                && getAttributes().getVolley() >= minimumAttributes.getVolley()
                && getAttributes().getForehand() >= minimumAttributes.getForehand()
                && getAttributes().getBackhand() >= minimumAttributes.getBackhand();
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
