package com.sweber.tennis.service;

import com.sweber.tennis.config.CsvProperties;

import java.nio.file.Path;

public final class TestCsvPropertiesFactory {
    private TestCsvPropertiesFactory() {}

    public static CsvProperties fromPaths(Path players, Path ownedPlayers, Path gear, Path ownedGear) {
        CsvProperties p = new CsvProperties();
        p.setPlayers(players.toString());
        p.setOwnedPlayers(ownedPlayers.toString());
        p.setGear(gear.toString());
        p.setOwnedGear(ownedGear.toString());
        return p;
    }
}

