package com.example.chencong.coolweather.util;

/**
 * Created by chencong on 2016/7/13.
 */
public interface HttpCallbackListener {
    //申明接口HttpCallbackListener在HttpUtil中引用
    void  onFinish(String response);
    void onError(Exception e);
}
