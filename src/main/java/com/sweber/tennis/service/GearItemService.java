package com.sweber.tennis.service;

import com.sweber.tennis.config.CsvProperties;
import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.gear.GearItem;
import com.sweber.tennis.model.gear.GearType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GearItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GearItemService.class);

    private final String gearCsv;
    private final String ownedGearCsv;

    private List<GearItem> gearItems;
    // Cache owned levels for O(1) lookup. Keyed by "<GEAR_TYPE>:<GENERIC_NAME>" to avoid collisions.
    private final Map<String, Integer> ownedLevelMap = new HashMap<>();

    // Constructor used by Spring: accept CsvProperties
    @Autowired
    public GearItemService(CsvProperties csvProperties) {
        this.gearCsv = csvProperties.getGear();
        this.ownedGearCsv = csvProperties.getOwnedGear();
        try {
            init();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load gear data", e);
        }
    }


    private void init() throws IOException {
        gearItems = loadGearItems();
        long distinctGearItems = gearItems.stream()
                .map(GearItem::getName)
                .map(s -> s.substring(0, s.lastIndexOf("_")))
                .distinct()
                .count();
        LOGGER.info("Loaded {} configurations for {} different gear items", gearItems.size(), distinctGearItems);
        List<GearItem> ownedGearItems = loadOwnedGearItems();
        LOGGER.info("Loaded {} owned gear items", ownedGearItems.size());
    }

    private Resource resolveResource(String path) {
        if (path == null) throw new IllegalArgumentException("Resource path must not be null");
        // If path explicitly references classpath, use ClassPathResource
        if (path.startsWith("classpath:")) {
            return new ClassPathResource(path.substring("classpath:".length()));
        }
        // If there's a leading ./ or / or it exists on file system, use FileSystemResource
        java.nio.file.Path fs = java.nio.file.Paths.get(path);
        if (fs.isAbsolute() || fs.toFile().exists()) {
            return new FileSystemResource(path);
        }
        // Fallback to ClassPathResource for relative paths
        return new ClassPathResource(path);
    }

    private List<GearItem> loadGearItems() throws IOException {
        List<GearItem> gearItemsData = new ArrayList<>();
        Resource resource = resolveResource(gearCsv);
        try (InputStream is = resource.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            br.readLine(); // ignore header
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
        Resource resource = resolveResource(ownedGearCsv);
        try (InputStream is = resource.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while (line != null) {
                String[] inputData = line.split(",");
                int level = Integer.parseInt(inputData[1].trim());
                if (level != 0) {
                    String gearItemName = inputData[0].trim() + "_" + level;
                    GearItem gearItem = getGearItem(gearItemName);
                    ownedGearItemsData.add(gearItem);
                    // populate cache keyed by gear type and generic name
                    String generic = getGearItemGenericName(gearItem.getName());
                    String key = gearItem.getGearType().name() + ":" + generic;
                    ownedLevelMap.merge(key, gearItem.getLevel(), Math::max);
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
                .orElseThrow(() -> new NoSuchElementException(gearItemName));
    }

    private GearItem getGearItem(String[] inputData) {
        String gearItemName = inputData[0].trim();
        GearType gearItemType = GearType.valueOf(inputData[1].trim());
        try {
            Attributes gearItemAttributes = new Attributes(Integer.parseInt(inputData[2].trim()), Integer.parseInt(inputData[3].trim()), Integer.parseInt(inputData[4].trim()), Integer.parseInt(inputData[5].trim()), Integer.parseInt(inputData[6].trim()), Integer.parseInt(inputData[7].trim()));
            int cost = Integer.parseInt(inputData[8].trim());
            int level = Integer.parseInt(inputData[9].trim());
            Config gearItemConfig = new Config(gearItemAttributes, cost, level);
            return new GearItem(gearItemName, gearItemType, gearItemConfig);
        } catch (NumberFormatException e) {
            String message = String.format("Error parsing GearItem %s of type %s - %s", gearItemName, gearItemType, String.join(",", inputData));
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }
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
        // use cached map for O(1) lookup
        String configGearName = getGearItemGenericName(gearItem.getName());
        String key = gearItem.getGearType().name() + ":" + configGearName;
        return ownedLevelMap.getOrDefault(key, 0);
    }

    private String getGearItemGenericName(String name) {
        int endIndex = name.lastIndexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}
