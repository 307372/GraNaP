package com.example.granap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public static final String WORD_LENGTH_MAX = "WordLengthMax";
    public static final String WORD_LENGTH_MIN = "WordLengthMin";
    public static final String HIDE_WORD = "HideWord";
    public static final String REROLL_WORDS_STARTING_WITH_P = "RerollWordsStartingWithP";

    EditText iWordLengthMax;
    EditText iWordLengthMin;
    Switch swHideWord;
    Switch swRerollWordsStartingWithP;
    TextView tvWordLength;

    int minWordLen;
    int maxWordLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swHideWord = findViewById(R.id.swHideWord);
        swRerollWordsStartingWithP = findViewById(R.id.swRerollWordsStartingWithP);

        setupWordLengthSettings();

        loadSettings();
    }

    private void setupWordLengthSettings()
    {
        minWordLen = getResources().getInteger(R.integer.min_len);
        maxWordLen = getResources().getInteger(R.integer.max_len);

        tvWordLength = findViewById(R.id.tvWordLength);
        String wordLengthLabel = getResources().getText(R.string.dlugosc_hasel).toString()
                + " (" + minWordLen + "-" + maxWordLen + ")";
        tvWordLength.setText(wordLengthLabel);

        iWordLengthMin = findViewById(R.id.iWordLengthMin);
        iWordLengthMin.setOnFocusChangeListener(new OnFocusChangeListenerMin(minWordLen));
        iWordLengthMin.setFilters(new InputFilter[]{new InputFilterMinMax(maxWordLen)});

        iWordLengthMax = findViewById(R.id.iWordLengthMax);
        iWordLengthMax.setOnFocusChangeListener(new OnFocusChangeListenerMin(minWordLen));
        iWordLengthMax.setFilters(new InputFilter[]{new InputFilterMinMax(maxWordLen)});
    }

    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        iWordLengthMax.setText(sharedPreferences.getString(WORD_LENGTH_MAX, ""+maxWordLen));
        iWordLengthMin.setText(sharedPreferences.getString(WORD_LENGTH_MIN, ""+minWordLen));
        swHideWord.setChecked(sharedPreferences.getBoolean(HIDE_WORD, false));
        swRerollWordsStartingWithP.setChecked(sharedPreferences.getBoolean(REROLL_WORDS_STARTING_WITH_P, false));
    }

    public void saveSettings()
    {
        // triggering value filters
        iWordLengthMin.clearFocus();
        iWordLengthMax.clearFocus();

        int min = Integer.parseInt(String.valueOf(iWordLengthMin.getText()));
        int max = Integer.parseInt(String.valueOf(iWordLengthMax.getText()));


        if (min > max)
        {
            iWordLengthMax.setText(String.valueOf(min));
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(WORD_LENGTH_MAX, iWordLengthMax.getText().toString());
        editor.putString(WORD_LENGTH_MIN, iWordLengthMin.getText().toString());
        editor.putBoolean(HIDE_WORD, swHideWord.isChecked());
        editor.putBoolean(REROLL_WORDS_STARTING_WITH_P, swRerollWordsStartingWithP.isChecked());

        editor.apply();
    }

    public void saveSettingsButton(View x)
    {
        saveSettings();
        Toast.makeText(this, "Ustawienia zapisane", Toast.LENGTH_SHORT).show();
        finish();
    }
}