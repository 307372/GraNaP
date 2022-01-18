package com.example.granap;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlacklistViewItem extends LinearLayout {
    private final TextView tvIgnoredWord;

    private int wordIndex = -1;

    public BlacklistViewItem(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.blacklist_view_item, this);
        tvIgnoredWord = findViewById(R.id.ignoredView);
    }

    public void update(int wordIndex, String word) {
        this.wordIndex = wordIndex;
        tvIgnoredWord.setText(word);
    }

    public int getWordIndex() {
        return wordIndex;
    }
    public String getWord() { return tvIgnoredWord.getText().toString(); }
}

