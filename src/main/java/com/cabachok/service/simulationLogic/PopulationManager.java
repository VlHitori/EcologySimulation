package com.cabachok.service.simulationLogic;

import com.cabachok.config.AppConfig;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.Organism;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PopulationManager {
    public void updateEcosystem(Ecosystem ecosystem, Map<String, Integer> consumedResources) {
        ecosystem.getOrganisms().forEach(Organism::updatePopulation);
        processDeadOrganisms(ecosystem, consumedResources);
    }

    private void processDeadOrganisms(Ecosystem ecosystem, Map<String, Integer> consumedResources) {
        List<Organism> organisms = ecosystem.getOrganisms();

        Map<String, List<Organism>> organismsByType = groupOrganismsByType(organisms);

        organismsByType.forEach((type, sameTypeOrganisms) -> {
            if (!type.equals(AppConfig.CARNIVORE)) {
                int consumedAmount = consumedResources.getOrDefault(type, 0);
                if (consumedAmount > 0) {
                    distributeConsumption(sameTypeOrganisms, consumedAmount);
                }
            }
        });

        int waterConsumed = consumedResources.getOrDefault(AppConfig.WATER, 0);
        if (waterConsumed > 0) {
            int currentWaterAvailability = ecosystem.getEnvironmentCondition().getWaterAvailability();
            ecosystem.getEnvironmentCondition().updateWaterAvailability(currentWaterAvailability - waterConsumed);
        }

        int soilConsumed = consumedResources.getOrDefault(AppConfig.SOIL, 0);
        if (soilConsumed > 0) {
            int currentSoilFertility = ecosystem.getEnvironmentCondition().getFertileSoilAmount();
            ecosystem.getEnvironmentCondition().updateFertileSoilAmount(currentSoilFertility - soilConsumed);
        }
    }

    private Map<String, List<Organism>> groupOrganismsByType(List<Organism> organisms) {
        return organisms.stream().collect(Collectors.groupingBy(Organism::getType));
    }

    private void distributeConsumption(List<Organism> sameTypeOrganisms, int consumedAmount) {
        int consumptionPerOrganism = !sameTypeOrganisms.isEmpty() ? consumedAmount / sameTypeOrganisms.size() : 0;
        int remainder = consumedAmount % sameTypeOrganisms.size();

        for (Organism organism : sameTypeOrganisms) {
            int toRemove = consumptionPerOrganism + (remainder > 0 ? 1 : 0);
            remainder = Math.max(0, remainder - 1);

            if (organism.getPopulation() >= toRemove) {
                organism.decreasePopulation(toRemove);
            }
        }
    }
}
