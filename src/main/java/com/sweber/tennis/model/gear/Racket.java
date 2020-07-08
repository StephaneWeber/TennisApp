package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.Config;

public enum Racket {
    BASIC_RACKET(new Config(0, 0, 0, 0, 3, 0, 0)),
    EAGLE(new Config(4, 0, 0, 0, 20, 0, 0)),
    EAGLE_2(new Config(4, 0, 0, 0, 23, 0, 10900)),
    PATRIOT(new Config(0, 0, 0, 0, 15, 6, 0)),
    PANTHER(new Config(0, 0, 0, 0, 11, 0, 0)),
    PANTHER_2(new Config(0, 0, 0, 0, 14, 0, 15000));

    private final Config config;

    Racket(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
