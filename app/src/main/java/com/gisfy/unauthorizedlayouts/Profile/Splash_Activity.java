package com.gisfy.unauthorizedlayouts.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gisfy.unauthorizedlayouts.BottomNavigation.NavigationView;
import com.gisfy.unauthorizedlayouts.R;

public class Splash_Activity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash_Activity.this, NavigationView.class);
                startActivity(intent);
                finish();
            }

        },  SPLASH_SCREEN_TIME_OUT);
    }
}
