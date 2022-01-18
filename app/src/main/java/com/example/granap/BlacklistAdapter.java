package com.example.granap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder>{

    private WordManager manager;
    private SortedMap<Integer,              // Index in adapter
            Pair<Integer, Integer>> items;  // Pair<Index in manager, Index in dictionary>

    public BlacklistAdapter(Context ctx) {
        manager = WordManager.get(ctx);
        items = manager.getAdapterMap();
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
        Pair<Integer, Integer> data = items.get(position);
        int wordIndex = data.second;
        int ignoredIndex = data.first;

        holder.blacklistViewItem.update(ignoredIndex, wordIndex, manager.getWordByIndex(wordIndex));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BlacklistViewHolder extends RecyclerView.ViewHolder {
        BlacklistViewItem blacklistViewItem;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            blacklistViewItem = (BlacklistViewItem) itemView;
        }
    }

    public void removeItem(int pos)
    {

        notifyItemRemoved(pos);
    }
}