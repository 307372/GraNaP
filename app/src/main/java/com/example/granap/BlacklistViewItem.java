package com.example.granap;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlacklistViewItem extends LinearLayout {
    private ImageView imageButton;
    private TextView tvIgnoredWord;

    private int wordIndex = -1;

    public BlacklistViewItem(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.blacklist_view_item, this);

        imageButton = findViewById(R.id.removeButton);
        tvIgnoredWord = findViewById(R.id.ignoredView);
    }

    public void update(int index, String word) {
        this.wordIndex = index;
        tvIgnoredWord.setText(word);
    }

    public int getWordIndex() {
        return wordIndex;
    }
    public String getWord() { return tvIgnoredWord.getText().toString(); }
}

