package com.sujyotraut.chatapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.models.Chat;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chats ORDER BY lastMsgTime DESC")
    LiveData<List<Chat>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Chat> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Chat chat);

    @Query("UPDATE chats SET lastMsg = :lastMsg, lastMsgTime = :lastMsgTime, unseenMsgCount = :unseenMsgCount WHERE chatId = :chatId")
    void updateChat(String chatId, String lastMsg, Timestamp lastMsgTime, int unseenMsgCount);

    @Delete
    void delete(Chat chat);
}