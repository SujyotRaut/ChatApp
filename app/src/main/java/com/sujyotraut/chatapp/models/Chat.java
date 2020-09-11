package com.sujyotraut.chatapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.Timestamp;

@Entity(tableName = "chats")
public class Chat {

    @NonNull
    @PrimaryKey
    private String chatId;
    private String chatName;
    private String lastMsg;
    private int unseenMsgCount;
    private Timestamp lastMsgTime;

    public Chat(@NonNull String chatId){
        this.chatId = chatId;
        this.chatName = "nun";
        this.lastMsg = "nun";
        this.lastMsgTime = Timestamp.now();
        this.unseenMsgCount = 0;
    }

    @NonNull
    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getUnseenMsgCount() {
        return unseenMsgCount;
    }

    public void setUnseenMsgCount(int unseenMsgCount) {
        this.unseenMsgCount = unseenMsgCount;
    }

    public Timestamp getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Timestamp lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

}
