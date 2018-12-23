package com.example.ativ.weather_forecast.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {

    private int id;

    private int pid;

    private String provinceName;

    private String cityCode="";

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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    /*
    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
    */

}
