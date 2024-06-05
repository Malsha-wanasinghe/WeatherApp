package com.example.myweatherapp;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

import java.util.List;

public class WeatherRes implements Result {
    @SerializedName("MAIN")
    private Main MAIN;
    @SerializedName("Weather")
    private List<Weather> Weather;

    public Main getMain() {
        return MAIN;
    }

    public List<Weather> getWeather() {
        return Weather;
    }

    @NonNull
    @Override
    public Status getStatus() {
        return null;
    }

    public class Main {
        @SerializedName("Temperature")
        private double Temperature;
        @SerializedName("Humidity")
        private int Humidity;

        public double getTemp() {
            return Temperature;
        }

        public int getHumidity() {
            return Humidity;
        }
    }

    public class Weather {
        @SerializedName("Description")
        private String Description;

        public String getDescription() {
            return Description;
        }
    }

}
