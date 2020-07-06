package com.sweber.tennis.model;

public enum Player {
    ALL(null),
    JONAH(new Config(5, 4, 5, 3, 7, 5, 0)),
    HOPE(new Config(7, 4, 4, 5, 5, 5, 0)),
    FLORENCE(new Config(10, 9, 2, 3, 4, 4, 0));

    private final Config config;

    Player(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
