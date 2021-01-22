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

    private String page;
    private String itemName;
    private String itemType;
    private List<String> levels = new ArrayList<>();
    private List<String> prices = new ArrayList<>();
    private Map<String, List<String>> skills = new HashMap<>();
    private List<String> output = new ArrayList<>();
    private Limits limits;

    public WikiPage(String page, String itemName, String itemType) {
        this.page = PAGE + page;
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    public Map<String, List<String>> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, List<String>> skills) {
        this.skills = skills;
    }

    public void addSkill(String skillName, List<String> skill) {
        skills.put(skillName, skill);
    }

    private void generateOutput() {
        for (int i = 0; i < levels.size(); i++) {
            String outputLine = itemName + "_" + levels.get(i) + "," + itemType + ",";
            if (skills.containsKey("AGILITY")) {
                outputLine = outputLine + skills.get("AGILITY").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("ENDURANCE")) {
                outputLine = outputLine + skills.get("ENDURANCE").get(i) + ",";
            } else {
                outputLine = outputLine + "0,";
            }
            if (skills.containsKey("SERVICE")) {
                outputLine = outputLine + skills.get("SERVICE").get(i) + ",";
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

    public void processWikiPage() throws IOException {
        Document doc = Jsoup.connect(page).get();
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

        determinePrices(articleTables);

        generateOutput();
        for (String outputLine : output) {
            System.out.println(outputLine);
        }
    }

    private void determinePrices(Elements articleTables) {
        Element pricesRow = articleTables.get(0).select("tr").get(2);
        Elements prices = pricesRow.select("td");
        List<String> price = new ArrayList<>();
        int level = limits.getFirstLevel();
        for (int i2 = 1; i2 <= limits.getLastLevel() - limits.getFirstLevel() + 1; i2++) {
            String indPrice = prices.get(level++).text().trim();
            if (indPrice.isEmpty()) {
                indPrice = "0";
            } else {
                indPrice = formatPrice(indPrice);
            }
            price.add(indPrice);
        }
        this.prices = price;
    }

    private void determineSkills(Element skillRow) {
        Elements cols = skillRow.select("td");
        String skillName = cols.get(0).text().toUpperCase();
        List<String> skill = new ArrayList<>();
        for (int i2 = limits.getFirstLevel(); i2 <= limits.getLastLevel(); i2++) {
            skill.add(cols.get(i2).text().trim());
        }
        addSkill(skillName, skill);
    }

    private void determineLevels(Elements levelsCols) {
        List<String> levels = new ArrayList<>();
        int level = limits.getFirstLevel();
        for (int i2 = 0; i2 <= limits.getLastLevel() - limits.getFirstLevel(); i2++) {
            levels.add(levelsCols.get(level).text().trim());
            level++;
        }
        this.levels = levels;
    }

    private void determineLimits(Elements skillsRows) {
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
        this.limits = limits;
    }

    private String formatPrice(String indPrice) {
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

    class Limits {
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
