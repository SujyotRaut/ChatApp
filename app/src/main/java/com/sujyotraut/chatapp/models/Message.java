package com.sujyotraut.chatapp.models;

import androidx.room.PrimaryKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;

public class Message {

    @PrimaryKey
    private Calendar timestamp;
    private String chatId;
    private String msg, senderId;
    private Boolean seen;

    public Message(String chatId, String msg) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.chatId = chatId;
        this.msg = msg;
        this.timestamp = Calendar.getInstance();
        this.senderId = user.getUid();
        this.seen = false;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
