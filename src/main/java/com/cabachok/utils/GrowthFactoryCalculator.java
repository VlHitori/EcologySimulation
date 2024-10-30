package com.cabachok.utils;

import com.cabachok.entity.Organism;

import java.util.List;
import java.util.function.ToIntFunction;

public final class GrowthFactoryCalculator {
    public static double resourceImpact(double deficitRatio, double foodInfluenceFactor) {
        double newFactor = 1.53 / (1 + 10 * Math.exp(-3 * deficitRatio));  // y = 0.1 * 15^x
        newFactor = Math.min(newFactor, 1.5);

        return adjustImpact(newFactor, foodInfluenceFactor);
    }

    public static double calculateHumidityImpact(double humidityDifference, double influenceFactor) {
        double humidityImpact = 1.3 / (1 + 0.004 * Math.exp(0.15 * humidityDifference));  // y = 1.3/(1 + 0.004*(e^0.15X))

        return adjustImpact(humidityImpact, influenceFactor);
    }

    public static double calculateTemperatureImpact(double temperatureDifference, double influenceFactor) {
        double temperatureImpact = 1.4 / (1 + 0.06 * Math.exp(0.12 * temperatureDifference));

        return adjustImpact(temperatureImpact, influenceFactor);
    }

    public static int calculateTotalWaterRequired(List<Organism> organisms, ToIntFunction<Organism> mapper) {
        return organisms.stream()
                .mapToInt(mapper)
                .sum();
    }

    public static int calculateTotalByType(List<Organism> organisms, String targetType, ToIntFunction<Organism> mapper) {
        return organisms.stream()
                .filter(o -> o.getType().equals(targetType))
                .mapToInt(mapper)
                .sum();
    }

    private static double adjustImpact(double impact, double influenceFactor) {
        if (impact > 1) {
            return 1 + ((impact - 1) * influenceFactor);
        } else {
            return 1 - (1 - impact) * influenceFactor;
        }
    }
}