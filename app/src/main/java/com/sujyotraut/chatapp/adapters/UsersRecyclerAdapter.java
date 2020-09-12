package com.sujyotraut.chatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.AddChatsActivity;
import com.sujyotraut.chatapp.activites.ConversationActivity;
import com.sujyotraut.chatapp.models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> users;
    private Context context;

    public UsersRecyclerAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        final String chatId = user.getId();
        final String name = user.getName();
        final String status = user.getStatus();

        if (name != null){
            holder.nameTv.setText(name);
        }

        holder.nameTv.setText(name);
        if (status != null){
            holder.statusTv.setText(status);
        }else {
            holder.statusTv.setText(context.getResources().getString(R.string.default_status));
        }

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
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImageView;
        private TextView nameTv, statusTv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.addChatProfilePicture);
            nameTv = itemView.findViewById(R.id.addChatNameTv);
            statusTv = itemView.findViewById(R.id.addChatStatusTv);
        }
    }

    public void setUsers(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }
}
