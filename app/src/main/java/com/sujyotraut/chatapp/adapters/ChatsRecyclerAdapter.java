package com.sujyotraut.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.models.ChatModel;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRecyclerAdapter extends RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder> {

    private List<ChatModel> chatModelList;

    public ChatsRecyclerAdapter(List<ChatModel> chatModelList){
        this.chatModelList = chatModelList;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View ChatView = inflater.inflate(R.layout.item_chat, parent, false);

        return new ChatsViewHolder(ChatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        String name, lastMsg;
        int unseenMsgCount;
        String lastMsgTime;

        name = chatModelList.get(position).getChatName();
        lastMsg = chatModelList.get(position).getLastMsg();
        lastMsgTime = chatModelList.get(position).getLastMsgTime();
        unseenMsgCount = chatModelList.get(position).getUnseenMsgCount();

        holder.nameTv.setText(name);
        holder.lastMsgTv.setText(lastMsg);
        holder.lastMsgTimeTv.setText("nun");
        holder.unseenMsgCountTv.setText(String.valueOf(unseenMsgCount));
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, lastMsgTv, lastMsgTimeTv, unseenMsgCountTv;
        public CircleImageView profileImageView;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.chatNameTv);
            lastMsgTv = itemView.findViewById(R.id.lastMsgTv);
            lastMsgTimeTv = itemView.findViewById(R.id.lastMsgTimeTv);
            unseenMsgCountTv = itemView.findViewById(R.id.unseenMsgCountTv);
            profileImageView = itemView.findViewById(R.id.chatProfileImageView);
        }
    }

    public void change(List<ChatModel> list){
        chatModelList = list;
        notifyDataSetChanged();
    }
}
