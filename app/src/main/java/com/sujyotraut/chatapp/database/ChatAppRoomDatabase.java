package com.sujyotraut.chatapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sujyotraut.chatapp.models.ChatModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ChatModel.class}, version = 1, exportSchema = false)
public abstract class ChatAppRoomDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();

    private static volatile ChatAppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ChatAppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ChatAppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ChatAppRoomDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}