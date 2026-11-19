package com.sweber.tennis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.csv")
public class CsvProperties {
    private String players;
    private String gear;
    private String ownedPlayers;
    private String ownedGear;

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getOwnedPlayers() {
        return ownedPlayers;
    }

    public void setOwnedPlayers(String ownedPlayers) {
        this.ownedPlayers = ownedPlayers;
    }

    public String getOwnedGear() {
        return ownedGear;
    }

    public void setOwnedGear(String ownedGear) {
        this.ownedGear = ownedGear;
    }
}
