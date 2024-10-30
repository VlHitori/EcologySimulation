package com.cabachok.service;

import com.cabachok.config.Configuration;
import com.cabachok.entity.Ecosystem;
import com.cabachok.entity.Organism;
import com.cabachok.utils.UserInterfaceService;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class EcosystemModifier {
    public void editEcosystem(Ecosystem ecosystem) {
        if (ecosystem == null) {
            System.out.println("\n No ecosystem selected.");
            return;
        }

        while (true) {
            int choice = UserInterfaceService.showEditEcosystemMenu_getChoice();

            switch (choice) {
                case 1:
                    editEnvironmentConditions(ecosystem);
                    break;
                case 2:
                    editOrganism(ecosystem);
                    break;
                case 3:
                    addNewOrganism(ecosystem);
                    break;
                case 4:
                    removeOrganism(ecosystem);
                    break;
                case 5:
                    System.out.println("Finished editing ecosystem.");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void removeOrganism(Ecosystem ecosystem) {
        List<Organism> organisms = ecosystem.getOrganisms();

        if (organisms.isEmpty()) {
            System.out.println("No organisms in ecosystem.");
            return;
        }

        UserInterfaceService.displayOrganismForList(organisms);

        int index = UserInterfaceService.getValidIntInput("Number of the organism to remove: ");

        if (index < 1 || index > organisms.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Organism organismToRemove = organisms.get(index - 1);
        organisms.remove(organismToRemove);
        System.out.println("Organism " + organismToRemove + " has been removed.");
    }

    private void addNewOrganism(Ecosystem ecosystem) {
        EcosystemCreator ecosystemCreator = new EcosystemCreator();
        Set<String> existingNames = new HashSet<>();
        ecosystem.getOrganisms().forEach(org -> existingNames.add(org.getName()));
        Organism newOrganism = ecosystemCreator.getOrganismDetails(existingNames);

        if (newOrganism != null) {
            ecosystem.getOrganisms().add(newOrganism);
            System.out.println("Organism " + newOrganism.getName() + " added to the ecosystem.");
        } else {
            System.out.println("Organism addition cancelled.");
        }

    }

    public void editEnvironmentConditions(Ecosystem ecosystem) {
        UserInterfaceService.showShortEcosystemData(ecosystem);
        int choice = UserInterfaceService.showEcosystemModificationMenu_getChoice();

        switch (choice) {
            case 1:
                double newTemperature = UserInterfaceService.getValidDoubleInput("Enter new temperature (-90 to 60):", Configuration.MIN_TEMPERATURE, Configuration.MAX_TEMPERATURE);
                ecosystem.getEnvironmentCondition().setTemperature(newTemperature);
                break;
            case 2:
                double newHumidity = UserInterfaceService.getValidDoubleInput("Enter new humidity (0% to 100%):", 0, 100);
                ecosystem.getEnvironmentCondition().setHumidity(newHumidity);
                break;
            case 3:
                int newWaterAvailability = UserInterfaceService.getValidIntInput("Enter new water availability:");
                ecosystem.getEnvironmentCondition().updateWaterAvailability(newWaterAvailability);
                break;
            case 4:
                int newSoilAmount = UserInterfaceService.getValidIntInput("Enter new fertile soil amount:");
                ecosystem.getEnvironmentCondition().setFertileSoilAmount(newSoilAmount);
                break;
            case 5:
                System.out.println("No modifications made.");
                break;
            default:
                System.out.println("Invalid choice, no changes made.");
                break;
        }
    }

    private void editOrganism(Ecosystem ecosystem) {
        List<Organism> organisms = ecosystem.getOrganisms();

        if (organisms.isEmpty()) {
            System.out.println("No found organisms in this ecosystem.");
            return;
        }

        UserInterfaceService.displayOrganismForList(organisms);

        int index = UserInterfaceService.getValidIntInput("Number organism to edit: ");

        if (index < 1 || index > organisms.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Organism organism = organisms.get(index - 1);

        while (true) {
            int choice = UserInterfaceService.showEditOrganismMenu_getChoice(organism.getName());

            switch (choice) {
                case 1:
                    int newPopulation = UserInterfaceService.getValidIntInput("Enter new population:");
                    organism.setPopulation(newPopulation);
                    break;
                case 2:
                    int newFoodRequirements = UserInterfaceService.getValidIntInput("Enter new food requirements:");
                    organism.setFoodRequirements(newFoodRequirements);
                    break;
                case 3:
                    int newWaterRequirements = UserInterfaceService.getValidIntInput("Enter new water requirements:");
                    organism.setWaterRequirements(newWaterRequirements);
                    break;
                case 4:
                    double newOptimalHumidity = UserInterfaceService.getValidDoubleInput("Enter new optimal humidity (0% to 100%):", 0, 100);
                    organism.setOptimalHumidity(newOptimalHumidity);
                    break;
                case 5:
                    double newOptimalTemperature = UserInterfaceService.getValidDoubleInput("Enter new optimal temperature (-90 to 60):", Configuration.MIN_TEMPERATURE, Configuration.MAX_TEMPERATURE);
                    organism.setOptimalTemperature(newOptimalTemperature);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
