package com.example.granap;

import static com.example.granap.SettingsFragment.SHARED_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlacklistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlacklistFragment extends Fragment {

    public static String IGNORED_PREF = "ignoredPreferences";
    public static String IGNORED_SET = "ignoredSet";

    RecyclerView recyclerView;
    BlacklistAdapter crimeAdapter;

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
        recyclerView = view.findViewById(R.id.recyclerView);
        crimeAdapter = new BlacklistAdapter(getContext());
        recyclerView.setAdapter(crimeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}