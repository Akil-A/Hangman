package com.example.android.s181142;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class WordHandler{

    private static ArrayList<String> sWordList;
    private static WordHandler sInstance;
    private static Context SContext;
    private static boolean sShuffle = true;

    //Return Instance of the class
    public static WordHandler getInstance(){
        if(sInstance == null)
            sInstance = new WordHandler();
        return sInstance;
    }

    public void makeGeekyList(Context context, boolean shuffle) {
        this.SContext = context;

        if(shuffle) {
            sWordList = new ArrayList<>();
            sWordList.addAll(Arrays.asList(SContext.getResources().getStringArray(R.array.geeky)));
            shuffle();
            sShuffle = false;
        }
    }

    public void makeMovieListContext(Context context, boolean shuffle) {
        this.SContext = context;

        if(shuffle) {
            sWordList = new ArrayList<>();
            sWordList.addAll(Arrays.asList(SContext.getResources().getStringArray(R.array.movies)));
            shuffle();
            sShuffle = false;
        }
    }

    public void makeMusicList(Context context, boolean shuffle) {
        this.SContext = context;

        if(shuffle) {
            sWordList = new ArrayList<>();
            sWordList.addAll(Arrays.asList(SContext.getResources().getStringArray(R.array.music)));
            shuffle();
            sShuffle = false;
        }
    }

    //Shuffle wordlist
    public static void shuffle(){
        Collections.shuffle(sWordList);
    }

    public static ArrayList<String> getWordList(){
        return sWordList;
    }

    //When shuffle is true we shuffle the list again
    public void setShuffle(boolean shuffle) {
        this.sShuffle = shuffle;
    }

    public boolean getShuffle() {
        return sShuffle;
    }
}
