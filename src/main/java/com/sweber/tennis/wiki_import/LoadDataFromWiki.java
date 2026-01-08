package com.sweber.tennis.wiki_import;

import java.io.IOException;

public class LoadDataFromWiki {
    public static void main(String... args) throws IOException {
        // Use TOLERATE so imports do not abort on transient fetch errors by default
        WikiImporter wikiImporter = new WikiImporter(WikiImporter.FetchFailureMode.TOLERATE_WITH_PLACEHOLDERS);
        wikiImporter.importPlayersData();
        wikiImporter.importGearData();
        wikiImporter.replaceDatasetWithImport();
    }
}