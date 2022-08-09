package com.sweber.tennis.model.config;

import com.sweber.tennis.model.gear.GearItem;

public class GearConfig {
    protected GearItem racket;
    protected GearItem grip;
    protected GearItem shoes;
    protected GearItem wristband;
    protected GearItem nutrition;
    protected GearItem workout;

    public static GearConfig of(GearItem racket, GearItem grip, GearItem shoes, GearItem wristband, GearItem nutrition, GearItem workout) {
        GearConfig gearConfig = new GearConfig();
        gearConfig.setRacket(racket);
        gearConfig.setGrip(grip);
        gearConfig.setShoes(shoes);
        gearConfig.setWristband(wristband);
        gearConfig.setNutrition(nutrition);
        gearConfig.setWorkout(workout);
        return gearConfig;
    }

    public GearConfig cloneConfig() {
        return of(racket, grip, shoes, wristband, nutrition, workout);
    }

    public GearItem getRacket() {
        return racket;
    }

    public void setRacket(GearItem racket) {
        this.racket = racket;
    }

    public GearItem getGrip() {
        return grip;
    }

    public void setGrip(GearItem grip) {
        this.grip = grip;
    }

    public GearItem getShoes() {
        return shoes;
    }

    public void setShoes(GearItem shoes) {
        this.shoes = shoes;
    }

    public GearItem getWristband() {
        return wristband;
    }

    public void setWristband(GearItem wristband) {
        this.wristband = wristband;
    }

    public GearItem getNutrition() {
        return nutrition;
    }

    public void setNutrition(GearItem nutrition) {
        this.nutrition = nutrition;
    }

    public GearItem getWorkout() {
        return workout;
    }

    public void setWorkout(GearItem workout) {
        this.workout = workout;
    }

    public int getCost() {
        return racket.getCost() + grip.getCost() + shoes.getCost() + wristband.getCost() + nutrition.getCost() + workout.getCost();
    }
}
