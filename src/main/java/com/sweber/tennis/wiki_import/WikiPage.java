package com.sweber.tennis.wiki_import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPage {
    private String itemName;
    private String itemType;
    private List<String> levels = new ArrayList<>();
    private List<String> prices = new ArrayList<>();
    private Map<String, List<String>> skills = new HashMap<>();
    private List<String> output = new ArrayList<>();

    public WikiPage(String itemName, String itemType) {
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

    public List<String> getOutput() {
        generateOutput();
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
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
}
