package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.model.ServerTimestamps;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.Value;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.fragments.SignInFragment;
import com.sujyotraut.chatapp.models.Message;
import com.sujyotraut.chatapp.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null){ //check if user is signed in
            //launch chats activity
            Log.d(TAG, "onCreate: user is not null");
            startActivity(new Intent(getApplicationContext(), ChatsActivity.class));
            finish();

        } else { //user is not signed in
            //launch sign in fragment
            Log.d(TAG, "onCreate: launching sign in fragment");
            launchSignInFragment();
        }

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference chatsRef = db.collection("chats");
//        DocumentReference chatRef = chatsRef.document("CseAirDciCdZevgfh5bBjmDyVzP2GO9dwCNNOBUUjmL3DwuATzZwVmE2");
//
//        Log.d(TAG, Timestamp.now().toString());
//        Message msg = new Message("Yo bro");
//        msg.send("CseAirDciCdZevgfh5bBjmDyVzP2");

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

    private void launchSignInFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignInFragment())
                .commit();
    }
}