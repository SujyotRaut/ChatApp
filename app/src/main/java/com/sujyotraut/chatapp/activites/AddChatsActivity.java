package com.sujyotraut.chatapp.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.FloatEvaluator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.UsersRecyclerAdapter;
import com.sujyotraut.chatapp.database.MyRepo;
import com.sujyotraut.chatapp.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AddChatsActivity extends AppCompatActivity {

    private static final String TAG = "myTag";
    private RecyclerView recyclerView;
    private UsersRecyclerAdapter adapter;
    private MyRepo myRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chats);

        recyclerView = findViewById(R.id.addChatRecyclerView);
        adapter = new UsersRecyclerAdapter(AddChatsActivity.this);
        recyclerView.setAdapter(adapter);

        myRepo = new MyRepo(getApplication());
        myRepo.getAllUsers().observe(AddChatsActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference profilePicturesRef = storage.getReference("profilePictures");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.orderBy("name").addSnapshotListener(AddChatsActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    List<DocumentChange> changes = value.getDocumentChanges();
                    for (DocumentChange change: changes){

                        final String chatId = change.getDocument().getId();
                        if (chatId.equals(user.getUid())){
                            continue;
                        }

                        String name = change.getDocument().getString("name");
                        String status = change.getDocument().getString("status");

                        User user = new User(chatId);
                        user.setName(name);
                        user.setStatus(status);

                        myRepo.insertUser(user);

                        final File distFile = new File(getExternalFilesDir("profilePictures"), chatId+".jpg");
                        if (!distFile.exists()){
                            long ONE_MEGABYTE = 1024*1024;
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

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}