package com.sujyotraut.chatapp.models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sujyotraut.chatapp.activites.ChatsActivity;
import com.sujyotraut.chatapp.activites.MainActivity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private static final String TAG = MainActivity.TAG;
    private String msg, senderName, senderId;
    private URI image, video, audio, document;
    private Uri senderProfile;
    private Timestamp timeStamp;
    private Boolean sent, seen;

    public Message(String msg) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.msg = msg;
        this.timeStamp = Timestamp.now();
        this.senderId = user.getUid();
        this.senderName = user.getDisplayName();
        this.senderProfile = user.getPhotoUrl();

        Log.d(TAG, "sender name: " + senderName);
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public Uri getSenderProfile() {
        return senderProfile;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public URI getVideo() {
        return video;
    }

    public void setVideo(URI video) {
        this.video = video;
    }

    public URI getAudio() {
        return audio;
    }

    public void setAudio(URI audio) {
        this.audio = audio;
    }

    public URI getDocument() {
        return document;
    }

    public void setDocument(URI document) {
        this.document = document;
    }

    public Boolean getSent() {
        return sent;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void send(final String targetId){
        String chatId = ChatsActivity.getCompoundId(targetId);
        CollectionReference chatsRef = FirebaseFirestore.getInstance().collection("chats");
        final DocumentReference chatRef = chatsRef.document(chatId);

        //sending msg
        final DocumentReference msgRef = chatRef.collection("messages").document();
        msgRef.set(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    msgRef.update("sent", true);
                    Log.d(TAG, "msg sent");
                }else {
                    Log.d(TAG, "msg not sent");
                }
            }
        });

        chatRef.update("lastMsg", msg, "lastMsgTime", timeStamp, "unseenMsgCount"+targetId, FieldValue.increment(1));

    }
}
