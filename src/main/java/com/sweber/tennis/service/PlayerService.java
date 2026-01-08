package com.sweber.tennis.service;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.Config;
import com.sweber.tennis.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);
    private static final String PLAYERS_CSV = "data/players.csv";
    private static final String OWNED_PLAYERS_CSV = "data/owned_players.csv";

    private final List<Player> players;
    private final List<Player> ownedPlayers;
    // Cache owned player levels keyed by generic player name
    private final Map<String, Integer> ownedLevelMap = new HashMap<>();

    public PlayerService() throws IOException {
        players = loadPlayers();
        long distinctPlayers = players.stream()
                .map(Player::getName)
                .map(s -> s.substring(0, s.lastIndexOf("_")))
                .distinct()
                .count();
        LOGGER.info("Loaded {} configurations for {} different players", players.size(), distinctPlayers);
        ownedPlayers = loadOwnedPlayers();
        LOGGER.info("Loaded {} owned players", ownedPlayers.size());
    }

    private List<Player> loadPlayers() throws IOException {
        List<Player> playersData = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(PLAYERS_CSV);
        try (InputStream is = resource.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            br.readLine(); // ignore header
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

    private List<Player> loadOwnedPlayers() throws IOException {
        List<Player> ownedPlayersData = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(OWNED_PLAYERS_CSV);
        try (InputStream is = resource.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while (line != null) {
                String[] inputData = line.split(",");
                int level = Integer.parseInt(inputData[1].trim());
                if (level != 0) {
                    String playerName = inputData[0].trim() + "_" + level;
                    Player player = getPlayer(playerName);
                    ownedPlayersData.add(player);
                    String generic = getPlayerGenericName(player.getName());
                    ownedLevelMap.merge(generic, player.getLevel(), Math::max);
                }
                line = br.readLine();
            }
        }
        return ownedPlayersData;
    }

    private Player getPlayer(String[] inputData) {
        String playerName = inputData[0].trim();
        try {
            Attributes attributes = new Attributes(Integer.parseInt(inputData[1].trim()), Integer.parseInt(inputData[2].trim()), Integer.parseInt(inputData[3].trim()), Integer.parseInt(inputData[4].trim()), Integer.parseInt(inputData[5].trim()), Integer.parseInt(inputData[6].trim()));
            int cost = Integer.parseInt(inputData[7].trim());
            int level = Integer.parseInt(inputData[8].trim());
            Config config = new Config(attributes, cost, level);
            return new Player(playerName, config);
        } catch (NumberFormatException e) {
            String message = String.format("Error parsing Player %s - %s", playerName, String.join(",", inputData));
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(item -> item.getName().equals(playerName))
                .findFirst()
                .orElse(null);
    }

    public List<Player> leveledPlayers(int maxLevel) {
        return players.stream()
                .filter(this::isOwned)
                .filter(item -> item.getLevel() <= maxLevel)
                .filter(item -> item.getLevel() >= Math.min(maxLevel, ownedLevel(item)))
                .collect(Collectors.toList());
    }

    public int ownedLevel(Player player) {
        String playerName = getPlayerGenericName(player.getName());
        return ownedLevelMap.getOrDefault(playerName, 0);
    }

    private boolean isOwned(Player player) {
        return player.getLevel() <= ownedLevel(player);
    }

    private String getPlayerGenericName(String name) {
        int endIndex = name.indexOf('_');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        return name;
    }
}
