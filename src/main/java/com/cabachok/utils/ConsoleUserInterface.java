package com.cabachok.utils;

import com.cabachok.config.AppConfig;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.Organism;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class ConsoleUserInterface {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getUserChoice() {
        return getValidIntInput("Enter choice: ");
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int getValidIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static int getValidIntInputForResources(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());

                if (value >= 0 && value <= 5) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer between 0 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static double getValidDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a value between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getOrganismType() {
        while (true) {
            System.out.print("Enter organism type ('Carnivore'/'Herbivore'/'Plant'): ");
            String type = scanner.nextLine().trim().toLowerCase();
            if (type.equals(AppConfig.HERBIVORE) || type.equals(AppConfig.CARNIVORE) || type.equals(AppConfig.PLANT)) {
                return type;
            } else {
                System.out.println("Invalid organism type. Please enter 'Herbivore', 'Carnivore', or 'Plant'.");
            }
        }
    }

    public static int showMainMenu_getChoice() {
        System.out.println("\nEcosystem Simulator");
        System.out.println("Please select an option:");
        System.out.println("1. Manage Ecosystem");
        System.out.println("2. Show selected Ecosystem");
        System.out.println("3. Run Ecosystem Simulator");
        System.out.println("4. Exit");

        return getUserChoice();
    }

    public static int showEcosystemModificationMenu_getChoice() {
        System.out.println("Choose an option to modify:");
        System.out.println("1. Change temperature");
        System.out.println("2. Change humidity");
        System.out.println("3. Change water availability");
        System.out.println("4. Change fertile soil amount");
        System.out.println("5. Cancel modifications");

        return getUserChoice();
    }

    public static int showEcosystemSelectionMenu_getChoice() {
        System.out.println("\nEcosystem Service Menu");
        System.out.println("1. View Available Ecosystems");
        System.out.println("2. Create Your Own Ecosystem");
        System.out.println("3. Select an Existing Ecosystem");
        System.out.println("4. Delete Ecosystem from Saved List");
        System.out.println("5. Edit ecosystem parameters");
        System.out.println("6. Save Ecosystem");
        System.out.println("7. Return to Main Menu");

        return getUserChoice();
    }

    public static int showEditOrganismMenu_getChoice(String name) {
        System.out.printf("\nEdit Organism Menu for '%s':%n", name);
        System.out.println("1. Change Population");
        System.out.println("2. Change Food Requirements");
        System.out.println("3. Change Water Requirements");
        System.out.println("4. Change Optimal Humidity");
        System.out.println("5. Change Optimal Temperature");
        System.out.println("6. Return to Previous Menu");

        return getUserChoice();
    }

    public static int showEditEcosystemMenu_getChoice() {
        System.out.println("\nEdit Ecosystem Menu:");
        System.out.println("1. Edit Environment Conditions");
        System.out.println("2. Edit Organism");
        System.out.println("3. Add new Organism");
        System.out.println("4. Remove Organism");
        System.out.println("5. Finish Editing");

        return getUserChoice();
    }

    public static void showSimulationRunnerMenu() {
        System.out.println("\nManual Simulation Mode Started.");
        System.out.println("Options:");
        System.out.println("Enter 'step' to advance simulation by one step.");
        System.out.println("Enter 'startAuto' to begin automatic simulation.   !!!Press Enter to stop!!!");
        System.out.println("Enter 'predict' to forecast ecosystem changes.");
        System.out.println("Enter 'change' to modify the ecosystem state.");
        System.out.println("Enter 'info' to display detailed ecosystem information.");
        System.out.println("Enter 'exit' to end the simulation.");
    }

    public static void displayNoEcosystemSelectedMessage() {
        System.out.println("\nNo ecosystem selected\n Please select ecosystem in ecosystem manager.");
    }

    public static void displayAvailableEcosystems(List<String> ecosystems) {
        if (ecosystems.isEmpty()) {
            System.out.println("\nThere are no available ecosystems");
            return;
        }
        System.out.println("\nAvailable ecosystems\n--------------------");

        for (int i = 0; i < ecosystems.size(); i++) {
            System.out.println((i + 1) + ". " + ecosystems.get(i));
        }
    }

    public static void displayEcosystemDetails(Ecosystem ecosystem) {
        if (ecosystem == null) {
            displayNoEcosystemSelectedMessage();
            return;
        }
        System.out.println(ecosystem);
    }

    public static void showShortEcosystemData(Ecosystem ecosystem) {
        if (ecosystem == null) {
            System.out.println("No ecosystem selected");
            return;
        }
        System.out.println("\n" + ecosystem.compactView());
    }

    public static String getUniqOrganismName(Set<String> existingNames) {
        String name;

        while (true) {
            name = ConsoleUserInterface.getString("Enter organism name (or 'done' to finish): ");

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
                continue;
            }

            if (name.equals("done")) {
                return null;
            }

            if (!existingNames.contains(name)) {
                break;
            } else {
                System.out.println("Organism name already taken. Please choose another name.");
            }
        }

        return name;
    }

    public static void displayOrganismForList(List<Organism> organisms) {
        if (organisms == null || organisms.isEmpty()) {
            System.out.println("No organism to display");
            return;
        }

        System.out.printf("%-5s%-10s%-10s%-10s%-10s%-10s%-10s%-10s%n",
                "No:", "Name:", "Type:", "Population:", "FoodReq:", "WaterReq:", "OptHumid:", "OptTemp:");

        for (int i = 0; i < organisms.size(); i++) {
            Organism organism = organisms.get(i);
            System.out.printf(
                    "%-5d%-10s%-10s%-10d%-10d%-10d%-10.2f%-10.2f%n",
                    i + 1,
                    organism.getName(),
                    organism.getType(),
                    organism.getPopulation(),
                    organism.getFoodRequirements(),
                    organism.getWaterRequirements(),
                    organism.getOptimalHumidity(),
                    organism.getOptimalTemperature()
            );
        }
    }
}
