package com.sweber.tennis.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadFromWiki {
    public static void main(String... args) throws IOException {
        String page = "http://tennis-clash.fandom.com/wiki/The_Patriot";
        WikiPage wikiPage = new WikiPage();
        Document doc = Jsoup.connect(page).get();
        Elements pagination = doc.select(".article-table");
        Element price = pagination.get(0);


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
        levels.add("Level");
        int level = firstLevel;
        for (int i2 = 0; i2 <= lastLevel - firstLevel; i2++) {
            levels.add(cardsCols.get(level).text().trim());
            level++;
        }

        wikiPage.setLevels(levels);
        for (int i1 = 1; i1 < skills.size(); i1++) {
            row = skills.get(i1);
            cols = row.select("td");
            String skillName = cols.get(0).text();
            List<String> skill = new ArrayList<>();
            skill.add(skillName);
            for (int i2 = firstLevel; i2 <= lastLevel; i2++) {
                skill.add(cols.get(i2).text().trim());
            }
            wikiPage.addSkill(skill);
        }

        System.out.println(wikiPage);
    }
}