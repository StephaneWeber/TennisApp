package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for shoes yet unknown to me
public enum Shoes {
    BASIC_SHOES_1(new Config(3, 0, 0, 0, 0, 0, 0, 1)),
    FEATHER_4(new Config(18, 0, 0, 0, 0, 0, 0, 4)),
    FEATHER_5(new Config(23, 0, 0, 0, 0, 0, 2500, 5)),
    FEATHER_6(new Config(28, 0, 0, 0, 0, 0, 2500, 6)),
    FEATHER_7(new Config(34, 0, 0, 0, 0, 0, 2500, 7)),
    FEATHER_8(new Config(38, 0, 0, 0, 0, 0, 2500, 8)),
    FEATHER_11(new Config(50, 0, 0, 0, 0, 0, 2500, 11)),
    RAPTOR_5(new Config(19, 6, 0, 0, 0, 0, 0, 5)),
    RAPTOR_6(new Config(24, 6, 0, 0, 0, 0, 0, 6)),
    RAPTOR_7(new Config(29, 6, 0, 0, 0, 0, 10900, 7)),
    RAPTOR_8(new Config(33, 7, 0, 0, 0, 0, 10900, 8)),
    RAPTOR_9(new Config(36, 7, 0, 0, 0, 0, 10900, 9)),
    RAPTOR_10(new Config(40, 8, 0, 0, 0, 0, 10900, 10)),
    RAPTOR_11(new Config(44, 8, 0, 0, 0, 0, 10900, 11)),
    HUNTER_3(new Config(9, 0, 4, 0, 0, 0, 0, 3)),
    HUNTER_4(new Config(14, 0, 5, 0, 0, 0, 7200, 4)),
    HUNTER_5(new Config(19, 0, 5, 0, 0, 0, 7200, 5)),
    HUNTER_6(new Config(24, 0, 5, 0, 0, 0, 7200, 6)),
    HUNTER_7(new Config(29, 0, 5, 0, 0, 0, 7200, 7)),
    HUNTER_8(new Config(33, 0, 5, 0, 0, 0, 7200, 8)),
    HUNTER_9(new Config(36, 0, 6, 0, 0, 0, 7200, 9)),
    HUNTER_10(new Config(40, 0, 6, 0, 0, 0, 7200, 10)),
    HUNTER_11(new Config(44, 0, 7, 0, 0, 0, 7200, 11)),
    PIRANHA_4(new Config(10, 12, 0, 0, 0, 0, 0, 4)),
    PIRANHA_5(new Config(15, 13, 0, 0, 0, 0, 21700, 5)),
    PIRANHA_6(new Config(20, 14, 0, 0, 0, 0, 21700, 6)),
    PIRANHA_7(new Config(25, 15, 0, 0, 0, 0, 21700, 7)),
    PIRANHA_8(new Config(28, 15, 0, 0, 0, 0, 21700, 8)),
    PIRANHA_11(new Config(39, 18, 0, 0, 0, 0, 21700, 11));

    private final Config config;

    Shoes(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Shoes> maxLevel(int maxLevel) {
        return Arrays.stream(Shoes.values())
                .filter(shoes -> shoes.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
