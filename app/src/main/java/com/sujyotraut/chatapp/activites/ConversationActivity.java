package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.models.Message;

public class ConversationActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private EditText msgEditText;
    private ImageView profileImageView;
    private TextView nameTv, statusTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

    }



    public void sendMsg(View view) {

    }

    public void addAttachment(View view) {

    }

    private void initViews(){
        msgEditText = findViewById(R.id.msgEditText);
        profileImageView = findViewById(R.id.profileImageView);
        nameTv = findViewById(R.id.nameTextView);
        statusTv = findViewById(R.id.statusTextView);
    }
}