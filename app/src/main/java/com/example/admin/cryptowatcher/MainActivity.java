package com.example.admin.cryptowatcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    private ProgressBar spinnerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        new ShowDialogAsyncTask().execute();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },2000*2);*/
    }


    private class ShowDialogAsyncTask extends AsyncTask<Void, Integer, Void> {

        boolean isLoaded = false;
        int i = 1;
        @Override
        protected void onPreExecute() {
            // обновляем пользовательский интерфейс сразу после выполнения задачи
            super.onPreExecute();

            Toast.makeText(MainActivity.this, "Вызов onPreExecute()", Toast.LENGTH_SHORT).show();




        }

        @Override
        protected Void doInBackground(Void... params) {

            //Загрузка API
            while(i<100){

                i += 2;

                publishProgress(i);
                SystemClock.sleep(100);

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);



        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            Toast.makeText(MainActivity.this, "Вызов onPostExecute()", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);


        }
    }

}
