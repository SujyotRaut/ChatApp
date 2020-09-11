package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.ChatsRecyclerAdapter;
import com.sujyotraut.chatapp.database.MyRepo;
import com.sujyotraut.chatapp.models.Chat;

import java.io.File;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private Toolbar topAppBar;
    private RecyclerView chatsRecyclerView;
    private ChatsRecyclerAdapter adapter;
    private FloatingActionButton addChatFab;

    private FirebaseFirestore db;
    private List<Chat> chats;
    private MyRepo myRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Log.d(TAG, "onCreate: chat activity");

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

        myRepo.getAllChats().observe(ChatsActivity.this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                adapter = new ChatsRecyclerAdapter(chats);
                chatsRecyclerView.setAdapter(adapter);
            }
        });

        DocumentReference currentUserRef = db.collection("users").document(user.getUid());
        currentUserRef.addSnapshotListener(ChatsActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                List<String> chats = ((List<String>) value.get("chats"));
                if (chats != null){
                    for (String chat: chats) {
                        updateChat(chat);
                    }
                }
            }
        });

        addChatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsActivity.this, AddChatsActivity.class);
                startActivity(intent);
            }
        });

    }

    public static String getConversationId(String targetId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid().compareTo(targetId) <= 0){
            return  user.getUid() + targetId;
        }else {
            return targetId + user.getUid();
        }
    }

    private void updateChat(final String chatId){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference usersRef = db.collection("users");
        final CollectionReference conversationsRef = db.collection("conversations");
        final DocumentReference chatRef = usersRef.document(chatId);
        final DocumentReference conversationRef = conversationsRef.document(getConversationId(chatId));

        db.runTransaction(new Transaction.Function<Chat>() {
            @Nullable
            @Override
            public Chat apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot chatSnapshot = transaction.get(chatRef);
                DocumentSnapshot conversationSnapshot = transaction.get(conversationRef);

                Timestamp lastMsgTime = conversationSnapshot.getTimestamp("lastMsgTime");
                String chatName = chatSnapshot.getString("name");
                String lastMsg = conversationSnapshot.getString("lastMsg");
                int unseenMsgCount = (int) conversationSnapshot.get("unseenMsgCount"+user.getUid());

                Chat chat = new Chat(chatId);
                chat.setLastMsgTime(lastMsgTime);
                chat.setLastMsg(lastMsg);
                chat.setChatName(chatName);
                chat.setUnseenMsgCount(unseenMsgCount);

                return chat;

            }
        }).addOnCompleteListener(new OnCompleteListener<Chat>() {
            @Override
            public void onComplete(@NonNull Task<Chat> task) {
                if (task.isSuccessful()){
                    myRepo.insertChat(task.getResult());
                }
            }
        });

        File profileDist = new File(getExternalFilesDir("profilePictures"), chatId + ".jpg");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference profilePicturesRef = storage.getReference("profilePictures");
        profilePicturesRef.getFile(profileDist);

        adapter.notifyDataSetChanged();
    }

    private void initViews(){

        myRepo = new MyRepo(getApplication());

        db = FirebaseFirestore.getInstance();

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