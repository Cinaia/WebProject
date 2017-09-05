package com.example.admin.cryptowatcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Alex on 11.07.2017.
 */

public class DetailActivity extends AppCompatActivity {

    boolean isFirstOpen = true;
    private static final String TAG = "my_deta";
    TextView hourDetailVal;
    TextView dayDetailVal;
    TextView weekDetailVal;
    TextView volumeDetailVal;
    TextView btcPriceVal;
    TextView utcTimeVal;
    TextView pairNameText;


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.detail_info);

        hourDetailVal = (TextView) findViewById(R.id.hourDetailVal);
        dayDetailVal = (TextView) findViewById(R.id.dayDetailVal);
        weekDetailVal = (TextView) findViewById(R.id.weekDetailVal);
        volumeDetailVal = (TextView) findViewById(R.id.volumeDetailVal);
        btcPriceVal = (TextView) findViewById(R.id.btcPriceVal);
        utcTimeVal = (TextView) findViewById(R.id.utcTimeText);
        pairNameText = (TextView) findViewById(R.id.pairNameText);
        Intent intent = getIntent();
        String inte = intent.getStringExtra("pairName");
        Log.d(TAG, inte);

        
        for (Currencies obj: MainActivity.API_COLLECTION){
            if (obj.getPAIR_NAME().equals(inte)){
                Log.d(TAG,"Found match!");


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
                
                volumeDetailVal.setText("" + obj.getMARKET_CAP_USD() + " USD");
                btcPriceVal.setText("" + obj.getPRICE_BTC() + " BTC");

                Date date = new Date(obj.getUTC_TIME() * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);
                utcTimeVal.setText("Данные за " + formattedDate);
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

