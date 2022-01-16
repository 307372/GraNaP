package com.example.granap;

import android.text.InputFilter;
import android.text.Spanned;

// source:
// https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android

public class InputFilterMinMax implements InputFilter {

    private int max;

    public InputFilterMinMax(int max) {
        this.max = max;
    }

    public InputFilterMinMax(String max) {
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();

            String newVal = dest.toString().substring(0, dstart) + replacement +dest.toString().substring(dend, dest.toString().length());

            int input = Integer.parseInt(newVal);

            if (input<=max)
                return null;
        } catch (NumberFormatException nfe) { }
//Maybe notify user that the value is not good
        return "";
    }
}
