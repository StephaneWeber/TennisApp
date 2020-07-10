package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

public enum Wrist {
    BASIC_WRIST(new Config(0, 0, 0, 0, 0, 0, 0, 0)),
    TOMAHAWK(new Config(0, 0, 0, 16, 0, 0, 0, 0)),
    MISSILE(new Config(3, 0, 0, 3, 0, 0, 0, 0)),
    MISSILE_2(new Config(3, 0, 0, 6, 0, 0, 2500, 0)),
    PIRATE(new Config(0, 0, 11, 5, 0, 0, 0, 0)),
    ARA(new Config(0, 0, 3, 0, 0, 0, 0, 0)),
    ARA_2(new Config(0, 0, 4, 1, 0, 0, 7200, 0));

    private final Config config;

    Wrist(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
