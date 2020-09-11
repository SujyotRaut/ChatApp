package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.fragments.SignInFragment;
import com.sujyotraut.chatapp.models.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
//        Intent intent = new Intent(this, ConversationActivity.class);
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
            Intent intent = new Intent(MainActivity.this, ChatsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Log.d(TAG, "onCreate: main activity is finished");

        } else { //user is not signed in
            //launch sign in fragment
            Log.d(TAG, "onCreate: launching sign in fragment");
            launchSignInFragment();
        }

//        Message message = new Message("1");
//        String targetId = "mZwRwVhdltMNGWLKFDKxd9eG2NI3";
//        testSendMsg(message, targetId);

//        CollectionReference chatsRef = FirebaseFirestore.getInstance().collection("chats");
//        CollectionReference messagesRef = chatsRef.document(ChatsActivity.getCompoundId("GO9dwCNNOBUUjmL3DwuATzZwVmE2")).collection("messages");
//
//        messagesRef.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (DocumentChange change: value.getDocumentChanges()){
//                    Log.d(TAG, "onEvent: " + change.getType());
//                    Log.d(TAG, "onEvent: " + change.getDocument().get("timestamp"));
//                }
//            }
//        });

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/jpeg");
//        startActivityForResult(intent, 111);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri androidUri = data.getData();


        }
    }
}