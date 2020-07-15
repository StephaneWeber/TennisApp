package com.sweber.tennis.web.model;

import com.sweber.tennis.model.config.Attributes;

public class ConfigFilter {
    private String selectedPlayer = null;
    private int minTotal = 100;
    private int upgradeAllowed = 0;
    private Attributes minAttributes;
    private int maxLevel;

    public String getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
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

    public Attributes getMinAttributes() {
        return minAttributes;
    }

    public void setMinAttributes(Attributes minAttributes) {
        this.minAttributes = minAttributes;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}
