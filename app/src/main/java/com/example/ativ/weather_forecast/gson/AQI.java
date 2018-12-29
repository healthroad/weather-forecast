package com.example.ativ.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class AQI {


        @SerializedName("shidu")
        public String shidu;
        @SerializedName("pm25")
        public int pm25;
        @SerializedName("pm10")
        public int pm10;
        @SerializedName("quality")
        public String quality;
        @SerializedName("wendu")
        public String wendu;
        @SerializedName("ganmao")
        public String ganmao;

}
