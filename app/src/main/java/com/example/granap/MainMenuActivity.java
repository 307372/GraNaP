package com.example.granap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("unused")
    public void showGame(View x)
    {
        startActivity(new Intent(this, GameActivity.class));
    }

    @SuppressWarnings("unused")
    public void showSettings(View x)
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @SuppressWarnings("unused")
    public void showHelp(View x)
    {
        startActivity(new Intent(this, HelpActivity.class));
    }
}