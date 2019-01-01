package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;



public class Weather {

    @SerializedName("time")
    public String systemTime;

    public CityInfo cityInfo;

    @SerializedName("date")
    public String date;

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public int status;

    public Data data;

}
