package com.example.granap;

import static com.example.granap.BlacklistFragment.IGNORED_PREF;
import static com.example.granap.BlacklistFragment.IGNORED_SET;

import static java.lang.Math.min;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class WordManager {
    private static WordManager manager;
    private final String[] dictionary;

    NavigableSet<Integer> ignoredIndices;
    final SharedPreferences sp;
    final Toast timeoutMessage;

    private WordManager(Context ctx)
    {
        sp = ctx.getSharedPreferences(IGNORED_PREF, Context.MODE_PRIVATE);
        dictionary = ctx.getResources().getStringArray(R.array.dict);
        String message = "Zmień ustawienia losowania, w tym zakresie nie ma nic co pasuje do obecnych ustawień!";
        timeoutMessage = Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        loadIgnoredWords();
    }

    private void loadIgnoredWords()
    {


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

    private int rollNext(int min, int range_size) {
        return min + (int) Math.floor(Math.random()*(range_size));
    }

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
            timeoutMessage.show();
        }
        return index;
    }

    public int getRandomWordIndexFromRange(int start, int stop, boolean rerollP)
    {
        int max = min(stop, dictionary.length);
        return getRandomViableIndex(start, max-start, rerollP);
    }

    public SortedMap<Integer, Integer> getAdapterMap()
    {
        Iterator<Integer> iter = ignoredIndices.iterator();
        SortedMap<Integer, Integer> res = new TreeMap<>();
        for (int i=0; i < ignoredIndices.size(); ++i)
        {
            res.put(i, iter.next());
        }

        return res;
    }

    public SortedMap<Integer, Integer> getFilteredAdapterMap(String pattern)
    {
        Iterator<Integer> iter = ignoredIndices.iterator();
        SortedMap<Integer, Integer> res = new TreeMap<>();
        for (int i=0; i < ignoredIndices.size(); ++i)
        {
            int current = iter.next();
            if (dictionary[current].contains(pattern)) {
                res.put(i, current);
            }
        }

        return res;
    }
}

