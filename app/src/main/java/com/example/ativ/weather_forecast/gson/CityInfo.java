package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class CityInfo {
    @SerializedName("city")
    public String cityName;
    @SerializedName("cityId")
    public String cityCode;
    @SerializedName("parent")
    public String pName;
    @SerializedName("updateTime")
    public String weatherTime;
}
