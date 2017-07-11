package com.example.admin.cryptowatcher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alex on 01.07.2017.
 */

public class Currencies  {

    private String PAIR_NAME = null;
    private Float LAST_BID = null;
    private Float HIGH_BID = null;
    private Float LOW_BID = null;
    private Float AVG = null;
    private Float BUY_PRICE = null;
    private Float SELL_PRICE = null;


    public Currencies(String name,float last, float high, float low, float avg, float buy, float sell){
        PAIR_NAME = name;
        LAST_BID = last;
        HIGH_BID = high;
        LOW_BID = low;
        AVG = avg;
        BUY_PRICE = buy;
        SELL_PRICE = sell;
    }



    public String getPAIR_NAME(){
        return PAIR_NAME;
    }

    public Float getBUY_PRICE() {
        return BUY_PRICE;
    }

    public Float getSELL_PRICE() {
        return SELL_PRICE;
    }

    public float getLAST_BID(){
        return  LAST_BID;
    }
    public float getHIGH_BID(){
        return  HIGH_BID;
    }
    public float getLOW_BID(){
        return  LOW_BID;
    }
    public float getAVG(){
        return  AVG;
    }


}
