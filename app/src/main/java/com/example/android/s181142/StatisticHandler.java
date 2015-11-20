package com.example.android.s181142;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;


/**
 * Created by akil on 02.09.15.
 */
public class StatisticHandler {

    //Rounds played of hangman
    private static int sRounds;

    public static void increaseRounds() {
        sRounds++;}

    public static int getRounds(){
        return sRounds;
    }

    public static void resetRounds(){
        sRounds = 0;
    }

    //Update wins
    public static void updateWin(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    "com.example.s181142.STATISTIC_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("com.example.s181142.Wins", sharedPreferences.getInt("com.example.s181142.Wins", 0)+1).commit();
    }

    //Update loss
    public static void updateLoss(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "com.example.s181142.STATISTIC_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("com.example.s181142.Loss", sharedPreferences.getInt("com.example.s181142.Loss", 0)+1).commit();
    }

    //Load statistics
    public static void loadStatistic(Context context, TextView winsTextView, TextView lossTextView) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "com.example.s181142.STATISTIC_DATA", Context.MODE_PRIVATE);
        int tempWins = sharedPreferences.getInt("com.example.s181142.Wins", 0);
        int tempLoss = sharedPreferences.getInt("com.example.s181142.Loss", 0);

        winsTextView.setText(context.getString(R.string.wins) + String.valueOf(tempWins));
        lossTextView.setText(context.getString(R.string.loss) + String.valueOf(tempLoss));
    }
}
