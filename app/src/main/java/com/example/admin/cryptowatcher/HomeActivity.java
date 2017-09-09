package com.example.admin.cryptowatcher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.cryptowatcher.MainActivity.PRICE;
import static com.example.admin.cryptowatcher.R.id.textView1;

public class HomeActivity extends AppCompatActivity {

    listAdapter listAdapter;

    private static final String TAG = "my_tag_home";                                               //tag for logs
    private SwipeRefreshLayout swipeContainer;

    boolean isFirstOpen = true;                                                                    //checks if activity is opened of the first time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("Текущий курс");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setDistanceToTriggerSync(200);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskRunner(false).execute();
            }
        });


        listAdapter = new listAdapter(this, MainActivity.API_COLLECTION);

        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.homeList);

        lvMain.setAdapter(listAdapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String pairName = MainActivity.API_COLLECTION.get(position).getPAIR_NAME();
                Log.d(TAG, pairName + " - got this name bitch");
                Intent detailScreen = new Intent(HomeActivity.this, DetailActivity.class);
                detailScreen.putExtra("pairName", pairName);
                startActivity(detailScreen);

            }
        });

    }


    void pushValues() {




    }


    private class AsyncTaskRunner extends AsyncTask<Void, Integer, String> {


        ProgressDialog mProgressDialog;     //progress dialog init for updating rates'values


        //*******Url init block********

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        private boolean showProgressDialog = true;

        public AsyncTaskRunner(boolean showLoading) {
            super();
            showProgressDialog = showLoading;
        }


        @Override
        protected String doInBackground(Void... params) {

            try {

                URL url = new URL(MainActivity.API_URL);//URL address for API

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
                Log.d(TAG,"got info");
                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {                                                                                  //parsing API request result


                JSONArray jsonArr = new JSONArray(resultJson);

                for(int i = 0; i < MainActivity.LIMIT; i++){
                    MainActivity.parseToList(jsonArr.getJSONObject(i));
                }

            } catch (final JSONException e){
                //add err handle


            }
            //SystemClock.sleep(1000);
            return resultJson;
        }


        @Override
        protected void onPostExecute(String result) {

                                                                                 //push values to textView fields
            if(showProgressDialog) {
                SystemClock.sleep(500);
                mProgressDialog.dismiss();
            }
            swipeContainer.setRefreshing(false);
        }


        @Override
        protected void onPreExecute() {

            MainActivity.API_COLLECTION.clear();                                                   //clear previous values
            if(showProgressDialog) {
                mProgressDialog = new ProgressDialog(
                        HomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);                    //set style for progressDialog
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                        //set spinner
                mProgressDialog.setMessage("Загружаю. Подождите...");


                mProgressDialog.show();
            }


        }
    }

    @Override
    protected void onStart() {

       super.onStart();
        Log.d(TAG,"onStart is called");

    }


    //on back button pressed
    //consider toast message instead of alertDialog!!
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setIcon(R.mipmap.logo)
                .setTitle("Выход")
                .setMessage("Покинуть приложение?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
                     if(isFirstOpen){
                          isFirstOpen = false;                          //if activity called again , update rate values
                     }
            new AsyncTaskRunner(true).execute();

        Log.d(TAG, "HomeActivity: onResume()" + isFirstOpen);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "HomeActivity: onPause()" + isFirstOpen);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "HomeActivity: onStop()" + isFirstOpen);
    }
    public void onDestroy() {
        super.onDestroy();

    }


}




