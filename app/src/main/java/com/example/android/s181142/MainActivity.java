package com.example.android.s181142;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.Locale;

public class MainActivity extends Activity{

    private TextView mWinsTextView;
    private TextView mLossTextView;
    private TextView mStartGame;
    private TextView mHeadline;
    private TextView mRules;
    private TextView mLanguage;
    private TextView mExit;
    private Typeface mTypefaceHead;
    private Typeface mTypefaceUsual;
    private int mPickedLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        //Make the Activity go full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadLanguage(this, getBaseContext());
        setContentView(R.layout.activity_main);

        mTypefaceHead = Typeface.createFromAsset(getAssets(), getString(R.string.chalkHeadFont));
        mTypefaceUsual = Typeface.createFromAsset(getAssets(), getString(R.string.chalkLetteFont));

        mHeadline = (TextView)findViewById(R.id.headline_textview);
        mHeadline.setTypeface(mTypefaceHead);

        mStartGame = (TextView)findViewById(R.id.start_game_textview);
        mStartGame.setTypeface(mTypefaceUsual);

        mRules = (TextView)findViewById(R.id.rules_textview);
        mRules.setTypeface(mTypefaceUsual);
        mRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RulesActivity.class);
                startActivity(intent);
            }
        });

        mLanguage = (TextView)findViewById(R.id.language_textview);
        mLanguage.setTypeface(mTypefaceUsual);

        mExit = (TextView)findViewById(R.id.exit_textview);
        mExit.setTypeface(mTypefaceUsual);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        mWinsTextView = (TextView)findViewById(R.id.wins_textView);
        mWinsTextView.setTypeface(mTypefaceHead);

        mLossTextView = (TextView)findViewById(R.id.loss_textView);
        mLossTextView.setTypeface(mTypefaceHead);

        mLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                languageDialog();
            }
        });
    }

    //Pop this dialog when chaning language
    private void languageDialog(){
        CharSequence[] language = {getString(R.string.language_code_en),getString(R.string.language_code_no)};

        //Make Alert Dialog with black theme
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        alertDialogBuilder.setTitle(R.string.language_dialog_title);

        alertDialogBuilder
                .setSingleChoiceItems(language, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPickedLanguage = which;
                    }
                })
                .setPositiveButton(R.string.language_dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveLanguage(mPickedLanguage == 0 ? getString(R.string.language_code_en_lowercase):getString(R.string.language_code_no_lowercase));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    //Load the chosen language
    public void loadLanguage(Context context, Context baseContext){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "Language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("locale", "en");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config,
                baseContext.getResources().getDisplayMetrics());
    }

    //Save the chosen language
    private void saveLanguage(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "Language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("locale", language).commit();

        updateText();
    }

    //Update text when language is changed so you don't have to restart activity
    private void updateText(){
        mRules.setText(R.string.rules);
        mLanguage.setText(R.string.language);
        mExit.setText(R.string.exit);
        StatisticHandler.loadStatistic(this,mWinsTextView,mLossTextView);
    }

    //Load statistic when Activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        StatisticHandler.loadStatistic(this, mWinsTextView, mLossTextView);
    }

    //Go to homescreen when back button pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }
}
