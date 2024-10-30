package com.cabachok.entity;

import com.cabachok.config.Configuration;

public class Herbivore extends Organism {
    public Herbivore(String name, int population, int foodRequirements, int waterRequirements,
                     double optimalHumidity, double optimalTemperature) {

        super(name, population, foodRequirements, waterRequirements,
                optimalHumidity, optimalTemperature, Configuration.START_GROWTH_DECAY_FACTOR);
    }

    @Override
    public String getType() {
        return Configuration.HERBIVORE;
    }
}
