package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.ChatsRecyclerAdapter;
import com.sujyotraut.chatapp.models.ChatModel;
import com.sujyotraut.chatapp.models.Message;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private Toolbar topAppBar;
    private RecyclerView chatsRecyclerView;
    private FloatingActionButton addChatFab;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(ChatsActivity.this, MainActivity.class);
            Log.d(TAG, "intent created");
            startActivity(intent);
            Log.d(TAG, "Main activity started");
            finish();
            Log.d(TAG, "Chat Activity finished");
        }

        initViews();
        
        final CollectionReference usersRef = db.collection("users");
        final CollectionReference chatsRef = db.collection("chats");
        final DocumentReference currentUserRef = usersRef.document(user.getUid());


        db.runTransaction(new Transaction.Function<List<ChatModel>>() {
            @Nullable
            @Override
            public List<ChatModel> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                List<ChatModel> chatModels = new ArrayList<>();

                List<String> list = ((List<String>) transaction.get(currentUserRef).get("chats"));

                if (!list.isEmpty()){
                    for (String chatId: list){

                        Log.d(TAG, "chatId: " + chatId);

                        DocumentSnapshot snapshot = transaction.get(chatsRef.document(getCompoundId(chatId)));
                        String lastMsg = snapshot.getString("lastMsg");
                        Timestamp lastMsgTime = snapshot.getTimestamp("lastMsgTime");
                        Object o = snapshot.get("unseenMsgCount"+user.getUid());
                        int unseenMsgCount = 0;

                        if (o != null){
                            unseenMsgCount = ((int) o);
                        }

                        String dataSource = snapshot.getMetadata().isFromCache() ? "local cache" : "server";

                        Log.d(TAG, "data from: " + dataSource);
                        Log.d(TAG, "lastMsg: " + lastMsg);
                        Log.d(TAG, "lastMsgTime: " + lastMsgTime);
                        Log.d(TAG, "unseenMsgCount: " + unseenMsgCount);

                        DocumentSnapshot chatSnapshot = transaction.get(usersRef.document(chatId));
                        String chatName = chatSnapshot.getString("name");
                        Uri profilePicture = ((Uri) chatSnapshot.get("profilePicture"));

                        ChatModel chatModel = new ChatModel(chatName);
                        chatModel.setProfilePicture(profilePicture);
                        chatModel.setLastMsg(lastMsg);
                        chatModel.setLastMsgTime(lastMsgTime);
                        chatModel.setUnseenMsgCount(unseenMsgCount);

                        chatModels.add(chatModel);
                    }
                    return chatModels;

                }else {
                    Log.d(TAG, "list is empty");
                    return null;
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<List<ChatModel>>() {
            @Override
            public void onComplete(@NonNull Task<List<ChatModel>> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "successful");
                    ChatsRecyclerAdapter adapter = new ChatsRecyclerAdapter(task.getResult());
                    chatsRecyclerView.setAdapter(adapter);
                } else {
                    Log.d(TAG, task.getException().toString());
                }
            }
        });
    }

    public static String getCompoundId(String targetId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid().compareTo(targetId) <= 0){
            return  user.getUid() + targetId;
        }else {
            return targetId + user.getUid();
        }
    }

    private void initViews(){

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        addChatFab = findViewById(R.id.addChatsFab);

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);

        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newGroupOption:
                        Log.d(TAG, "onMenuItemClick: new group");
                        break;
                    case R.id.settingsOption:
                        Log.d(TAG, "onMenuItemClick: settings");
                        Intent settingsIntent = new Intent(ChatsActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.shareOption:
                        Log.d(TAG, "onMenuItemClick: share");
                        break;
                    case R.id.aboutOption:
                        Log.d(TAG, "onMenuItemClick: above");
                        Intent aboutIntent = new Intent(ChatsActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                }
                return false;
            }
        });
    }
}