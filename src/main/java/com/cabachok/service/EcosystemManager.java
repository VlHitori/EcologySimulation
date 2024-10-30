package com.cabachok.service;

import com.cabachok.config.Configuration;
import com.cabachok.entity.Ecosystem;
import com.cabachok.utils.UserInterfaceService;


public class EcosystemManager {
    public Ecosystem manageEcosystem(Ecosystem ecosystem) {
        EcosystemCatalog ecosystemCatalog = new EcosystemCatalog();
        EcosystemModifier ecosystemModifier = new EcosystemModifier();
        while (true) {
            int choice = UserInterfaceService.showEcosystemSelectionMenu_getChoice();

            switch (choice) {
                case 1:
                    UserInterfaceService.displayAvailableEcosystems(
                            ecosystemCatalog.getEcosystemNames(Configuration.ECOSYSTEMS_PATH)
                    );
                    break;
                case 2:
                    EcosystemCreator ecosystemCreator = new EcosystemCreator();
                    ecosystem = ecosystemCreator.createNewEcosystem();
                    break;
                case 3:
                    ecosystem = ecosystemCatalog.chooseExistingEcosystem();
                    break;
                case 4:
                    ecosystemCatalog.removeEcosystem();
                    break;
                case 5:
                    ecosystemModifier.editEcosystem(ecosystem);
                    break;
                case 6:
                    ecosystemCatalog.saveEcosystem(ecosystem);
                    break;
                case 7:
                    System.out.println("Return to Main Menu...");
                    return ecosystem;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
