package com.cabachok.utils;


import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Organism;
import com.cabachok.factory.OrganismFactory;

import java.util.ArrayList;
import java.util.List;

public final class EcosystemDataParser {
    public static Ecosystem parse(List<String> lines) {

        String ecosystemName = null;
        EnvironmentCondition environmentCondition = null;
        List<Organism> organisms = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("Ecosystem:")) {
                ecosystemName = extractValue(line);
            } else if (line.startsWith("EnvironmentCondition:")) {
                environmentCondition = parseEnvironmentCondition(lines, ++i);
            } else if (line.startsWith("Organisms:")) {
                organisms = parseOrganisms(lines, i + 1);
                break;
            }
        }

        return new Ecosystem(ecosystemName, organisms, environmentCondition);
    }

    private static EnvironmentCondition parseEnvironmentCondition(List<String> lines, int startIndex) {
        double temperature = 0, humidity = 0;
        int waterAvailability = 0, fertileSoilAmount = 0, dailyPrecipitation = 0, organicMatterAccumulator = 0;

        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) break;

            String[] parts = line.split(":");
            if (parts.length < 2) continue;

            switch (parts[0].trim()) {
                case "Temperature":
                    temperature = Double.parseDouble(parts[1].trim());
                    break;
                case "Humidity":
                    humidity = Double.parseDouble(parts[1].trim());
                    break;
                case "WaterAvailability":
                    waterAvailability = Integer.parseInt(parts[1].trim());
                    break;
                case "FertileSoilAmount":
                    fertileSoilAmount = Integer.parseInt(parts[1].trim());
                    break;
                case "DailyPrecipitation":
                    dailyPrecipitation = Integer.parseInt(parts[1].trim());
                    break;
                case "OrganicMatterAccumulation":
                    organicMatterAccumulator = Integer.parseInt(parts[1].trim());
                    break;
            }
        }

        return new EnvironmentCondition(temperature, humidity, waterAvailability, fertileSoilAmount, dailyPrecipitation, organicMatterAccumulator);
    }

    private static List<Organism> parseOrganisms(List<String> lines, int startIndex) {
        List<Organism> organisms = new ArrayList<>();
        String name = null, type = null;
        int population = 0, foodRequirements = 0, waterRequirements = 0;
        double optimalHumidity = 0, optimalTemperature = 0;

        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("Organism:")) {
                String[] parts = extractValue(line).split("'");
                name = parts[0].trim();
                type = parts[1].trim();
            } else {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;

                switch (parts[0].trim()) {
                    case "Population":
                        population = Integer.parseInt(parts[1].trim());
                        break;
                    case "FoodRequirements":
                        foodRequirements = Integer.parseInt(parts[1].trim());
                        break;
                    case "WaterRequirements":
                        waterRequirements = Integer.parseInt(parts[1].trim());
                        break;
                    case "OptimalHumidity":
                        optimalHumidity = Double.parseDouble(parts[1].trim());
                        break;
                    case "OptimalTemperature":
                        optimalTemperature = Double.parseDouble(parts[1].trim());
                        break;
                }
            }

            if (i == lines.size() - 1 || lines.get(i + 1).trim().startsWith("Organism:")) {
                organisms.add(OrganismFactory.builder()
                        .name(name)
                        .type(type)
                        .population(population)
                        .foodRequirements(foodRequirements)
                        .waterRequirements(waterRequirements)
                        .optimalHumidity(optimalHumidity)
                        .optimalTemperature(optimalTemperature)
                        .build()
                        .createOrganism());
            }
        }

        return organisms;
    }

    private static String extractValue(String line) {
        return line.split(":")[1].trim();
    }
}
