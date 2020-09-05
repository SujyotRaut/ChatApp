package com.sujyotraut.chatapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.AddChatsRecyclerAdapter;

public class AddChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chats);

        RecyclerView recyclerView = findViewById(R.id.addChatRecyclerView);
        AddChatsRecyclerAdapter adapter = new AddChatsRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }
}