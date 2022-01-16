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
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    TextView tvRandomWord;
    ConstraintLayout backgroundDiv;

    private TypedArray dictionary;
    private TypedArray dictWordLenAmounts;
    private TypedArray dictWordLenIndices;
    private int wordlengthMinAvailable;
    private int wordlengthMaxAvailable;

    private int wordLengthMaxSetting;
    private int wordLengthMinSetting;
    boolean hideWord;
    boolean rerollPWords;

    static private String STATE_WORD = "stateWord";

    CountDownTimer timer = new CountDownTimer(1000, 100) {
        @Override public void onTick(long x) {}
        public void onFinish() {setWordInvisible();}
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_WORD, tvRandomWord.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        tvRandomWord.setText(savedInstanceState.getString(STATE_WORD));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadResources();
        loadSettings();

        if (savedInstanceState != null) {
            tvRandomWord.setText(savedInstanceState.getString(STATE_WORD));
        }
        else rerollWord();

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

    @Override
    protected void onPause() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATE_WORD, tvRandomWord.getText().toString());
        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadSettings();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        tvRandomWord.setText(sharedPreferences.getString(STATE_WORD, getRandomWord()));

        if (hideWord) setWordInvisible();
        else setWordVisible();
    }

    private void loadResources()
    {
        Resources res = getResources();
        dictionary = res.obtainTypedArray(R.array.dict);
        wordlengthMinAvailable = res.getInteger(R.integer.min_len);
        wordlengthMaxAvailable = res.getInteger(R.integer.max_len);
        dictWordLenAmounts = res.obtainTypedArray(R.array.len_amount);
        dictWordLenIndices = res.obtainTypedArray(R.array.len_index);

        tvRandomWord = findViewById(R.id.tvRandomWord);
        backgroundDiv = findViewById(R.id.backgroundDiv);
    }

    private void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        wordLengthMaxSetting = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MAX, ""+wordlengthMaxAvailable));
        wordLengthMinSetting = Integer.parseInt(sharedPreferences.getString(WORD_LENGTH_MIN, ""+wordlengthMinAvailable));
        hideWord = sharedPreferences.getBoolean(HIDE_WORD, false);
        rerollPWords = sharedPreferences.getBoolean(REROLL_WORDS_STARTING_WITH_P, false);
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

    private String getRandomWord()
    {
        int start = dictWordLenIndices.getInt(wordLengthMinSetting, -1);
        int stop = dictWordLenIndices.getInt(wordLengthMaxSetting, -1)
                 + dictWordLenAmounts.getInt(wordLengthMaxSetting, -1);

        return getRandomWord(start, stop);
    }

    private String getRandomWord(int start, int stop)
    {
        int max = min(stop, dictionary.length());
        int index = start + (int) Math.floor(Math.random()*(max-start));

        return dictionary.getString(index);
    }

    private void rerollWord()
    {
        String newWord = getRandomWord();
        if (rerollPWords) while(newWord.startsWith("p")) newWord = getRandomWord();

        tvRandomWord.setText(newWord);
        if (hideWord) showWordFor1s();
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