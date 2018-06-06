package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/*
* Данный класс отвечает за логику основного экрана со списком валют и их значений
* Формируется запрос к API coinmarketcap.com , после чего парсим значения и формируем
* ListView с этими данными.
* */

public class HomeActivity extends AppCompatActivity {

    private static final int LIMIT = 30;
    private static final String CURRENCY = "USD";
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=" + LIMIT;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE_BTC = "price_btc";
    private static final String MARKET_CAP_USD = "market_cap_usd";
    private static final String ONE_HOUR_SHIFT = "percent_change_1h";
    private static final String TWENTYFOUR_HOUR_SHIFT = "percent_change_24h";
    private static final String WEEK_CHANGE = "percent_change_7d";
    private static final String DAY_VOLUME = "24h_volume_usd";
    private static final String PRICE = "price_usd";
    private static final String SYMBOL = "symbol";
    private static final String UTC_TIME = "last_updated";
    private Handler handler = new Handler();

   public static ArrayList<Currencies> API_COLLECTION; //Список объектов типа Currencies

    listAdapter listAdapter;

    private static final String TAG = "my_tag_home";
    private SwipeRefreshLayout swipeContainer;

    boolean isFirstOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        //Основной массив с данными.
        API_COLLECTION = new ArrayList<>();

        final ListView lvMain = (ListView) findViewById(R.id.homeList);

        new AsyncTaskRunner(true).execute();

        //Формируем ListView из списка API_COLLECTION и пушим во view
        listAdapter = new listAdapter(this, API_COLLECTION);
        lvMain.setAdapter(listAdapter);

        //Обработка нажатия на валютную пару
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Animation animation1 = new AlphaAnimation(0.7f, 1.0f);
                animation1.setDuration(10);
                animation1.setBackgroundColor(Color.parseColor("#fafafa"));

                view.startAnimation(animation1);

                String pairName = API_COLLECTION.get(position).getABBR();
                //Переходим на DetailActivity
                Intent detailScreen = new Intent(HomeActivity.this, DetailActivity.class);
                detailScreen.putExtra("pairName", pairName);
                Log.d("transitString",pairName + "1");
                startActivity(detailScreen);



            }
        });

        //Обновление свайпом вниз
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setDistanceToTriggerSync(200);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskRunner(false).execute();
                listAdapter.notifyDataSetChanged();
            }
        });
        //Таймер на автоматическое обновление
        handler.postDelayed(runnable, 50000);

    }

    //Метод автоматического обновления
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            new AsyncTaskRunner(true).execute();

            handler.postDelayed(this, 50000);
        }
    };

    //проверка значений API на null
     private String checkIfNull(String value){
         if(value == "null"){
             return "0";
         }else{ return value;}

     }
     //Парсим ответ с API . Значения помещаем в API_COLLECTION
     private void parseToList(JSONObject obj){

        try {
            float oneHourShift = Float.parseFloat(checkIfNull(obj.getString(ONE_HOUR_SHIFT)));
            float twentyFourHourShift = Float.parseFloat(checkIfNull(obj.getString(TWENTYFOUR_HOUR_SHIFT)));
            float dayVolume = Float.parseFloat(checkIfNull(obj.getString(DAY_VOLUME)));
            float buyPrice = Float.parseFloat(checkIfNull(obj.getString(PRICE)));
            double btcPrice = Double.parseDouble(obj.getString(PRICE_BTC));
            float weekChange = Float.parseFloat(checkIfNull(obj.getString(WEEK_CHANGE)));
            long marketCap = (obj.getLong(MARKET_CAP_USD));
            long utcTime = (obj.getLong(UTC_TIME));
            String symbol = obj.getString(SYMBOL);
            String pairName = obj.getString(ID);


            Currencies pair = new Currencies(pairName,oneHourShift,twentyFourHourShift,dayVolume,
                    buyPrice,symbol,btcPrice,marketCap,weekChange,utcTime);//String name,float hourChange, float dayChange, float dayVolume, float currentPrice,
            // String shortName,float btcPrice, long marketCap , float weekChange, String utcTime

            Log.d("JsonP", pair.getPAIR_NAME());
            API_COLLECTION.add(pair);

        }catch (final JSONException e){
            Log.d("eredd", e.toString());
        }
    }

    //Загрузка информации с API в отдельном потоке
    private class AsyncTaskRunner extends AsyncTask<Void, Integer, String> {


        ProgressDialog mProgressDialog;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        private boolean showProgressDialog = true;

        public AsyncTaskRunner(boolean showLoading) {
            super();

            showProgressDialog = showLoading;
        }

        @Override
        protected void onPreExecute() {
            //Очищаем предыдущие данные
            API_COLLECTION.clear();
            //Показываем Loader
            if(showProgressDialog) {
                mProgressDialog = new ProgressDialog(
                        HomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Загружаю. Подождите...");


                mProgressDialog.show();
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            //Обращаемся к API в бэкграунд потоке
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
                Log.d("Fuck","data from API has been obtained");
                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return resultJson;
        }


        @Override
        protected void onPostExecute(String result) {
            //Парсим данные и обновляем ListView
            try {

                JSONArray jsonArr = new JSONArray(resultJson);

                for(int i = 0; i < LIMIT; i++){
                    parseToList(jsonArr.getJSONObject(i));

                }

            }
            catch (final JSONException e){
                Log.d(TAG, "ERROR -" + e);
            }

            if(showProgressDialog) {
                SystemClock.sleep(500);
                mProgressDialog.dismiss();
            }
            swipeContainer.setRefreshing(false);
            listAdapter.notifyDataSetChanged();
        }



    }

    @Override
    protected void onStart() {

       super.onStart();
        Log.d(TAG,"onStart is called");

    }


    @Override
    public void onBackPressed() {
        Log.d("Back pressed","bacccc");
    }

    @Override
    protected void onResume() {
        super.onResume();

            new AsyncTaskRunner(true).execute();
            //listAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
      }

    @Override
    protected void onStop() {
        super.onStop();

    }
    public void onDestroy() {
        super.onDestroy();


    }


}




