package com.sweber.tennis.wiki_import;

import java.io.IOException;

public class LoadDataFromWiki {
    public static void main(String... args) throws IOException {
        WikiImporter wikiImporter = new WikiImporter();
        wikiImporter.importPlayersData();
        wikiImporter.importGearData();
        wikiImporter.replaceDatasetWithImport();
    }
}