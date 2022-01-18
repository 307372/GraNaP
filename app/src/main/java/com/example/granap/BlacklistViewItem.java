package com.example.granap;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlacklistViewItem extends LinearLayout {
    ImageView imageButton;
    TextView tvIgnoredWord;

    private int index = -1;

    public BlacklistViewItem(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.blacklist_view_item, this);

        imageButton = findViewById(R.id.removeButton);
        tvIgnoredWord = findViewById(R.id.ignoredView);
    }

    public void update(int index, String word) {
        this.index = index;
        tvIgnoredWord.setText(word);
    }

    public int getIndex() {
        return index;
    }
}

