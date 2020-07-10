package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for wrists yet unknown to me
public enum Wristband {
    BASIC_WRIST(new Config(0, 0, 0, 0, 0, 0, 0, 0)),
    TOMAHAWK_5(new Config(0, 0, 0, 16, 0, 0, 0, 5)),
    TOMAHAWK_6(new Config(0, 0, 0, 21, 0, 0, 0, 6)),
    TOMAHAWK_7(new Config(0, 0, 0, 25, 0, 0, 0, 7)),
    TOMAHAWK_8(new Config(0, 0, 0, 28, 0, 0, 0, 8)),
    TOMAHAWK_9(new Config(0, 0, 0, 31, 0, 0, 0, 9)),
    TOMAHAWK_11(new Config(0, 0, 0, 37, 0, 0, 0, 11)),
    MISSILE_3(new Config(3, 0, 0, 3, 0, 0, 0, 3)),
    MISSILE_4(new Config(3, 0, 0, 6, 0, 0, 2500, 4)),
    MISSILE_5(new Config(3, 0, 0, 10, 0, 0, 2500, 5)),
    MISSILE_6(new Config(3, 0, 0, 14, 0, 0, 2500, 6)),
    MISSILE_7(new Config(4, 0, 0, 19, 0, 0, 2500, 7)),
    MISSILE_8(new Config(4, 0, 0, 21, 0, 0, 2500, 8)),
    MISSILE_9(new Config(4, 0, 0, 24, 0, 0, 2500, 9)),
    MISSILE_10(new Config(4, 0, 0, 26, 0, 0, 2500, 10)),
    MISSILE_11(new Config(5, 0, 0, 28, 0, 0, 2500, 11)),
    PIRATE_5(new Config(0, 0, 11, 5, 0, 0, 0, 5)),
    PIRATE_6(new Config(0, 0, 12, 9, 0, 0, 0, 6)),
    PIRATE_7(new Config(0, 0, 12, 13, 0, 0, 0, 7)),
    PIRATE_8(new Config(0, 0, 13, 15, 0, 0, 0, 8)),
    PIRATE_9(new Config(0, 0, 14, 17, 0, 0, 0, 9)),
    PIRATE_11(new Config(0, 0, 15, 21, 0, 0, 0, 11)),
    ARA_1(new Config(0, 0, 3, 0, 0, 0, 0, 1)),
    ARA_2(new Config(0, 0, 4, 1, 0, 0, 7200, 2)),
    ARA_3(new Config(0, 0, 4, 3, 0, 0, 7200, 3)),
    ARA_4(new Config(0, 0, 5, 6, 0, 0, 7200, 4)),
    ARA_5(new Config(0, 0, 5, 10, 0, 0, 7200, 5)),
    ARA_6(new Config(0, 0, 5, 14, 0, 0, 7200, 6)),
    ARA_7(new Config(0, 0, 5, 19, 0, 0, 7200, 7)),
    ARA_8(new Config(0, 0, 5, 21, 0, 0, 7200, 8)),
    ARA_9(new Config(0, 0, 6, 24, 0, 0, 7200, 9)),
    ARA_10(new Config(0, 0, 6, 26, 0, 0, 7200, 10)),
    ARA_11(new Config(0, 0, 7, 28, 0, 0, 7200, 11));

    private final Config config;

    Wristband(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Wristband> maxLevel(int maxLevel) {
        return Arrays.stream(Wristband.values())
                .filter(wristband -> wristband.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
