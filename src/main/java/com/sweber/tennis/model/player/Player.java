package com.sweber.tennis.model.player;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Player {
    //TODO Maintain versions from level 5 for handling cap at low levels. Not needed until level 5 is obtained.
    //No concept of update.
    JONAH(new Config(new Attributes(6, 4, 6, 4, 8, 6), 0, 4)),
    HOPE(new Config(new Attributes(7, 4, 4, 5, 5, 5), 0, 3)),
    FLORENCE(new Config(new Attributes(10, 9, 2, 3, 4, 4), 0, 3));

    private final Config config;

    Player(Config config) {
        this.config = config;
    }

    public static List<Player> maxLevel(int maxLevel) {
        return Arrays.stream(Player.values())
                .filter(item -> item.getLevel() <= maxLevel)
                .collect(Collectors.toList());
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
                "config=" + config +
                '}';
    }
}
