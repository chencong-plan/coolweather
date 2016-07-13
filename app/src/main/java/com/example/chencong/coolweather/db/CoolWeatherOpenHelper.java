package com.example.chencong.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chencong on 2016/7/12.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

    //Province表创建语句
    public static final String CREATE_PROVINCE = "create table Province("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code text)";

    //City表创建语句
    public  static final String CREATE_CITY = "create table City("
            +"id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer)";

    //County表创建
    public static final String CREATE_COUNTY = "create table County("
            + "id integer primary key autoincrement,"
            + "county_name text,"
            + "county_code text,"
            + "city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);  //创建Province表
        db.execSQL(CREATE_CITY);      //创建City表
        db.execSQL(CREATE_COUNTY);    //创建County表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}





/*
    表Province
    create table Province(
        id integer primary key autoincrement,
        province_name text;
        province_code text)
    )

    表City
    create table City(
        id integer primary key autoincrement,
        city_name text,
        city_code text,
        province_id integer;
    )

    表County
    create table County(
        id integer primary key autoincrement,
        county_name text,
        county_code text,
        city_id text
    )
 */