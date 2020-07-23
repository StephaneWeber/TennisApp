package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.player.Player;
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
public class PlayerService {
    private final List<Player> players;
    private final List<Player> ownedPlayers;

    public PlayerService() throws IOException {
        players = loadData();
        ownedPlayers = loadOwnedData();
    }

    private List<Player> loadData() throws IOException {
        List<Player> playersData = new ArrayList<>();
        File dataFile = new ClassPathResource("data/players.csv").getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] inputData = line.split(",");
                Player player = getPlayer(inputData);
                playersData.add(player);
                line = br.readLine();
            }
        }

        return playersData;
    }

    private List<Player> loadOwnedData() throws IOException {
        List<Player> ownedPlayersData = new ArrayList<>();
        File dataFile = new ClassPathResource("data/owned_players.csv").getFile();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line = br.readLine();
            while (line != null) {
                String playerName = line.trim();
                Player player = getPlayer(playerName);
                ownedPlayersData.add(player);
                line = br.readLine();
            }
        }

        return ownedPlayersData;
    }

    private Player getPlayer(String[] inputData) {
        String playerName = inputData[0].trim();
        Attributes attributes = new Attributes(Integer.parseInt(inputData[1].trim()), Integer.parseInt(inputData[2].trim()), Integer.parseInt(inputData[3].trim()), Integer.parseInt(inputData[4].trim()), Integer.parseInt(inputData[5].trim()), Integer.parseInt(inputData[6].trim()));
        Config config = new Config(attributes, Integer.parseInt(inputData[7].trim()), Integer.parseInt(inputData[8].trim()));
        return new Player(playerName, config);
    }

    public List<Player> maxLevel(int maxLevel) {
        return ownedPlayers.stream()
                .filter(item -> item.getLevel() <= maxLevel)
                .collect(Collectors.toList());
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(item -> item.getName().equals(playerName))
                .findFirst()
                .orElse(null);
    }
}
