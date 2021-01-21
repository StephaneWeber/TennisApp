package com.sweber.tennis.wiki_import;

import java.util.ArrayList;
import java.util.List;

public class WikiPage {
    List<String> levels = new ArrayList<>();
    List<String> price = new ArrayList<>();
    List<List<String>> skills = new ArrayList<>();

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<List<String>> getSkills() {
        return skills;
    }

    public void setSkills(List<List<String>> skills) {
        this.skills = skills;
    }

    public void addSkill(List<String> skill) {
        skills.add(skill);
    }
}
