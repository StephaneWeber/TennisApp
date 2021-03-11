package com.sweber.tennis.wiki_import;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class WikiImporter {
    private static final String RACKET = "RACKET";
    private static final String GRIP = "GRIP";
    private static final String SHOES = "SHOES";
    private static final String WRISTBAND = "WRISTBAND";
    private static final String NUTRITION = "NUTRITION";
    private static final String WORKOUT = "WORKOUT";

    private static final String GEAR_FILENAME = "src/main/resources/data/gear.csv";
    private static final String IMPORTED_GEAR_FILENAME = "src/main/resources/data/imported_gear.csv";
    private static final String DELTA_GEAR_FILENAME = "src/main/resources/data/changed_gear.csv";
    private static final String PLAYER_FILENAME = "src/main/resources/data/players.csv";
    private static final String IMPORTED_PLAYER_FILENAME = "src/main/resources/data/imported_players.csv";
    private static final String DELTA_PLAYER_FILENAME = "src/main/resources/data/changed_players.csv";

    private BufferedWriter bufferedWriter;

    public void importPlayersData() throws IOException {
        System.out.println("Starting importing players data");
        bufferedWriter = new BufferedWriter(new FileWriter(IMPORTED_PLAYER_FILENAME));
        importPlayers();
        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(IMPORTED_PLAYER_FILENAME);
        System.out.println("Imported players data to " + IMPORTED_PLAYER_FILENAME);

        System.out.println("Saving changes into " + DELTA_PLAYER_FILENAME);
        bufferedWriter = new BufferedWriter(new FileWriter(DELTA_PLAYER_FILENAME));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PLAYER_FILENAME));
             BufferedReader bufferedReaderImported = new BufferedReader(new FileReader(IMPORTED_PLAYER_FILENAME))) {
            bufferedReader.lines()
                    .forEach(line -> compareLine(bufferedWriter, bufferedReaderImported, line));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void compareLine(BufferedWriter bufferedWriter, BufferedReader bufferedReader, String line) {
        String importedLine = null;
        try {
            importedLine = bufferedReader.readLine();
            if (!importedLine.equals(line)) {
                bufferedWriter.write(line + "\n");
                bufferedWriter.write(importedLine + "\n");
                bufferedWriter.write("=====\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importGearData() throws IOException {
        System.out.println("Starting importing gear data");
        bufferedWriter = new BufferedWriter(new FileWriter(IMPORTED_GEAR_FILENAME));
        bufferedWriter.write("Name,Type,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");
        importRackets();
        importGrips();
        importShoes();
        importWristbands();
        importNutritions();
        importWorkouts();
        bufferedWriter.flush();
        bufferedWriter.close();
        removeLastEmptyline(IMPORTED_GEAR_FILENAME);
        System.out.println("Imported gear data to " + IMPORTED_GEAR_FILENAME);

        System.out.println("Saving changes into " + DELTA_GEAR_FILENAME);
        bufferedWriter = new BufferedWriter(new FileWriter(DELTA_GEAR_FILENAME));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(GEAR_FILENAME));
             BufferedReader bufferedReaderImported = new BufferedReader(new FileReader(IMPORTED_GEAR_FILENAME))) {
            bufferedReader.lines()
                    .forEach(line -> compareLine(bufferedWriter, bufferedReaderImported, line));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    private void removeLastEmptyline(String fileName) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(fileName, "rw")) {
            long length = f.length() - 2;
            f.seek(length);
            f.readByte();
            f.setLength(length + 1);
        }
    }

    private void importPlayers() throws IOException {
        bufferedWriter.write("Player,Agility,Endurance,Service,Volley,Forehand,Backhand,Cost,Level\n");
        Arrays.stream(Players.values())
                .forEach(player -> processWikiPage(player.getPage(), player.name(), null));
    }

    private void importRackets() throws IOException {
        bufferedWriter.write("BASIC_RACKET_1,RACKET,0,0,0,0,3,0,0,1\n");
        Arrays.stream(Rackets.values())
                .forEach(racket -> processWikiPage(racket.getPage(), racket.name(), RACKET));
    }

    private void importGrips() throws IOException {
        bufferedWriter.write("BASIC_GRIP_1,GRIP,0,0,0,5,0,3,0,1\n");
        Arrays.stream(Grips.values())
                .forEach(grip -> processWikiPage(grip.getPage(), grip.name(), GRIP));
    }

    private void importShoes() throws IOException {
        bufferedWriter.write("BASIC_SHOES_1,SHOES,3,0,0,0,0,0,0,1\n");
        Arrays.stream(Shoes.values())
                .forEach(shoes -> processWikiPage(shoes.getPage(), shoes.name(), SHOES));
    }

    private void importWristbands() throws IOException {
        bufferedWriter.write("BASIC_WRIST_1,WRISTBAND,0,0,0,0,0,0,0,0\n");
        Arrays.stream(Wristbands.values())
                .forEach(wristband -> processWikiPage(wristband.getPage(), wristband.name(), WRISTBAND));
    }

    private void importNutritions() throws IOException {
        bufferedWriter.write("BASIC_NUTRITION_1,NUTRITION,0,0,0,0,0,0,0,1\n");
        Arrays.stream(Nutritions.values())
                .forEach(nutrition -> processWikiPage(nutrition.getPage(), nutrition.name(), NUTRITION));
    }

    private void importWorkouts() throws IOException {
        bufferedWriter.write("BASIC_TRAINING_1,WORKOUT,0,0,1,0,0,0,0,1\n");
        Arrays.stream(Workouts.values())
                .forEach(workout -> processWikiPage(workout.getPage(), workout.name(), WORKOUT));
    }

    private void processWikiPage(String page, String itemName, String itemType) {
        try {
            WikiPage wikiPage = new WikiPage(page, itemName, itemType);
            StringBuilder stringBuffer = wikiPage.processWikiPage();
            //write contents of StringBuffer to a file
            bufferedWriter.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replaceDatasetWithImport() {
        System.out.println("Updating data with import files");
        try {
            Files.move(Paths.get(IMPORTED_PLAYER_FILENAME), Paths.get(PLAYER_FILENAME), StandardCopyOption.REPLACE_EXISTING);
            Files.move(Paths.get(IMPORTED_GEAR_FILENAME), Paths.get(GEAR_FILENAME), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}