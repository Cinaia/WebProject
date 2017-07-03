package com.example.admin.cryptowatcher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class HomeActivity extends AppCompatActivity {



    TextView btc;
    TextView ltc;
    TextView eth;
    TextView nvc;
    public ProgressDialog progDialog;
    private static final String TAG = "my_tag_home";
    boolean isFirstOpen = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        btc = (TextView)findViewById(R.id.btcVal);
        ltc =(TextView)findViewById(R.id.ltcVal);
        eth = (TextView)findViewById(R.id.ethVal);
        nvc = (TextView)findViewById(R.id.nvcVal);



        showRates();

    }

    private void showRates(){
        for (Currencies obj: MainActivity.API_COLLECTION){
            if (obj.getPAIR_NAME() == MainActivity.BTC){
                btc.setText( Float.toString(obj.getLAST_BID()) + " USD");
            }else if (obj.getPAIR_NAME() == MainActivity.LTC){
                ltc.setText(Float.toString(obj.getLAST_BID()) + " USD");
            }else if (obj.getPAIR_NAME() == MainActivity.ETH){
                eth.setText(Float.toString(obj.getLAST_BID()) + " USD");
            }else if(obj.getPAIR_NAME() == MainActivity.NVC){
                nvc.setText(Float.toString(obj.getLAST_BID()) + " USD");
            }
        }
    }
    private class AsyncTaskRunner extends AsyncTask<Void , Integer, String> {

        private String resp;
        ProgressDialog mProgressDialog;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {

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


                parseToList(jsonObj.getJSONObject(MainActivity.BTC),MainActivity.BTC);
                parseToList(jsonObj.getJSONObject(MainActivity.LTC),MainActivity.LTC);
                parseToList(jsonObj.getJSONObject(MainActivity.ETH),MainActivity.ETH);
                parseToList(jsonObj.getJSONObject(MainActivity.NVC),MainActivity.NVC);


            } catch (final JSONException e) {

                // statusField.setText("Connection error!");


            }
            SystemClock.sleep(1000);
            return resultJson;
        }


        @Override
        protected void onPostExecute(String result) {

            showRates();
            mProgressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {

            MainActivity.API_COLLECTION.clear();


            mProgressDialog = new ProgressDialog(
                    HomeActivity.this,R.style.Theme_AppCompat_DayNight_Dialog);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Загружаю. Подождите...");

            mProgressDialog.show();

        }


    }
    void parseToList(JSONObject obj, String pairName){

        try {

            float lastValue = Float.parseFloat(obj.getString(MainActivity.LAST));
            float highValue = Float.parseFloat(obj.getString(MainActivity.HIGH));
            float lowValue = Float.parseFloat(obj.getString(MainActivity.LOW));
            float avgValue = Float.parseFloat(obj.getString(MainActivity.AVEREGE));

            Currencies pair = new Currencies(pairName,lastValue,highValue,lowValue,avgValue);

            MainActivity.API_COLLECTION.add(pair);

        }catch (final JSONException e){

        }


    }

    @Override
    protected void onStart() {

        super.onStart();
        if(isFirstOpen) {

            isFirstOpen = false;
        } else {
            new AsyncTaskRunner().execute();
        }

    }

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

