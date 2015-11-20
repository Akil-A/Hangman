package com.example.android.s181142;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class RulesActivity extends Activity {

    private TextView headLineTextView;
    private TextView rulesTextView;
    private Typeface mTypefaceHead;
    private Typeface mTypefaceUsual;
    private final MainActivity mMainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity.loadLanguage(this, getBaseContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rules);

        mTypefaceHead = Typeface.createFromAsset(getAssets(), getString(R.string.chalkHeadFont));
        mTypefaceUsual = Typeface.createFromAsset(getAssets(), getString(R.string.chalkLetteFont));

        headLineTextView = (TextView)findViewById(R.id.activity_rules_headline);
        headLineTextView.setTypeface(mTypefaceHead);

        rulesTextView = (TextView)findViewById(R.id.activity_rules_rules);
        rulesTextView.setTypeface(mTypefaceUsual);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
