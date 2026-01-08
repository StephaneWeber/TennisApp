package com.sweber.tennis.wiki_import;

public enum Players {
    JONAH("Jonah"),
    HOPE("Hope"),
    FLORENCE("Florence"),
    LEO("Leo"),
    KAITO("Kaito"),
    VIKTORIA("Viktoria"),
    OMAR("Omar"),
    DIANA("Diana"),
    ABEKE("Abeke"),
    MEILI("Mei-Li"),
    LUC("Luc"),
    DIEGO("Diego"),
    MARK("Mark");

    private final String page;

    Players(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
