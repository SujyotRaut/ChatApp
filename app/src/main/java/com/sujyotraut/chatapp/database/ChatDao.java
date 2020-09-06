package com.sujyotraut.chatapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sujyotraut.chatapp.models.ChatModel;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chats")
    LiveData<List<ChatModel>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChatModel> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChatModel chat);

    @Delete
    void delete(ChatModel chat);
}