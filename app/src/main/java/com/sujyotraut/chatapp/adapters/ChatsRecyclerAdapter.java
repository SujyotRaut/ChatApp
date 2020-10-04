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
import java.util.Locale;

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

        final Chat chat = chats.get(position);

        Calendar lastMsgTimestamp;
        lastMsgTimestamp = Calendar.getInstance();
        lastMsgTimestamp.setTime(chat.getLastMsgTime().toDate());

        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY", Locale.ENGLISH);
        int diff = (int) (Calendar.getInstance().getTimeInMillis() - lastMsgTimestamp.getTimeInMillis());

        String lastMsgTime = "";
        if (diff < 60000){
            lastMsgTime = "just now";
        }else if (lastMsgTimestamp.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)){
            lastMsgTime = timeFormat.format(lastMsgTimestamp.getTime());
        }else if (lastMsgTimestamp.get(Calendar.DAY_OF_YEAR) == (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1)){
            lastMsgTime = "yesterday";
        }else {
            lastMsgTime = dateFormat.format(lastMsgTimestamp.getTime());
        }

        holder.nameTv.setText(chat.getChatName());
        holder.lastMsgTv.setText(chat.getLastMsg());
        holder.lastMsgTimeTv.setText(lastMsgTime);
        holder.unseenMsgCountTv.setText(String.valueOf(chat.getUnseenMsgCount()));

        File profileImage = new File(context.getExternalFilesDir("profilePictures"),chat.getChatId()+".jpg");
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
                intent.putExtra("chatId", chat.getChatId());
                intent.putExtra("name", chat.getChatName());
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