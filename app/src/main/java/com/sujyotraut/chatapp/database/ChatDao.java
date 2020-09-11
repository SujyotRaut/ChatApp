package com.sujyotraut.chatapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sujyotraut.chatapp.models.Chat;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chats")
    LiveData<List<Chat>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Chat> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Chat chat);

    @Delete
    void delete(Chat chat);
}