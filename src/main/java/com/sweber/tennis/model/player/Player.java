package com.sweber.tennis.model.player;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.ConfigValues;
import com.sweber.tennis.service.BeanUtil;
import com.sweber.tennis.service.PlayerService;

public class Player {
    private final String name;
    private final ConfigValues configValues;

    public Player(String name, ConfigValues configValues) {
        this.name = name;
        this.configValues = configValues;
    }

    public static Player dummy(ConfigValues configValues) {
        return new Player("DUMMY", configValues);
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return configValues.getAttributes();
    }

    public int getCost() {
        return BeanUtil.getBean(PlayerService.class).ownedLevel(this) - getLevel() >= 0 ? 0 : configValues.getCost();
    }

    public int getLevel() {
        return configValues.getLevel();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ",config=" + configValues +
                '}';
    }
}
