package com.sujyotraut.chatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.sujyotraut.chatapp.activites.ConversationActivity;
import com.sujyotraut.chatapp.activites.MainActivity;
import com.sujyotraut.chatapp.models.Chat;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRecyclerAdapter extends RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder> {

    private Context context;
    private List<Chat> chats;

    public ChatsRecyclerAdapter(Context context){
        this.context = context;
        this.chats = new ArrayList<>();
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
        final String chatId, name, lastMsg;
        Timestamp lastMsgTimestamp;
        int unseenMsgCount;

        Chat chat = chats.get(position);

        chatId = chat.getChatId();
        name = chat.getChatName();
        lastMsg = chat.getLastMsg();
        lastMsgTimestamp = chat.getLastMsgTime();
        unseenMsgCount = chat.getUnseenMsgCount();

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

        File profileImage = new File(context.getExternalFilesDir("profilePictures"), chatId+".jpg");
        if (profileImage.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(profileImage.getAbsolutePath());
            holder.profileImageView.setImageBitmap(bitmap);
        }else {
            holder.profileImageView.setImageResource(R.drawable.default_profile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, ConversationActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("name", name);
                intent.putExtra("status", "status");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
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

    public void setChats(List<Chat> chats){
        this.chats = chats;
        notifyDataSetChanged();
    }
}