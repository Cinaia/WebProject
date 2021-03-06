package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Класс для отрисвки графиков. Данные для графиков
 * берутся с cryptocompare.com
 */

public class graphFragment extends Fragment {

    protected MaterialSpinner materialSpinnerGraph;
    protected GraphView graphF;
    private ArrayList<CurrencyHist> HISTORICAL_DATA;

    public String BASE_URL = null;

    public String tsym = "USD";
    public String limitPeriod = "30";
    public String aggregate = "1";
    public String market = "CCCAGG";
    public static String fsym = null;
    public static final String ARRAY_TAG = "Data";
    public static final String GET_REPSONSE = "Response";
    private static final String timeMark = "time";
    private static final String priceMark = "close";
    String mParam1;
    private String pairName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
           mParam1 = getArguments().getString("params");
        }
        View view = inflater.inflate(R.layout.graph_fragment,  container, false);

        pairName = mParam1;

        HISTORICAL_DATA = new ArrayList<>();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        materialSpinnerGraph = (MaterialSpinner)getView().findViewById(R.id.materialSpinnerGraph1);
        materialSpinnerGraph.setItems("Месячный график", "Недельный график", "Дневной график");
        materialSpinnerGraph.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                new getDataAsync(pairName,position + 1).execute();

            }
        });
        Toast toast = Toast.makeText(getView().getContext(),
                mParam1, Toast.LENGTH_SHORT);
        toast.show();
        new getDataAsync(pairName, 1).execute();
    }


    @Override
    public void onPause() {
        super.onPause();
        HISTORICAL_DATA.clear();
    }

    private class getDataAsync extends AsyncTask<Void , Integer, String> {

        private String fsymAsync = null;
        private  int timePeriod = 1;



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

            graphF = (GraphView)getView().findViewById(R.id.graphF);


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

        //@RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            String rspn = null;//server response Success/Error

            try {

                JSONObject histObj = new JSONObject(resultJson);

                //rspn = histObj.getString(GET_REPSONSE);
                Log.d("TestP" , histObj.getString("Response"));
                if(histObj.getString("Response") == "Error"){
                    initializeLineGraphView(graphF,timePeriod,false);
                }else {
                    JSONArray jsonArr = histObj.getJSONArray(ARRAY_TAG);

                    int jsonLinesNum = Integer.parseInt(limitPeriod);


                    for (int i = 0; i < jsonLinesNum + 1; i++) {
                        parseHistory(jsonArr.getJSONObject(i)); //parsing data into CurrencyHist type and putting it into ArrayList
                    }
                    initializeLineGraphView(graphF, timePeriod, true);
                }

            } catch (final JSONException e) {

                initializeLineGraphView(graphF,timePeriod,false);//initialize graph error
            }
        }
    }


    protected void parseHistory(JSONObject object) throws JSONException {

        Log.d("inGrap", "parse if triggered");
        long utcTime = (object.getLong(timeMark));

        float priceAt = Float.parseFloat(object.getString(priceMark));

        CurrencyHist temp = new CurrencyHist(utcTime,priceAt);
        HISTORICAL_DATA.add(temp);


    }


    protected void initializeLineGraphView(GraphView graph1, int periodOfTime, boolean ifSuccess) {
        Log.d("speedUP", "initialize graph called");

        if(ifSuccess) {
            try {


                graph1.removeAllSeries();
                final int pot = periodOfTime;

                //Подготавливаем данные для отображения на графике.
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(HISTORICAL_DATA.get(0).getUtcTime(), HISTORICAL_DATA.get(0).getCloseValue())
                });
                for (int i = 1; i < HISTORICAL_DATA.size(); i++) {

                    DataPoint kek = new DataPoint(HISTORICAL_DATA.get(i).getUtcTime(), HISTORICAL_DATA.get(i).getCloseValue());
                    series.appendData(kek, false, HISTORICAL_DATA.size());
                    Log.d("inGrap", HISTORICAL_DATA.get(i).getCloseValue() + "");
                }



                series.setDrawDataPoints(true);
                series.setDataPointsRadius(6);
                series.setThickness(3);
                series.setColor(Color.parseColor("#FF99cc00"));
                series.setTitle(pairName + "/USD");

                graph1.getLegendRenderer().setBackgroundColor(Color.parseColor("#3C3D44"));
                graph1.getLegendRenderer().setTextSize(20f);
                graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                graph1.getLegendRenderer().setVisible(true);

                //Устанавливаем минимальное и максимальное значнение для каждой оси
                graph1.getViewport().setMinX((int) HISTORICAL_DATA.get(0).getUtcTime());
                Log.d("GraphX", ((int) HISTORICAL_DATA.get(0).getUtcTime()) + "");
                graph1.getViewport().setMaxX((int) HISTORICAL_DATA.get(HISTORICAL_DATA.size() - 1).getUtcTime());
                Log.d("GraphX", ((int) HISTORICAL_DATA.get(HISTORICAL_DATA.size() - 1).getUtcTime()) + "");
                double minY = HISTORICAL_DATA.get(0).getCloseValue() - (HISTORICAL_DATA.get(0).getCloseValue() / 100) * 20;
                graph1.getViewport().setMinY(minY);


                graph1.getViewport().setScrollable(true);
                graph1.getViewport().setScalable(true);
                graph1.getViewport().setXAxisBoundsManual(true);
                if (pot == 2) {
                    graph1.getGridLabelRenderer().setNumHorizontalLabels(HISTORICAL_DATA.size());
                } else
                    graph1.getGridLabelRenderer().setNumHorizontalLabels(HISTORICAL_DATA.size() / 2);

                graph1.getGridLabelRenderer().setHorizontalLabelsAngle(50);

                graph1.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#FFFFFF"));
                graph1.getGridLabelRenderer().setTextSize(20.3f);


                graph1.getGridLabelRenderer().setGridColor(Color.parseColor("#505665"));
                graph1.getGridLabelRenderer().setLabelsSpace(10);



                graph1.addSeries(series);


                graph1.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

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

                Log.d("inGrap", "catch if triggered - " + e);
            }

        }
        else{
              Log.d("TestP" , "False Graph plotter called");
              graph1.setVisibility(View.INVISIBLE);

              TextView graphErrorText = (TextView)getView().findViewById(R.id.graphErrorText);
              graphErrorText.setVisibility(View.VISIBLE);
              graphErrorText.setText("График для валютной пары " + pairName  + "/" + tsym  + " недоступен для биржи " + market);

        }

    }

}
