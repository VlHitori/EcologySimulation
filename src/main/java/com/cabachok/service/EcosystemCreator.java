package com.cabachok.service;

import com.cabachok.config.Configuration;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Herbivore;
import com.cabachok.entity.Organism;
import com.cabachok.entity.Carnivore;
import com.cabachok.entity.Plant;
import com.cabachok.factory.OrganismFactory;
import com.cabachok.utils.UserInterfaceService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EcosystemCreator {
    public Ecosystem createNewEcosystem() {

        String ecosystemName = UserInterfaceService.getString(
                """
                        
                        Creating a new Ecosystem
                        Enter ecosystem name(or q to quit):\s"""
        );

        if (ecosystemName.equals("q")) {
            return null;
        }

        EnvironmentCondition environmentCondition = getEnvironmentConditionFromUser();
        List<Organism> organisms = getOrganismsFromUser();

        return new Ecosystem(ecosystemName, organisms, environmentCondition);
    }

    private EnvironmentCondition getEnvironmentConditionFromUser() {
        double temperature = UserInterfaceService.getValidDoubleInput("Enter ecosystem temperature (-90 to 60): ", Configuration.MIN_TEMPERATURE, Configuration.MAX_TEMPERATURE);
        double humidity = UserInterfaceService.getValidDoubleInput("Enter ecosystem humidity( 0% to 100%): ", 0, 100);
        int waterAvailability = UserInterfaceService.getValidIntInput("Enter ecosystem water availability: ");
        int fertileSoilAmount = UserInterfaceService.getValidIntInput("Enter ecosystem fertile soil amount: ");
        int dailyPrecipitation = UserInterfaceService.getValidIntInput("Enter ecosystem daily precipitation: ");

        return new EnvironmentCondition(temperature, humidity, waterAvailability, fertileSoilAmount, dailyPrecipitation);
    }

    private List<Organism> getOrganismsFromUser() {
        List<Organism> organisms = new ArrayList<>();
        Set<String> organismTypes = new HashSet<>();
        Set<String> organismNames = new HashSet<>();

        while (true) {
            Organism organism = getOrganismDetails(organismNames);
            if (organism != null) {
                organisms.add(organism);
                organismTypes.add(getInstanceType(organism));
            } else {
                if (hasAllOrganismTypes(organismTypes)) {
                    break;
                } else {
                    System.out.println("Please add at least one Herbivore, one Carnivore, and one Plant.");
                }
            }
        }

        return organisms;
    }

    public Organism getOrganismDetails(Set<String> existingNames) {
        String name = UserInterfaceService.getUniqOrganismName(existingNames);

        if (name == null) {
            return null;
        }

        existingNames.add(name);

        String type = UserInterfaceService.getOrganismType();
        int population = UserInterfaceService.getValidIntInput("Enter population: ");
        int foodRequirements = UserInterfaceService.getValidIntInputForResources("Enter food requirements (0 to 5): ");
        int waterRequirements = UserInterfaceService.getValidIntInputForResources("Enter water requirements (0 to 5): ");
        double optimalHumidity = UserInterfaceService.getValidDoubleInput("Enter optimal humidity ( 0% to 100%): ", 0, 100);
        double optimalTemperature = UserInterfaceService.getValidDoubleInput("Enter optimal temperature (-90 to 60): ", Configuration.MIN_TEMPERATURE, Configuration.MAX_TEMPERATURE);

        return OrganismFactory.builder()
                .name(name)
                .type(type)
                .population(population)
                .foodRequirements(foodRequirements)
                .waterRequirements(waterRequirements)
                .optimalHumidity(optimalHumidity)
                .optimalTemperature(optimalTemperature)
                .build()
                .createOrganism();
    }

    private boolean hasAllOrganismTypes(Set<String> organismTypes) {
        return organismTypes.contains(Configuration.HERBIVORE) &&
                organismTypes.contains(Configuration.CARNIVORE) &&
                organismTypes.contains(Configuration.PLANT);
    }

    private String getInstanceType(Organism organism) {
        if (organism instanceof Herbivore) {
            return Configuration.HERBIVORE;
        } else if (organism instanceof Carnivore) {
            return Configuration.CARNIVORE;
        } else if (organism instanceof Plant) {
            return Configuration.PLANT;
        }

        return null;
    }
}