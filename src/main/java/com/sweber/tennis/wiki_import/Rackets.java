package com.sweber.tennis.wiki_import;

public enum Rackets {
    EAGLE("The_Eagle"),
    PATRIOT("The_Patriot"),
    OUTBACK("The_Outback"),
    PANTHER("The_Panther"),
    SAMURAI("The_Samurai"),
    HAMMER("The_Hammer"),
    BULLS_EYE("The_Bullseye"),
    ZEUS("Zeus");

    private String page;

    Rackets(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
