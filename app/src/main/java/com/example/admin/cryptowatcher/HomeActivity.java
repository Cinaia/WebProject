package com.example.admin.cryptowatcher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.cryptowatcher.MainActivity.BUY;
import static com.example.admin.cryptowatcher.R.id.textView;

public class HomeActivity extends AppCompatActivity {


    //******Init textView fields********
    TextView btc;
    TextView ltc;
    TextView eth;
    TextView nvc;



    //public ProgressDialog progDialog;


    private static final String TAG = "my_tag_home";                                               //tag for logs


    boolean isFirstOpen = true;                                                                    //checks if activity is opened of the first time


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        btc = (TextView)findViewById(R.id.btcVal);
        ltc =(TextView)findViewById(R.id.ltcVal);
        eth = (TextView)findViewById(R.id.ethVal);
        nvc = (TextView)findViewById(R.id.nvcVal);


        //set on click listener for textfields
        showRates();                                                                               // push values from API_COLLECTION to textView fields

        btc.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                String pairName = "btc";
                Intent detailScreen = new Intent(HomeActivity.this, DetailActivity.class);
                detailScreen.putExtra("pairName",pairName);
                startActivity(detailScreen);
            }

        });
    }

    private void showRates(){                                                                      //formatting and pushing values from API_COLLECTION to the text fields
        for (Currencies obj: MainActivity.API_COLLECTION){
            if (obj.getPAIR_NAME().equals(MainActivity.BTC)){
                btc.setText(String.format("%s USD", Float.toString(obj.getLAST_BID())));
            }else if (obj.getPAIR_NAME().equals(MainActivity.LTC)){
                ltc.setText(String.format("%s USD", Float.toString(obj.getLAST_BID())));
            }else if (obj.getPAIR_NAME().equals(MainActivity.ETH)){
                eth.setText(String.format("%s USD", Float.toString(obj.getLAST_BID())));
            }else if(obj.getPAIR_NAME().equals(MainActivity.NVC)){
                nvc.setText(String.format("%s USD", Float.toString(obj.getLAST_BID())));
            }
        }
    }


    void parseToList(JSONObject obj, String pairName){                                             //API parsing and filling in the API_COLLECTION list

        try {                                                                                      //extractin values from JSON file and push them to the API_COLLECTION

            float lastValue = Float.parseFloat(obj.getString(MainActivity.LAST));
            float highValue = Float.parseFloat(obj.getString(MainActivity.HIGH));
            float lowValue = Float.parseFloat(obj.getString(MainActivity.LOW));
            float avgValue = Float.parseFloat(obj.getString(MainActivity.AVERAGE));
            float buyPrice = Float.parseFloat(obj.getString(MainActivity.BUY));
            float sellPrice = Float.parseFloat(obj.getString(MainActivity.SELL));

            Currencies pair = new Currencies(pairName,lastValue,highValue,lowValue,avgValue,buyPrice,sellPrice);

            MainActivity.API_COLLECTION.add(pair);

        }catch (final JSONException e){

        }


    }

    private class AsyncTaskRunner extends AsyncTask<Void , Integer, String> {


        ProgressDialog mProgressDialog;     //progress dialog init for updating rates'values


        //*******Url init block********

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {

                URL url = new URL("https://btc-e.nz/api/3/ticker/btc_usd-ltc_usd-eth_usd-nvc_usd");//URL address for API

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

            try {                                                                                  //parsing API request result


                JSONObject jsonObj = new JSONObject(resultJson);


                parseToList(jsonObj.getJSONObject(MainActivity.BTC),MainActivity.BTC);
                parseToList(jsonObj.getJSONObject(MainActivity.LTC),MainActivity.LTC);
                parseToList(jsonObj.getJSONObject(MainActivity.ETH),MainActivity.ETH);
                parseToList(jsonObj.getJSONObject(MainActivity.NVC),MainActivity.NVC);


            } catch (final JSONException e) {

               //add err handler


            }
            //SystemClock.sleep(1000);
            return resultJson;
        }


        @Override
        protected void onPostExecute(String result) {

            showRates();                                                                           //push values to textView fields

            mProgressDialog.dismiss();                                                             //hide ProgressDialog
        }


        @Override
        protected void onPreExecute() {

            MainActivity.API_COLLECTION.clear();                                                   //clear previous values


            mProgressDialog = new ProgressDialog(
                    HomeActivity.this,R.style.Theme_AppCompat_DayNight_Dialog);                    //set style for progressDialog
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                        //set spinner
            mProgressDialog.setMessage("Загружаю. Подождите...");

            mProgressDialog.show();

        }


    }


    @Override
    protected void onStart() {

        super.onStart();
        if(isFirstOpen) {                                                                          //if activity is first created

            isFirstOpen = false;
        } else {                                                                                   //if activity called again , update rate values
            new AsyncTaskRunner().execute();
        }

    }

    //on back button pressed
    //consider toast message instead of alertDialog!!
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setIcon(R.mipmap.logo)
                .setTitle("Выход")
                .setMessage("Покинуть приложение?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstOpen) {                                                                          //if activity is first created

            isFirstOpen = false;
        } else {                                                                                   //if activity called again , update rate values
            new AsyncTaskRunner().execute();
        }


        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }

}

