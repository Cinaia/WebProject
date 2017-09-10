package com.example.admin.cryptowatcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
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

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

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
    TextView priceVal;



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

                priceVal.setText(obj.getPRICE() + " USD");


                volumeDetailVal.setText("" + NumberFormat.getNumberInstance(Locale.US).format(obj.getMARKET_CAP_USD()) + " USD");
                btcPriceVal.setText("" + obj.getPRICE_BTC() + " BTC");

                Date date = new Date(obj.getUTC_TIME() * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);
                utcTimeVal.setText(formattedDate);
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

