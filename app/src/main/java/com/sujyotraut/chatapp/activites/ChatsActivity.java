package com.sujyotraut.chatapp.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.adapters.ChatsRecyclerAdapter;

public class ChatsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.TAG;

    private Toolbar topAppBar;
    private RecyclerView chatsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        initViews();

    }

    private void initViews(){

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);
        ChatsRecyclerAdapter recyclerAdapter = new ChatsRecyclerAdapter();
        chatsRecyclerView.setAdapter(recyclerAdapter);

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