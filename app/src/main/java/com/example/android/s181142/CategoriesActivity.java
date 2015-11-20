package com.example.android.s181142;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class CategoriesActivity extends Activity {

    private TextView mGeeky_textview;
    private TextView mMovies_textview;
    private TextView mMusic_textview;
    private TextView mHeadLineTextView;
    private TextView mWinsTextView;
    private TextView mLossTextView;
    private Typeface mTypefaceHead;
    private Typeface mTypefaceUsual;
    private final MainActivity mMainActivity = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Make the Activity go full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMainActivity.loadLanguage(this, getBaseContext());

        setContentView(R.layout.activity_categories);


        mTypefaceHead = Typeface.createFromAsset(getAssets(), getString(R.string.chalkHeadFont));
        mTypefaceUsual = Typeface.createFromAsset(getAssets(), getString(R.string.chalkLetteFont));

        mHeadLineTextView = (TextView)findViewById(R.id.category_textview);
        mHeadLineTextView.setTypeface(mTypefaceHead);


        mGeeky_textview = (TextView)findViewById(R.id.geeky_textview);
        mGeeky_textview.setTypeface(mTypefaceUsual);
        mGeeky_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HangmanActivity.class);
                i.putExtra("category", 0);
                startActivity(i);
                finish();
            }
        });

        mMovies_textview = (TextView)findViewById(R.id.movies_textview);
        mMovies_textview.setTypeface(mTypefaceUsual);
        mMovies_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HangmanActivity.class);
                i.putExtra("category", 1);
                startActivity(i);
                finish();
            }
        });

        mMusic_textview = (TextView)findViewById(R.id.music_textview);
        mMusic_textview.setTypeface(mTypefaceUsual);
        mMusic_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HangmanActivity.class);
                i.putExtra("category", 2);
                startActivity(i);
                finish();
            }
        });

        mWinsTextView = (TextView)findViewById(R.id.categories_wins_textView);
        mWinsTextView.setTypeface(mTypefaceHead);

        mLossTextView = (TextView)findViewById(R.id.categories_loss_textView);
        mLossTextView.setTypeface(mTypefaceHead);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticHandler.loadStatistic(this,mWinsTextView,mLossTextView);
    }
}
