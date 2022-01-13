package com.example.granap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;

public class GameActivity extends AppCompatActivity {

    FileInputStream fileInputStream;
    TextView tvRandomWord;
    TypedArray dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dictionary = getResources().obtainTypedArray(R.array.dict);
        tvRandomWord = findViewById(R.id.tvRandomWord);
    }

    public void rerollWord(View x)
    {
        int max = dictionary.length();
        int index = (int) Math.floor(Math.random()*max);
        tvRandomWord.setText(dictionary.getString(index));
        int a =0;
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