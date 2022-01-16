package com.example.granap;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import java.util.concurrent.atomic.AtomicInteger;

// source:
// https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android

public class OnFocusChangeListenerMin implements OnFocusChangeListener {

    private int min;

    public OnFocusChangeListenerMin(int min) {
        this.min = min;
    }

    public OnFocusChangeListenerMin(String min) {
        this.min = Integer.parseInt(min);
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            String val = ((EditText)v).getText().toString();
            if(!TextUtils.isEmpty(val)) {
                if(Integer.parseInt(val) < min) {
                    ((EditText)v).setText(String.valueOf(min));

                    //Notify user that the value is not good
                }

            }
            else ((EditText)v).setText(String.valueOf(min));
        }
    }
}