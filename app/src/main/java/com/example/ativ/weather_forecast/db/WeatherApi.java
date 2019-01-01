package com.example.ativ.weather_forecast.db;

import org.litepal.crud.DataSupport;

public class WeatherApi extends DataSupport {

    private String cityCode;

    private String responseText;

    private String date;

    public String getCityCode(){
        return cityCode;
    }

    public void setCityCode(String cityCode){
        this.cityCode=cityCode;
    }

    public String getResponseText(){
        return responseText;
    }

    public void setResponseText(String responseText){
        this.responseText=responseText;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date=date;
    }
}
