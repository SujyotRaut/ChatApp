package com.sujyotraut.chatapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sujyotraut.chatapp.models.ChatModel;

import java.util.List;

public class MyRepo {

    private ChatDao mChatDao;
    private LiveData<List<ChatModel>> allChats;

    public MyRepo(Application application) {
        ChatAppRoomDatabase db = ChatAppRoomDatabase.getDatabase(application);
        mChatDao = db.chatDao();
        allChats = mChatDao.getAll();
    }

    public LiveData<List<ChatModel>> getAllChats() {
        return this.allChats;
    }

    public void insert(final ChatModel chat) {
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChatDao.insert(chat);
            }
        });
    }

    public void insertAll(final List<ChatModel> chats){
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChatDao.insertAll(chats);
            }
        });
    }
}
