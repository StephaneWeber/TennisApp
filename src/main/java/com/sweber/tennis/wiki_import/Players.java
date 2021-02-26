package com.sweber.tennis.wiki_import;

public enum Players {
    JONAH("Jonah"),
    HOPE("Hope"),
    FLORENCE("Florence"),
    LEO("Leo"),
    KAITO("Kaito"),
    VIKTORIA("Viktoria"),
    DIANA("Diana"),
    MEILI("Mei-Li"),
    LUC("Luc");

    private String page;

    Players(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}