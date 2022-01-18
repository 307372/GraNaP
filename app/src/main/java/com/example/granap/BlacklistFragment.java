package com.example.granap;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class BlacklistFragment extends Fragment {

    public static String IGNORED_PREF = "ignoredPreferences";
    public static String IGNORED_SET = "ignoredSet";

    EditText iFilter;
    RecyclerView recyclerView;
    BlacklistAdapter blacklistAdapter;

    public BlacklistFragment() {} // Required empty public constructor

    public static BlacklistFragment newInstance() {
        return new BlacklistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_blacklist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        iFilter = view.findViewById(R.id.iFilter);

        recyclerView = view.findViewById(R.id.recyclerView);
        blacklistAdapter = new BlacklistAdapter(getContext());
        recyclerView.setAdapter(blacklistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}