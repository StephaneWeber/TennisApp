package com.sweber.tennis.model.player;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;

public class Player {
    private final String name;
    private final Config config;

    public Player(String name, Config config) {
        this.name = name;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return config.getAttributes();
    }

    public int getCost() {
        return config.getCost();
    }

    public int getLevel() {
        return config.getLevel();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ",config=" + config +
                '}';
    }
}
