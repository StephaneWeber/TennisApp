package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

public class GearItem {
    private final Config config;

    GearItem(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
