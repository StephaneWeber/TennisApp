package com.sweber.tennis.wiki_import;

import java.io.IOException;

public class LoadFromWiki {

    public static final String RACKET = "RACKET";
    public static final String GRIP = "GRIP";

    public static void main(String... args) throws IOException {
        importRackets();
        importGrips();
//        importShoes();
    }

    private static void importRackets() throws IOException {
        processWikiPage("The_Eagle", "EAGLE", RACKET);
        processWikiPage("The_Patriot", "PATRIOT", RACKET);
        processWikiPage("The_Outback", "OUTBACK", RACKET);
        processWikiPage("The_Panther", "PANTHER", RACKET);
        processWikiPage("The_Samurai", "SAMURAI", RACKET);
        processWikiPage("The_Hammer", "HAMMER", RACKET);
        processWikiPage("The_Bullseye", "BULLS_EYE", RACKET);
        processWikiPage("Zeus", "ZEUS", RACKET);
    }

    private static void importGrips() throws IOException {
        processWikiPage("The_Warrior", "WARRIOR", GRIP);
        processWikiPage("The_Talon", "TALON", GRIP);
        processWikiPage("The_Machete", "MACHETE", GRIP);
        processWikiPage("The_Cobra", "COBRA", GRIP);
        processWikiPage("The_Katana", "KATANA", GRIP);
        processWikiPage("The_Forge", "FORGE", GRIP);
        processWikiPage("Tactical_Grip", "TACTICAL_GRIP", GRIP);
        processWikiPage("The_Titan", "TITAN", GRIP);
    }

    private static void processWikiPage(String page, String itemName, String itemType) throws IOException {
        WikiPage wikiPage = new WikiPage(page, itemName, itemType);
        wikiPage.processWikiPage();
    }
}