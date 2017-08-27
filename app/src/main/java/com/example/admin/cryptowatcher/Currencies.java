package com.example.admin.cryptowatcher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alex on 01.07.2017.
 */

public class Currencies  {

    private String PAIR_NAME = null;
    private Float HOUR_CHANGE = null;
    private Float DAY_CHANGE = null;
    private Float DAY_VOL = null;
    private Float PRICE = null;


    public Currencies(String name,float hourChange, float dayChange, float dayVolume, float currentPrice){
        PAIR_NAME = name;
        HOUR_CHANGE = hourChange;
        DAY_CHANGE = dayChange;
        DAY_VOL = dayVolume;
        PRICE = currentPrice;

    }



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



}
