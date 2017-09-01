package com.example.admin.cryptowatcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Alex on 11.07.2017.
 */

public class DetailActivity extends AppCompatActivity {

    boolean isFirstOpen = true;
    private static final String TAG = "my_deta";
    TextView volume;
    TextView price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
    //    getSupportActionBar().show();
        setContentView(R.layout.detail_info);

        volume = (TextView) findViewById(R.id.textView9);
        price = (TextView) findViewById(R.id.textView12);
        Intent intent = getIntent();
        String inte = intent.getStringExtra("pairName");
        Log.d(TAG, inte);
        for (Currencies obj: MainActivity.API_COLLECTION){
            if (obj.getPAIR_NAME().equals(inte)){
                Log.d(TAG,"Found match!");
                volume.setText("" + obj.getDAY_VOL());
                price.setText("" + obj.getPRICE());
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

