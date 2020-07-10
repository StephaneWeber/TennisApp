package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

public enum Shoes {
    BASIC_SHOES(new Config(3, 0, 0, 0, 0, 0, 0, 0)),
    FEATHER(new Config(18, 0, 0, 0, 0, 0, 0, 0)),
    FEATHER_2(new Config(23, 0, 0, 0, 0, 0, 2500, 0)),
    RAPTOR(new Config(24, 6, 0, 0, 0, 0, 0, 0)),
    RAPTOR_2(new Config(29, 6, 0, 0, 0, 0, 10900, 0)),
    HUNTER(new Config(9, 0, 4, 0, 0, 0, 0, 0)),
    HUNTER_2(new Config(14, 0, 5, 0, 0, 0, 7200, 0)),
    PIRANHA(new Config(10, 12, 0, 0, 0, 0, 0, 0)),
    PIRANHA_2(new Config(15, 13, 0, 0, 0, 0, 21700, 0));

    private final Config config;

    Shoes(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
