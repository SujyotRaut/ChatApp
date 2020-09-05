package com.sujyotraut.chatapp.models;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.R;

public class ChatModel {

    private String chatName;
    private String lastMsg;
    private int unseenMsgCount;
    private Timestamp lastMsgTime;
    private Uri profilePicture;

    public ChatModel(String chatName){

        if (!chatName.isEmpty()) {this.chatName = chatName;}
        else {this.chatName = "nun";}

        this.lastMsg = "nun";
        this.lastMsgTime = Timestamp.now();
        this.unseenMsgCount = 0;
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

    public void setLastMsgTime(Timestamp lastMsgTime) {
        if (chatName != null){
            this.lastMsgTime = lastMsgTime;
        }
    }

    public void setProfilePicture(Uri profilePicture) {
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

    public Timestamp getLastMsgTime() {
        return lastMsgTime;
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }
}
