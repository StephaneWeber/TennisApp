package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for rackets yet unknown to me
public enum Racket {
    BASIC_RACKET_1(new Config(0, 0, 0, 0, 3, 0, 0, 1)),
    EAGLE_5(new Config(3, 0, 0, 0, 13, 0, 2500, 5)),
    EAGLE_6(new Config(3, 0, 0, 0, 16, 0, 4600, 6)),
    EAGLE_7(new Config(4, 0, 0, 0, 20, 0, 7200, 7)),
    EAGLE_8(new Config(4, 0, 0, 0, 23, 0, 10900, 8)),
    EAGLE_9(new Config(4, 0, 0, 0, 25, 0, 15000, 9)),
    EAGLE_10(new Config(4, 0, 0, 0, 27, 0, 21700, 10)),
    EAGLE_11(new Config(5, 0, 0, 0, 30, 0, 27500, 11)),
    PATRIOT_5(new Config(0, 0, 0, 0, 15, 6, 0, 5)),
    PATRIOT_6(new Config(0, 0, 0, 0, 19, 6, 0, 6)),
    PATRIOT_7(new Config(0, 0, 0, 0, 23, 6, 0, 7)),
    PATRIOT_8(new Config(0, 0, 0, 0, 25, 7, 0, 8)),
    PATRIOT_10(new Config(0, 0, 0, 0, 30, 8, 0, 10)),
    PATRIOT_11(new Config(0, 0, 0, 0, 33, 8, 0, 11)),
    PANTHER_3(new Config(0, 0, 0, 0, 11, 0, 0, 3)),
    PANTHER_4(new Config(0, 0, 0, 0, 14, 0, 15000, 4)),
    PANTHER_5(new Config(0, 0, 0, 0, 18, 0, 15000, 5)),
    PANTHER_6(new Config(0, 0, 0, 0, 22, 0, 15000, 6)),
    PANTHER_7(new Config(0, 0, 0, 0, 26, 0, 15000, 7)),
    PANTHER_8(new Config(0, 0, 0, 0, 28, 0, 15000, 8)),
    PANTHER_9(new Config(0, 0, 0, 0, 31, 0, 15000, 9)),
    PANTHER_10(new Config(0, 0, 0, 0, 34, 0, 15000, 10)),
    PANTHER_11(new Config(0, 0, 0, 0, 36, 0, 15000, 11));

    private final Config config;

    Racket(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Racket> maxLevel(int maxLevel) {
        return Arrays.stream(Racket.values())
                .filter(racket -> racket.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
