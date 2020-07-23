package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GearItemService {
    private final List<GearItem> gearItems;
    private final List<GearItem> ownedGearItems;

    public GearItemService() throws IOException {
        gearItems = loadData();
        ownedGearItems = loadOwnedData();
    }

    private List<GearItem> loadData() throws IOException {
        List<GearItem> gearItemsData = new ArrayList<>();
        File dataFile = new ClassPathResource("data/gear.csv").getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                GearItem book = getGearItem(attributes);
                gearItemsData.add(book);
                line = br.readLine();
            }
        }

        return gearItemsData;
    }

    private List<GearItem> loadOwnedData() throws IOException {
        List<GearItem> ownedGearItemsData = new ArrayList<>();
        File dataFile = new ClassPathResource("data/owned_gear.csv").getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String gearItem = line.trim();
                GearItem item = getGearItem(gearItem);
                ownedGearItemsData.add(item);
                line = br.readLine();
            }
        }

        return ownedGearItemsData;
    }

    private GearItem getGearItem(String gearItem) {
        return gearItems.stream()
                .filter(item -> item.getName().equals(gearItem))
                .findFirst()
                .orElseThrow();
    }

    private GearItem getGearItem(String[] inputData) {
        String gearName = inputData[0].trim();
        GearType gearType = GearType.valueOf(inputData[1].trim());
        Attributes attributes = new Attributes(Integer.parseInt(inputData[2].trim()), Integer.parseInt(inputData[3].trim()), Integer.parseInt(inputData[4].trim()), Integer.parseInt(inputData[5].trim()), Integer.parseInt(inputData[6].trim()), Integer.parseInt(inputData[7].trim()));
        Config config = new Config(attributes, Integer.parseInt(inputData[8].trim()), Integer.parseInt(inputData[9].trim()));
        return new GearItem(gearName, gearType, config);
    }

    public List<GearItem> leveledGearItems(int maxLevel, int upgradesAllowed) {
        return gearItems.stream()
                .filter(item -> item.getLevel() <= maxLevel)
                .filter(item -> item.getLevel() >= Math.min(maxLevel, ownedLevel(item)))
                .filter(item -> upgradesAllowed == 0 ? isOwned(item) : isPossibleUpgrade(item))
                .collect(Collectors.toList());
    }

    public boolean isPossibleUpgrade(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) <= 1);
    }

    public boolean isNextLevel(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) == 1);
    }

    private int ownedLevel(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGripName = getGearItemGenericName(gearItem.getName());
        return ownedGearItems.stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(item -> item.getName().startsWith(configGripName))
                .findFirst()
                .map(GearItem::getLevel)
                .orElse(0);
    }

    private boolean isOwned(GearItem gearItem) {
        return (gearItem.getLevel() - ownedLevel(gearItem) <= 0);
    }

    private String getGearItemGenericName(String name) {
        int endIndex = name.indexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}
