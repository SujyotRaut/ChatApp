package com.sujyotraut.chatapp.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sujyotraut.chatapp.database.MyRepo;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private MyRepo myRepo;
    private LiveData<List<ChatModel>> mAllChats;

    public MyViewModel(@NonNull Application application) {
        super(application);
        this.myRepo = new MyRepo(application);
        this.mAllChats = myRepo.getAllChats();
    }

    public LiveData<List<ChatModel>> getAllChat(){
        return this.mAllChats;
    }

    public void insertAll(List<ChatModel> chats){
        myRepo.insertAll(chats);
    }

    public void insert(ChatModel chat){
        myRepo.insert(chat);
    }

}
