package com.example.ativ.weather_forecast.util;

import android.text.TextUtils;
import com.example.ativ.weather_forecast.db.Province;
import com.example.ativ.weather_forecast.db.City;
import com.example.ativ.weather_forecast.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setId(provinceObject.getInt("id"));
                    province.setPid(provinceObject.getInt("pid"));
                    province.setCityName(provinceObject.getString("city_name"));
                    province.setCityCode(provinceObject.getString("city_code"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回城市的数据
     */
    public static boolean handleCityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setId(cityObject.getInt("id"));
                    city.setPid(cityObject.getInt("pid"));
                    city.setCityName(cityObject.getString("city_name"));
                    city.setCityCode(cityObject.getString("city_code"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     *
     * 处理得到的 weather 数据，转化为 weather 对象
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            //JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
