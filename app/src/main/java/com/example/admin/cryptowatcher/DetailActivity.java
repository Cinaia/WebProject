package com.example.admin.cryptowatcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.math.BigDecimal;
import android.icu.text.DateFormat;
import android.icu.text.DateFormatSymbols;
import android.icu.text.DateIntervalFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alex on 11.07.2017.
 */

public class DetailActivity extends AppCompatActivity {

    boolean isFirstOpen = true;

    public static ArrayList<CurrencyHist> HISTORICAL_DATA = new ArrayList<>();
    private static final String TAG = "my_deta";

    TextView hourDetailVal;
    TextView dayDetailVal;
    TextView weekDetailVal;
    TextView volumeDetailVal;
    TextView btcPriceVal;
    TextView utcTimeVal;
    TextView pairNameText;
    TextView priceVal;

    public String BASE_URL = null;
    public static  String fsym = null;//show this currency
    public String tsym = "USD";//in this value
    public String limitPeriod = "30";//period of time to gather data
    public String aggregate = "1";// interval
    public String market = "CCCAGG";// take values from
    public static String ttt = null;
    public static final String ARRAY_TAG = "Data";

    private static final String timeMark = "time";//json tag for utc time
    private static final String priceMark = "close";//json tag for price

    public GraphView graph;


    /**
     * calendar to avoid creating new date objects
     */


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_LEFT_ICON);
       // requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.detail_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("List Activity");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });


        hourDetailVal = (TextView) findViewById(R.id.hourDetailVal);
        dayDetailVal = (TextView) findViewById(R.id.dayDetailVal);
        weekDetailVal = (TextView) findViewById(R.id.weekDetailVal);
        volumeDetailVal = (TextView) findViewById(R.id.volumeDetailVal);
        btcPriceVal = (TextView) findViewById(R.id.btcPriceVal);
        utcTimeVal = (TextView) findViewById(R.id.utcTimeText);
        pairNameText = (TextView) findViewById(R.id.pairNameText);
        priceVal = (TextView) findViewById(R.id.priceVal);
        GraphView graph = (GraphView) findViewById(R.id.graph);


        //https://api.coindesk.com/v1/bpi/historical/close.json?start=2017-09-04&end=2017-09-11
        //https://poloniex.com/public?command=returnChartData&currencyPair=USDT_XRP&start=1499999999&end=1505154320&period=14400










        Intent intent = getIntent();
        String inte = intent.getStringExtra("pairName");
        Log.d(TAG, inte);

        
        for (Currencies obj: MainActivity.API_COLLECTION){
            if (obj.getPAIR_NAME().equals(inte)){



               fsym = obj.getABBR().toUpperCase();
                Log.d("Graph" , fsym);
                new getDataAsync(fsym).execute();

                pairNameText.setText(obj.getPAIR_NAME().toUpperCase());

                if(obj.getHOUR_CHANGE() > 0) {
                    hourDetailVal.setText("+" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FF99cc00"));
                }else{
                    hourDetailVal.setText("" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FFFF4444"));
                }

                if(obj.getDAY_CHANGE() > 0) {
                    dayDetailVal.setText("+" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FF99cc00"));
                }else{
                    dayDetailVal.setText("" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FFFF4444"));
                }

                if(obj.getWEEK_CHANGE() > 0) {
                    weekDetailVal.setText("+" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FF99cc00"));
                }else{
                    weekDetailVal.setText("" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FFFF4444"));
                }

                priceVal.setText(obj.getPRICE() + " USD");


                volumeDetailVal.setText("" + NumberFormat.getNumberInstance(Locale.US).format(obj.getMARKET_CAP_USD()) + " USD");
                btcPriceVal.setText("" + obj.getPRICE_BTC() + " BTC");

                Date date = new Date(obj.getUTC_TIME() * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);
                utcTimeVal.setText(formattedDate);

               // Log.d("Graph", HISTORICAL_DATA.get(0).getCloseValue() + "");

            }
        }

              //  Log.d("Graph", HISTORICAL_DATA.get(0).getCloseValue() + "");
              /*  */


    }

    public void initializeLineGraphView() {




    }
    public static void parseHistory(JSONObject object) throws JSONException {

        long utcTime = (object.getLong(timeMark));

        float priceAt = Float.parseFloat(object.getString(priceMark));


        CurrencyHist temp = new CurrencyHist(utcTime,priceAt);
        HISTORICAL_DATA.add(temp);

    }
    private class getDataAsync extends AsyncTask<Void , Integer, String> {

        private String fsymAsync = null;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        public getDataAsync(String pairName) {
            super();
            fsymAsync = pairName;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        //boolean isLoaded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HISTORICAL_DATA.clear();
            BASE_URL = "https://min-api.cryptocompare.com/data/histoday?fsym=" + fsymAsync + "&tsym=" + tsym + "&limit="+ limitPeriod +"&aggregate="+ aggregate + "&e=" + market;

        }


        @Override
        protected String doInBackground(Void... params) {

            try {

                URL url = new URL(BASE_URL);

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                JSONObject histObj = new JSONObject(strJson);
                JSONArray jsonArr = histObj.getJSONArray(ARRAY_TAG);

                int l = Integer.parseInt(limitPeriod);
                Log.d("Graph", fsymAsync);
                for(int i = 0; i < l +1; i++){
                       parseHistory(jsonArr.getJSONObject(i));
                }

            } catch (final JSONException e) {

            }

            for(CurrencyHist obj : HISTORICAL_DATA){
                Log.d("Graph", obj.getUtcTime() + " : " + obj.getCloseValue() + "");
            }


            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {


                    new DataPoint(HISTORICAL_DATA.get(0).getUtcTime(), HISTORICAL_DATA.get(0).getCloseValue())//(int)HISTORICAL_DATA.get(0).getUtcTime()

            });
            for(int i = 1; i < HISTORICAL_DATA.size();i++){



                //Date date = new Date(HISTORICAL_DATA.get(i).getUtcTime());

               // date.setTime(HISTORICAL_DATA.get(i).getUtcTime());

               Log.d("time1",HISTORICAL_DATA.get(i).getUtcTime() + "");

                DataPoint kek = new DataPoint(HISTORICAL_DATA.get(i).getUtcTime(), HISTORICAL_DATA.get(i).getCloseValue());
                series.appendData(kek,false,HISTORICAL_DATA.size());
            }

            series.setDrawDataPoints(true);
            series.setDataPointsRadius(5);
            series.setThickness(3);


            graph.setTitle("Месячный график");

            // graph.getGridLabelRenderer().setHumanRounding(false);

            graph.getViewport().setMinX((int)HISTORICAL_DATA.get(0).getUtcTime());

            graph.getViewport().setMaxX((int)HISTORICAL_DATA.get(HISTORICAL_DATA.size()-1).getUtcTime());

            double minY =  HISTORICAL_DATA.get(0).getCloseValue() - (HISTORICAL_DATA.get(0).getCloseValue()/100)*20;
            graph.getViewport().setMinY(minY);

            graph.getViewport().setScrollable(true);
            graph.getViewport().setScalable(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setNumHorizontalLabels(HISTORICAL_DATA.size()/2);
            graph.getGridLabelRenderer().setNumVerticalLabels(7);
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
            graph.getGridLabelRenderer().setTextSize(27.3f);
            graph.getGridLabelRenderer().setHighlightZeroLines(true);


            //graph.getGridLabelRenderer().setLabelsSpace(30);
           // graph.getGridLabelRenderer().setHorizontalAxisTitle("Дата");
            //graph.getGridLabelRenderer().setVerticalAxisTitle("$");


            graph.setHorizontalScrollBarEnabled(true);

            graph.addSeries(series);



            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){




                @Override
                public String formatLabel(double value, boolean isValueX){

                    if(isValueX){

                        value = (long)value;
                        Date date = new Date((long) (value * 1000));
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd");
                        String formattedDate = "      " + sdf.format(date);

                        return formattedDate;
                    }else{
                        Log.d("time4", value + "");
                        return super.formatLabel(value, isValueX);
                    }
                }



            });


           //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        }
    }


    @Override
    protected void onStart() {

        super.onStart();
        if(isFirstOpen) {                                                                          //if activity is first created

            isFirstOpen = false;
        } else {                                                                                   //if activity called again , update rate values
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
        }

    }
}

