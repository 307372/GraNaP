package com.example.granap;

import static com.example.granap.SettingsActivity.HIDE_WORD;
import static com.example.granap.SettingsActivity.REROLL_WORDS_STARTING_WITH_P;
import static com.example.granap.SettingsActivity.SHARED_PREFERENCES;
import static com.example.granap.SettingsActivity.WORD_LENGTH_MAX;
import static com.example.granap.SettingsActivity.WORD_LENGTH_MIN;

import static java.lang.Math.min;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;

public class GameActivity extends AppCompatActivity {

    TextView tvRandomWord;
    ConstraintLayout backgroundDiv;

    TypedArray dictionary;
    TypedArray dictWordLenAmounts;
    TypedArray dictWordLenIndices;
    int wordlengthMinAvailable;
    int wordlengthMaxAvailable;

    int wordLengthMax;
    int wordLengthMin;
    boolean hideWord;
    boolean rerollPWords;

    CountDownTimer timer = new CountDownTimer(1000, 100) {
        @Override public void onTick(long x) {}
        public void onFinish() {setWordInvisible();}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadResources();
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

    private void loadResources()
    {
        Resources res = getResources();
        dictionary = res.obtainTypedArray(R.array.dict);
        dictWordLenAmounts = res.obtainTypedArray(R.array.len_amount);
        dictWordLenIndices = res.obtainTypedArray(R.array.len_index);
        wordlengthMinAvailable = res.getInteger(R.integer.min_len);
        wordlengthMaxAvailable = res.getInteger(R.integer.max_len);

        tvRandomWord = findViewById(R.id.tvRandomWord);
        backgroundDiv = findViewById(R.id.backgroundDiv);
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

        if (hideWord) setWordInvisible();
        else tvRandomWord.setInputType(InputType.TYPE_CLASS_TEXT);
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

    public String getRandomWord()
    {
        int start = dictWordLenIndices.getInt(wordLengthMin, -1);
        int stop = dictWordLenIndices.getInt(wordLengthMax, -1)
                 + dictWordLenAmounts.getInt(wordLengthMax, -1);
        // Log.i("wordLengthMin:", String.valueOf(wordLengthMin));
        // Log.i("wordLengthMax:", String.valueOf(wordLengthMax));
        assert dictWordLenIndices.getInt(wordLengthMin, -1) != -1;
        assert dictWordLenIndices.getInt(wordLengthMax, -1) != -1;
        assert dictWordLenAmounts.getInt(wordLengthMax, -1) != -1;

        return getRandomWord(start, stop);
    }

    public String getRandomWord(int start, int stop)
    {
        int max = min(stop, dictionary.length());
        int index = start + (int) Math.floor(Math.random()*(max-start));
        // Log.i("random index:", String.valueOf(index));
        return dictionary.getString(index);
    }

    public void rerollWord()
    {
        String newWord = getRandomWord();
        // TODO: fix what happens when rerolling is happening
        if (rerollPWords) while(newWord.startsWith("p")) newWord = getRandomWord(0, dictionary.length());

        tvRandomWord.setText(newWord);
        if (hideWord) showWordFor1s();
    }


    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        wordLengthMax = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MAX, "20"));
        wordLengthMin = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MIN, "1"));
        hideWord = sharedPreferences.getBoolean(HIDE_WORD, false);
        rerollPWords = sharedPreferences.getBoolean(REROLL_WORDS_STARTING_WITH_P, false);
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