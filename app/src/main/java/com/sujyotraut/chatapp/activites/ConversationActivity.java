package com.sujyotraut.chatapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.database.MyRepo;
import com.sujyotraut.chatapp.models.Message;
import com.sujyotraut.chatapp.adapters.MessagesRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private EditText msgEditText;
    private RecyclerView messagesRecyclerView;

    private MyRepo myRepo;
    private MessagesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        initViews();

        String chatId = getIntent().getStringExtra("chatId");
        String conversationId = ChatsActivity.getConversationId(chatId);
        myRepo.getAllMessages(conversationId).observe(ConversationActivity.this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
                messagesRecyclerView.smoothScrollToPosition(messages.size()-1);

                String chatId = getIntent().getStringExtra("chatId");
                String conversationId = ChatsActivity.getConversationId(chatId);
                CollectionReference conversationsRef = db.collection("conversations");
                DocumentReference conversationRef = conversationsRef.document(conversationId);
                conversationRef.update("unseenMsgCount"+user.getUid(), 0);

                CollectionReference messagesRef = conversationsRef.document(conversationId).collection("messages");
                messagesRef.whereEqualTo("senderId", chatId)
                        .addSnapshotListener(ConversationActivity.this, new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null){
                                    List<DocumentChange> changes = value.getDocumentChanges();
                                    if (!changes.isEmpty()){
                                        for (DocumentChange change: changes){
                                            change.getDocument().getReference().update("seen", true);
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        updateMessages();

    }

    private void updateMessages() {
        String chatId = getIntent().getStringExtra("chatId");
        String conversationId = ChatsActivity.getConversationId(chatId);
        CollectionReference conversationsRef = db.collection("conversations");
        CollectionReference messagesRef = conversationsRef.document(conversationId).collection("messages");
        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentChange> changes = value.getDocumentChanges();
                if (!changes.isEmpty()){
                    for (DocumentChange change: changes){
                        Message message = change.getDocument().toObject(Message.class);
                        myRepo.insertMessage(message);
                    }
                }
            }
        });
    }

    public void sendMsg(View view) {
        String msgText = msgEditText.getText().toString();
        if (!msgText.isEmpty()){

            //initializing batch
            WriteBatch batch = db.batch();

            String chatId = getIntent().getStringExtra("chatId");
            String conversationId = ChatsActivity.getConversationId(chatId);
            String msgId;

            DocumentReference conversationRef = db.collection("conversations").document(conversationId);
            DocumentReference chatRef = db.collection("users").document(chatId);
            DocumentReference userRef = db.collection("users").document(user.getUid());
            DocumentReference msgRef = conversationRef.collection("messages").document();
            msgId = msgRef.getId();

            final Message msg = new Message(msgId, conversationId, msgText);
            msg.setSent(true);

            //storing last msg values to a map
            Map<String, Object> map = new HashMap<>();
            map.put("lastMsg", msg.getMsgText());
            map.put("lastMsgTime", msg.getTimestamp());
            map.put("unseenMsgCount"+chatId, FieldValue.increment(1));

            //updating last msg info
            batch.set(conversationRef, map, SetOptions.merge());
            //sending msg
            batch.set(msgRef, msg);
            //adding chat to user chats
            batch.update(userRef, "chats", FieldValue.arrayUnion(chatId));
            //adding user to chat chats
            batch.update(chatRef, "chats", FieldValue.arrayUnion(user.getUid()));

            //committing atomic batch write operation
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "onComplete: msg sent");
                    } else {
                        Log.d(TAG, "onComplete: msg not sent");
                    }
                }
            });

            myRepo.insertMessage(msg);
            msgEditText.getText().clear();
            InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void addAttachment(View view) {

    }

    private void initViews(){
        CircleImageView profileImageView = findViewById(R.id.profileImageView);
        TextView nameTv = findViewById(R.id.nameTextView);
        TextView statusTv = findViewById(R.id.statusTextView);
        msgEditText = findViewById(R.id.msgEditText);

        myRepo = new MyRepo(getApplication());
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        adapter = new MessagesRecyclerAdapter();
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setAdapter(adapter);

        String chatId = getIntent().getStringExtra("chatId");
        String name = getIntent().getStringExtra("name");
        String status = getIntent().getStringExtra("status");

        nameTv.setText(name);
        statusTv.setText(status);

        File profileImage = new File(getExternalFilesDir("profilePictures"), chatId+".jpg");
        if (profileImage.exists()){
            profileImageView.setImageURI(Uri.fromFile(profileImage));
        }
    }
}