package com.example.ativ.weather_forecast.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {

    private int id;

    private int pid;

    private String countyName;

    private String cityCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid(){
        return pid;
    }

    public void setPid(int pid){
        this.pid = pid;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

}