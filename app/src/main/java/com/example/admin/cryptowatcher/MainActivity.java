package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
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

public class MainActivity extends Activity {

    private static final String LAST = "last";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String AVEREGE = "avg";

    List<Currencies> API_COLLECTION = new ArrayList<>();

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

        statusField = (TextView) findViewById(R.id.splashProccessText);
        splashImageBtc = (ImageView) findViewById(R.id.splashImage);

        new ShowDialogAsyncTask().execute();


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
                URL url = new URL("https://btc-e.nz/api/3/ticker/btc_usd-btc_rur");

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





            return resultJson;
        }

       /* @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            statusField.setText("Progress: " + values[0]);

            int imgAlphaValue = values[0]*2;

            splashImageBtc.setImageAlpha(imgAlphaValue);


        }*/

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

           // statusField.setText("Parsing results");





            statusField.setText("Done!");






            Log.d(LOG_TAG, strJson);
            //statusField.setText(strJson);
            /*JSONObject dataJsonObj = null;
            String secondName = "";

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray friends = dataJsonObj.getJSONArray("friends");

                // 1. достаем инфо о втором друге - индекс 1
                JSONObject secondFriend = friends.getJSONObject(1);
                secondName = secondFriend.getString("name");
                Log.d(LOG_TAG, "Второе имя: " + secondName);

                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("contacts");

                    String phone = contacts.getString("mobile");
                    String email = contacts.getString("email");
                    String skype = contacts.getString("skype");

                    Log.d(LOG_TAG, "phone: " + phone);
                    Log.d(LOG_TAG, "email: " + email);
                    Log.d(LOG_TAG, "skype: " + skype);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            */






            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);


        }
    }

}
