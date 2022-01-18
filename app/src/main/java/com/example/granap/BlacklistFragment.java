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

    public static final String IGNORED_PREF = "ignoredPreferences";
    public static final String IGNORED_SET = "ignoredSet";

    EditText iFilter;
    RecyclerView recyclerView;
    BlacklistAdapter blacklistAdapter;

    public BlacklistFragment() {} // Required empty public constructor

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
        iFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                blacklistAdapter.filter(s.toString());
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        blacklistAdapter = new BlacklistAdapter(getContext());
        recyclerView.setAdapter(blacklistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}