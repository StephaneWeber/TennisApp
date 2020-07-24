package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(GearItemService.class);
    private static final String GEAR_CSV = "data/gear.csv";
    private static final String OWNED_GEAR_CSV = "data/owned_gear.csv";

    private final List<GearItem> gearItems;
    private final List<GearItem> ownedGearItems;

    public GearItemService() throws IOException {
        gearItems = loadGearItems();
        LOGGER.info("Loaded {} gear items", gearItems.size());
        ownedGearItems = loadOwnedGearItems();
        LOGGER.info("Loaded {} owned gear items", ownedGearItems.size());
    }

    private List<GearItem> loadGearItems() throws IOException {
        List<GearItem> gearItemsData = new ArrayList<>();
        File dataFile = new ClassPathResource(GEAR_CSV).getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] inputData = line.split(",");
                GearItem gearItem = getGearItem(inputData);
                gearItemsData.add(gearItem);
                line = br.readLine();
            }
        }
        return gearItemsData;
    }

    private List<GearItem> loadOwnedGearItems() throws IOException {
        List<GearItem> ownedGearItemsData = new ArrayList<>();
        File dataFile = new ClassPathResource(OWNED_GEAR_CSV).getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] inputData = line.split(",");
                int level = Integer.parseInt(inputData[1].trim());
                if (level != 0) {
                    String gearItemName = inputData[0].trim() + "_" + level;
                    GearItem gearItem = getGearItem(gearItemName);
                    ownedGearItemsData.add(gearItem);
                }
                line = br.readLine();
            }
        }
        return ownedGearItemsData;
    }

    private GearItem getGearItem(String gearItemName) {
        return gearItems.stream()
                .filter(item -> item.getName().equals(gearItemName))
                .findFirst()
                .orElseThrow();
    }

    private GearItem getGearItem(String[] inputData) {
        String gearItemName = inputData[0].trim();
        GearType gearItemType = GearType.valueOf(inputData[1].trim());
        Attributes gearItemAttributes = new Attributes(Integer.parseInt(inputData[2].trim()), Integer.parseInt(inputData[3].trim()), Integer.parseInt(inputData[4].trim()), Integer.parseInt(inputData[5].trim()), Integer.parseInt(inputData[6].trim()), Integer.parseInt(inputData[7].trim()));
        int cost = Integer.parseInt(inputData[8].trim());
        int level = Integer.parseInt(inputData[9].trim());
        Config gearItemConfig = new Config(gearItemAttributes, cost, level);
        return new GearItem(gearItemName, gearItemType, gearItemConfig);
    }

    public List<GearItem> leveledGearItems(int maxLevel, int upgradesAllowed) {
        return gearItems.stream()
                .filter(item -> matchingLevel(item, maxLevel, upgradesAllowed))
                .collect(Collectors.toList());
    }

    private boolean matchingLevel(GearItem item, int maxLevel, int upgradesAllowed) {
        return item.getLevel() >= Math.min(maxLevel, ownedLevel(item))
                && item.getLevel() <= ownedLevel(item) + (upgradesAllowed == 0 ? 0 : 1)
                && item.getLevel() <= maxLevel;
    }

    public int ownedLevel(GearItem gearItem) {
        GearType gearType = gearItem.getGearType();
        String configGearName = getGearItemGenericName(gearItem.getName());
        return ownedGearItems.stream()
                .filter(item -> item.getGearType() == gearType)
                .filter(item -> item.getName().startsWith(configGearName))
                .findFirst()
                .map(GearItem::getLevel)
                .orElse(0);
    }

    private String getGearItemGenericName(String name) {
        int endIndex = name.indexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}
