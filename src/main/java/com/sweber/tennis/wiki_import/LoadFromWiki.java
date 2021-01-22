package com.sweber.tennis.wiki_import;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadFromWiki {

    public static final String PAGE = "http://tennis-clash.fandom.com/wiki/";
    public static final String RACKET = "RACKET";
    public static final String GRIP = "GRIP";

    public static void main(String... args) throws IOException {
        handleRackets();
        handleGrips();
    }

    private static void handleRackets() throws IOException {
        handlePage(PAGE + "The_Eagle", "EAGLE", RACKET);
        handlePage(PAGE + "The_Patriot", "PATRIOT", RACKET);
        handlePage(PAGE + "The_Outback", "OUTBACK", RACKET);
        handlePage(PAGE + "The_Panther", "PANTHER", RACKET);
        handlePage(PAGE + "The_Samurai", "SAMURAI", RACKET);
        handlePage(PAGE + "The_Hammer", "HAMMER", RACKET);
        handlePage(PAGE + "The_Bullseye", "BULLS_EYE", RACKET);
        handlePage(PAGE + "Zeus", "ZEUS", RACKET);
    }

    private static void handleGrips() throws IOException {
        handlePage(PAGE + "The_Warrior", "WARRIOR", GRIP);
        handlePage(PAGE + "The_Talon", "TALON", GRIP);
        handlePage(PAGE + "The_Machete", "MACHETE", GRIP);
        handlePage(PAGE + "The_Cobra", "COBRA", GRIP);
        handlePage(PAGE + "The_Katana", "KATANA", GRIP);
        handlePage(PAGE + "The_Forge", "FORGE", GRIP);
        handlePage(PAGE + "Tactical_Grip", "TACTICAL_GRIP", GRIP);
        handlePage(PAGE + "The_Titan", "TITAN", GRIP);
    }

    private static void handlePage(String page, String itemName, String itemType) throws IOException {
        WikiPage wikiPage = new WikiPage(itemName, itemType);
        Document doc = Jsoup.connect(page).get();
        Elements articleTables = doc.select(".article-table");

        Element skillsTable = articleTables.get(1);
        Elements skillsRows = skillsTable.select("tr");
        Element levelsRow = skillsRows.get(0);
        Elements levelNames = levelsRow.select("th");

        Limits limits = determineLimits(skillsRows);

        List<String> levels = determineLevels(levelNames, limits);
        wikiPage.setLevels(levels);

        for (int i1 = 1; i1 < skillsRows.size(); i1++) {
            Element skillRow = skillsRows.get(i1);
            determineSkills(wikiPage, skillRow, limits);
        }

        List<String> price = determinePrices(articleTables, limits);
        wikiPage.setPrices(price);

        List<String> output = wikiPage.getOutput();
        for (String outputLine : output) {
            System.out.println(outputLine);
        }
    }

    private static List<String> determinePrices(Elements articleTables, Limits limits) {
        Element pricesRow = articleTables.get(0).select("tr").get(2);
        Elements prices = pricesRow.select("td");
        List<String> price = new ArrayList<>();
        int level = limits.getFirstLevel();
        for (int i2 = 0; i2 < limits.getLastLevel() - limits.getFirstLevel(); i2++) {
            String indPrice = prices.get(level++).text().trim();
            if (indPrice.isEmpty()) {
                indPrice = "0";
            } else {
                indPrice = formatPrice(indPrice);
            }
            price.add(indPrice);
        }
        return price;
    }

    private static void determineSkills(WikiPage wikiPage, Element skillRow, Limits limits) {
        Elements cols = skillRow.select("td");
        String skillName = cols.get(0).text().toUpperCase();
        List<String> skill = new ArrayList<>();
        for (int i2 = limits.getFirstLevel(); i2 <= limits.getLastLevel(); i2++) {
            skill.add(cols.get(i2).text().trim());
        }
        wikiPage.addSkill(skillName, skill);
    }

    private static List<String> determineLevels(Elements levelsCols, Limits limits) {
        List<String> levels = new ArrayList<>();
        int level = limits.getFirstLevel();
        for (int i2 = 0; i2 <= limits.getLastLevel() - limits.getFirstLevel(); i2++) {
            levels.add(levelsCols.get(level).text().trim());
            level++;
        }
        return levels;
    }

    private static Limits determineLimits(Elements skillsRows) {
        Limits limits = new Limits(0, 0);
        Element row = skillsRows.get(1);
        Elements cols = row.select("td");
        for (int i1 = 0; i1 < cols.size(); i1++) {
            String trim = cols.get(i1).text().trim();
            if (i1 > 0 && limits.getFirstLevel() == 0 && !trim.isEmpty()) {
                limits.setFirstLevel(i1);
            }
            if (!trim.isEmpty()) {
                limits.setLastLevel(i1);
            }
        }
        return limits;
    }

    private static String formatPrice(String indPrice) {
        indPrice = indPrice.toUpperCase();
        if (indPrice.endsWith("K")) {
            if (indPrice.contains(".")) {
                indPrice = indPrice.replaceAll("K", "00");
                indPrice = indPrice.replaceAll("\\.", "");
            } else {
                indPrice = indPrice.replaceAll("K", "000");
            }
        }
        return indPrice;
    }

    static class Limits {
        int firstLevel;
        int lastLevel;

        public Limits(int firstLevel, int lastLevel) {
            this.firstLevel = firstLevel;
            this.lastLevel = lastLevel;
        }

        public int getFirstLevel() {
            return firstLevel;
        }

        public void setFirstLevel(int firstLevel) {
            this.firstLevel = firstLevel;
        }

        public int getLastLevel() {
            return lastLevel;
        }

        public void setLastLevel(int lastLevel) {
            this.lastLevel = lastLevel;
        }
    }
}