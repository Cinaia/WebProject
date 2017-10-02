package com.example.admin.cryptowatcher;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by admin on 28.09.17.
 */

public class detailFragment extends Fragment implements DetailActivity.DataPass {


    TextView hourDetailVal;
    TextView dayDetailVal;
    TextView weekDetailVal;
    TextView volumeDetailVal;
    TextView btcPriceVal;
    TextView pairNameText;
    TextView priceVal;
    TextView graphErrorText;
    ImageView graphErrorImg;

    protected String pairNameData;
    private String fsym = "BTC";
    Context ctx;
    //DataPass mDataPasser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment,  container, false);

       // Log.d("transitLOL2", "" + priceVal.getText());
         setHasOptionsMenu(true);
        return view;
    }


   @Override
   public void onAttach(Context a) {
       super.onAttach(a);

     //  mDataPasser = (DataPass) a;
     //  Log.d("transitLOL3", "" + priceVal.getText());
       Log.d("transitFragN", a + "");
       Log.d("transitFragN", pairNameData + "");
   }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        graphErrorText =  (TextView)getView().findViewById(R.id.graphErrorText);
        hourDetailVal = (TextView)getView().findViewById(R.id.hourDetailVal);
        dayDetailVal = (TextView)getView().findViewById(R.id.dayDetailVal);
        weekDetailVal = (TextView)getView().findViewById(R.id.weekDetailVal);
        volumeDetailVal = (TextView)getView().findViewById(R.id.volumeDetailVal);
        btcPriceVal = (TextView)getView().findViewById(R.id.btcPriceVal);
        pairNameText = (TextView)getView().findViewById(R.id.pairNameText);
        priceVal = (TextView)getView().findViewById(R.id.priceVal);

       // Log.d("tranitString",pairNameData);

        Log.d("transitFragQ", "" + priceVal.getText());


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_f, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("transitFragQN", "" + priceVal.getText());
        for (Currencies obj: MainActivity.API_COLLECTION) { //passing through data array and finding our needed currency pair
            if (obj.getPAIR_NAME().equals(priceVal.getText())) {
                Log.d("speedUP", "my index: " + MainActivity.API_COLLECTION.indexOf(obj.getPAIR_NAME()));

                fsym = obj.getABBR().toUpperCase();

                String pairNameRecieved = fsym; //set the pair symbol name

                pairNameText.setText(obj.getPAIR_NAME().toUpperCase());//API loves Upper-case

                if (obj.getHOUR_CHANGE() > 0) {
                    hourDetailVal.setText("+" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                } else {
                    hourDetailVal.setText("" + obj.getHOUR_CHANGE() + "%");
                    hourDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                if (obj.getDAY_CHANGE() > 0) {
                    dayDetailVal.setText("+" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                } else {
                    dayDetailVal.setText("" + obj.getDAY_CHANGE() + "%");
                    dayDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                if (obj.getWEEK_CHANGE() > 0) {
                    weekDetailVal.setText("+" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FF99cc00"));//H--C
                } else {
                    weekDetailVal.setText("" + obj.getWEEK_CHANGE() + "%");
                    weekDetailVal.setTextColor(Color.parseColor("#FFFF4444"));//H--C
                }

                priceVal.setText(obj.getPRICE() + " USD");//H--C

                volumeDetailVal.setText("" + NumberFormat.getNumberInstance(Locale.US).format(obj.getMARKET_CAP_USD()) + " USD");//H--C

                btcPriceVal.setText("" + String.format("%.10f", obj.getPRICE_BTC()) + " BTC");//H--C
            }
        }
    }


    @Override
    public void sendData(String data) {
        Log.d("fragmentTrans2", data);
    }
}
