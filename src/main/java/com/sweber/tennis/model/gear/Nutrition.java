package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.Config;

public enum Nutrition {
    BASIC_NUTRITION(new Config(0, 0, 0, 0, 0, 0, 0)),
    PROTEIN(new Config(0, 13, 0, 0, 0, 0, 0)),
    HYDRATION(new Config(0, 10, 0, 0, 4, 0, 0)),
    HYDRATION_2(new Config(0, 14, 0, 0, 4, 0, 7200)),
    MACROBIOTICS(new Config(0, 0, 0, 11, 0, 0, 0)),
    MACROBIOTICS_2(new Config(0, 4, 0, 11, 0, 0, 10900)),
    VEGAN(new Config(0, 0, 0, 3, 0, 0, 0)),
    VEGAN_2(new Config(0, 1, 0, 4, 0, 0, 7200));

    private final Config config;

    Nutrition(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
