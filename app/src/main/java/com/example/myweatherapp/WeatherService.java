package com.example.myweatherapp;

import android.telecom.Call;

public interface WeatherService {
    @GET(.getString(R.string.weather))
    Call<WeatherRes> getCurrentWeather(@Query("latitude") double lat, @Query("longitude") double lon, @Query("appid") String apiKey);

}
