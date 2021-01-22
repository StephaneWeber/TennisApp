package com.sweber.tennis.wiki_import;

import java.io.IOException;

public class LoadFromWiki {
    public static final String RACKET = "RACKET";
    public static final String GRIP = "GRIP";
    public static final String SHOES = "SHOES";
    public static final String WRISTBAND = "WRISTBAND";
    public static final String NUTRITION = "NUTRITION";
    public static final String WORKOUT = "WORKOUT";

    public static void main(String... args) throws IOException {
//        importRackets();
//        importGrips();
//        importShoes();
//        importWristbands();
 //       importNutritions();
 //       importWorkouts();
    }

    private static void importRackets() {
        processWikiPage("The_Eagle", "EAGLE", RACKET);
        processWikiPage("The_Patriot", "PATRIOT", RACKET);
        processWikiPage("The_Outback", "OUTBACK", RACKET);
        processWikiPage("The_Panther", "PANTHER", RACKET);
        processWikiPage("The_Samurai", "SAMURAI", RACKET);
        processWikiPage("The_Hammer", "HAMMER", RACKET);
        processWikiPage("The_Bullseye", "BULLS_EYE", RACKET);
        processWikiPage("Zeus", "ZEUS", RACKET);
    }

    private static void importGrips() {
        processWikiPage("The_Warrior", "WARRIOR", GRIP);
        processWikiPage("The_Talon", "TALON", GRIP);
        processWikiPage("The_Machete", "MACHETE", GRIP);
        processWikiPage("The_Cobra", "COBRA", GRIP);
        processWikiPage("The_Katana", "KATANA", GRIP);
        processWikiPage("The_Forge", "FORGE", GRIP);
        processWikiPage("Tactical_Grip", "TACTICAL_GRIP", GRIP);
        processWikiPage("The_Titan", "TITAN", GRIP);
    }

    private static void importShoes() {
        processWikiPage("The_Feather", "FEATHER", SHOES);
        processWikiPage("The_Raptor", "RAPTOR", SHOES);
        processWikiPage("The_Hunter", "HUNTER", SHOES);
        processWikiPage("The_Piranha", "PIRANHA", SHOES);
        processWikiPage("The_Shuriken", "SHURIKEN", SHOES);
        processWikiPage("The_Anvil", "ANVIL", SHOES);
        processWikiPage("The_Ballistic", "BALLISTIC", SHOES);
        processWikiPage("The_Hades_Treads", "HADES_TREADS", SHOES);
    }

    private static void importWristbands() {
        processWikiPage("The_Tomahawk", "TOMAHAWK", WRISTBAND);
        processWikiPage("The_Rocket", "MISSILE", WRISTBAND);
        processWikiPage("Jolly_Roger", "PIRATE", WRISTBAND);
        processWikiPage("The_Macaw", "ARA", WRISTBAND);
        processWikiPage("The_Koi", "KOI", WRISTBAND);
        processWikiPage("The_Kodiak", "KODIAK", WRISTBAND);
        processWikiPage("The_Gladiator", "GLADIATOR", WRISTBAND);
        processWikiPage("The_Shield", "SHIELD", WRISTBAND);
    }

    private static void importNutritions() {
        processWikiPage("Lean_Protein", "PROTEIN", NUTRITION);
        processWikiPage("Increased_Hydration", "HYDRATION", NUTRITION);
        processWikiPage("Macrobiotic", "MACROBIOTICS", NUTRITION);
        processWikiPage("Vegan_Diet", "VEGAN", NUTRITION);
        processWikiPage("Keto_Sourcing", "KETO_SOURCE", NUTRITION);
        processWikiPage("Antioxidants", "ANTIOXIDANTS", NUTRITION);
        processWikiPage("Carboload", "CARBO_LOAD", NUTRITION);
        processWikiPage("Neutral_Energy", "NEUTRAL_ENERGY", NUTRITION);
    }

    private static void processWikiPage(String page, String itemName, String itemType) {
        try {
            WikiPage wikiPage = new WikiPage(page, itemName, itemType);
            wikiPage.processWikiPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}