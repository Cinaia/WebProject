package com.example.admin.cryptowatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 05.07.17.
 */

public class ApiConnect {


    public static final String BTC = "btc_usd";
    public static final String LTC = "ltc_usd";
    public static final String ETH= "eth_usd";
    public static final String NVC = "nvc_usd";

    //json values tags
    public static final String LAST = "last";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String AVERAGE = "avg";
    public static final String BUY = "buy";
    public static final String SELL = "sell";

    public static String API_URL_1 = "https://www.btc-e.nz/api/3";



    public static List<Currencies> API_COLLECTION = new ArrayList<>();






    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";

    public void ApiConnect(){ //add url option to parameters
                              //add boolean "Use proxy"
/*
        try {
            URL url = new URL("https://btc-e.nz/api/3/ticker/btc_usd-ltc_usd-eth_usd-nvc_usd");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(resultJson);


            parseToList(jsonObj.getJSONObject(BTC),BTC);
            parseToList(jsonObj.getJSONObject(LTC),LTC);
            parseToList(jsonObj.getJSONObject(ETH),ETH);
            parseToList(jsonObj.getJSONObject(NVC),NVC);


        } catch (final JSONException e) {

            // statusField.setText("Connection error!");


        }*/

    }



  /*  void parseToList(JSONObject obj, String pairName){

        try {

            float lastValue = Float.parseFloat(obj.getString(LAST));
            float highValue = Float.parseFloat(obj.getString(HIGH));
            float lowValue = Float.parseFloat(obj.getString(LOW));
            float avgValue = Float.parseFloat(obj.getString(AVERAGE));
            float buyPrice = Float.parseFloat(obj.getString(BUY));
            float sellPrice = Float.parseFloat(obj.getString(SELL));

            Currencies pair = new Currencies(pairName,lastValue,highValue,lowValue,avgValue,buyPrice,sellPrice);

            API_COLLECTION.add(pair);

        }catch (final JSONException e){

        }


    }*/


}
