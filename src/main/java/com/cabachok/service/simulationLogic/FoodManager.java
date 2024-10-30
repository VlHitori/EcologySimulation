package com.cabachok.service.simulationLogic;

import com.cabachok.config.Configuration;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Organism;
import com.cabachok.utils.GrowthFactoryCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodManager {
    public Map<String, Integer> measureFoodImpact(
            Ecosystem ecosystem,
            double resourceAvailabilityFactor,
            double foodInfluenceFactor
    ) {
        List<Organism> organisms = ecosystem.getOrganisms();
        EnvironmentCondition condition = ecosystem.getEnvironmentCondition();

        int soilFertility = condition.getFertileSoilAmount();

        int availablePlants = (int) (
                GrowthFactoryCalculator.calculateTotalByType(
                        organisms,
                        Configuration.PLANT,
                        Organism::getPopulation
                ) * Configuration.ENERGY_TRANSFER_RATE
        );

        int availableHerbivores = (int) (
                GrowthFactoryCalculator.calculateTotalByType(
                        organisms,
                        Configuration.HERBIVORE,
                        Organism::getPopulation
                ) * Configuration.ENERGY_TRANSFER_RATE
        );

        int totalSoilRequired = GrowthFactoryCalculator.calculateTotalByType(organisms, Configuration.PLANT, Organism::getTotalFoodRequirement);
        int totalPlantsRequired = GrowthFactoryCalculator.calculateTotalByType(organisms, Configuration.HERBIVORE, Organism::getTotalFoodRequirement);
        int totalHerbivoreRequired = GrowthFactoryCalculator.calculateTotalByType(organisms, Configuration.CARNIVORE, Organism::getTotalFoodRequirement);

        Map<String, Integer> consumptionMap = new HashMap<>();

        int soilConsumed = updateGrowthDecayFactors(
                organisms,
                Configuration.PLANT,
                soilFertility,
                totalSoilRequired,
                resourceAvailabilityFactor,
                foodInfluenceFactor
        );

        int plantsConsumed = updateGrowthDecayFactors(
                organisms,
                Configuration.HERBIVORE,
                availablePlants,
                totalPlantsRequired,
                resourceAvailabilityFactor,
                foodInfluenceFactor
        );

        int herbivoresConsumed = updateGrowthDecayFactors(
                organisms,
                Configuration.CARNIVORE,
                availableHerbivores,
                totalHerbivoreRequired,
                resourceAvailabilityFactor,
                foodInfluenceFactor
        );

        consumptionMap.put(Configuration.SOIL, soilConsumed);
        consumptionMap.put(Configuration.PLANT, plantsConsumed);
        consumptionMap.put(Configuration.HERBIVORE, herbivoresConsumed);

        return consumptionMap;
    }

    private int updateGrowthDecayFactors(
            List<Organism> organisms,
            String organismType,
            int availableFood,
            int requiredFood,
            double resourceAvailabilityFactor,
            double foodInfluenceFactor
    ) {

        double adjustedFood = availableFood / resourceAvailabilityFactor;

        if (adjustedFood >= requiredFood) {
            for (Organism organism : organisms) {
                if (organism.getType().equals(organismType)) {
                    organism.setGrowthDecayFactor(organism.getGrowthDecayFactor() * (1 + (0.2 * foodInfluenceFactor)));
                }
            }

            return requiredFood;
        }

        int totalOrganismsCount = GrowthFactoryCalculator.calculateTotalByType(organisms, organismType, Organism::getPopulation);

        double foodPerOrganism = adjustedFood / totalOrganismsCount;

        for (Organism organism : organisms) {
            if (organism.getType().equals(organismType)) {
                double foodRequirement = organism.getFoodRequirements();
                double deficitRatio = foodPerOrganism / foodRequirement;

                double foodImpact = GrowthFactoryCalculator.resourceImpact(deficitRatio, foodInfluenceFactor);

                System.out.println("Food Impact: " + foodImpact + " for organism" + organism.getName());
                organism.setGrowthDecayFactor(organism.getGrowthDecayFactor() * foodImpact);

            }
        }

        return (int) adjustedFood;
    }
}
