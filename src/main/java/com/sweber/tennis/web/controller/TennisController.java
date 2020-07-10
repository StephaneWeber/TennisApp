package com.sweber.tennis.web.controller;

import com.sweber.tennis.config.Config;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.service.ConfigGenerator;
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

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        initModel(model);
        return "home";
    }

    private void initModel(Model model) {
        ConfigFilter configFilter = new ConfigFilter();
        configFilter.setMinAgility(20);
        configFilter.setMinService(20);
        configFilter.setMinForehand(20);
        configFilter.setMinBackhand(20);
        configFilter.setMinTotal(150);
        configFilter.setUpgradeAllowed(0);
        configFilter.setMaxLevel(6);

        List<FullConfig> fullConfigs = generateConfigs(configFilter);

        model.addAttribute("appName", appName);
        model.addAttribute("list", fullConfigs);
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", configFilter);
    }

    @PostMapping("/resetFilters")
    public String resetFilters(Model model) {
        initModel(model);
        return HOME_PAGE;
    }

    @PostMapping("/")
    public String postVerification(ConfigFilter configFilter, Model model) {
        List<FullConfig> fullConfigs = generateConfigs(configFilter);

        model.addAttribute("appName", appName);
        model.addAttribute("list", fullConfigs);
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", configFilter);
        return HOME_PAGE;
    }

    private List<FullConfig> generateConfigs(ConfigFilter configFilter) {
        List<FullConfig> fullConfigs;
        ConfigGenerator configGenerator = new ConfigGenerator();
        String player = configFilter.getPlayer();
        int minTotal = configFilter.getMinTotal();
        int upgradeAllowed = configFilter.getUpgradeAllowed();
        int maxLevel = configFilter.getMaxLevel();
        Config minConfig = createMinConfig(configFilter);
        if (player != null && !player.isEmpty()) {
            fullConfigs = configGenerator.generateAllConfigs(Player.valueOf(player), minConfig, minTotal, maxLevel, upgradeAllowed);
        } else {
            fullConfigs = configGenerator.generateAllConfigs(null, minConfig, minTotal, maxLevel, upgradeAllowed);
        }
        return fullConfigs;
    }

    private Config createMinConfig(ConfigFilter configFilter) {
        return new Config(configFilter.getMinAgility(),
                configFilter.getMinEndurance(),
                configFilter.getMinService(),
                configFilter.getMinVolley(),
                configFilter.getMinForehand(),
                configFilter.getMinBackhand(),
                0, 0);
    }
}