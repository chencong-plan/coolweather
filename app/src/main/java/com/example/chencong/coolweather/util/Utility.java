package com.example.chencong.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.chencong.coolweather.db.CoolWeatherDB;
import com.example.chencong.coolweather.model.City;
import com.example.chencong.coolweather.model.County;
import com.example.chencong.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chencong on 2016/7/13.
 */
//由于服务器返回的数据库为“代号|城市”这种格式的，所以需要提供包工具类来解析处理这种数据
public class Utility {
    /*
    * 解析和处理服务器返回的省级数据*/
    public synchronized  static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response){
        if (!TextUtils.isEmpty(response)){
            String [] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length>0){
                for (String p : allProvinces){
                    String [] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析的数据存到表Province中
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /*
    * 解析和处理市级数据*/
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        if (! TextUtils.isEmpty(response)){
            String [] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出来的数据存储到表City中
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /*
    * 解析和处理县级数据*/
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if (! TextUtils.isEmpty(response)){
            String [] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0){
                for (String c : allCounties){
                    String [] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    //将解析的数据存储到表County中
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /*
    * 解析服务器返回饿JSON数据，并将解析的数据存到本地*/
    public static void handleWeatherRespone(Context context,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherInfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");

            //调用方法saveWeatherInfo(),将天气信息写入到SharePreference文件中
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /*
    * 将服务器返回的所有天气数据存储到SharedPreference文件中*/
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}

//提供三个方法,boolean类型
//handleProvincesResponse()   处理省级数据
//handleCitiesResponse()      处理市级数据
//handleCountiesResponse()    处理县级数据
//处理方法：
//1、先用逗号隔开，再用单竖线隔开
//2、接着讲解析出来的数据设置到实体中
//2、调用CoolWeatherDB中的三个save()方法将数据存储到相应的表中