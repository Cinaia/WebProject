package com.example.admin.cryptowatcher;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;



/**
 * Created by Alex on 11.07.2017.
 * H--C means that smth needs to get hardcoded!!!
 */

public class DetailActivity extends SwipeBackActivity  {
    //SwipeBackActivity
    boolean isFirstOpen = true;
   // private static FragmentManager fragmentManager;
 public interface DataPass {
      public void sendData(String data);
    }
    //private graphFragment graphs;
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
        Bundle bundle = new Bundle();
        bundle.putString("params", pairNameRecieved);

        graphFragment youFragment = new graphFragment();
        youFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("params1",pairNameRecieved);
        detailFragment youFragment1 = new detailFragment();

        youFragment.setArguments(bundle);
        youFragment1.setArguments(bundle1);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()          // получаем экземпляр FragmentTransaction
                .add(R.id.fragmentContainer,youFragment1)
                .add(R.id.graphContainer, youFragment)
                .addToBackStack("myStack")
                .commit();



// set MyFragment Arguments


        //TransitionManager.callComplete(pairNameData);

    }


    @Override
    protected void onResume() {
        super.onResume();
      //  passDataToDetailFragment(findViewById(R.id.detailF));
       // passDataToGraphFragment(findViewById(R.id.graphicF));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailActivity.this, HomeActivity.class);

        startActivity(intent);
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

