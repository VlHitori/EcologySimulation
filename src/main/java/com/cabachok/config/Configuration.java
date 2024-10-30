package com.cabachok.config;

public class Configuration {
    public static final String ECOSYSTEMS_PATH = "src/main/resources/ecosystems";
    public static final String LOG_PATH = "src/main/resources/logs";

    public static final int RESOURCE_AVAILABILITY_FACTOR = 3; // при превышении потребности в 3 раза - изобилие
    public static final double START_GROWTH_DECAY_FACTOR = 1;
    public static final double ENERGY_TRANSFER_RATE = 1; // 100% энергии передаётся на следующий уровень

    public static final double WATER_INFLUENCE_FACTOR = 0.3;
    public static final double FOOD_INFLUENCE_FACTOR = 0.5;
    public static final double TEMPERATURE_INFLUENCE_FACTOR = 0.3;
    public static final double HUMIDITY_INFLUENCE_FACTOR = 0.3;

    public static final int RAIN_FREQUENCY = 2;

    public static final double ORGANIC_MATTER_TRANSFER_RATE = 0.6;

    public static final String SOIL = "soil";
    public static final String PLANT = "plant";
    public static final String HERBIVORE = "herbivore";
    public static final String CARNIVORE = "carnivore";
    public static final String WATER = "water";
    public static final double MAX_TEMPERATURE = 60;
    public static final double MIN_TEMPERATURE = -90;
}
