package com.example.admin.cryptowatcher;

/**
 * Created by Alex on 12.09.2017.
 */

public class CurrencyHist {

    private long utcTime;
    private float close;

    public CurrencyHist(long time, float value){

        utcTime = time;
        close = value;

    }

    public long getUtcTime(){
        return utcTime;
    }
    public float getCloseValue(){
        return close;
    }


}
