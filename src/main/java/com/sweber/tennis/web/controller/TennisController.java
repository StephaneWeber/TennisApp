package com.sweber.tennis.web.controller;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.GameConfig;
import com.sweber.tennis.model.player.Player;
import com.sweber.tennis.service.ConfigGeneratorService;
import com.sweber.tennis.service.PlayerService;
import com.sweber.tennis.web.model.ConfigFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TennisController.class);
    private static final String HOME_PAGE = "home";
    private static final int PAGE_SIZE = 100;

    private final ConfigGeneratorService configGeneratorService;
    private final PlayerService playerService;

    private ConfigFilter configFilter;
    private Attributes maxAttributes;
    private List<GameConfig> gameConfigs = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();

    @Value("${spring.application.name}")
    String appName;

    public TennisController(ConfigGeneratorService configGeneratorService, PlayerService playerService) {
        this.configGeneratorService = configGeneratorService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        setupInitialConfigFilter();
        generateGameConfigs();
        populateModelWithPage(model, Optional.empty());
        return HOME_PAGE;
    }

    @GetMapping("/config")
    public String goToPage(Model model, @RequestParam("page") Optional<Integer> pageNumber) {
        populateModelWithPage(model, pageNumber);
        return HOME_PAGE;
    }

    @PostMapping("/config")
    public String applyConfigFilter(ConfigFilter newConfigFilter, Model model) {
        this.configFilter = newConfigFilter;
        generateGameConfigs();
        populateModelWithPage(model, Optional.empty());
        return HOME_PAGE;
    }

    private void generateGameConfigs() {
        long before = System.currentTimeMillis();
        this.gameConfigs = configGeneratorService.generateGameConfigs(configFilter);
        long after = System.currentTimeMillis();
        LOGGER.info("Generating the {} possible gameConfigs took {} ms", gameConfigs.size(), after-before);
        this.playerList = playerService.leveledPlayers(configFilter.getMaxLevel());
        this.maxAttributes = computeMaxAttributes(gameConfigs);
    }

    private void populateModelWithPage(Model model, Optional<Integer> pageNumber) {
        Page<GameConfig> configPage = getConfigPage(pageNumber);
        model.addAttribute("page", configPage);
        model.addAttribute("resultsCount", gameConfigs.size());
        model.addAttribute("appName", appName);
        model.addAttribute("playerList", playerList);
        model.addAttribute("configFilter", configFilter);
        model.addAttribute("maxAttributes", maxAttributes);
    }

    private Page<GameConfig> getConfigPage(Optional<Integer> pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber.orElse(1) - 1, PAGE_SIZE);
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

    private void setupInitialConfigFilter() {
        configFilter = new ConfigFilter();
        Attributes minAttributes = new Attributes();
        minAttributes.setAgility(70);
        minAttributes.setEndurance(40);
        minAttributes.setService(25);
        minAttributes.setForehand(70);
        minAttributes.setBackhand(70);
        configFilter.setMinAttributes(minAttributes);
        configFilter.setMinTotal(300);
        configFilter.setUpgradeAllowed(0);
        configFilter.setMaxLevel(12);
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