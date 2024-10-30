package com.cabachok.entity;

import com.cabachok.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public abstract class Organism {
    private String name;
    private int population;
    private int foodRequirements;
    private int waterRequirements;
    private double optimalHumidity;
    private double optimalTemperature;
    private double growthDecayFactor;

    public abstract String getType();

    public void updatePopulation() {
        if (growthDecayFactor > 1.4) {
            growthDecayFactor = 1.4;
        }
        population = (int) (population * growthDecayFactor);
        growthDecayFactor = AppConfig.START_GROWTH_DECAY_FACTOR;
    }

    public void decreasePopulation(int deadCount) {
        if (population > deadCount) {
            population -= deadCount;
        } else {
            System.err.println("The population is not sufficient to remove " + deadCount + " objects. Setting the population to 0.");
            population = 0;
        }
    }

    public int getTotalFoodRequirement() {
        return population * foodRequirements;
    }

    public int getTotalWaterRequirement() {
        return population * waterRequirements;
    }

    @Override
    public String toString() {
        return "\n\tOrganism: " + name + " '" + getType() + "'" +
                "\nFoodRequirements: " + foodRequirements +
                "\nPopulation: " + population +
                "\nWaterRequirements: " + waterRequirements +
                "\nOptimalHumidity: " + optimalHumidity +
                "\nOptimalTemperature: " + optimalTemperature;
    }
}
