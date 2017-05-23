package com.example.leo.radarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadarView radarView = (RadarView) findViewById(R.id.rv_main);
        radarView.setValues(new double[]{30,40,50,60,70,80});
        radarView.setTitles(new String[]{"防御","辅助","击杀","物理","法术","金钱"});
    }
}
