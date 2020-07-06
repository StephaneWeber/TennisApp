package com.sweber.tennis.web.model;

public class ConfigFilter {
    private String player = "ALL";
    private int minTotal = 100;
    private int upgradeAllowed = 1;
    private int minAgility;
    private int minEndurance;
    private int minService;
    private int minVolley;
    private int minForehand;
    private int minBackhand;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
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

    public int getMinVolley() {
        return minVolley;
    }

    public void setMinVolley(int minVolley) {
        this.minVolley = minVolley;
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
}
