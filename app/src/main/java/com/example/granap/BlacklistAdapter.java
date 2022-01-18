package com.example.granap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder>{

    private WordManager manager;
    private SortedMap<Integer, Integer> items;  // Pair<Index in manager, Index in dictionary>

    Integer[] keys;
    private final Lock lock = new ReentrantLock();
    final Object mutex = new Object();

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
        int key = keys[position];

        int wordIndex = items.get(key);
        int ignoredIndex = key;

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

    public void removeItem(Integer wordIndex) {
        lock.lock();
        try {
            if (!items.containsValue(wordIndex)) return;
            int i;
            for (i = 0; i < keys.length; ++i) {
                int ithKey = keys[i];
                Integer newWordIndex = items.get(ithKey);
                if (wordIndex.equals(newWordIndex)) break;
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