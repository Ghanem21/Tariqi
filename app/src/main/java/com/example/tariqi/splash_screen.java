package com.example.tariqi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class splash_screen extends AppCompatActivity {

    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(SignInActivity.PREFS_NAME,0);
                Boolean hasLoggedIn =sharedPreferences.getBoolean("hasLoggedIn",false);
                if (hasLoggedIn){
                    Intent i = new Intent(splash_screen.this, Home.class);
                    startActivity(i);
                    finish();

                }
                else{
                Intent i = new Intent(splash_screen.this, SignInActivity.class);
                startActivity(i);
                finish();}
            }
        },1500);

    }
}