package com.example.chencong.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chencong.coolweather.R;
import com.example.chencong.coolweather.util.HttpCallbackListener;
import com.example.chencong.coolweather.util.HttpUtil;
import com.example.chencong.coolweather.util.Utility;

/**
 * Created by chencong on 2016/7/14.
 */
public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;   //显示城市名
    private TextView publishText;   //显示发布时间
    private TextView weatherDespText;  //显示天气描述信息
    private TextView temp1Text;   //显示气温1
    private TextView temp2Text;     //显示气温2
    private TextView currentDateText;     //显示当前时间日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        //初始化各种控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        String countycode = getIntent().getStringExtra("county_code");   //从Intent中取出县级代号
        if (!TextUtils.isEmpty(countycode)){
            //有县级代码就去查询天气
            publishText.setText("天气正在同步中.....");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countycode);
        }else{
            //没有县级代号就显示本地天气
            showWeather();
        }
    }

    /*拿着县级代号查询天气代号*/
    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
    }
    /*拿着天气代号查询天气信息*/
    private  void  queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    /*
    * 根据传入的地址和类型向服务器查询天气代号或者天气信息*/
    private  void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish( final  String response) {
                if ("countyCode".equals(type)){
                    if (!TextUtils.isEmpty(response)){
                        //从服务器返回的数据中解析出天气代号
                        String [] array = response.split("\\|");
                        if (array !=null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }else if ("weatherCode".equals(type)){
                        //处理服务器返回的天气信息
                        Utility.handleWeatherRespone(WeatherActivity.this,response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败...(。・_・)/~~~");
                    }
                });
            }
        });
    }

    /*
    * 从SharedPreferences文件中读取的天气信息，并显示到界面上*/
    private  void  showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
