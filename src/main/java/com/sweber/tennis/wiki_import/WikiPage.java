package com.sweber.tennis.wiki_import;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPage {
    private static final String PAGE = "http://tennis-clash.fandom.com/wiki/";

    private final String pageSuffix;
    private final String itemName;
    private final String itemType;
    private final List<String> output = new ArrayList<>();
    private final Map<String, List<String>> skills = new HashMap<>();
    private final List<String> levels = new ArrayList<>();
    private final List<String> prices = new ArrayList<>();
    private Limits limits;

    public WikiPage(String pageSuffix, String itemName, String itemType) {
        this.pageSuffix = PAGE + pageSuffix;
        this.itemName = itemName;
        this.itemType = itemType;
    }

    private void generateOutput() {
        for (int i = 0; i < levels.size(); i++) {
            String outputLine = itemName + "_" + levels.get(i) + ",";
            if (itemType != null) {
                outputLine = outputLine + itemType + ",";
            }
            if (skills.containsKey("AGILITY")) {
                outputLine = outputLine + skills.get("AGILITY").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("STAMINA")) {
                outputLine = outputLine + skills.get("STAMINA").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("SERVE")) {
                outputLine = outputLine + skills.get("SERVE").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("VOLLEY")) {
                outputLine = outputLine + skills.get("VOLLEY").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("FOREHAND")) {
                outputLine = outputLine + skills.get("FOREHAND").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("BACKHAND")) {
                outputLine = outputLine + skills.get("BACKHAND").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }

            outputLine = outputLine + prices.get(i) + "," + levels.get(i);
            output.add(outputLine);
        }
    }

    public StringBuilder processWikiPage() throws IOException {
        Document doc = Jsoup.connect(pageSuffix).get();
        Elements articleTables = doc.select(".article-table");

        Element skillsTable = articleTables.get(1);
        Elements skillsRows = skillsTable.select("tr");
        Element levelsRow = skillsRows.get(0);
        Elements levelNames = levelsRow.select("th");

        determineLimits(skillsRows);

        determineLevels(levelNames);

        for (int i1 = 1; i1 < skillsRows.size(); i1++) {
            Element skillRow = skillsRows.get(i1);
            determineSkills(skillRow);
        }

        determinePrices(articleTables, pageSuffix);

        generateOutput();
        StringBuilder stringBuilder = new StringBuilder();
        for (String outputLine : output) {
            stringBuilder.append(outputLine).append("\n");
        }
        return stringBuilder;
    }

    private void determinePrices(Elements articleTables, String pageSuffix) {
        Element pricesRow = articleTables.get(0).select("tr").get(2);
        Elements pricesColumns = pricesRow.select("td");
        int level = limits.getFirstLevel();
        for (int i2 = 1; i2 <= limits.getLastLevel() - limits.getFirstLevel() + 1; i2++) {
            String indPrice = null;
            try {
                indPrice = pricesColumns.get(level++).text().trim();
            } catch (Exception e) {
                String message = String.format("Error determining price for %s", pageSuffix);
                throw new IllegalStateException(message);
            }
            if (indPrice.isEmpty() || indPrice.equals("/")) {
                indPrice = "0";
            } else {
                indPrice = formatPrice(indPrice);
            }
            prices.add(indPrice);
        }
    }

    private void determineSkills(Element skillRow) {
        Elements cols = skillRow.select("td");
        String skillName = cols.get(0).text().toUpperCase();
        List<String> skill = new ArrayList<>();
        for (int i2 = limits.getFirstLevel(); i2 <= limits.getLastLevel(); i2++) {
            skill.add(cols.get(i2).text().trim());
        }
        skills.put(skillName, skill);
    }

    private void determineLevels(Elements levelsCols) {
        int level = limits.getFirstLevel();
        for (int i2 = 0; i2 <= limits.getLastLevel() - limits.getFirstLevel(); i2++) {
            levels.add(levelsCols.get(level).text().trim());
            level++;
        }
    }

    private void determineLimits(Elements skillsRows) {
        Limits limitColumns = new Limits(0, 0);
        Element row = skillsRows.get(1);
        Elements cols = row.select("td");
        for (int i1 = 0; i1 < cols.size(); i1++) {
            String trim = cols.get(i1).text().trim();
            if (i1 > 0 && limitColumns.getFirstLevel() == 0 && !trim.isEmpty()) {
                limitColumns.setFirstLevel(i1);
            }
            if (!trim.isEmpty()) {
                limitColumns.setLastLevel(i1);
            }
        }
        this.limits = limitColumns;
    }

    private String formatPrice(String indPrice) {
        indPrice = indPrice.toUpperCase();
        if (indPrice.endsWith("K")) {
            if (indPrice.contains(".")) {
                indPrice = indPrice.replace("K", "00");
                indPrice = indPrice.replace(".", "");
            } else {
                indPrice = indPrice.replace("K", "000");
            }
            if (indPrice.startsWith("0")) {
                indPrice = indPrice.substring(1);
            }
        }
        if (indPrice.endsWith("M")) {
            if (indPrice.contains(".") && (indPrice.indexOf("M") == indPrice.indexOf(".") + 2)) {
                indPrice = indPrice.replace("M", "00000");
                indPrice = indPrice.replace(".", "");
            } else if (indPrice.contains(".")) {
                indPrice = indPrice.replace("M", "0000");
                indPrice = indPrice.replace(".", "");
            } else {
                indPrice = indPrice.replace("M", "000000");
            }
            if (indPrice.startsWith("0")) {
                indPrice = indPrice.substring(1);
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
