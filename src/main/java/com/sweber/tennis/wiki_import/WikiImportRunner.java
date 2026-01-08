package com.sweber.tennis.wiki_import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WikiImportRunner {
    public static void main(String[] args) throws Exception {
        boolean dryRun = Arrays.asList(args).contains("--dry-run");
        boolean doPlayers = Arrays.asList(args).contains("players");
        boolean doGear = Arrays.asList(args).contains("gear");
        if (!doPlayers && !doGear) {
            System.out.println("Usage: java -jar ... --dry-run players|gear");
            System.out.println("Example: mvn -DskipTests -Dexec.mainClass=com.sweber.tennis.wiki_import.WikiImportRunner -Dexec.args=\"--dry-run players\" exec:java");
            return;
        }

        if (doPlayers) {
            List<WikiPage> pages = new ArrayList<>();
            Arrays.stream(Players.values()).forEach(p -> pages.add(new WikiPage(p.getPage(), p.name(), null)));
            if (dryRun) {
                System.out.println("Players pages (dry-run):");
                pages.forEach(p -> System.out.println(p.getUrl()));
            } else {
                System.out.println("Fetching players pages...");
                WikiFetcher fetcher = new WikiFetcher(10, 2);
                fetcher.fetchAndProcessAll(pages, r -> {
                    if (r.error != null) {
                        System.err.println("Error: " + r.error.getMessage());
                    } else {
                        System.out.println("Fetched: " + r.page.getUrl());
                    }
                });
                fetcher.shutdown();
            }
        }

        if (doGear) {
            List<WikiPage> pages = new ArrayList<>();
            Arrays.stream(Rackets.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "RACKET")));
            Arrays.stream(Grips.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "GRIP")));
            Arrays.stream(Shoes.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "SHOES")));
            Arrays.stream(Wristbands.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "WRISTBAND")));
            Arrays.stream(Nutritions.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "NUTRITION")));
            Arrays.stream(Workouts.values()).forEach(r -> pages.add(new WikiPage(r.getPage(), r.name(), "WORKOUT")));
            if (dryRun) {
                System.out.println("Gear pages (dry-run):");
                pages.forEach(p -> System.out.println(p.getUrl()));
            } else {
                System.out.println("Fetching gear pages...");
                WikiFetcher fetcher = new WikiFetcher(20, 4);
                fetcher.fetchAndProcessAll(pages, r -> {
                    if (r.error != null) {
                        System.err.println("Error: " + r.error.getMessage());
                    } else {
                        System.out.println("Fetched: " + r.page.getUrl());
                    }
                });
                fetcher.shutdown();
            }
        }
    }
}

