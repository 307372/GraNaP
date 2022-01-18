package com.example.granap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.SortedMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder>{

    private final WordManager manager;
    private SortedMap<Integer, Integer> items;  // Pair<Index in manager, Index in dictionary>

    Integer[] keys;
    private final Lock lock = new ReentrantLock();

    public BlacklistAdapter(Context ctx) {
        manager = WordManager.get(ctx);

        items = manager.getAdapterMap();
        keys = items.keySet().toArray(new Integer[0]);
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
    public void onBindViewHolder(@NonNull BlacklistViewHolder holder, int position)
    {
        int wordIndex = items.get(keys[position]);
        holder.blacklistViewItem.update(wordIndex, manager.getWordByIndex(wordIndex));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BlacklistViewHolder extends RecyclerView.ViewHolder {
        final BlacklistViewItem blacklistViewItem;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            blacklistViewItem = (BlacklistViewItem) itemView;
        }
    }

    public void removeItem(Integer wordIndex) {
        lock.lock();
        try {
            if (!items.containsValue(wordIndex))
                return; // in case the same delete op was started 2x

            int i;
            for (i = 0; i < keys.length; ++i) {
                if (wordIndex.equals(items.get(keys[i]))) break;
            }
            assert i != keys.length;

            manager.removeFromIgnored(items.get(keys[i]));
            items.remove(keys[i]);
            keys = items.keySet().toArray(keys);
            notifyItemRemoved(i);
        } finally {
            lock.unlock();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String pattern)
    {
        lock.lock();
        try {
            items = manager.getFilteredAdapterMap(pattern.toLowerCase());
            keys = items.keySet().toArray(keys);
            notifyDataSetChanged();
        } finally {
            lock.unlock();
        }
    }
}