package com.example.granap;

import static com.example.granap.SettingsActivity.HIDE_WORD;
import static com.example.granap.SettingsActivity.SHARED_PREFERENCES;
import static com.example.granap.SettingsActivity.WORD_LENGTH_MAX;
import static com.example.granap.SettingsActivity.WORD_LENGTH_MIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;

public class GameActivity extends AppCompatActivity {

    TextView tvRandomWord;
    ConstraintLayout backgroundDiv;

    TypedArray dictionary;

    int wordLengthMax;
    int wordLengthMin;
    boolean hideWord;

    CountDownTimer timer = new CountDownTimer(1000, 100) {
        @Override public void onTick(long x) {}
        public void onFinish() {setWordInvisible();}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dictionary = getResources().obtainTypedArray(R.array.dict);
        tvRandomWord = findViewById(R.id.tvRandomWord);
        backgroundDiv = findViewById(R.id.backgroundDiv);

        rerollWord();

        backgroundDiv.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (hideWord) {
                    timer.cancel();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Pressed down
                            setWordVisible();
                            return true;
                        case MotionEvent.ACTION_UP:
                            // Released
                            setWordInvisible();
                            return true;
                        case MotionEvent.ACTION_CANCEL:
                            // Released - Dragged finger outside
                            setWordInvisible();
                            return true;
                    }
                }
                return false;
            }
        });
    }

    public void setWordVisible()
    {
        tvRandomWord.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    public void setWordInvisible()
    {
        tvRandomWord.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSettings();
        if (hideWord)
        {
            setWordInvisible();
        }
        else {
            tvRandomWord.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public void showWordFor1s()
    {
        timer.cancel();
        setWordVisible();
        timer.start();
    }

    public void rerollWordAdapter(View x)
    {
        rerollWord();
    }

    public void rerollWord()
    {
        int max = dictionary.length();
        int index = (int) Math.floor(Math.random()*max);
        tvRandomWord.setText(dictionary.getString(index));
        if (hideWord) {
            showWordFor1s();
        }
    }


    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        wordLengthMax = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MAX, "20"));
        wordLengthMin = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MIN, "1"));
        hideWord = sharedPreferences.getBoolean(HIDE_WORD, false);
    }

    public void showSettings(View x)
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void showHelp(View x)
    {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void googleTheWord(View v) {
        Intent googleStuff = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=" +
                        tvRandomWord.getText().toString().replace(" ", "+")));

        startActivity(googleStuff);
    }
}