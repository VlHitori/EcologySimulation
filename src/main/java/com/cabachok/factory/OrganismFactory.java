package com.cabachok.factory;

import com.cabachok.entity.Carnivore;
import com.cabachok.entity.Herbivore;
import com.cabachok.entity.Organism;
import com.cabachok.entity.Plant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganismFactory {
    private String name;
    private int population;
    private int foodRequirements;
    private int waterRequirements;
    private double optimalHumidity;
    private double optimalTemperature;
    private String type;

    public Organism createOrganism() {
        return switch (type.toLowerCase()) {
            case "carnivore" -> new Carnivore(name, population, foodRequirements,
                    waterRequirements, optimalHumidity, optimalTemperature);

            case "herbivore" -> new Herbivore(name, population, foodRequirements,
                    waterRequirements, optimalHumidity, optimalTemperature);

            case "plant" -> new Plant(name, population, foodRequirements,
                    waterRequirements, optimalHumidity, optimalTemperature);

            default -> throw new IllegalArgumentException("Unknown organism type: " + type);
        };
    }
}
