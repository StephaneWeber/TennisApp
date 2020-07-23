package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;

public class GearItem {
    private final String name;
    private final Config config;
    private final GearType gearType;

    public GearItem(String name, GearType gearType, Config config) {
        this.name = name;
        this.gearType = gearType;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return config.getAttributes();
    }

    public int getCost() {
        return config.getCost();
    }

    public int getLevel() {
        return config.getLevel();
    }

    public GearType getGearType() {
        return gearType;
    }
}
