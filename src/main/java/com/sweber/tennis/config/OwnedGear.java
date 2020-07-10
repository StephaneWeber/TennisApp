package com.sweber.tennis.config;

import com.sweber.tennis.model.Player;
import com.sweber.tennis.model.gear.Grip;
import com.sweber.tennis.model.gear.Nutrition;
import com.sweber.tennis.model.gear.Racket;
import com.sweber.tennis.model.gear.Shoes;
import com.sweber.tennis.model.gear.Training;
import com.sweber.tennis.model.gear.Wrist;

import java.util.Arrays;
import java.util.List;

public class OwnedGear {
    public static List<Player> players = Arrays.asList();
    public static List<Racket> rackets = Arrays.asList();
    public static List<Grip> grips = Arrays.asList();
    public static List<Shoes> shoes = Arrays.asList();
    public static List<Wrist> wrists = Arrays.asList();
    public static List<Nutrition> nutritions = Arrays.asList();
    public static List<Training> trainings = Arrays.asList();

    public boolean isUpgradeableTo(Racket racket) {
        String configRacketName = racket.name().substring(0, racket.name().indexOf("_"));
        int racketLevel = racket.getConfig().getLevel();
        Integer currentLevel = rackets.stream()
                .filter(item -> item.name().startsWith(configRacketName))
                .findFirst()
                .map(Racket::getConfig)
                .map(Config::getLevel)
                .orElseThrow(IllegalStateException::new);
        return (racketLevel - currentLevel) <= 1;
    }
}
