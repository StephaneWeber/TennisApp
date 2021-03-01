package com.sweber.tennis.wiki_import;

public enum Nutritions {
    PROTEIN("Lean_Protein"),
    HYDRATION("Increased_Hydration"),
    MACROBIOTICS("Macrobiotic"),
    VEGAN("Vegan_Diet"),
    KETO_SOURCE("Keto_Sourcing"),
    ANTIOXIDANTS("Antioxidants"),
    CARBO_LOAD("Carboload"),
    NEUTRAL_ENERGY("Neutral_Energy");

    private String page;

    Nutritions(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
