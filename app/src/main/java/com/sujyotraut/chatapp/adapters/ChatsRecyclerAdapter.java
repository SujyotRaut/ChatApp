package com.sujyotraut.chatapp.adapters;

import android.icu.text.RelativeDateTimeFormatter;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.type.TimeOfDay;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.MainActivity;
import com.sujyotraut.chatapp.models.Chat;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRecyclerAdapter extends RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder> {

    private List<Chat> chatModelList;
    private View.OnClickListener mClickListener;

    public ChatsRecyclerAdapter(List<Chat> chatModelList){
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
        Timestamp lastMsgTimestamp;
        Uri profilePicture;
        int unseenMsgCount;

        name = chatModelList.get(position).getChatName();
        lastMsg = chatModelList.get(position).getLastMsg();
        lastMsgTimestamp = chatModelList.get(position).getLastMsgTime();
        unseenMsgCount = chatModelList.get(position).getUnseenMsgCount();

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat1 = new SimpleDateFormat("DD/MM/YY");
        int diff = (int) (Timestamp.now().getSeconds() - lastMsgTimestamp.getSeconds());
        int diffInHours = diff / 3600;

        String lastMsgTime = "";
        if (diff < 60){
            lastMsgTime = "just now";
        }else if (diffInHours < 24){
            lastMsgTime = dateFormat.format(lastMsgTimestamp.toDate());
        }else if (diffInHours < 48){
            lastMsgTime = "yesterday";
        }else {
            lastMsgTime = dateFormat1.format(lastMsgTimestamp.toDate());
        }

        holder.nameTv.setText(name);
        holder.lastMsgTv.setText(lastMsg);
        holder.lastMsgTimeTv.setText(lastMsgTime);
        holder.unseenMsgCountTv.setText(String.valueOf(unseenMsgCount));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "onClick: ");
            }
        });
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
}
