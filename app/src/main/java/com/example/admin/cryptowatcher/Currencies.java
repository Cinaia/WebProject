package com.example.admin.cryptowatcher;

/**
 * Created by Alex on 01.07.2017.
 */

public class Currencies {

    private String PAIR_NAME = null;
    private Double LAST_BID = null;
    private Double HIGH_BID = null;
    private Double LOW_BID = null;
    private Double AVG = null;

    public Currencies(String name,Double last, double high, double low, double avg){
        PAIR_NAME = name;
        LAST_BID = last;
        HIGH_BID = high;
        LOW_BID = low;
        AVG = avg;
    }

    public String getPAIR_NAME(){
        return PAIR_NAME;
    }
    public double getLAST_BID(){
        return  LAST_BID;
    }
    public double getHIGH_BID(){
        return  HIGH_BID;
    }
    public double getLOW_BID(){
        return  LOW_BID;
    }
    public double getAVG(){
        return  AVG;
    }

}
