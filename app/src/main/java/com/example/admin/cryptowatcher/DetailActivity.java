package com.example.admin.cryptowatcher;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackActivity;

import java.util.ArrayList;



/**
 * Created by Alex on 11.07.2017.
 * H--C means that smth needs to get hardcoded!!!
 */

public class DetailActivity extends SwipeBackActivity  {
    //SwipeBackActivity
    boolean isFirstOpen = true;
 public interface DataPass {
      public void sendData(String data);
    }

    public static final String TAG = "my_deta";

    public String BASE_URL = null;

    private String pairNameRecieved = null;
    DataPass dp;
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);


        Intent intent = getIntent();
        String pairNameData = intent.getStringExtra("pairName");
       pairNameRecieved =pairNameData;
        //TransitionManager.callComplete(pairNameData);
    }


    @Override
    protected void onResume() {
        super.onResume();
        passDataToDetailFragment(findViewById(R.id.detailF));
        passDataToGraphFragment(findViewById(R.id.graphicF));
    }

    public void passDataToDetailFragment(View v){

    ((TextView) v.findViewById(R.id.detailF).findViewById(R.id.priceVal))
            .setText(pairNameRecieved);
    }
    public void passDataToGraphFragment(View v){

        ((TextView) v.findViewById(R.id.graphicF).findViewById(R.id.textFieldTmp))
                .setText(pairNameRecieved);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_f, menu);
        // Associate searchable configuration with the SearchView
        return true;
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

    @Override
    protected void onPause() {
        super.onPause();

      //  HISTORICAL_DATA.clear();
        //graphAsync.setVisibility(View.GONE);
    }
}

