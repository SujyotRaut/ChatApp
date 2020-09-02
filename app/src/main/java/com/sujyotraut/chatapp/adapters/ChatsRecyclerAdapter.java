package com.sujyotraut.chatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujyotraut.chatapp.R;

public class ChatsRecyclerAdapter extends RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder> {

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View ChatView = inflater.inflate(R.layout.item_chat, parent, false);

        return new ChatsViewHolder(ChatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
