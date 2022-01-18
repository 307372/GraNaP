package com.example.granap;

import static com.example.granap.BlacklistFragment.IGNORED_PREF;
import static com.example.granap.BlacklistFragment.IGNORED_SET;

import static java.lang.Math.min;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Toast;

import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class WordManager {
    private static WordManager manager;
    private String[] dictionary;

    NavigableSet<Integer> ignoredIndices;
    Context ctx;

    private WordManager(Context ctx)
    {
        this.ctx = ctx;
        Resources res = ctx.getResources();
        dictionary = res.getStringArray(R.array.dict);

        loadIgnoredWords();
    }

    private void loadIgnoredWords()
    {
        SharedPreferences sp = ctx.getSharedPreferences(IGNORED_PREF, Context.MODE_PRIVATE);

        Set<String> loadedIgnored = sp.getStringSet(IGNORED_SET, new HashSet<>());
        ignoredIndices = new TreeSet<>();

        loadedIgnored.forEach(e -> ignoredIndices.add(Integer.parseInt(e)));
    }

    public static WordManager get(Context ctx) {
        if (manager == null) {
            manager = new WordManager(ctx);
        }
        return manager;
    }

    private void saveIgnored()
    {
        SharedPreferences sp = ctx.getSharedPreferences(IGNORED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> stringIgnored = ignoredIndices.stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

        editor.putStringSet(IGNORED_SET, stringIgnored);
        editor.apply();
    }

    public void addToIgnored(int wordIndex)
    {
        ignoredIndices.add(wordIndex);
        saveIgnored();
    }

    public void removeFromIgnored(int wordIndex)
    {
        ignoredIndices.remove(wordIndex);
        saveIgnored();
    }

    public String getWordByIndex(int wordIndex)
    {
        return dictionary[wordIndex];
    }

    public int getNthValueFromIgnored(int n)
    {
        int i=0;
        for (Integer ignoredIndex : ignoredIndices) {
            if (i++ == n) return ignoredIndex;
        }
        return -1;
    }

    public String getNthIgnoredWord(int wordIndex)
    {
        return getWordByIndex(getNthValueFromIgnored(wordIndex));
    }

    private int rollNext(int min, int range_size) {
        return min + (int) Math.floor(Math.random()*(range_size));
    };

    private int getRandomViableIndex(int min, int range_size, boolean rerollP)
    {
        int index = rollNext(min, range_size);
        char firstLetter = dictionary[index].charAt(0);
        int timeoutCounter = 0;
        final int timeout_constant = 101;
        if (rerollP) {
            while ((firstLetter == 'p' || ignoredIndices.contains(index))
                    && ++timeoutCounter < timeout_constant)
            {
                index = rollNext(min, range_size);
                firstLetter = dictionary[index].charAt(0);
            }

        } else {
            while (ignoredIndices.contains(index)
                    && ++timeoutCounter < timeout_constant)
            {
                index = rollNext(min, range_size);
            }
        }
        if (timeoutCounter == timeout_constant) {
            Toast.makeText(ctx, "TIMEOUT", Toast.LENGTH_LONG).show();
        }
        return index;
    }

    public int findInIgnored(int wordIndex)
    {
        int pos=0;
        for (Integer ignoredIndex : ignoredIndices) {
            if (ignoredIndex == wordIndex) return pos;
            ++pos;
        }
        return -1;
    }

    public int getRandomWordIndexFromRange(int start, int stop, boolean rerollP)
    {
        /*
        if (start == stop)
        {
            String result = getWordByIndex(start);
            if (rerollP && result.startsWith("p") || ignoredIndices.contains(start)) {
                String message = "W podanym zakresie rozmiarów nie ma słów spełniających twoje wymagania!\nPoszerzam zakres poszukiwań";
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            }
        }
                // TO DO: RETRIEVE WORD FROM DICT
        //       IF IT'S IGNORED, THEN if start==stop show toast saying
        //       "no more words of this size, rolling from all possible"
        //
        */

        int max = min(stop, dictionary.length);

        return getRandomViableIndex(start, max-start, rerollP);
    }

    public int getIgnoredSize() {return ignoredIndices.size();}
}

