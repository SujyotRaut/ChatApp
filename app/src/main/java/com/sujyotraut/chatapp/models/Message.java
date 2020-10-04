package com.sujyotraut.chatapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sujyotraut.chatapp.utils.MsgType;

@Entity(tableName = "messages")
public class Message {

    @NonNull
    @PrimaryKey
    private String msgId;
    private Timestamp timestamp;
    private String conversationId;
    private String msgText, senderId;
    private MsgType msgType;
    private Boolean sent, seen;

    public Message(){
        //no argument constructor is required for getting message instance from firestore document snapshot
    }

    public Message(@NonNull String msgId, String conversationId, String msgText) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.msgId = msgId;
        this.conversationId = conversationId;
        this.msgText = msgText;
        this.timestamp = Timestamp.now();
        this.senderId = user.getUid();
        this.msgType = MsgType.TEXT;
        this.sent = true;
        this.seen = false;
    }

    @NonNull
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(@NonNull String msgId) {
        this.msgId = msgId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }
}
