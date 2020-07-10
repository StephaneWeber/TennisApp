package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for nutritions yet unknown to me
public enum Nutrition {
    BASIC_NUTRITION_1(new Config(0, 0, 0, 0, 0, 0, 0, 1)),
    PROTEIN_4(new Config(0, 13, 0, 0, 0, 0, 0, 4)),
    PROTEIN_5(new Config(0, 17, 0, 0, 0, 0, 0, 5)),
    PROTEIN_6(new Config(0, 22, 0, 0, 0, 0, 0, 6)),
    PROTEIN_7(new Config(0, 26, 0, 0, 0, 0, 0, 7)),
    PROTEIN_8(new Config(0, 29, 0, 0, 0, 0, 0, 8)),
    PROTEIN_11(new Config(0, 38, 0, 0, 0, 0, 0, 11)),
    HYDRATION_5(new Config(0, 10, 0, 0, 4, 0, 0, 5)),
    HYDRATION_6(new Config(0, 14, 0, 0, 4, 0, 7200, 6)),
    HYDRATION_7(new Config(0, 18, 0, 0, 4, 0, 7200, 7)),
    HYDRATION_8(new Config(0, 21, 0, 0, 4, 0, 7200, 8)),
    HYDRATION_9(new Config(0, 23, 0, 0, 5, 0, 7200, 9)),
    HYDRATION_10(new Config(0, 25, 0, 0, 5, 0, 7200, 10)),
    HYDRATION_11(new Config(0, 28, 0, 0, 5, 0, 7200, 11)),
    MACROBIOTICS(new Config(0, 0, 0, 11, 0, 0, 0, 0)),
    MACROBIOTICS_2(new Config(0, 4, 0, 11, 0, 0, 10900, 0)),
    VEGAN_1(new Config(0, 0, 0, 3, 0, 0, 0, 1)),
    VEGAN_2(new Config(0, 1, 0, 4, 0, 0, 7200, 2)),
    VEGAN_3(new Config(0, 3, 0, 4, 0, 0, 7200, 3)),
    VEGAN_4(new Config(0, 6, 0, 5, 0, 0, 7200, 4)),
    VEGAN_5(new Config(0, 10, 0, 5, 0, 0, 7200, 5)),
    VEGAN_6(new Config(0, 14, 0, 5, 0, 0, 7200, 6)),
    VEGAN_7(new Config(0, 18, 0, 5, 0, 0, 7200, 7)),
    VEGAN_8(new Config(0, 21, 0, 5, 0, 0, 7200, 8)),
    VEGAN_9(new Config(0, 23, 0, 6, 0, 0, 7200, 9)),
    VEGAN_10(new Config(0, 25, 0, 6, 0, 0, 7200, 10)),
    VEGAN_11(new Config(0, 28, 0, 7, 0, 0, 7200, 11));

    private final Config config;

    Nutrition(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Nutrition> maxLevel(int maxLevel) {
        return Arrays.stream(Nutrition.values())
                .filter(nutrition -> nutrition.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
