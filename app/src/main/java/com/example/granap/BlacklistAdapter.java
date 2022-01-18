package com.example.granap;

import static com.example.granap.BlacklistFragment.IGNORED_PREF;
import static com.example.granap.BlacklistFragment.IGNORED_SET;
import static com.example.granap.SettingsFragment.SHARED_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder>{

    Context context;
    WordManager manager;

    public BlacklistAdapter(Context ctx) {
        context = ctx;
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
        int index = manager.getNthIndexOfIgnored(position);

        holder.blacklistViewItem.update(index, manager.getWordByIndex(index));
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