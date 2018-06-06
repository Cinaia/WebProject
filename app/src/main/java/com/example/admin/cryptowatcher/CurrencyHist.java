package com.example.admin.cryptowatcher;

/**
 * Класс для описания точек на графике
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
