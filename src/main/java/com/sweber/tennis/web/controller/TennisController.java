package com.sweber.tennis.web.controller;

import com.sweber.tennis.model.Config;
import com.sweber.tennis.model.FullConfig;
import com.sweber.tennis.model.Player;
import com.sweber.tennis.service.ConfigGenerator;
import com.sweber.tennis.web.model.ConfigFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        model.addAttribute("appName", appName);
        model.addAttribute("list", new ArrayList<>());
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", new ConfigFilter());
    }

    @PostMapping("/resetFilters")
    public String resetFilters(Model model) {
        initModel(model);
        return "home";
    }

    @PostMapping("/")
    public String postVerification(ConfigFilter configFilter, Model model, HttpServletRequest httpServletRequest) {
        List<FullConfig> fullConfigs;
        ConfigGenerator configGenerator = new ConfigGenerator();
        String player = configFilter.getPlayer();
        int minTotal = configFilter.getMinTotal();
        int upgradeAllowed = configFilter.getUpgradeAllowed();
        Config minConfig = createMinConfig(configFilter);
        if (player != null && !player.isEmpty()) {
            fullConfigs = configGenerator.generateAllConfigs(Player.valueOf(player), minConfig, minTotal, upgradeAllowed);
        } else {
            fullConfigs = configGenerator.generateAllConfigs(null, minConfig, minTotal, upgradeAllowed);
        }

        model.addAttribute("appName", appName);
        model.addAttribute("list", fullConfigs);
        model.addAttribute("playerList", Player.values());
        model.addAttribute("configFilter", configFilter);
        return HOME_PAGE;
    }

    private Config createMinConfig(ConfigFilter configFilter) {
        return new Config(configFilter.getMinAgility(),
                configFilter.getMinEndurance(),
                configFilter.getMinService(),
                configFilter.getMinVolley(),
                configFilter.getMinForehand(),
                configFilter.getMinBackhand(),
                0);
    }
}