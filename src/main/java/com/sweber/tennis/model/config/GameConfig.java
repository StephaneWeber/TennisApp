package com.sweber.tennis.model.config;

import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.player.Player;

import java.util.stream.Stream;

public class GameConfig {
    protected Player player;
    protected GearConfig gearConfig;

    protected ConfigValues configValues;
    protected int value;
    protected boolean upgradesAllowed;

    public GameConfig(Player player, GearConfig gearConfig, boolean upgradesAllowed) {
        this.player = player;
        this.gearConfig = gearConfig;
        this.upgradesAllowed = upgradesAllowed;
        computeConfig();
    }


    public GameConfig cloneConfig() {
        return new GameConfig(player, gearConfig.cloneConfig(), upgradesAllowed);
    }

    public void updatePlayer(Player newPlayer) {
        this.player = newPlayer;
        computeConfig();
    }

    public void updateGearConfig(GearConfig newGearConfig) {
        this.gearConfig = newGearConfig;
        computeConfig();
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getRacketName() {
        return gearConfig.racket.getName();
    }

    public GearItem getRacket() {
        return gearConfig.racket;
    }

    public String getGripName() {
        return gearConfig.grip.getName();
    }

    public GearItem getGrip() {
        return gearConfig.grip;
    }

    public String getShoesName() {
        return gearConfig.shoes.getName();
    }

    public GearItem getShoes() {
        return gearConfig.shoes;
    }

    public String getWristbandName() {
        return gearConfig.wristband.getName();
    }

    public GearItem getWristband() {
        return gearConfig.wristband;
    }

    public String getNutritionName() {
        return gearConfig.nutrition.getName();
    }

    public GearItem getNutrition() {
        return gearConfig.nutrition;
    }

    public String getWorkoutName() {
        return gearConfig.workout.getName();
    }

    public GearItem getWorkout() {
        return gearConfig.workout;
    }

    public Attributes getAttributes() {
        return configValues.getAttributes();
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return configValues.getCost();
    }

    public int getLevel() {
        return configValues.getLevel();
    }

    private void computeConfig() {
        int agility = player.getAttributes().getAgility() + gearConfig.racket.getAttributes().getAgility() + gearConfig.grip.getAttributes().getAgility() + gearConfig.shoes.getAttributes().getAgility() + gearConfig.wristband.getAttributes().getAgility() + gearConfig.nutrition.getAttributes().getAgility() + gearConfig.workout.getAttributes().getAgility();
        int endurance = player.getAttributes().getEndurance() + gearConfig.racket.getAttributes().getEndurance() + gearConfig.grip.getAttributes().getEndurance() + gearConfig.shoes.getAttributes().getEndurance() + gearConfig.wristband.getAttributes().getEndurance() + gearConfig.nutrition.getAttributes().getEndurance() + gearConfig.workout.getAttributes().getEndurance();
        int service = player.getAttributes().getService() + gearConfig.racket.getAttributes().getService() + gearConfig.grip.getAttributes().getService() + gearConfig.shoes.getAttributes().getService() + gearConfig.wristband.getAttributes().getService() + gearConfig.nutrition.getAttributes().getService() + gearConfig.workout.getAttributes().getService();
        int volley = player.getAttributes().getVolley() + gearConfig.racket.getAttributes().getVolley() + gearConfig.grip.getAttributes().getVolley() + gearConfig.shoes.getAttributes().getVolley() + gearConfig.wristband.getAttributes().getVolley() + gearConfig.nutrition.getAttributes().getVolley() + gearConfig.workout.getAttributes().getVolley();
        int forehand = player.getAttributes().getForehand() + gearConfig.racket.getAttributes().getForehand() + gearConfig.grip.getAttributes().getForehand() + gearConfig.shoes.getAttributes().getForehand() + gearConfig.wristband.getAttributes().getForehand() + gearConfig.nutrition.getAttributes().getForehand() + gearConfig.workout.getAttributes().getForehand();
        int backhand = player.getAttributes().getBackhand() + gearConfig.racket.getAttributes().getBackhand() + gearConfig.grip.getAttributes().getBackhand() + gearConfig.shoes.getAttributes().getBackhand() + gearConfig.wristband.getAttributes().getBackhand() + gearConfig.nutrition.getAttributes().getBackhand() + gearConfig.workout.getAttributes().getBackhand();
        configValues = new ConfigValues(new Attributes(agility, endurance, service, volley, forehand, backhand), computeCost(), computeMaxLevel());
        value = agility + endurance + service + volley + forehand + backhand;
    }

    private int computeCost() {
        if (!upgradesAllowed) {
            return 0;
        }
        return player.getCost() + gearConfig.getCost();
    }

    private int computeMaxLevel() {
        return Stream.of(player.getLevel(), gearConfig.racket.getLevel(), gearConfig.grip.getLevel(), gearConfig.shoes.getLevel(), gearConfig.wristband.getLevel(), gearConfig.nutrition.getLevel(), gearConfig.workout.getLevel())
                .mapToInt(v -> v)
                .max()
                .orElse(13); // MaxLevel by default if we cannot compute one ...
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "player=" + player.getName() +
                ", racket=" + gearConfig.racket.getName() +
                ", grip=" + gearConfig.grip.getName() +
                ", shoes=" + gearConfig.shoes.getName() +
                ", wristband=" + gearConfig.wristband.getName() +
                ", nutrition=" + gearConfig.nutrition.getName() +
                ", workout=" + gearConfig.workout.getName() +
                ", upgradesAllowed=" + upgradesAllowed +
                ", config=" + configValues +
                ", value=" + value +
                '}';
    }
}
