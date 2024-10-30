package com.cabachok.service.simulationLogic;

import com.cabachok.config.AppConfig;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Organism;
import com.cabachok.utils.GrowthFactoryCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterManager {
    public Map<String, Integer> measureWaterImpact(Ecosystem ecosystem, double resourceAvailabilityFactor, double waterInfluenceFactor) {
        List<Organism> organisms = ecosystem.getOrganisms();
        EnvironmentCondition condition = ecosystem.getEnvironmentCondition();

        int availableWater = condition.getWaterAvailability();
        int totalWaterRequired = GrowthFactoryCalculator
                .calculateTotalWaterRequired(organisms, Organism::getTotalWaterRequirement);

        int waterConsumed = updateGrowthDecayFactors(
                organisms,
                availableWater,
                totalWaterRequired,
                resourceAvailabilityFactor,
                waterInfluenceFactor
        );

        Map<String, Integer> consumptionMap = new HashMap<>();
        consumptionMap.put(AppConfig.WATER, waterConsumed);

        return consumptionMap;
    }

    private int updateGrowthDecayFactors(
            List<Organism> organisms,
            int availableWater,
            int totalWaterRequired,
            double resourceAvailabilityFactor,
            double waterInfluenceFactor
    ) {

        double adjustedWaterAvailability = availableWater / resourceAvailabilityFactor;

        if (adjustedWaterAvailability >= availableWater) {
            for (Organism organism : organisms) {
                organism.setGrowthDecayFactor(organism.getGrowthDecayFactor() * (1 + (0.3 * waterInfluenceFactor)));
            }

            return totalWaterRequired;
        }

        double waterPerOrganism = adjustedWaterAvailability / organisms.size();

        for (Organism organism : organisms) {
            double requirement = organism.getTotalWaterRequirement();
            double deficitRatio = waterPerOrganism / requirement;
            double waterImpact = GrowthFactoryCalculator.resourceImpact(deficitRatio, waterInfluenceFactor);

            System.out.println("Water impact: " + waterImpact + " For organism: " + organism.getName());

            organism.setGrowthDecayFactor(organism.getGrowthDecayFactor() * waterImpact);
        }

        return (int) adjustedWaterAvailability;
    }
}