package com.sujyotraut.chatapp.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.R;

import javax.annotation.Nonnull;

@Entity(tableName = "chats")
public class ChatModel {

    @NonNull
    @PrimaryKey
    private String id;
    private String chatName;
    private String lastMsg;
    private int unseenMsgCount;
    private String lastMsgTime;
    private String profilePicture;

    public ChatModel(String id, String chatName){

        if (!chatName.isEmpty()) {this.chatName = chatName;}
        else {this.chatName = "nun";}

        this.lastMsg = "nun";
        this.lastMsgTime = "nun";
        this.unseenMsgCount = 0;
        this.id = id;
    }

    public void setChatName(String chatName) {
        if (chatName != null){
            this.chatName = chatName;
        }
    }

    public void setLastMsg(String lastMsg) {
        if (chatName != null){
            this.lastMsg = lastMsg;
        }
    }

    public void setUnseenMsgCount(int unseenMsgCount) {
        if (chatName != null){
            this.unseenMsgCount = unseenMsgCount;
        }
    }

    public void setLastMsgTime(String lastMsgTime) {
        if (chatName != null){
            this.lastMsgTime = lastMsgTime;
        }
    }

    public void setProfilePicture(String profilePicture) {
        if (chatName != null){
            this.profilePicture = profilePicture;
        }
    }

    public String getChatName(){
        return chatName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getUnseenMsgCount() {
        return unseenMsgCount;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
