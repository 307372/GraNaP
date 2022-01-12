package com.example.granap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public void showSettings(View x)
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void showHelp(View x)
    {
        startActivity(new Intent(this, HelpActivity.class));
    }

}