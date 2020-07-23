package com.sweber.tennis.model.config;

import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.player.Player;

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

    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getRacketName() {
        return racket.getName();
    }

    public GearItem getRacket() {
        return racket;
    }

    public String getGripName() {
        return grip.getName();
    }

    public GearItem getGrip() {
        return grip;
    }

    public String getShoesName() {
        return shoes.getName();
    }

    public GearItem getShoes() {
        return shoes;
    }

    public String getWristbandName() {
        return wristband.getName();
    }

    public GearItem getWristband() {
        return wristband;
    }

    public String getNutritionName() {
        return nutrition.getName();
    }

    public GearItem getNutrition() {
        return nutrition;
    }

    public String getWorkoutName() {
        return workout.getName();
    }

    public GearItem getWorkout() {
        return workout;
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
        return Stream.of(player.getLevel(), racket.getLevel(), grip.getLevel(), shoes.getLevel(), wristband.getLevel(), nutrition.getLevel(), workout.getLevel())
                .mapToInt(v -> v)
                .max()
                .orElse(11); // MaxLevel by default if we cannot compute one ...
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "player=" + player.getName() +
                ", racket=" + racket.getName() +
                ", grip=" + grip.getName() +
                ", shoes=" + shoes.getName() +
                ", wristband=" + wristband.getName() +
                ", nutrition=" + nutrition.getName() +
                ", workout=" + workout.getName() +
                ", config=" + config +
                ", value=" + value +
                '}';
    }
}
