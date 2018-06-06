package com.example.admin.cryptowatcher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс для описания объекта API ( Валютной пары)
 */

public class Currencies  {

    private String PAIR_NAME = null;
    private Float HOUR_CHANGE = null;
    private Float DAY_CHANGE = null;
    private Float DAY_VOL = null;
    private Float PRICE = null;
    private String ABBR = null;
    private double PRICE_BTC ;
    private long MARKET_CAP_USD;
    private Float WEEK_CHANGE = null;
    private long UTC_TIME;


    public Currencies(String name,float hourChange, float dayChange, float dayVolume, float currentPrice,
                      String shortName,double btcPrice, long marketCap , float weekChange, long utcTime){
        PAIR_NAME = name;
        HOUR_CHANGE = hourChange;
        DAY_CHANGE = dayChange;
        DAY_VOL = dayVolume;
        PRICE = currentPrice;
        ABBR = shortName;
        PRICE_BTC = btcPrice;
        MARKET_CAP_USD = marketCap;
        WEEK_CHANGE = weekChange;
        UTC_TIME = utcTime;

    }


    public String getABBR() {return  ABBR;}
    public String getPAIR_NAME(){return PAIR_NAME;}
    public Float getPRICE() {
        return PRICE;
    }
    public Float getHOUR_CHANGE() {
        return HOUR_CHANGE;
    }
    public float getDAY_CHANGE(){ return  DAY_CHANGE; }
    public float getDAY_VOL(){
        return  DAY_VOL;
    }

    public double getPRICE_BTC() {
        return PRICE_BTC;
    }

    public Float getWEEK_CHANGE() {
        return WEEK_CHANGE;
    }

    public long getMARKET_CAP_USD() {
        return MARKET_CAP_USD;
    }

    public long getUTC_TIME() {
        return UTC_TIME;
    }
}
