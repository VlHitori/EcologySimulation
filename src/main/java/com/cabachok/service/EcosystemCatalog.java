package com.cabachok.service;

import com.cabachok.config.Configuration;
import com.cabachok.entity.Ecosystem;
import com.cabachok.utils.EcosystemDataParser;
import com.cabachok.utils.TxtFileReader;
import com.cabachok.utils.TxtFileWriter;
import com.cabachok.utils.UserInterfaceService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EcosystemCatalog {
    public Ecosystem chooseExistingEcosystem() {
        List<String> ecosystemNames = getEcosystemNames(Configuration.ECOSYSTEMS_PATH);

        if (ecosystemNames.isEmpty()) {
            System.out.println("There are no Ecosystems in the directory.");
            return null;
        }

        UserInterfaceService.displayAvailableEcosystems(getEcosystemNames(Configuration.ECOSYSTEMS_PATH));

        while (true) {
            System.out.println("\nPlease enter the number of the ecosystem you want to select(0 to quit): ");
            int choice = UserInterfaceService.getUserChoice();

            if (choice == 0) {
                return null;
            }

            if (choice < 1 || choice > ecosystemNames.size()) {
                System.out.println("Invalid choice. Please try again.");
            } else {
                String selectedEcosystemName = ecosystemNames.get(choice - 1);
                EcosystemCatalog ecosystemCatalog = new EcosystemCatalog();
                Ecosystem selectedEcosystem = ecosystemCatalog.loadEcosystem(selectedEcosystemName);
                System.out.println("You have selected the ecosystem: " + selectedEcosystemName);

                return selectedEcosystem;
            }
        }
    }

    public String chooseEcosystemToDelete() {
        List<String> ecosystemNames = getEcosystemNames(Configuration.ECOSYSTEMS_PATH);

        if (ecosystemNames.isEmpty()) {
            System.out.println("No ecosystems in the directory.");
            return null;
        }

        UserInterfaceService.displayAvailableEcosystems(getEcosystemNames(Configuration.ECOSYSTEMS_PATH));

        while (true) {
            System.out.println("Enter the number of the ecosystem to delete (or 0 to quit): ");
            int choice = UserInterfaceService.getUserChoice();

            if (choice >= 1 && choice <= ecosystemNames.size()) {
                return ecosystemNames.get(choice - 1);
            }

            if (choice == 0) {
                return null;
            }

            System.out.println("Invalid choice. Try again.");
        }
    }

    public void removeEcosystem() {
        String ecosystemName = chooseEcosystemToDelete();
        if (ecosystemName == null) {
            System.out.println("No ecosystem selected for deletion.");
            return;
        }

        File file = new File(Configuration.ECOSYSTEMS_PATH, ecosystemName + ".txt");

        if (!file.exists()) {
            System.out.println("Ecosystem " + ecosystemName + " does not exist.");
            return;
        }

        boolean deleted = file.delete();
        System.out.println(deleted ? "Ecosystem " + ecosystemName + " deleted successfully."
                : "Failed to delete ecosystem " + ecosystemName + ".");
    }

    public List<String> getEcosystemNames(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            return List.of();
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return List.of();
        }

        return Arrays.stream(files)
                .filter(File::isFile) // Оставляем только файлы
                .map(file -> {
                    String fileName = file.getName();
                    int dotIndex = fileName.lastIndexOf('.');
                    return (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
                })
                .collect(Collectors.toList());
    }

    public Ecosystem loadEcosystem(String ecosystemName) {
        String fileName = ecosystemName + ".txt";
        File file = new File(Configuration.ECOSYSTEMS_PATH, fileName);

        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Ecosystem " + ecosystemName + " does not exist or is not a file.");
        }

        return parseFileSafely(file);
    }

    private Ecosystem parseFileSafely(File file) {
        try {
            List<String> fileContent = TxtFileReader.read(file.getAbsolutePath());
            return EcosystemDataParser.parse(fileContent);
        } catch (Exception e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return null;
    }

    public void saveEcosystem(Ecosystem ecosystem) {

        if (ecosystem == null) {
            System.out.println("\nNo selected ecosystem. Cannot save");
            return;
        }

        String fileName = ecosystem.getName() + ".txt";
        String filePath = Configuration.ECOSYSTEMS_PATH + File.separator + fileName;

        List<String> lines = Arrays.asList(ecosystem.toString().split("\n"));
        TxtFileWriter.write(filePath, lines);

        System.out.println("Ecosystem saved to: " + filePath);
    }
}
