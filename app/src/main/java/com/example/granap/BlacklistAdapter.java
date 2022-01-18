package com.example.granap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder>{

    WordManager manager;
    ArrayList<BlacklistViewItem> items;

    public BlacklistAdapter(Context ctx) {
        manager = WordManager.get(ctx);
    }

    @NonNull
    @Override
    public BlacklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BlacklistViewItem itemView = new BlacklistViewItem(parent.getContext());

        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return new BlacklistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlacklistViewHolder holder, int position) {
        int wordIndex = manager.getNthValueFromIgnored(position);

        holder.blacklistViewItem.update(wordIndex, manager.getWordByIndex(wordIndex));
    }

    @Override
    public int getItemCount() {
        return manager.getIgnoredSize();
    }

    public class BlacklistViewHolder extends RecyclerView.ViewHolder {
        BlacklistViewItem blacklistViewItem;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            blacklistViewItem = (BlacklistViewItem) itemView;
        }
    }

}