package com.sweber.tennis.wiki_import;

public enum Wristbands {
    TOMAHAWK("The_Tomahawk"),
    MISSILE("The_Rocket"),
    PIRATE("Jolly_Roger"),
    ARA("The_Macaw"),
    KOI("The_Koi"),
    KODIAK("The_Kodiak"),
    GLADIATOR("The_Gladiator"),
    SHIELD("The_Shield");

    private final String page;

    Wristbands(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
