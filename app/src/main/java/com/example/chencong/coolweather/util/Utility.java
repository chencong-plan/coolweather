package com.example.chencong.coolweather.util;

import android.text.TextUtils;

import com.example.chencong.coolweather.db.CoolWeatherDB;
import com.example.chencong.coolweather.model.City;
import com.example.chencong.coolweather.model.County;
import com.example.chencong.coolweather.model.Province;

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
}

//提供三个方法,boolean类型
//handleProvincesResponse()   处理省级数据
//handleCitiesResponse()      处理市级数据
//handleCountiesResponse()    处理县级数据
//处理方法：
//1、先用逗号隔开，再用单竖线隔开
//2、接着讲解析出来的数据设置到实体中
//2、调用CoolWeatherDB中的三个save()方法将数据存储到相应的表中