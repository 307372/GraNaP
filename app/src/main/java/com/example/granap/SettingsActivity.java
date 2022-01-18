package com.example.granap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SettingsFragment settingsFragment;
    private BlacklistFragment blacklistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        settingsFragment = new SettingsFragment();
        blacklistFragment = new BlacklistFragment();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(settingsFragment, "Ustawienia");
        viewPagerAdapter.addFragment(blacklistFragment, "Czarna lista");

        viewPager.setAdapter(viewPagerAdapter);

    }

    public void saveSettingsButton(View x)
    {
        settingsFragment.saveSettingsOnClick();
        Toast.makeText(this, "Ustawienia zapisane", Toast.LENGTH_SHORT).show();
        finish();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public void removeIgnoredItem(View itemview)
    {
        BlacklistViewItem item = (BlacklistViewItem) itemview.getParent().getParent();
        String message = "Usunięto \"" + item.getWord() + "\"!";
        Toast.makeText(item.getContext(), message, Toast.LENGTH_SHORT).show();
        RecyclerView recyclerView = (RecyclerView) item.getParent();

        ((BlacklistAdapter) recyclerView.getAdapter()).removeItem(item.getWordIndex());
    }
}