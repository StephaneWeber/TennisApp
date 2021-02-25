package com.sweber.tennis.wiki_import;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WikiImporter {
    private static final String RACKET = "RACKET";
    private static final String GRIP = "GRIP";
    private static final String SHOES = "SHOES";
    private static final String WRISTBAND = "WRISTBAND";
    private static final String NUTRITION = "NUTRITION";
    private static final String WORKOUT = "WORKOUT";

    private static final String GEAR_FILENAME = "src/main/resources/data/imported_gear.csv";
    private static final String PLAYER_FILENAME = "src/main/resources/data/imported_players.csv";

    private static BufferedWriter bufferedWriter;

    public void importPlayersData() throws IOException {
        System.out.println("Starting importing players data");
        bufferedWriter = new BufferedWriter(new FileWriter(PLAYER_FILENAME));
        bufferedWriter.write("Player,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");
        importPlayers();
        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(PLAYER_FILENAME);
        System.out.println("Imported players data to " + PLAYER_FILENAME);
    }

    public void importGearData() throws IOException {
        System.out.println("Starting importing gear data");
        bufferedWriter = new BufferedWriter(new FileWriter(GEAR_FILENAME));
        bufferedWriter.write("Name,Type,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");
        bufferedWriter.write("BASIC_RACKET_1,RACKET,0,0,0,0,3,0,0,1\n");
        importRackets();
        bufferedWriter.write("BASIC_GRIP_1,GRIP,0,0,0,5,0,3,0,1\n");
        importGrips();
        bufferedWriter.write("BASIC_SHOES_1,SHOES,3,0,0,0,0,0,0,1\n");
        importShoes();
        bufferedWriter.write("BASIC_WRIST_1,WRISTBAND,0,0,0,0,0,0,0,0\n");
        importWristbands();
        bufferedWriter.write("BASIC_NUTRITION_1,NUTRITION,0,0,0,0,0,0,0,1\n");
        importNutritions();
        bufferedWriter.write("BASIC_TRAINING_1,WORKOUT,0,0,1,0,0,0,0,1\n");
        importWorkouts();
        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(GEAR_FILENAME);
        System.out.println("Imported gear data to " + GEAR_FILENAME);
    }

    private void removeLastEmptyline(String fileName) throws IOException {
        RandomAccessFile f = new RandomAccessFile(fileName, "rw");
        long length = f.length() - 2;
        f.seek(length);
        f.readByte();
        f.setLength(length+1);
        f.close();
    }

    private void importPlayers() {
        processWikiPage("Jonah", "JONAH", null);
        processWikiPage("Hope", "HOPE", null);
        processWikiPage("Florence", "FLORENCE", null);
        processWikiPage("Leo", "LEO", null);
        processWikiPage("Kaito", "KAITO", null);
        processWikiPage("Viktoria", "VIKTORIA", null);
        processWikiPage("Diana", "DIANA", null);
        processWikiPage("Mei-Li", "MEILI", null);
        processWikiPage("Luc", "LUC", null);
    }

    private void importRackets() {
        processWikiPage("The_Eagle", "EAGLE", RACKET);
        processWikiPage("The_Patriot", "PATRIOT", RACKET);
        processWikiPage("The_Outback", "OUTBACK", RACKET);
        processWikiPage("The_Panther", "PANTHER", RACKET);
        processWikiPage("The_Samurai", "SAMURAI", RACKET);
        processWikiPage("The_Hammer", "HAMMER", RACKET);
        processWikiPage("The_Bullseye", "BULLS_EYE", RACKET);
        processWikiPage("Zeus", "ZEUS", RACKET);
    }

    private void importGrips() {
        processWikiPage("The_Warrior", "WARRIOR", GRIP);
        processWikiPage("The_Talon", "TALON", GRIP);
        processWikiPage("The_Machete", "MACHETE", GRIP);
        processWikiPage("The_Cobra", "COBRA", GRIP);
        processWikiPage("The_Katana", "KATANA", GRIP);
        processWikiPage("The_Forge", "FORGE", GRIP);
        processWikiPage("Tactical_Grip", "TACTICAL_GRIP", GRIP);
        processWikiPage("The_Titan", "TITAN", GRIP);
    }

    private void importShoes() {
        processWikiPage("The_Feather", "FEATHER", SHOES);
        processWikiPage("The_Raptor", "RAPTOR", SHOES);
        processWikiPage("The_Hunter", "HUNTER", SHOES);
        processWikiPage("The_Piranha", "PIRANHA", SHOES);
        processWikiPage("The_Shuriken", "SHURIKEN", SHOES);
        processWikiPage("The_Anvil", "ANVIL", SHOES);
        processWikiPage("The_Ballistic", "BALLISTIC", SHOES);
        processWikiPage("The_Hades_Treads", "HADES_TREADS", SHOES);
    }

    private void importWristbands() {
        processWikiPage("The_Tomahawk", "TOMAHAWK", WRISTBAND);
        processWikiPage("The_Rocket", "MISSILE", WRISTBAND);
        processWikiPage("Jolly_Roger", "PIRATE", WRISTBAND);
        processWikiPage("The_Macaw", "ARA", WRISTBAND);
        processWikiPage("The_Koi", "KOI", WRISTBAND);
        processWikiPage("The_Kodiak", "KODIAK", WRISTBAND);
        processWikiPage("The_Gladiator", "GLADIATOR", WRISTBAND);
        processWikiPage("The_Shield", "SHIELD", WRISTBAND);
    }

    private void importNutritions() {
        processWikiPage("Lean_Protein", "PROTEIN", NUTRITION);
        processWikiPage("Increased_Hydration", "HYDRATION", NUTRITION);
        processWikiPage("Macrobiotic", "MACROBIOTICS", NUTRITION);
        processWikiPage("Vegan_Diet", "VEGAN", NUTRITION);
        processWikiPage("Keto_Sourcing", "KETO_SOURCE", NUTRITION);
        processWikiPage("Antioxidants", "ANTIOXIDANTS", NUTRITION);
        processWikiPage("Carboload", "CARBO_LOAD", NUTRITION);
        processWikiPage("Neutral_Energy", "NEUTRAL_ENERGY", NUTRITION);
    }

    private void importWorkouts() {
        processWikiPage("Endurance", "ENDURANCE", WORKOUT);
        processWikiPage("Sprint", "SPRINT", WORKOUT);
        processWikiPage("Plyometrics", "PLYOMETRICS", WORKOUT);
        processWikiPage("Powerlifting", "POWERLIFTING", WORKOUT);
        processWikiPage("Weight_lifting", "WEIGHTLIFTING", WORKOUT);
        processWikiPage("Resistance_Band", "RESISTANCE_BAND", WORKOUT);
        processWikiPage("Mountain_Climber", "MOUNTAIN_CLIMBER", WORKOUT);
        processWikiPage("Lunges", "LUNGES", WORKOUT);
    }

    private void processWikiPage(String page, String itemName, String itemType) {
        try {
            WikiPage wikiPage = new WikiPage(page, itemName, itemType);
            StringBuffer stringBuffer = wikiPage.processWikiPage();
            //write contents of StringBuffer to a file
            bufferedWriter.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}