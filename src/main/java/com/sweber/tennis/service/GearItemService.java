package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GearItemService {
    private final List<GearItem> GEAR_ITEMS;
    private final List<GearItem> OWNED_GEAR_ITEMS;

    public GearItemService() {
        GEAR_ITEMS = loadData();
        OWNED_GEAR_ITEMS = loadOwnedData();
    }

    private List<GearItem> loadData() {
        List<GearItem> gearItems = new ArrayList<>();
        String pathToFile = "c:/dev/workspace/perso/TennisApp/src/main/resources/data/gears.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                GearItem book = getGearItem(attributes);
                gearItems.add(book);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return gearItems;
    }

    private List<GearItem> loadOwnedData() {
        List<GearItem> gearItems = new ArrayList<>();
        String pathToFile = "c:/dev/workspace/perso/TennisApp/src/main/resources/data/owned_gear.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line = br.readLine();
            while (line != null) {
                String gearItem = line.trim();
                GearItem item = getGearItem(gearItem);
                gearItems.add(item);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return gearItems;
    }

    private GearItem getGearItem(String gearItem) {
        return GEAR_ITEMS.stream()
                .filter(item -> item.getName().equals(gearItem))
                .findFirst()
                .orElseThrow();
    }

    private GearItem getGearItem(String[] attributes) {
        Config config = new Config(new Attributes(Integer.parseInt(attributes[2].trim()), Integer.parseInt(attributes[3].trim()), Integer.parseInt(attributes[4].trim()), Integer.parseInt(attributes[5].trim()), Integer.parseInt(attributes[6].trim()), Integer.parseInt(attributes[7].trim())), Integer.parseInt(attributes[8].trim()), Integer.parseInt(attributes[9].trim()));
        return new GearItem(attributes[0].trim(), GearType.valueOf(attributes[1].trim()), config);
    }

    public List<GearItem> leveledGearItems(int maxLevel, int upgradesAllowed) {
        return GEAR_ITEMS.stream()
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
        return OWNED_GEAR_ITEMS.stream()
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
