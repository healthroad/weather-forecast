package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("shidu")
    public String shidu;
    @SerializedName("pm25")
    public float pm25;
    @SerializedName("pm10")
    public float pm10;
    @SerializedName("quality")
    public String quality;
    @SerializedName("wendu")
    public String wendu;
    @SerializedName("ganmao")
    public String ganmao;
    @SerializedName("yesterday")
    public Yesterday yesterday;
    @SerializedName("forecast")
    public List<Forecast> forecastList;

}
