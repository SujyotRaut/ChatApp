package com.sujyotraut.chatapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.models.Chat;
import com.sujyotraut.chatapp.models.Message;
import com.sujyotraut.chatapp.models.User;

import java.util.List;
import java.util.concurrent.Callable;

public class MyRepo {

    private ChatDao mChatDao;
    private UserDao mUserDao;
    private MessageDao mMessageDao;
    private LiveData<List<Chat>> allChats;
    private LiveData<List<User>> allUsers;

    public MyRepo(Application application) {
        ChatAppRoomDatabase db = ChatAppRoomDatabase.getDatabase(application);
        mChatDao = db.chatDao();
        mUserDao = db.userDao();
        mMessageDao = db.messageDao();
        allChats = mChatDao.getAll();
        allUsers = mUserDao.getAll();
    }

    public LiveData<List<Chat>> getAllChats() {
        return this.allChats;
    }

    public void updateChat(final String chatId, final String lastMsg,
                           final Timestamp lastMsgTime, final int unseenMsgCount){
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChatDao.updateChat(chatId, lastMsg, lastMsgTime, unseenMsgCount);
            }
        });
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

    public LiveData<List<Message>> getAllMessages(String conversationId) {
        return mMessageDao.getAll(conversationId);
    }

    public void insertMessage(final Message message) {
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mMessageDao.insert(message);
            }
        });
    }

    public void insertAllMessages(final List<Message> messages){
        ChatAppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mMessageDao.insertAll(messages);
            }
        });
    }
}
