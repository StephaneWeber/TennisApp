package com.sweber.tennis.model.player;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.service.BeanUtil;
import com.sweber.tennis.service.PlayerService;

public class Player {
    private final String name;
    private final Config config;

    public Player(String name, Config config) {
        this.name = name;
        this.config = config;
    }

    public static Player dummy(Config config) {
        return new Player("DUMMY", config);
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return config.getAttributes();
    }

    public int getCost() {
        return BeanUtil.getBean(PlayerService.class).ownedLevel(this) - getLevel() >= 0 ? 0 : config.getCost();
    }

    public int getLevel() {
        return config.getLevel();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ",config=" + config +
                '}';
    }
}
