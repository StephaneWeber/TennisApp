package com.sweber.tennis.wiki_import;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadFromWiki {
    public static void main(String... args) throws IOException {
        String page = "http://tennis-clash.fandom.com/wiki/";
        handlePage(page + "The_Eagle", "EAGLE", "RACKET");
        handlePage(page + "The_Patriot", "PATRIOT", "RACKET");
        handlePage(page + "The_Outback", "OUTBACK", "RACKET");
        handlePage(page + "The_Panther", "PANTHER", "RACKET");
        handlePage(page + "The_Samurai", "SAMURAI", "RACKET");
        handlePage(page + "The_Hammer", "HAMMER", "RACKET");
        handlePage(page + "The_Bullseye", "BULLS_EYE", "RACKET");
        handlePage(page + "Zeus", "ZEUS", "RACKET");

    }

    private static void handlePage(String page, String itemName, String itemType) throws IOException {
        WikiPage wikiPage = new WikiPage(itemName, itemType);
        Document doc = Jsoup.connect(page).get();
        Elements pagination = doc.select(".article-table");

        Element skillsTable = pagination.get(1);
        Elements skills = skillsTable.select("tr");
        Element cardsRow = skills.get(0);
        Elements cardsCols = cardsRow.select("th");

        int firstLevel = 0;
        int lastLevel = 0;
        Element row = skills.get(1);
        Elements cols = row.select("td");
        for (int i1 = 0; i1 < cols.size(); i1++) {
            String trim = cols.get(i1).text().trim();
            if (i1 > 0 && firstLevel == 0 && !trim.isEmpty()) {
                firstLevel = i1;
            }
            if (!trim.isEmpty()) {
                lastLevel = i1;
            }
        }

        List<String> levels = new ArrayList<>();
        int level = firstLevel;
        for (int i2 = 0; i2 <= lastLevel - firstLevel; i2++) {
            levels.add(cardsCols.get(level).text().trim());
            level++;
        }

        wikiPage.setLevels(levels);
        for (int i1 = 1; i1 < skills.size(); i1++) {
            row = skills.get(i1);
            cols = row.select("td");
            String skillName = cols.get(0).text().toUpperCase();
            List<String> skill = new ArrayList<>();
            for (int i2 = firstLevel; i2 <= lastLevel; i2++) {
                skill.add(cols.get(i2).text().trim());
            }
            wikiPage.addSkill(skillName, skill);
        }

        Element priceTable = pagination.get(0);
        Elements pricesRow = priceTable.select("tr");
        Element prices = pricesRow.get(2);
        Elements upgradeRow = prices.select("td");
        List<String> price = new ArrayList<>();
        level = firstLevel;
        for (int i2 = 0; i2 <= lastLevel - firstLevel; i2++) {
            String indPrice = upgradeRow.get(level++).text().trim();
            if (indPrice.isEmpty()) {
                indPrice = "0";
            } else {
                indPrice = formatPrice(indPrice);
            }
            price.add(indPrice);
        }
        wikiPage.setPrices(price);

        wikiPage.generateOutput();

        List<String> output = wikiPage.getOutput();
        for (String s : output) {
            System.out.println(s);
        }
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
}