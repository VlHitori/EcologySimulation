package com.cabachok.service;

import com.cabachok.config.AppConfig;
import com.cabachok.entity.Ecosystem;
import com.cabachok.service.simulationLogic.*;

import java.util.Map;

public class Simulation {
    private final FoodManager foodManager;
    private final WaterManager waterManager;
    private final ConditionManager conditionManager;
    private final NaturalEvents naturalEvents;
    private final PopulationManager populationManager;

    public Simulation() {
        foodManager = new FoodManager();
        waterManager = new WaterManager();
        conditionManager = new ConditionManager();
        naturalEvents = new NaturalEvents();
        populationManager = new PopulationManager();
    }

    public void makeStep(Ecosystem ecosystem) {

        Map<String, Integer> consumedResources = foodManager.measureFoodImpact(
                ecosystem, AppConfig.RESOURCE_AVAILABILITY_FACTOR, AppConfig.FOOD_INFLUENCE_FACTOR
        );
        Map<String, Integer> waterConsumption = waterManager.measureWaterImpact(
                ecosystem, AppConfig.RESOURCE_AVAILABILITY_FACTOR, AppConfig.WATER_INFLUENCE_FACTOR
        );
        consumedResources.putAll(waterConsumption);

        conditionManager.measureConditionImpact(ecosystem, AppConfig.TEMPERATURE_INFLUENCE_FACTOR, AppConfig.HUMIDITY_INFLUENCE_FACTOR);

        int consumedSoil = consumedResources.getOrDefault("soil", 0);

        naturalEvents.applyNaturalEvents(ecosystem, AppConfig.RAIN_FREQUENCY, AppConfig.ORGANIC_MATTER_TRANSFER_RATE, consumedSoil);

        populationManager.updateEcosystem(ecosystem, consumedResources);
    }
}

