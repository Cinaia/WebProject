package com.example.admin.cryptowatcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by Alex on 11.07.2017.
 * H--C means that smth needs to get hardcoded!!!
 */

public class DetailActivity extends AppCompatActivity  {

    boolean isFirstOpen = true;

    public static ArrayList<CurrencyHist> HISTORICAL_DATA = new ArrayList<>();
    public static final String TAG = "my_deta";

    TextView hourDetailVal;
    TextView dayDetailVal;
    TextView weekDetailVal;
    TextView volumeDetailVal;
    TextView btcPriceVal;
    TextView utcTimeVal;
    TextView pairNameText;
    TextView priceVal;
    TextView graphErrorText;
    ImageView graphErrorImg;

    public static final String GET_REPSONSE = "Response";
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

    GraphView graphAsync;
    MaterialSpinner materialSpinnerGraph;
    ArrayAdapter spinnerOptions;
    private String pairNameRecieved = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_info);
        //make toolbar grate again
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("List Activity");//H--C
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        //yep , setting a bunch of elements from the layout
        graphErrorText = (TextView) findViewById(R.id.graphErrorText);
        hourDetailVal = (TextView) findViewById(R.id.hourDetailVal);
        dayDetailVal = (TextView) findViewById(R.id.dayDetailVal);
        weekDetailVal = (TextView) findViewById(R.id.weekDetailVal);
        volumeDetailVal = (TextView) findViewById(R.id.volumeDetailVal);
        btcPriceVal = (TextView) findViewById(R.id.btcPriceVal);
        pairNameText = (TextView) findViewById(R.id.pairNameText);
        priceVal = (TextView) findViewById(R.id.priceVal);

        graphErrorImg = (ImageView) findViewById(R.id.graphErrorImg);


        materialSpinnerGraph = (MaterialSpinner) findViewById(R.id.materialSpinnerGraph);
        materialSpinnerGraph.setItems("Месячный график", "Недельный график", "Дневной график");

        materialSpinnerGraph.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                new getDataAsync(pairNameRecieved.toUpperCase(),position + 1).execute();
            }
        });

        graphAsync = (GraphView) findViewById(R.id.graph);

        //getting data from previous activity
        Intent intent = getIntent();
        String pairNameData = intent.getStringExtra("pairName");

        
        for (Currencies obj: MainActivity.API_COLLECTION){ //passing through data array and finding our needed currency pair
            if (obj.getPAIR_NAME().equals(pairNameData)){

                fsym = obj.getABBR().toUpperCase();

                pairNameRecieved = fsym; //set the pair symbol name
                new getDataAsync(pairNameRecieved.toUpperCase(), 1).execute();
                pairNameText.setText(obj.getPAIR_NAME().toUpperCase());//API loves Upper-case

                if(obj.getHOUR_CHANGE() > 0) {
                    hourDetailVal.setText("+" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                }else{
                    hourDetailVal.setText("" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                if(obj.getDAY_CHANGE() > 0) {
                    dayDetailVal.setText("+" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                }else{
                    dayDetailVal.setText("" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                if(obj.getWEEK_CHANGE() > 0) {
                    weekDetailVal.setText("+" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                }else{
                    weekDetailVal.setText("" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                priceVal.setText(obj.getPRICE() + " USD");//H--C

                volumeDetailVal.setText("" + NumberFormat.getNumberInstance(Locale.US).format(obj.getMARKET_CAP_USD()) + " USD");//H--C

                btcPriceVal.setText("" + String.format("%.10f", obj.getPRICE_BTC()) + " BTC");//H--C

            }
        }

    }
/*
    TextView spinnerDialogText = (TextView) view;

       new getDataAsync(pairNameRecieved.toUpperCase(),position + 1).execute();



    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int position , long id){
    TextView spinnerDialogText = (TextView) view;

       new getDataAsync(pairNameRecieved.toUpperCase(),position + 1).execute();
        Log.d("pairNameTest", position + "pos");
        Log.d("pairNameTest", id + "id");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "nothing selected", Toast.LENGTH_SHORT).show();
    }*/

    public void initializeLineGraphView(GraphView graph, int periodOfTime, boolean ifSuccess) {
        if(ifSuccess) {
            try {
                graph.removeAllSeries();
                graph.refreshDrawableState();

                final int pot = periodOfTime;

                //this could be written better
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                        new DataPoint(HISTORICAL_DATA.get(0).getUtcTime(), HISTORICAL_DATA.get(0).getCloseValue())//(int)HISTORICAL_DATA.get(0).getUtcTime()

                });
                for (int i = 1; i < HISTORICAL_DATA.size(); i++) {

                    DataPoint kek = new DataPoint(HISTORICAL_DATA.get(i).getUtcTime(), HISTORICAL_DATA.get(i).getCloseValue());
                    series.appendData(kek, false, HISTORICAL_DATA.size());
                }

                series.setDrawDataPoints(true);
                series.setDataPointsRadius(6);
                series.setThickness(3);
                series.setColor(Color.parseColor("#FF99cc00"));//H--C
                series.setTitle(fsym + "/USD");//H--C

                graph.getLegendRenderer().setBackgroundColor(Color.parseColor("#3C3D44"));//H--C
                graph.getLegendRenderer().setTextSize(20f);
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                graph.getLegendRenderer().setVisible(true);

                //set graph's MIN and MAX values on both axis
                graph.getViewport().setMinX((int) HISTORICAL_DATA.get(0).getUtcTime());
                Log.d("GraphX", ((int) HISTORICAL_DATA.get(0).getUtcTime()) + "");
                graph.getViewport().setMaxX((int) HISTORICAL_DATA.get(HISTORICAL_DATA.size() - 1).getUtcTime());
                Log.d("GraphX", ((int) HISTORICAL_DATA.get(HISTORICAL_DATA.size() - 1).getUtcTime()) + "");
                double minY = HISTORICAL_DATA.get(0).getCloseValue() - (HISTORICAL_DATA.get(0).getCloseValue() / 100) * 20;
                graph.getViewport().setMinY(minY);


                graph.getViewport().setScrollable(true);
                graph.getViewport().setScalable(true);
                graph.getViewport().setXAxisBoundsManual(true);
                if (pot == 2) {
                    graph.getGridLabelRenderer().setNumHorizontalLabels(HISTORICAL_DATA.size());
                } else
                    graph.getGridLabelRenderer().setNumHorizontalLabels(HISTORICAL_DATA.size() / 2);

                graph.getGridLabelRenderer().setHorizontalLabelsAngle(50);

                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#FFFFFF"));//H--C
                graph.getGridLabelRenderer().setTextSize(20.3f);
                graph.getGridLabelRenderer().setHighlightZeroLines(true);
                graph.getViewport().setBackgroundColor(Color.parseColor("#44454A"));//H--C

                graph.getGridLabelRenderer().setGridColor(Color.parseColor("#505665"));//H--C
                graph.getGridLabelRenderer().setLabelsSpace(10);
                graph.getViewport().setDrawBorder(true);

                graph.setHorizontalScrollBarEnabled(true);

                graph.addSeries(series);

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

                    @Override
                    public String formatLabel(double value, boolean isValueX) {

                        String dateFormat = "";

                        switch (pot) {
                            case 1:
                                dateFormat = "MM.dd";
                                break;
                            case 2:
                                dateFormat = "EEE";
                                break;
                            case 3:
                                dateFormat = "HH:mm";
                                break;
                            default:
                                dateFormat = "MM.dd";
                                break;
                        }
                        if (isValueX) {

                            java.util.TimeZone tz = java.util.TimeZone.getDefault();
                            int offset = tz.getOffset(new Date(0).getTime());
                            value = (long) value;
                            Date date = new Date((long) (value * 1000) + offset + 1500000);

                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);

                            String formattedDate = sdf.format(date);

                            return formattedDate;
                        } else {
                            Log.d("time4", value + "");
                            return super.formatLabel(value, isValueX);
                        }
                    }

                });

            } catch (IndexOutOfBoundsException e) {
                 graphAsync.setVisibility(View.INVISIBLE);
                 graphErrorImg.setVisibility(View.VISIBLE);
                 graphErrorText.setVisibility(View.VISIBLE);
                 materialSpinnerGraph.setVisibility(View.INVISIBLE);
            }

        }
        else{
                graphAsync.setVisibility(View.INVISIBLE);
                graphErrorImg.setVisibility(View.VISIBLE);
                graphErrorText.setVisibility(View.VISIBLE);
                 materialSpinnerGraph.setVisibility(View.INVISIBLE);
        }

    }

    public static void parseHistory(JSONObject object) throws JSONException {

        long utcTime = (object.getLong(timeMark));

        float priceAt = Float.parseFloat(object.getString(priceMark));

        CurrencyHist temp = new CurrencyHist(utcTime,priceAt);
        HISTORICAL_DATA.add(temp);

    }
    private class getDataAsync extends AsyncTask<Void , Integer, String> {

        private String fsymAsync = null;//pairName
        private  int timePeriod = 1;//amount of hours or days for API call

        GraphView graphAsync = (GraphView) findViewById(R.id.graph);
        public getDataAsync(String pairName, int period) {
            super();
            fsymAsync = pairName;
            timePeriod = period;
        }


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            HISTORICAL_DATA.clear(); //clear previous data for the new graph

            //from requests for the API
           if(timePeriod ==1 ){
               limitPeriod = "30";
               BASE_URL = "https://min-api.cryptocompare.com/data/histoday?fsym=" + fsymAsync + "&tsym=" + tsym
                       + "&limit="+ limitPeriod +"&aggregate="+ aggregate + "&e=" + market;

           } else if(timePeriod == 2 ){
                limitPeriod = "7";
               BASE_URL = "https://min-api.cryptocompare.com/data/histoday?fsym=" + fsymAsync + "&tsym=" + tsym
                       + "&limit="+ limitPeriod +"&aggregate="+ aggregate + "&e=" + market;

           }else if(timePeriod == 3){
               limitPeriod = "24";
              BASE_URL =  "https://min-api.cryptocompare.com/data/histohour?fsym=" + fsymAsync + "&tsym=" + tsym
                      + "&limit="+ limitPeriod +"&aggregate="+ aggregate + "&e=" + market;
            }

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

            String rspn = null;//server response Success/Error
            try {

                    JSONObject histObj = new JSONObject(strJson);

                    rspn = histObj.getString(GET_REPSONSE);

                    JSONArray jsonArr = histObj.getJSONArray(ARRAY_TAG);

                    int jsonLinesNum = Integer.parseInt(limitPeriod);

                    for (int i = 0; i < jsonLinesNum + 1; i++) {
                        parseHistory(jsonArr.getJSONObject(i)); //parsing data into CurrencyHist type and putting it into ArrayList
                    }
                    initializeLineGraphView(graphAsync,timePeriod,true);//initialize graph


            } catch (final JSONException e) {
                initializeLineGraphView(graphAsync,timePeriod,false);//initialize graph error
            }
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

