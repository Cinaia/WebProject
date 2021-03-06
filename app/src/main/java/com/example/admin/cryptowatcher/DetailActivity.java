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
 * Класс отвечает за управление фрагментами (detailFragment , graphFragment)
 */

public class DetailActivity extends SwipeBackActivity  {
    //SwipeBackActivity
    boolean isFirstOpen = true;
   // private static FragmentManager fragmentManager;

    public static final String TAG = "my_deta";

    public String BASE_URL = null;

    private String pairNameRecieved = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);

        Intent intent = getIntent();
        String pairNameData = intent.getStringExtra("pairName");
        pairNameRecieved =pairNameData;

        //Формируем данные для передачи во фрагменты
        Bundle bundle = new Bundle();
        bundle.putString("params", pairNameRecieved);

        graphFragment youFragment = new graphFragment();
        youFragment.setArguments(bundle);

        //Формируем данные для передачи во фрагменты
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
    }


    @Override
    protected void onResume() {
        super.onResume();
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

        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(isFirstOpen) {
            isFirstOpen = false;
        } else {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

