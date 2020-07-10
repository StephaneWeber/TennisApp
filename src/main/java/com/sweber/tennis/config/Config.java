package com.sweber.tennis.config;

public class Config {
    private final int agility;
    private final int endurance;
    private final int service;
    private final int volley;
    private final int forehand;
    private final int backhand;
    private final int cost;
    private final int level;

    public Config(int agility, int endurance, int service, int volley, int forehand, int backhand, int cost, int level) {
        this.agility = agility;
        this.endurance = endurance;
        this.service = service;
        this.volley = volley;
        this.forehand = forehand;
        this.backhand = backhand;
        this.cost = cost;
        this.level = level;
    }

    public int getAgility() {
        return agility;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getService() {
        return service;
    }

    public int getVolley() {
        return volley;
    }

    public int getForehand() {
        return forehand;
    }

    public int getBackhand() {
        return backhand;
    }

    public int getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Config{" +
                "agility=" + agility +
                ", endurance=" + endurance +
                ", service=" + service +
                ", volley=" + volley +
                ", forehand=" + forehand +
                ", backhand=" + backhand +
                ", cost=" + cost +
                ", level=" + level +
                '}';
    }
}
