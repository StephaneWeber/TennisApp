package com.sweber.tennis.wiki_import;

public enum Shoes {
    FEATHER("The_Feather"),
    RAPTOR("The_Raptor"),
    HUNTER("The_Hunter"),
    PIRANHA("The_Piranha"),
    SHURIKEN("The_Shuriken"),
    ANVIL("The_Anvil"),
    BALLISTIC("The_Ballistic"),
    HADES_TREADS("The_Hades_Treads");

    private String page;

    Shoes(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
