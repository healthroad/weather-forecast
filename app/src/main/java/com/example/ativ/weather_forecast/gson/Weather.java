package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Yesterday yesterday;
    @SerializedName("forecast")
    public List<Forecast> forecastList;
}
