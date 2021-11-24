package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        TextView changeFont, textView,textView2;
        SeekBar bar;

        changeFont = (TextView)findViewById(R.id.changeFont) ;
        textView = (TextView)findViewById(R.id.textView) ;
        textView2 = (TextView)findViewById(R.id.textView2) ;

        bar = (SeekBar)findViewById(R.id.seekBar);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeFont.setTextSize(Float.valueOf(i));
                textView.setTextSize(Float.valueOf(i));
                textView2.setTextSize(Float.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}