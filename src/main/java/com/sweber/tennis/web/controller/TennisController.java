package com.sweber.tennis.web.controller;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.service.ConfigGeneratorService;
import com.sweber.tennis.service.PlayerService;
import com.sweber.tennis.web.model.ConfigFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Controller
public class TennisController {
    private static final String HOME_PAGE = "home";
    private static final int PAGE_SIZE = 100;

    private final ConfigGeneratorService configGeneratorService;
    private final PlayerService playerService;

    private ConfigFilter configFilter;
    private Attributes maxAttributes;
    private List<GameConfig> gameConfigs = new ArrayList<>();

    @Value("${spring.application.name}")
    String appName;

    public TennisController(ConfigGeneratorService configGeneratorService, PlayerService playerService) {
        this.configGeneratorService = configGeneratorService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        configFilter = setupInitialConfigFilter();
        gameConfigs = configGeneratorService.generateConfigs(configFilter);
        maxAttributes = computeMaxAttributes(gameConfigs);
        initModel(model, Optional.empty());
        return HOME_PAGE;
    }

    @GetMapping("/config")
    public String getPage(Model model, @RequestParam("page") Optional<Integer> page) {
        initModel(model, page);
        return HOME_PAGE;
    }

    @PostMapping("/config")
    public String updateConfigFilter(ConfigFilter newConfigFilter, Model model) {
        this.configFilter = newConfigFilter;
        gameConfigs = configGeneratorService.generateConfigs(configFilter);
        maxAttributes = computeMaxAttributes(gameConfigs);
        initModel(model, Optional.empty());
        return HOME_PAGE;
    }

    private void initModel(Model model, Optional<Integer> page) {
        Page<GameConfig> configPage = getConfigPage(page);
        model.addAttribute("page", configPage);
        model.addAttribute("resultsCount", gameConfigs.size());
        model.addAttribute("appName", appName);
        model.addAttribute("playerList", playerService.leveledPlayers(configFilter.getMaxLevel()));
        model.addAttribute("configFilter", configFilter);
        model.addAttribute("maxAttributes", maxAttributes);
    }

    private Page<GameConfig> getConfigPage(Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, PAGE_SIZE);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<GameConfig> list;

        if (gameConfigs.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, gameConfigs.size());
            list = gameConfigs.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), gameConfigs.size());
    }

    private ConfigFilter setupInitialConfigFilter() {
        ConfigFilter configFilter = new ConfigFilter();
        Attributes minAttributes = new Attributes();
        minAttributes.setAgility(40);
        minAttributes.setEndurance(30);
        minAttributes.setService(40);
        minAttributes.setForehand(35);
        minAttributes.setBackhand(35);
        configFilter.setMinAttributes(minAttributes);
        configFilter.setMinTotal(260);
        configFilter.setUpgradeAllowed(0);
        configFilter.setMaxLevel(9);
        return configFilter;
    }

    private Attributes computeMaxAttributes(List<GameConfig> gameConfigs) {
        Attributes attributes = new Attributes();
        gameConfigs.forEach(gameConfig -> computeMaxAttributes(attributes, gameConfig));
        return attributes;
    }

    private void computeMaxAttributes(Attributes attributes, GameConfig gameConfig) {
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
}