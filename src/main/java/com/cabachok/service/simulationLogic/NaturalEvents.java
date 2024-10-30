package com.cabachok.service.simulationLogic;

import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;

import java.util.Random;

public class NaturalEvents {
    public void applyNaturalEvents(Ecosystem ecosystem, int rainFrequency, double organicMatterTransferRate, int consumedSoil) {
        simulateSoilNourishment(ecosystem, organicMatterTransferRate, consumedSoil);
        simulateRainEvent(ecosystem, rainFrequency);
    }

    private void simulateSoilNourishment(Ecosystem ecosystem, double organicMatterTransferRate, int consumedSoil) {
        EnvironmentCondition condition = ecosystem.getEnvironmentCondition();

        int organicMatter = condition.getOrganicMatterAccumulation();
        int nutrientToTransfer = (int) (organicMatter * organicMatterTransferRate);

        condition.setFertileSoilAmount(condition.getFertileSoilAmount() + nutrientToTransfer);
        condition.setOrganicMatterAccumulation(organicMatter - nutrientToTransfer + consumedSoil);
    }

    private void simulateRainEvent(Ecosystem ecosystem, int rainFrequency) {
        EnvironmentCondition condition = ecosystem.getEnvironmentCondition();
        Random random = new Random();

        int randomDay = random.nextInt(rainFrequency) + 1;

        if (randomDay == rainFrequency) {
            int basePrecipitation = condition.getDailyPrecipitation();
            double randomPercentage = random.nextDouble() * 0.1;
            int precipitationAmount = (int) (basePrecipitation * (1 + randomPercentage) * rainFrequency);

            condition.setWaterAvailability(condition.getWaterAvailability() + precipitationAmount);
        }
    }
}
