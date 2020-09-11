package com.sujyotraut.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.models.Message;
import com.sujyotraut.chatapp.utils.MsgType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT = 19;
    private static final int IMAGE = 20;
    private static final int VIDEO = 21;
    private static final int AUDIO = 22;
    private static final int FILE = 23;
    private List<Message> messages;

    public MessagesRecyclerAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        MsgType msgType = messages.get(position).getMsgType();
        switch (msgType){
            case TEXT:
                return TEXT;
            case IMAGE:
                return IMAGE;
            case VIDEO:
                return VIDEO;
            case AUDIO:
                return AUDIO;
            default:
                return FILE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case TEXT:
                View textMsgView = inflater.inflate(R.layout.item_msg_text, parent, false);
                return new TextMsgViewHolder(textMsgView);
            case IMAGE:
                View imageMsgView = inflater.inflate(R.layout.item_msg_image, parent, false);
                return new TextMsgViewHolder(imageMsgView);
            case VIDEO:
                View videoMsgView = inflater.inflate(R.layout.item_msg_video, parent, false);
                return new TextMsgViewHolder(videoMsgView);
            case AUDIO:
                View audioMsgView = inflater.inflate(R.layout.item_msg_audio, parent, false);
                return new TextMsgViewHolder(audioMsgView);
            default:
                View fileMsgView = inflater.inflate(R.layout.item_msg_file, parent, false);
                return new TextMsgViewHolder(fileMsgView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        TextMsgViewHolder textMsgViewHolder = ((TextMsgViewHolder) holder);
        textMsgViewHolder.msgTv.setText(message.getMsgText());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String date = dateFormat.format(message.getTimestamp().toDate());
        textMsgViewHolder.timeTv.setText(date);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (message.getSenderId().equals(user.getUid())) {
            ConstraintLayout root = ((ConstraintLayout) holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class TextMsgViewHolder extends RecyclerView.ViewHolder{
        private TextView msgTv, timeTv;
        private ImageView tickImageView;
        public TextMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTv = itemView.findViewById(R.id.msgTextView);
            timeTv = itemView.findViewById(R.id.timeTextView);
            tickImageView = itemView.findViewById(R.id.tickImageView);
        }
    }

}
