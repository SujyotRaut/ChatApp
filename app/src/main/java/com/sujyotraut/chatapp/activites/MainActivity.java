package com.sujyotraut.chatapp.activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.fragments.SignInFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Just for testing purpose
//        Intent intent = new Intent(this, ChatsActivity.class);
//        startActivity(intent);

        //setting status bar color to white
//        Window window = MainActivity.this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.white));
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //check if app opens for first time
        if (true){
            showSplashScreen();
        } else if (true){ //check if user is signed in
            //launch chats activity
        } else { //user is not signed in
            //launch sign in fragment
//            launchSignInFragment();
        }
    }

    private void showSplashScreen(){
        //show splash screen & launch sign in fragment
        Log.d(TAG, "initViews: showing splash screen");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchSignInFragment();
            }
        }, 2000);
    }

    private void launchSignInFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignInFragment())
                .commit();
    }
}