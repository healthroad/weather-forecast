package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("time")
    public String systemupdate;

    public CityInfo cityInfo;
    public class CityInfo {
        @SerializedName("city")
        public String cityName;

        @SerializedName("cityId")
        public String citycode;

        @SerializedName("parent")
        public String pname;

        @SerializedName("updateTime")
        public String weatherupdate;
    }

    @SerializedName("date")
    public String weatherdate;

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public int status;

}