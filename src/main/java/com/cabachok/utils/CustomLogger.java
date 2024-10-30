package com.cabachok.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class CustomLogger {
    private static final Logger logger = Logger.getLogger("EcosystemLogger");

    private CustomLogger() {
    }

    public static void logToFile(String filename, String data) {
        FileHandler fileHandler = null;
        try {
            logger.setUseParentHandlers(false);

            fileHandler = new FileHandler(filename, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.info(data);
        } catch (IOException e) {
            System.err.println("Error logging to file: " + e.getMessage());
        } finally {
            if (fileHandler != null) {
                fileHandler.close();
                logger.removeHandler(fileHandler);
            }
        }
    }
}
