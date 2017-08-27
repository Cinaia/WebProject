package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.json.JSONObject.NULL;

public class MainActivity extends Activity {

    public static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=10";


    public static final int BTC_ID = 0;
    public static final int ETH_ID = 1;
    public static final int BTCC_ID = 2;
    public static final int LTC_ID = 4;
    //json obj tags
    public static final String BTC = "bitcoin";
    public static final String LTC = "litecoin";
    public static final String ETH= "ethereum";
    public static final String BTCC = "bitcoin-cash";

    //json values tags
    public static final String ID = "id";
    public static final String ONE_HOUR_SHIFT = "percent_change_1h";
    public static final String TWENTYFOUR_HOUR_SHIFT = "percent_change_24h";
    public static final String DAY_VOLUME = "24h_volume_usd";
    public static final String PRICE = "price_usd";


    public static List<Currencies> API_COLLECTION = new ArrayList<>();//List for parsed data from API

    public static String LOG_TAG = "my_log";

    ImageView splashImageBtc;
    TextView statusField;

    ProgressBar spinnerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);



        statusField = (TextView) findViewById(R.id.splashProccessText);
        splashImageBtc = (ImageView) findViewById(R.id.splashImage);
        spinnerBar = (ProgressBar) findViewById(R.id.progressBar);
        SystemClock.sleep(500);

        if(isNetworkConnected()){
            new ShowDialogAsyncTask().execute();
        }else{
            statusField.setText("Connection failed!");
        }
    }


   public static void parseToList(JSONObject obj,String pairName){

        try {
            Log.d(LOG_TAG, "parseToList is called for the " + pairName);

            float oneHourShift = Float.parseFloat(obj.getString(ONE_HOUR_SHIFT));
            float twentyFourHourShift = Float.parseFloat(obj.getString(TWENTYFOUR_HOUR_SHIFT));
            float dayVolume = Float.parseFloat(obj.getString(DAY_VOLUME));
            float buyPrice = Float.parseFloat(obj.getString(PRICE));

            Log.d(LOG_TAG, obj.getString(PRICE));
            Log.d(LOG_TAG,obj.getString(DAY_VOLUME));
            Currencies pair = new Currencies(pairName,oneHourShift,twentyFourHourShift,dayVolume,buyPrice);//String name,float hourChange, float dayChange, float dayVolume, float currentPrice

            API_COLLECTION.add(pair);

        }catch (final JSONException e){

        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private class ShowDialogAsyncTask extends AsyncTask<Void , Integer, String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        //boolean isLoaded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            statusField.setText("Connecting..");

        }

        @Override
        protected String doInBackground(Void... params) {



            try {
                URL url = new URL(API_URL);

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
                JSONArray jsonArr = new JSONArray(resultJson);

                parseToList(jsonArr.getJSONObject(BTC_ID),BTC);
                parseToList(jsonArr.getJSONObject(LTC_ID),LTC);
                parseToList(jsonArr.getJSONObject(ETH_ID),ETH);
                parseToList(jsonArr.getJSONObject(BTCC_ID),BTCC);


            } catch (final JSONException e) {

               // statusField.setText("Connection error!");

            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            statusField.setText("Done!");

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                startActivity(intent);

            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
