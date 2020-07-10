package com.sweber.tennis.model.gear;

import com.sweber.tennis.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO Input all data from website for workouts yet unknown to me
public enum Workout {
    BASIC_TRAINING_1(new Config(0, 0, 1, 0, 0, 0, 0, 1)),
    ENDURANCE_5(new Config(0, 6, 11, 0, 0, 0, 0, 5)),
    ENDURANCE_6(new Config(0, 6, 16, 0, 0, 0, 0, 6)),
    ENDURANCE_7(new Config(0, 6, 20, 0, 0, 0, 0, 7)),
    ENDURANCE_8(new Config(0, 7, 23, 0, 0, 0, 0, 8)),
    ENDURANCE_9(new Config(0, 7, 25, 0, 0, 0, 0, 9)),
    ENDURANCE_10(new Config(0, 8, 27, 0, 0, 0, 0, 10)),
    ENDURANCE_11(new Config(0, 8, 30, 0, 0, 0, 0, 11)),
    PLIOMETRICS_3(new Config(0, 0, 4, 0, 0, 3, 0, 3)),
    PLIOMETRICS_4(new Config(0, 0, 7, 0, 0, 4, 7200, 4)),
    PLIOMETRICS_5(new Config(0, 0, 11, 0, 0, 4, 7200, 5)),
    PLIOMETRICS_6(new Config(0, 0, 16, 0, 0, 4, 7200, 6)),
    PLIOMETRICS_7(new Config(0, 0, 20, 0, 0, 4, 7200, 7)),
    PLIOMETRICS_8(new Config(0, 0, 23, 0, 0, 4, 7200, 8)),
    PLIOMETRICS_9(new Config(0, 0, 25, 0, 0, 5, 7200, 9)),
    PLIOMETRICS_10(new Config(0, 0, 27, 0, 0, 5, 7200, 10)),
    PLIOMETRICS_11(new Config(0, 0, 30, 0, 0, 5, 7200, 11));

    private final Config config;

    Workout(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public static List<Workout> maxLevel(int maxLevel) {
        return Arrays.stream(Workout.values())
                .filter(workout -> workout.getConfig().getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }
}
