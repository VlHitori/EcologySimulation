package com.cabachok.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Ecosystem {
    private String name;
    private List<Organism> organisms;
    private EnvironmentCondition environmentCondition;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ecosystem: ").append(name)
                .append(environmentCondition.toString())
                .append("\nOrganisms:");

        for (Organism organism : organisms) {
            sb.append(organism.toString());
        }

        return sb.toString();
    }

    public String compactView() {
        StringBuilder sb = new StringBuilder();

        sb.append("Temp\tHumid\tWater\tSoil\n");
        sb.append(String.format("%.1f\t%.1f\t%d\t%d\n",
                environmentCondition.getTemperature(),
                environmentCondition.getHumidity(),
                environmentCondition.getWaterAvailability(),
                environmentCondition.getFertileSoilAmount()
        ));

        sb.append("Organism\tPopulation\n");

        for (Organism organism : organisms) {
            sb.append(organism.getName()).append("\t\t").append(organism.getPopulation()).append("\n");
        }

        return sb.toString();
    }
}


