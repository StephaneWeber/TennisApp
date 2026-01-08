package com.sweber.tennis.wiki_import;

public enum Grips {
    WARRIOR("The_Warrior"),
    TALON("The_Talon"),
    MACHETE("The_Machete"),
    COBRA("The_Cobra"),
    KATANA("The_Katana"),
    FORGE("The_Forge"),
    TACTICAL_GRIP("Tactical_Grip"),
    TITAN("The_Titan");

    private final String page;

    Grips(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
