package com.cabachok.service;

import com.cabachok.config.Configuration;
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
                ecosystem, Configuration.RESOURCE_AVAILABILITY_FACTOR, Configuration.FOOD_INFLUENCE_FACTOR
        );
        Map<String, Integer> waterConsumption = waterManager.measureWaterImpact(
                ecosystem, Configuration.RESOURCE_AVAILABILITY_FACTOR, Configuration.WATER_INFLUENCE_FACTOR
        );
        consumedResources.putAll(waterConsumption);

        conditionManager.measureConditionImpact(ecosystem, Configuration.TEMPERATURE_INFLUENCE_FACTOR, Configuration.HUMIDITY_INFLUENCE_FACTOR);

        int consumedSoil = consumedResources.getOrDefault("soil", 0);

        naturalEvents.applyNaturalEvents(ecosystem, Configuration.RAIN_FREQUENCY, Configuration.ORGANIC_MATTER_TRANSFER_RATE, consumedSoil);

        populationManager.updateEcosystem(ecosystem, consumedResources);
    }
}

