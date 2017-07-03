package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    public static final String BTC = "btc_usd";
    public static final String LTC = "ltc_usd";
    public static final String ETH= "eth_usd";
    public static final String NVC = "nvc_usd";


    public static final String LAST = "last";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String AVEREGE = "avg";

    public static List<Currencies> API_COLLECTION = new ArrayList<>();//List for parsed data from API

    public static String LOG_TAG = "my_log";

    ImageView splashImageBtc;
    TextView statusField;

    private ProgressBar spinnerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);


       // getActionBar().setTitle("Текущий курс");
        statusField = (TextView) findViewById(R.id.splashProccessText);
        splashImageBtc = (ImageView) findViewById(R.id.splashImage);

        new ShowDialogAsyncTask().execute();


    }


    void parseToList(JSONObject obj,String pairName){

        try {

            float lastValue = Float.parseFloat(obj.getString(LAST));
            float highValue = Float.parseFloat(obj.getString(HIGH));
            float lowValue = Float.parseFloat(obj.getString(LOW));
            float avgValue = Float.parseFloat(obj.getString(AVEREGE));

            Currencies pair = new Currencies(pairName,lastValue,highValue,lowValue,avgValue);

            API_COLLECTION.add(pair);

        }catch (final JSONException e){

        }


    }

    private class ShowDialogAsyncTask extends AsyncTask<Void , Integer, String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        boolean isLoaded = false;
        int i = 1;

        void setProgStatus(int alpha){
            splashImageBtc.setImageAlpha(alpha);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            statusField.setText("Connecting..");

        }

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


                parseToList(jsonObj.getJSONObject(BTC),BTC);
                parseToList(jsonObj.getJSONObject(LTC),LTC);
                parseToList(jsonObj.getJSONObject(ETH),ETH);
                parseToList(jsonObj.getJSONObject(NVC),NVC);


            } catch (final JSONException e) {

               // statusField.setText("Connection error!");


            }

            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            statusField.setText("Done!");

            Intent intent = new Intent(MainActivity.this,HomeActivity.class);

            startActivity(intent);


            Log.d(LOG_TAG, strJson);

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
