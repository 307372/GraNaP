package com.example.granap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        iWordLengthMax = findViewById(R.id.iWordLengthMax);
        iWordLengthMin = findViewById(R.id.iWordLengthMin);
        swHideWord = findViewById(R.id.swHideWord);
        swRerollWordsStartingWithP = findViewById(R.id.swRerollWordsStartingWithP);

        loadSettings();
    }

    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        iWordLengthMax.setText(sharedPreferences.getString(WORD_LENGTH_MAX, "20"));
        iWordLengthMin.setText(sharedPreferences.getString(WORD_LENGTH_MIN, "1"));
        swHideWord.setChecked(sharedPreferences.getBoolean(HIDE_WORD, false));
        swRerollWordsStartingWithP.setChecked(sharedPreferences.getBoolean(REROLL_WORDS_STARTING_WITH_P, false));
    }

    public void saveSettings()
    {
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