package com.cabachok.service;

import com.cabachok.entity.Ecosystem;
import com.cabachok.utils.UserInterfaceService;

public class AppManager {
    private Ecosystem ecosystem;

    public void start() {

        while (true) {
            int choice = UserInterfaceService.showMainMenu_getChoice();

            switch (choice) {
                case 1:
                    EcosystemManager ecosystemManager = new EcosystemManager();
                    ecosystem = ecosystemManager.manageEcosystem(ecosystem);
                    break;
                case 2:
                    UserInterfaceService.displayEcosystemDetails(ecosystem);
                    break;
                case 3:
                    if (ecosystem == null) {
                        UserInterfaceService.displayNoEcosystemSelectedMessage();
                        break;
                    }
                    SimulationRunner simulationRunner = new SimulationRunner();
                    simulationRunner.run(ecosystem);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}