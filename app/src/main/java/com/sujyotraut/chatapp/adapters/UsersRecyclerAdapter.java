package com.sujyotraut.chatapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.models.User;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> users;
    private Context context;

    public UsersRecyclerAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
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
        String name = user.getName();
        String status = user.getStatus();

        if (name != null){
            holder.nameTv.setText(name);
        }

        holder.nameTv.setText(name);
        if (status != null){
            holder.statusTv.setText(status);
        }

        File profile = new File(context.getExternalFilesDir("profilePictures"), user.getId()+".jpg");
        if (profile.exists()){
            Uri uri = Uri.fromFile(profile);
            holder.profileImageView.setImageURI(uri);
        }
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
}
