package com.example.wing.readpdf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建启动Service的Intent,以及Intent属性
        final Intent intent = new Intent();
        intent.setAction("com.example.wing.readpdf.Transtate");
        intent.setPackage(getPackageName());
        startButton = findViewById(R.id.startbutton);
        stopButton = findViewById(R.id.stopbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                stopService(intent);
            }
        });
    }
}
