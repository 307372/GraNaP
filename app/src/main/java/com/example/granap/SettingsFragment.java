package com.example.granap;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

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

    public SettingsFragment() {} // Required empty public constructor

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        swHideWord = view.findViewById(R.id.swHideWord);
        swRerollWordsStartingWithP = view.findViewById(R.id.swRerollWordsStartingWithP);

        setupWordLengthSettings(view);
        loadSettings();
    }


    private void setupWordLengthSettings(View view)
    {
        minWordLen = getResources().getInteger(R.integer.min_len);
        maxWordLen = getResources().getInteger(R.integer.max_len);

        tvWordLength = view.findViewById(R.id.tvWordLength);
        String wordLengthLabel = getResources().getText(R.string.dlugosc_hasel).toString()
                + " (" + minWordLen + "-" + maxWordLen + ")";
        tvWordLength.setText(wordLengthLabel);

        iWordLengthMin = view.findViewById(R.id.iWordLengthMin);
        iWordLengthMin.setOnFocusChangeListener(new OnFocusChangeListenerMin(minWordLen));
        iWordLengthMin.setFilters(new InputFilter[]{new InputFilterMinMax(maxWordLen)});

        iWordLengthMax = view.findViewById(R.id.iWordLengthMax);
        iWordLengthMax.setOnFocusChangeListener(new OnFocusChangeListenerMin(minWordLen));
        iWordLengthMax.setFilters(new InputFilter[]{new InputFilterMinMax(maxWordLen)});
    }

    public void loadSettings()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(WORD_LENGTH_MAX, iWordLengthMax.getText().toString());
        editor.putString(WORD_LENGTH_MIN, iWordLengthMin.getText().toString());
        editor.putBoolean(HIDE_WORD, swHideWord.isChecked());
        editor.putBoolean(REROLL_WORDS_STARTING_WITH_P, swRerollWordsStartingWithP.isChecked());

        editor.apply();
    }

    public void saveSettingsOnClick()
    {
        saveSettings();
    }
}
