package com.cabachok.entity;

import com.cabachok.config.AppConfig;

public class Plant extends Organism {
    public Plant(String name, int population, int foodRequirements, int waterRequirements,
                 double optimalHumidity, double optimalTemperature) {

        super(name, population, foodRequirements, waterRequirements,
                optimalHumidity, optimalTemperature, AppConfig.START_GROWTH_DECAY_FACTOR);
    }

    @Override
    public String getType() {
        return AppConfig.PLANT;
    }
}
