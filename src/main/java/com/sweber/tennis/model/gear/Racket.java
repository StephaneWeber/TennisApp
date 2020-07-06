package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.Config;

public enum Racket {
    BASIC_RACKET(new Config(0, 0, 0, 0, 3, 0, 0)),
    EAGLE(new Config(4, 0, 0, 0, 20, 0, 0)),
    PATRIOT(new Config(0, 0, 0, 0, 15, 6, 0));

    private final Config config;

    Racket(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
