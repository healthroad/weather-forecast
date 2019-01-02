package com.example.ativ.weather_forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ativ.weather_forecast.gson.Forecast;
import com.example.ativ.weather_forecast.gson.Weather;
import com.example.ativ.weather_forecast.service.AutoUpdateService;
import com.example.ativ.weather_forecast.util.HttpUtil;
import com.example.ativ.weather_forecast.util.Utility;
import com.example.ativ.weather_forecast.db.WeatherApi;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private TextView systemTimeText;

    private LinearLayout forecastLayout;

    private TextView qualityText;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView pm10Text;

    private TextView ganmaoText;

    private TextView noticeText;

    private ImageView bingPicImg;

    private String mCityCode;

    private List<WeatherApi> weatherApiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        systemTimeText = (TextView) findViewById(R.id.system_time_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        qualityText=(TextView) findViewById(R.id.quality_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        pm10Text=(TextView) findViewById(R.id.pm10_text);
        ganmaoText = (TextView) findViewById(R.id.ganmao_text);
        noticeText = (TextView) findViewById(R.id.notice_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mCityCode=weather.cityInfo.cityCode;
            showWeatherInfo(weather);
            Toast.makeText(WeatherActivity.this, "从缓存中读取天气信息。", Toast.LENGTH_SHORT).show();
        } else {
            // 无缓存时去服务器查询天气
            mCityCode = getIntent().getStringExtra("city_code");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mCityCode);
            Toast.makeText(WeatherActivity.this, "从服务器中查询天气信息。", Toast.LENGTH_SHORT).show();
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mCityCode);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }


    /*
    * 当传入城市id后，先判断缓存，缓存当天的id，无缓冲再从服务器查找
    * */
    public void searchjudge(final String cityCode){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        weatherApiList=DataSupport.where("date=?",dateFormat.format(new Date())).find(WeatherApi.class);
        if(weatherApiList.size()>0){
            for(WeatherApi weatherApi : weatherApiList){
                if(cityCode.equals(weatherApi.getCityCode())) {
                    Weather weather = Utility.handleWeatherResponse(weatherApi.getResponseText());
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("weather", weatherApi.getResponseText());
                    editor.apply();
                    mCityCode=weatherApi.getCityCode();
                    showWeatherInfo(weather);
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(WeatherActivity.this, "从缓存中读取天气信息!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            searchWeather(cityCode);
        }else{
            searchWeather(cityCode);
        }
    }

    public void searchWeather(final String cityCode){
        String weatherUrl = "http://t.weather.sojson.com/api/weather/city/" + cityCode;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status == 200) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                            WeatherApi weatherApi = new WeatherApi();
                            weatherApi.setCityCode(weather.cityInfo.cityCode);
                            weatherApi.setResponseText(responseText);
                            weatherApi.setDate(dateFormat.format(new Date()));
                            weatherApi.save();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mCityCode=weather.cityInfo.cityCode;
                            showWeatherInfo(weather);
                            Toast.makeText(WeatherActivity.this, "从服务器中查询天气信息!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WeatherActivity.this, "输入的城市ID错误，获取天气信息失败!", Toast.LENGTH_LONG).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败!", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }
    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String cityCode) {
        String weatherUrl = "http://t.weather.sojson.com/api/weather/city/"+cityCode;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status==200) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mCityCode=weather.cityInfo.cityCode;
                            showWeatherInfo(weather);
                            Toast.makeText(WeatherActivity.this, "从服务器中查询天气信息", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败。", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.cityInfo.cityName;
        String updateTime = weather.cityInfo.weatherTime;
        String degree = weather.data.wendu + "℃";
        String weatherInfo = weather.data.forecastList.get(0).type;
        String systemTime = weather.systemTime;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        systemTimeText.setText(systemTime);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.data.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.type);
            maxText.setText(forecast.high);
            minText.setText(forecast.low);
            forecastLayout.addView(view);
        }
        if (weather.data != null) {
            String quality=weather.data.quality;
            String aqi=String.valueOf((int)(weather.data.forecastList.get(0).aqi));
            String pm25=String.valueOf((int)(weather.data.pm25));
            String pm10=String.valueOf((int)(weather.data.pm10));
            qualityText.setText(quality);
            aqiText.setText(aqi);
            pm25Text.setText(pm25);
            pm10Text.setText(pm10);
        }
        String ganmao = "感冒指数：" + weather.data.ganmao;
        String notice =   weather.data.forecastList.get(0).notice;
        ganmaoText.setText(ganmao);
        noticeText.setText(notice);

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

}

