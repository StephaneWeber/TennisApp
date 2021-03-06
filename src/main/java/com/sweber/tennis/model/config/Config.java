package com.sweber.tennis.model.config;

public class Config {
    private final Attributes attributes;
    private final int cost;
    private final int level;

    public Config(Attributes attributes, int cost, int level) {
        this.attributes = attributes;
        this.cost = cost;
        this.level = level;
    }

    public Attributes getAttributes() {
        return attributes;
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
                "attributes=" + attributes +
                ", cost=" + cost +
                ", level=" + level +
                '}';
    }
}
