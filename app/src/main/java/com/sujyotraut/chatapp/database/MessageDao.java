package com.sujyotraut.chatapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sujyotraut.chatapp.models.Chat;
import com.sujyotraut.chatapp.models.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    LiveData<List<Message>> getAll(String conversationId);

    @Query("SELECT COUNT(*) FROM messages")
    int getMessagesCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Message> messages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Delete
    void delete(Message message);
}
