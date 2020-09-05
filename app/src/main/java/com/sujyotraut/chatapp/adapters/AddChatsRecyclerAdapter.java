package com.sujyotraut.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujyotraut.chatapp.R;

public class AddChatsRecyclerAdapter extends RecyclerView.Adapter<AddChatsRecyclerAdapter.AddChatViewHolder> {

    @NonNull
    @Override
    public AddChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_add_chat, parent, false);
        return new AddChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class AddChatViewHolder extends RecyclerView.ViewHolder{

        public AddChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
