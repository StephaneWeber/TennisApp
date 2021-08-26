package com.sweber.tennis.wiki_import;

public enum Workouts {
    ENDURANCE("Endurance"),
    SPRINT("Sprint"),
    PLYOMETRICS("Plyometrics"),
    POWERLIFTING("Powerlifting"),
    WEIGHTLIFTING("Weight_lifting"),
    RESISTANCE_BAND("Resistance_Band"),
    MOUNTAIN_CLIMBER("Mountain_Climber"),
    LUNGES("Lunges");

    private String page;

    Workouts(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
