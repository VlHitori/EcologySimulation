package com.cabachok.service.simulationLogic;

import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Organism;
import com.cabachok.utils.GrowthFactoryCalculator;

import java.util.List;

public class ConditionManager {
    public void measureConditionImpact(Ecosystem ecosystem, double temperatureInfluenceFactor, double humidityInfluenceFactor) {
        EnvironmentCondition condition = ecosystem.getEnvironmentCondition();
        double actualHumidity = condition.getHumidity();
        double actualTemperature = condition.getTemperature();

        List<Organism> organisms = ecosystem.getOrganisms();

        for (Organism organism : organisms) {
            double optimalHumidity = organism.getOptimalHumidity();
            double optimalTemperature = organism.getOptimalTemperature();

            double humidityDifference = Math.abs(actualHumidity - optimalHumidity);
            double temperatureDifference = Math.abs(actualTemperature - optimalTemperature);

            double humidityImpact = GrowthFactoryCalculator.calculateHumidityImpact(humidityDifference, humidityInfluenceFactor);
            double temperatureImpact = GrowthFactoryCalculator.calculateTemperatureImpact(temperatureDifference, temperatureInfluenceFactor);

            organism.setGrowthDecayFactor(organism.getGrowthDecayFactor() * humidityImpact * temperatureImpact);
        }
    }
}
