package com.sweber.tennis.wiki_import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPage {
    String itemName;
    String itemType;
    List<String> levels = new ArrayList<>();
    List<String> prices = new ArrayList<>();
    Map<String, List<String>> skills = new HashMap<>();
    List<String> output = new ArrayList<>();

    public WikiPage(String itemName, String itemType) {
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
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

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public void addOutputLine(String outputLine) {
        this.output.add(outputLine);
    }

    public void generateOutput() {
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
}
