package com.sweber.tennis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.defaults")
public class ConfigDefaultsProperties {
    private int minAgility = 90;
    private int minEndurance = 50;
    private int minService = 40;
    private int minForehand = 80;
    private int minBackhand = 80;
    private int minTotal = 350;
    private int upgradeAllowed = 0;
    private int maxLevel = 15;
    private int pageSize = 100;

    public int getMinAgility() {
        return minAgility;
    }

    public void setMinAgility(int minAgility) {
        this.minAgility = minAgility;
    }

    public int getMinEndurance() {
        return minEndurance;
    }

    public void setMinEndurance(int minEndurance) {
        this.minEndurance = minEndurance;
    }

    public int getMinService() {
        return minService;
    }

    public void setMinService(int minService) {
        this.minService = minService;
    }

    public int getMinForehand() {
        return minForehand;
    }

    public void setMinForehand(int minForehand) {
        this.minForehand = minForehand;
    }

    public int getMinBackhand() {
        return minBackhand;
    }

    public void setMinBackhand(int minBackhand) {
        this.minBackhand = minBackhand;
    }

    public int getMinTotal() {
        return minTotal;
    }

    public void setMinTotal(int minTotal) {
        this.minTotal = minTotal;
    }

    public int getUpgradeAllowed() {
        return upgradeAllowed;
    }

    public void setUpgradeAllowed(int upgradeAllowed) {
        this.upgradeAllowed = upgradeAllowed;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
