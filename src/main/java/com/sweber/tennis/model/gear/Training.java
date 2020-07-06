package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.Config;

public enum Training {
    BASIC_TRAINING(new Config(0, 0, 1, 0, 0, 0, 0)),
    ENDURANCE(new Config(0, 6, 20, 0, 0, 0, 0)),
    ENDURANCE_2(new Config(0, 7, 23, 0, 0, 0, 10900)),
    PLIOMETRICS(new Config(0, 0, 4, 0, 0, 3, 0)),
    PLIOMETRICS_2(new Config(0, 0, 7, 0, 0, 4, 7200));

    private final Config config;

    Training(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
