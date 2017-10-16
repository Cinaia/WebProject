package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;

public class MainActivity extends Activity {


    ImageView splashImageBtc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);




        splashImageBtc = (ImageView) findViewById(R.id.splashImage);

       // SystemClock.sleep(3000);



        if(isNetworkConnected()){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

            startActivity(intent);

        }
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
