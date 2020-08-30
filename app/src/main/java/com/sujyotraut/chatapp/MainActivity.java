package com.sujyotraut.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void initViews(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignInFragment())
                .commit();
    }
}