package com.sujyotraut.chatapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sujyotraut.chatapp.models.Chat;
import com.sujyotraut.chatapp.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(Converters.class)
@Database(entities = {Chat.class, User.class}, version = 1, exportSchema = false)
public abstract class ChatAppRoomDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();
    public abstract UserDao userDao();

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