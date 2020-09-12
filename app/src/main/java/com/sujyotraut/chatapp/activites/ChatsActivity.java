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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.ChatsRecyclerAdapter;
import com.sujyotraut.chatapp.database.MyRepo;
import com.sujyotraut.chatapp.models.Chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private Toolbar topAppBar;
    private RecyclerView chatsRecyclerView;
    private ChatsRecyclerAdapter adapter;
    private FloatingActionButton addChatFab;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private MyRepo myRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Log.d(TAG, "onCreate: chat activity");

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            Intent intent = new Intent(ChatsActivity.this, MainActivity.class);
            startActivity(intent);
            Log.d(TAG, "Main activity started");
            finish();
            Log.d(TAG, "Chat Activity finished");
        }

        initViews();

        myRepo.getAllChats().observe(ChatsActivity.this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                adapter.setChats(chats);
            }
        });

        DocumentReference currentUserRef = db.collection("users").document(user.getUid());
        currentUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "onEvent: user document changed");
                Log.d(TAG, "error: " + error);
                if (error == null){
                    List<String> chatIds = ((List<String>) value.get("chats"));
                    if (chatIds != null){
                        for (String chatId: chatIds) {
                            updateChat(chatId);
                        }
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
                int unseenMsgCount = 0;
                Long o = conversationSnapshot.getLong("unseenMsgCount"+user.getUid());
                if (o != null){
                    unseenMsgCount = o.intValue();
                }

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
                    Log.d(TAG, "chat updated: " + task.getResult().getChatName());
                    myRepo.insertChat(task.getResult());
                }else {
                    Log.d(TAG, "chat update failed: " + task.getResult().getChatName());
                }
            }
        });

        final File distFile = new File(getExternalFilesDir("profilePictures"), chatId+".jpg");
        if (!distFile.exists()){
            long ONE_MEGABYTE = 1024*1024;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference profilePicturesRef = storage.getReference("profilePictures");
            profilePicturesRef.child(distFile.getName()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try (OutputStream stream = new FileOutputStream(distFile);) {
                        stream.write(bytes);
                        stream.flush();
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        conversationRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "msg updated");
                String lastMsg = value.getString("lastMsg");
                Timestamp lastMsgTime = value.getTimestamp("lastMsgTime");
                Long l = value.getLong("unseenMsgCount"+user.getUid());
                int unseenMsgCount = 0;
                if (l != null){
                    unseenMsgCount = l.intValue();
                }

                myRepo.updateChat(chatId, lastMsg, lastMsgTime, unseenMsgCount);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void initViews(){

        myRepo = new MyRepo(getApplication());

        db = FirebaseFirestore.getInstance();

        addChatFab = findViewById(R.id.addChatsFab);

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);
        adapter = new ChatsRecyclerAdapter(ChatsActivity.this);
        chatsRecyclerView.setAdapter(adapter);

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