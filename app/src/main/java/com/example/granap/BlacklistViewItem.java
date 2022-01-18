package com.example.granap;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlacklistViewItem extends LinearLayout {
    private ImageView imageButton;
    private TextView tvIgnoredWord;

    private int wordIndex = -1;
    private int ignoredIndex = -1;

    public BlacklistViewItem(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.blacklist_view_item, this);

        imageButton = findViewById(R.id.removeButton);
        tvIgnoredWord = findViewById(R.id.ignoredView);
    }

    public void update(int ignoredIndex, int wordIndex, String word) {
        this.wordIndex = wordIndex;
        this.ignoredIndex = ignoredIndex;
        tvIgnoredWord.setText(word);
    }

    public int getWordIndex() {
        return wordIndex;
    }
    public int getIgnoredIndex() {
        return ignoredIndex;
    }
    public String getWord() { return tvIgnoredWord.getText().toString(); }
}

