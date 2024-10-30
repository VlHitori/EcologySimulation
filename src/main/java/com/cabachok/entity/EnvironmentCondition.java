package com.cabachok.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EnvironmentCondition {
    private double temperature;
    private double humidity;
    private int waterAvailability;
    private int fertileSoilAmount;
    private int dailyPrecipitation;
    private int organicMatterAccumulation;

    public EnvironmentCondition(double temperature, double humidity, int waterAvailability, int fertileSoilAmount, int dailyPrecipitation) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.waterAvailability = waterAvailability;
        this.fertileSoilAmount = fertileSoilAmount;
        this.dailyPrecipitation = dailyPrecipitation;
        this.organicMatterAccumulation = 0;
    }

    public void updateWaterAvailability(int waterAvailability) {
        this.waterAvailability = waterAvailability;
    }

    public void updateFertileSoilAmount(int fertileSoilAmount) {
        this.fertileSoilAmount = fertileSoilAmount;
    }

    @Override
    public String toString() {
        return "\nEnvironmentCondition:" +
                "\nTemperature: " + temperature +
                "\nHumidity: " + humidity +
                "\nFertileSoilAmount: " + fertileSoilAmount +
                "\nWaterAvailability: " + waterAvailability +
                "\nDailyPrecipitation: " + dailyPrecipitation +
                "\nOrganicMatterAccumulation: " + organicMatterAccumulation;
    }
}
