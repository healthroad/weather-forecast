package com.example.ativ.weather_forecast.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {

    private int id;

    private int pid;

    private String cityName;

    private String cityCode;

    //private int provinceId;

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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    /*
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
    */
}