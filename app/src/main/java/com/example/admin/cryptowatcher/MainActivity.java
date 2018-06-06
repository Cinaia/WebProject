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
/*
* Данный класс проверяет наличие интернет соединения.
* В случае его отсутствия, будет отображен layout
* Если инет есть, будет отображено HomeActivity.
* */
public class MainActivity extends Activity {


    ImageView splashImageBtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Формируем сплэш-скрин
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Загружаем layout для данной activity
        setContentView(R.layout.splash_screen);
        //Загружаем картинку
        splashImageBtc = (ImageView) findViewById(R.id.splashImage);

       if(isNetworkConnected()){
            //Если есть соединение, формируем интент на homeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            //Запрашиваем переход
            startActivity(intent);

        }
    }



    private boolean isNetworkConnected() {
        //Запрашиваем информацию о состоянии подключения
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
