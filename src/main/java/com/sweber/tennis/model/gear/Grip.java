package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for grips yet unknown to me
public enum Grip {
    BASIC_GRIP(new Config(0, 0, 0, 5, 0, 3, 0, 1)),
    WARRIOR_5(new Config(0, 0, 0, 5, 0, 13, 0, 5)),
    WARRIOR_6(new Config(0, 0, 0, 5, 0, 16, 0, 6)),
    WARRIOR_7(new Config(0, 0, 0, 5, 0, 20, 0, 7)),
    WARRIOR_8(new Config(0, 0, 0, 5, 0, 23, 0, 8)),
    WARRIOR_9(new Config(0, 0, 0, 6, 0, 25, 0, 9)),
    WARRIOR_10(new Config(0, 0, 0, 6, 0, 27, 0, 10)),
    WARRIOR_11(new Config(0, 0, 0, 7, 0, 30, 0, 11)),
    TALON_5(new Config(0, 0, 0, 0, 6, 15, 0, 5)),
    TALON_6(new Config(0, 0, 0, 0, 6, 19, 0, 6)),
    TALON_7(new Config(0, 0, 0, 0, 6, 23, 0, 7)),
    TALON_8(new Config(0, 0, 0, 0, 7, 25, 0, 8)),
    TALON_9(new Config(0, 0, 0, 0, 7, 28, 0, 9)),
    TALON_10(new Config(0, 0, 0, 0, 7, 30, 0, 10)),
    TALON_11(new Config(0, 0, 0, 0, 8, 33, 0, 11)),
    MACHETE_3(new Config(0, 0, 0, 0, 0, 11, 0, 3)),
    MACHETE_4(new Config(0, 0, 0, 0, 0, 14, 7200, 4)),
    MACHETE_5(new Config(0, 0, 0, 0, 0, 18, 7200, 5)),
    MACHETE_6(new Config(0, 0, 0, 0, 0, 22, 7200, 6)),
    MACHETE_7(new Config(0, 0, 0, 0, 0, 26, 7200, 7)),
    MACHETE_8(new Config(0, 0, 0, 0, 0, 28, 7200, 8)),
    MACHETE_9(new Config(0, 0, 0, 0, 0, 31, 7200, 9)),
    MACHETE_10(new Config(0, 0, 0, 0, 0, 34, 7200, 10)),
    MACHETE_11(new Config(0, 0, 0, 0, 0, 36, 7200, 11));

    private final Config config;

    Grip(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Grip> maxLevel(int maxLevel) {
        return Arrays.stream(Grip.values())
                .filter(grip -> grip.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
