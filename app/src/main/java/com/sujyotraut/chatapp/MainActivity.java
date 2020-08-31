package com.sujyotraut.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initViews();
            }
        }, 2000);
    }

    public void initViews(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignInFragment())
                .commit();
    }
}