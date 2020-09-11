package com.sujyotraut.chatapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sujyotraut.chatapp.models.Chat;
import com.sujyotraut.chatapp.models.User;

import java.util.List;

public class MyRepo {

    private ChatDao mChatDao;
    private UserDao mUserDao;
    private LiveData<List<Chat>> allChats;
    private LiveData<List<User>> allUsers;

    public MyRepo(Application application) {
        ChatAppRoomDatabase db = ChatAppRoomDatabase.getDatabase(application);
        mChatDao = db.chatDao();
        mUserDao = db.userDao();
        allChats = mChatDao.getAll();
        allUsers = mUserDao.getAll();
    }

    public LiveData<List<Chat>> getAllChats() {
        return this.allChats;
    }

    public void insertChat(final Chat chat) {
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChatDao.insert(chat);
            }
        });
    }

    public void insertAllChats(final List<Chat> chats){
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChatDao.insertAll(chats);
            }
        });
    }

    public LiveData<List<User>> getAllUsers() {
        return this.allUsers;
    }

    public void insertUser(final User user) {
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mUserDao.insert(user);
            }
        });
    }

    public void insertAllUsers(final List<User> users){
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mUserDao.insertAll(users);
            }
        });
    }
}
