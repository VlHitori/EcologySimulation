package com.cabachok.service;

import com.cabachok.config.AppConfig;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.EnvironmentCondition;
import com.cabachok.entity.Organism;
import com.cabachok.utils.CustomLogger;
import com.cabachok.utils.GrowthFactoryCalculator;
import com.cabachok.utils.ConsoleUserInterface;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@NoArgsConstructor
public class SimulationRunner {
    private static double calculateResourceImpact(double requirement, double available, double influenceFactor) {
        double deficitRatio = available / requirement;
        return GrowthFactoryCalculator.resourceImpact(deficitRatio, influenceFactor);
    }

    private static double updatePopulationEstimate(double currentPopulation, double totalImpact) {
        if (totalImpact > 1.1) {
            return currentPopulation * 1.1;
        } else if (totalImpact < 0.9) {
            return currentPopulation * 0.9;
        }
        return currentPopulation;
    }

    private static double calculateTotalFoodAvailable(Ecosystem ecosystem) {
        return GrowthFactoryCalculator.calculateTotalWaterRequired(ecosystem.getOrganisms(), Organism::getFoodRequirements);
    }

    public void run(Ecosystem ecosystem) {
        EcosystemModifier ecosystemModifier = new EcosystemModifier();
        Simulation simulation = new Simulation();
        ConsoleUserInterface.showSimulationRunnerMenu();

        while (true) {
            String userInput = ConsoleUserInterface.getString("\nYour choice: ").toLowerCase();

            switch (userInput) {
                case "step":
                    simulation.makeStep(ecosystem);
                    ConsoleUserInterface.showShortEcosystemData(ecosystem);
                    break;
                case "startauto":
                    startAutomaticSimulation(ecosystem, simulation);
                    break;
                case "change":
                    ecosystemModifier.editEnvironmentConditions(ecosystem);
                    break;
                case "info":
                    ConsoleUserInterface.displayEcosystemDetails(ecosystem);
                    break;
                case "predict":
                    predictPopulationChange(ecosystem);
                    break;
                case "exit":
                    System.out.println("Exiting simulation...");
                    ConsoleUserInterface.displayEcosystemDetails(ecosystem);
                    System.out.println("\nSimulation completed.");
                    EcosystemCatalog catalog = new EcosystemCatalog();
                    catalog.saveEcosystem(ecosystem);
                    return;
                default:
                    System.out.println("Unknown command. Please enter 'step', 'startAuto', 'change', 'info', 'predict', or 'exit'.");
            }
        }
    }

    private void startAutomaticSimulation(Ecosystem ecosystem, Simulation simulation) {
        String logFileName = AppConfig.LOG_PATH + "/" + ecosystem.getName() + "Log.txt";

        System.out.println("Starting automatic simulation... Press Enter to stop.");
        CustomLogger.logToFile(logFileName, "Starting automatic simulation with ecosystem details: \n" + ecosystem);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> simulationTask = executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                simulation.makeStep(ecosystem);
                CustomLogger.logToFile(logFileName, "Ecosystem current state:\n" + ecosystem.compactView());
                ConsoleUserInterface.showShortEcosystemData(ecosystem);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            reader.readLine();
            simulationTask.cancel(true);
            executor.shutdownNow();
            System.out.println("Automatic simulation stopped.");
        } catch (IOException e) {
            CustomLogger.logToFile(logFileName, "Error reading input: " + e.getMessage());
            System.err.println("Error reading input: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println("No input available. Please try again.");
        } finally {
            executor.shutdown();
        }
    }

    public void predictPopulationChange(Ecosystem ecosystem) {
        System.out.printf("%-20s %-15s %-20s%n", "Name", "Type", "Population Change");


        for (Organism organism : ecosystem.getOrganisms()) {
            double initialPopulation = organism.getPopulation();
            double currentPopulation = initialPopulation;

            EnvironmentCondition conditions = ecosystem.getEnvironmentCondition();

            double waterImpact = calculateResourceImpact(organism.getWaterRequirements(), conditions.getWaterAvailability(), AppConfig.WATER_INFLUENCE_FACTOR);
            double foodImpact = calculateResourceImpact(organism.getFoodRequirements(), calculateTotalFoodAvailable(ecosystem), AppConfig.FOOD_INFLUENCE_FACTOR);
            double temperatureImpact = GrowthFactoryCalculator.calculateTemperatureImpact(Math.abs(conditions.getTemperature() - organism.getOptimalTemperature()), AppConfig.TEMPERATURE_INFLUENCE_FACTOR);
            double humidityImpact = GrowthFactoryCalculator.calculateHumidityImpact(Math.abs(conditions.getHumidity() - organism.getOptimalHumidity()), AppConfig.HUMIDITY_INFLUENCE_FACTOR);

            double totalImpact = waterImpact * foodImpact * temperatureImpact * humidityImpact;

            currentPopulation = updatePopulationEstimate(currentPopulation, totalImpact);

            String change = (currentPopulation > initialPopulation) ? "It will grow" :
                    (currentPopulation < initialPopulation) ? "It will decrease" :
                            "It will remain stable";

            System.out.printf("%-20s %-15s %-20s%n", organism.getName(), organism.getType(), change);
        }
    }
}

