package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;
    TextView textView_general, textViewTheme, textView2, textViewNotif, textViewFiles, tv_Size;
    ImageView imageViewBackToHome;
    Slider slider;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchCompat = findViewById(R.id.switchCompat);
        textView_general = (TextView) findViewById(R.id.textView_general);
        textView2 = (TextView) findViewById(R.id.textView2);
        textViewTheme = (TextView) findViewById(R.id.textViewTheme);
        textViewNotif = (TextView) findViewById(R.id.textViewNotif);
        textViewFiles = (TextView) findViewById(R.id.textViewFiles);
        tv_Size = (TextView) findViewById(R.id.tv_Size);
        slider = findViewById(R.id.slider);
        imageViewBackToHome = findViewById(R.id.imageViewBackToHome);


        sharedPreferences = getSharedPreferences("night", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", true);
                    editor.commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", false);
                    editor.commit();

                }
            }
        });


        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                textView_general.setTextSize(Float.valueOf(slider.getValue()));
                textView2.setTextSize(Float.valueOf(slider.getValue()));
                textViewTheme.setTextSize(Float.valueOf(slider.getValue()));
                textViewNotif.setTextSize(Float.valueOf(slider.getValue()));
                textViewFiles.setTextSize(Float.valueOf(slider.getValue()));
                tv_Size.setTextSize(Float.valueOf(slider.getValue()));


            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });


        imageViewBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
