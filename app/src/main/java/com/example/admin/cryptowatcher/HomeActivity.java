package com.example.admin.cryptowatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {



    TextView btc;
    TextView ltc;
    TextView eth;
    TextView nvc;

    private static final String TAG = "my_tag_home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        btc = (TextView)findViewById(R.id.btcVal);
        ltc =(TextView)findViewById(R.id.ltcVal);
        eth = (TextView)findViewById(R.id.ethVal);
        nvc = (TextView)findViewById(R.id.nvcVal);



      for (Currencies obj: MainActivity.API_COLLECTION){
          if (obj.getPAIR_NAME() == MainActivity.BTC){
              btc.setText( Float.toString(obj.getLAST_BID()) + " USD");
          }else if (obj.getPAIR_NAME() == MainActivity.LTC){
              ltc.setText(Float.toString(obj.getLAST_BID()) + " USD");
          }else if (obj.getPAIR_NAME() == MainActivity.ETH){
              eth.setText(Float.toString(obj.getLAST_BID()) + " USD");
          }else if(obj.getPAIR_NAME() == MainActivity.NVC){
              nvc.setText(Float.toString(obj.getLAST_BID()) + " USD");
          }
      }

    }


}
