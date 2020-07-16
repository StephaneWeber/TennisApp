package com.sweber.tennis.web.controller;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.player.Player;
import com.sweber.tennis.service.ConfigGeneratorService;
import com.sweber.tennis.web.model.ConfigFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TennisController {
    public static final String HOME_PAGE = "home";

    private final ConfigGeneratorService configGeneratorService;

    @Value("${spring.application.name}")
    String appName;

    public TennisController(ConfigGeneratorService configGeneratorService) {
        this.configGeneratorService = configGeneratorService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        initModel(model);
        return "home";
    }

    private void initModel(Model model) {
        ConfigFilter configFilter = setupInitialConfigFilter();

        List<GameConfig> gameConfigs = generateConfigs(configFilter);

        model.addAttribute("appName", appName);
        model.addAttribute("list", gameConfigs);
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", configFilter);
        model.addAttribute("maxAttributes", new Attributes(50, 50, 50, 50, 50, 50));
    }

    private ConfigFilter setupInitialConfigFilter() {
        ConfigFilter configFilter = new ConfigFilter();
        Attributes minAttributes = new Attributes();
        minAttributes.setAgility(20);
        minAttributes.setService(30);
        minAttributes.setForehand(30);
        minAttributes.setBackhand(20);
        configFilter.setMinAttributes(minAttributes);
        configFilter.setMinTotal(150);
        configFilter.setUpgradeAllowed(0);
        configFilter.setMaxLevel(6);
        return configFilter;
    }

    @PostMapping("/resetFilters")
    public String resetFilters(Model model) {
        initModel(model);
        return HOME_PAGE;
    }

    @PostMapping("/")
    public String postVerification(ConfigFilter configFilter, Model model) {
        List<GameConfig> gameConfigs = generateConfigs(configFilter);
        Attributes maxAttributes = computeMaxAttributes(gameConfigs);

        model.addAttribute("appName", appName);
        model.addAttribute("list", gameConfigs);
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", configFilter);
        model.addAttribute("maxAttributes", maxAttributes);
        return HOME_PAGE;
    }

    private Attributes computeMaxAttributes(List<GameConfig> gameConfigs) {
        Attributes attributes = new Attributes();
        gameConfigs.forEach(gameConfig -> upgradeMaxAttributes(attributes, gameConfig));
        return attributes;
    }

    private void upgradeMaxAttributes(Attributes attributes, GameConfig gameConfig) {
        Attributes gameConfigAttributes = gameConfig.getAttributes();
        if (gameConfigAttributes.getAgility() >= attributes.getAgility()) {
            attributes.setAgility(gameConfigAttributes.getAgility());
        }
        if (gameConfigAttributes.getEndurance() >= attributes.getEndurance()) {
            attributes.setEndurance(gameConfigAttributes.getEndurance());
        }
        if (gameConfigAttributes.getService() >= attributes.getService()) {
            attributes.setService(gameConfigAttributes.getService());
        }
        if (gameConfigAttributes.getVolley() >= attributes.getVolley()) {
            attributes.setVolley(gameConfigAttributes.getVolley());
        }
        if (gameConfigAttributes.getForehand() >= attributes.getForehand()) {
            attributes.setForehand(gameConfigAttributes.getForehand());
        }
        if (gameConfigAttributes.getBackhand() >= attributes.getBackhand()) {
            attributes.setBackhand(gameConfigAttributes.getBackhand());
        }
    }

    private List<GameConfig> generateConfigs(ConfigFilter configFilter) {
        List<GameConfig> gameConfigs;
        String player = configFilter.getSelectedPlayer();
        int minTotal = configFilter.getMinTotal();
        int upgradeAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        if (player != null && !player.isEmpty()) {
            gameConfigs = configGeneratorService.generateAllConfigs(Player.valueOf(player), configFilter.getMinAttributes(), minTotal, maxLevel, upgradeAllowed);
        } else {
            gameConfigs = configGeneratorService.generateAllConfigs(null, configFilter.getMinAttributes(), minTotal, maxLevel, upgradeAllowed);
        }
        return gameConfigs;
    }
}