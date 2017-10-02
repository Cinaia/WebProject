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

    public static final int LIMIT = 12;
    public static final String CURRENCY = "USD";
    public static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=" + LIMIT;


    //json values tags
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRICE_BTC = "price_btc";
    public static final String MARKET_CAP_USD = "market_cap_usd";
    public static final String ONE_HOUR_SHIFT = "percent_change_1h";
    public static final String TWENTYFOUR_HOUR_SHIFT = "percent_change_24h";
    public static final String WEEK_CHANGE = "percent_change_7d";
    public static final String DAY_VOLUME = "24h_volume_usd";
    public static final String PRICE = "price_usd";
    public static final String SYMBOL = "symbol";
    public static final String UTC_TIME = "last_updated";

    public static ArrayList<Currencies> API_COLLECTION; //List for parsed data from API

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

        Log.d("eredd", "onCreate method is called");
        API_COLLECTION = new ArrayList<>();
        statusField = (TextView) findViewById(R.id.splashProccessText);
        splashImageBtc = (ImageView) findViewById(R.id.splashImage);
        spinnerBar = (ProgressBar) findViewById(R.id.progressBar);
        //SystemClock.sleep(500);

        if(isNetworkConnected()){
            new ShowDialogAsyncTask().execute();
        }else{
            statusField.setText("Connection failed!");
        }
    }


   public static void parseToList(JSONObject obj){

        try {


            float oneHourShift = Float.parseFloat(obj.getString(ONE_HOUR_SHIFT));
            float twentyFourHourShift = Float.parseFloat(obj.getString(TWENTYFOUR_HOUR_SHIFT));
            float dayVolume = Float.parseFloat(obj.getString(DAY_VOLUME));
            float buyPrice = Float.parseFloat(obj.getString(PRICE));
            double btcPrice = Double.parseDouble(obj.getString(PRICE_BTC));
            float weekChange = Float.parseFloat(obj.getString(WEEK_CHANGE));
            long marketCap = (obj.getLong(MARKET_CAP_USD));
            long utcTime = (obj.getLong(UTC_TIME));
            String symbol = obj.getString(SYMBOL);
            String pairName = obj.getString(ID);



            Log.d(LOG_TAG, obj.getString(DAY_VOLUME));
            Currencies pair = new Currencies(pairName,oneHourShift,twentyFourHourShift,dayVolume,
                    buyPrice,symbol,btcPrice,marketCap,weekChange,utcTime);//String name,float hourChange, float dayChange, float dayVolume, float currentPrice,
           // String shortName,float btcPrice, long marketCap , float weekChange, String utcTime

            API_COLLECTION.add(pair);

        }catch (final JSONException e){
            Log.d("eredd", e.toString());
        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("eredd", "isConnected method is called");
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

            statusField.setText("Connecting..");//H--C

        }

        @Override
        protected String doInBackground(Void... params) {


            Log.d("eredd", "doing");
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


                for(int i = 0; i < LIMIT ; i++){
                    parseToList(jsonArr.getJSONObject(i));
                }

            } catch (final JSONException e) {

               // statusField.setText("Connection error!");

            }
            Log.d("eredd", resultJson);
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
           // Log.d("eredd", strJson);
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
