package com.sweber.tennis.model;

import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Training;
import com.sweber.tennis.model.gear.Wrist;

public class FullConfig {
    private Player player;
    private Racket racket;
    private Grip grip;
    private Shoes shoes;
    private Wrist wrist;
    private Nutrition nutrition;
    private Training training;
    private Config config;
    private int value;

    public FullConfig(Player player, Racket racket, Grip grip, Shoes shoes, Wrist wrist, Nutrition nutrition, Training training) {
        this.player = player;
        this.racket = racket;
        this.grip = grip;
        this.shoes = shoes;
        this.wrist = wrist;
        this.nutrition = nutrition;
        this.training = training;
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

    public String getWrist() {
        return wrist.name();
    }

    public String getNutrition() {
        return nutrition.name();
    }

    public String getTraining() {
        return training.name();
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

    private void computeConfig() {
        int agility = player.getConfig().getAgility() + racket.getConfig().getAgility() + grip.getConfig().getAgility() + shoes.getConfig().getAgility() + wrist.getConfig().getAgility() + nutrition.getConfig().getAgility() + training.getConfig().getAgility();
        int endurance = player.getConfig().getEndurance() + racket.getConfig().getEndurance() + grip.getConfig().getEndurance() + shoes.getConfig().getEndurance() + wrist.getConfig().getEndurance() + nutrition.getConfig().getEndurance() + training.getConfig().getEndurance();
        int service = player.getConfig().getService() + racket.getConfig().getService() + grip.getConfig().getService() + shoes.getConfig().getService() + wrist.getConfig().getService() + nutrition.getConfig().getService() + training.getConfig().getService();
        int volley = player.getConfig().getVolley() + racket.getConfig().getVolley() + grip.getConfig().getVolley() + shoes.getConfig().getVolley() + wrist.getConfig().getVolley() + nutrition.getConfig().getVolley() + training.getConfig().getVolley();
        int forehand = player.getConfig().getForehand() + racket.getConfig().getForehand() + grip.getConfig().getForehand() + shoes.getConfig().getForehand() + wrist.getConfig().getForehand() + nutrition.getConfig().getForehand() + training.getConfig().getForehand();
        int backhand = player.getConfig().getBackhand() + racket.getConfig().getBackhand() + grip.getConfig().getBackhand() + shoes.getConfig().getBackhand() + wrist.getConfig().getBackhand() + nutrition.getConfig().getBackhand() + training.getConfig().getBackhand();
        int cost = player.getConfig().getCost() + racket.getConfig().getCost() + grip.getConfig().getCost() + shoes.getConfig().getCost() + wrist.getConfig().getCost() + nutrition.getConfig().getCost() + training.getConfig().getCost();
        config = new Config(agility, endurance, service, volley, forehand, backhand, cost);
        value = agility + endurance + service + volley + forehand + backhand;
    }

    @Override
    public String toString() {
        return "FullConfig{" +
                "player=" + player +
                ", racket=" + racket +
                ", grip=" + grip +
                ", shoes=" + shoes +
                ", wrist=" + wrist +
                ", nutrition=" + nutrition +
                ", training=" + training +
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
        String fullName = player.name() + racket.name() + grip.name() + shoes.name() + wrist.name() + nutrition.name() + training.name();
        long count = fullName.chars().filter(ch -> ch == '2').count();
        return count <= maxUpgradesAllowed;
    }
}
