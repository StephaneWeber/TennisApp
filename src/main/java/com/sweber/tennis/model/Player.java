package com.sweber.tennis.model;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Player {
    //TODO Maintain versions from level 5 for handling cap at low levels. Not needed until level 5 is obtained.
    //No concept of update.
    ALL(null),
    JONAH(new Config(6, 4, 6, 4, 8, 6, 0, 4)),
    HOPE(new Config(7, 4, 4, 5, 5, 5, 0, 3)),
    FLORENCE(new Config(10, 9, 2, 3, 4, 4, 0, 3));

    private final Config config;

    Player(Config config) {
        this.config = config;
    }

    public static List<Player> maxLevel(int maxLevel) {
        return Arrays.stream(Player.values())
                .filter(item -> item.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }

    public Config getConfig() {
        return config;
    }
}
