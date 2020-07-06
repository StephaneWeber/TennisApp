package com.sweber.tennis.model;

public class Config {
    private final int agility;
    private final int endurance;
    private final int service;
    private final int volley;
    private final int forehand;
    private final int backhand;
    private final int cost;

    public Config(int agility, int endurance, int service, int volley, int forehand, int backhand, int cost) {
        this.agility = agility;
        this.endurance = endurance;
        this.service = service;
        this.volley = volley;
        this.forehand = forehand;
        this.backhand = backhand;
        this.cost = cost;
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
                '}';
    }
}
