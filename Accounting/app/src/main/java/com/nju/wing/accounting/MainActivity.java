package com.nju.wing.accounting;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmsManger smsManger = new SmsManger(this);
        Map<String,String> mess = smsManger.getSmsFromPhone();
        for(Map.Entry<String,String> entry : mess.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
            MessAnalysis messAnalysis = new MessAnalysis(entry.getValue());
        }

    }

}
