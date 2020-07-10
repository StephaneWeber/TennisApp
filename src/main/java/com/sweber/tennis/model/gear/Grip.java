package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

public enum Grip {
    BASIC_GRIP(new Config(0, 0, 0, 5, 0, 3, 0, 0)),
    WARRIOR(new Config(0, 0, 0, 5, 0, 23, 0, 0)),
    CLAW(new Config(0, 0, 0, 0, 6, 15, 0, 0)),
    MACHETE(new Config(0, 0, 0, 0, 0, 11, 0, 0)),
    MACHETE_2(new Config(0, 0, 0, 0, 0, 14, 7200, 0));

    private final Config config;

    Grip(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
