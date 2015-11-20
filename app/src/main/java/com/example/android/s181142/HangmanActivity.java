package com.example.android.s181142;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class HangmanActivity extends Activity implements OnClickListener {

    private int mAttempts;
    private TextView mCorrectWordTextView;
    private TextView mIncorrectWordTextView;
    private ArrayList<Integer> mGuessedIndexes;
    private ArrayList<String> mWrongGuessesLetterList;
    private String mCurrentGuessedWord = "";
    private String mWrongLetterString = "";
    private String mHiddenWord = "";
    private String mGuessWord = "";
    private LinearLayout mButtonLayout;
    private ArrayList<String> mCorrectGuessedButton;
    private ArrayList<String> mIncorrectGuessedButton;
    private Typeface mButtonTypeFace;
    private Typeface mLetterTypeFace;
    private Locale mCurrentLocale;
    private HangmanView mHangmanView;
    private MainActivity mMainActivity = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make the Activity go full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMainActivity.loadLanguage(this, getBaseContext());

        setContentView(R.layout.activity_hangman);

        mCurrentLocale = getResources().getConfiguration().locale;

        mButtonTypeFace = Typeface.createFromAsset(getAssets(), getString(R.string.chalkHeadFont));
        mLetterTypeFace = Typeface.createFromAsset(getAssets(), getString(R.string.chalkLetteFont));

        mGuessedIndexes = new ArrayList<>();
        mWrongGuessesLetterList = new ArrayList<>();
        mCorrectGuessedButton = new ArrayList<>();
        mIncorrectGuessedButton = new ArrayList<>();

        mIncorrectWordTextView = (TextView)findViewById(R.id.wrong_letters_textview);
        mIncorrectWordTextView.setTextColor(Color.RED);
        mIncorrectWordTextView.setTypeface(mButtonTypeFace);

        mCorrectWordTextView = (TextView)findViewById(R.id.correct_word_textview);
        mCorrectWordTextView.setTextColor(Color.GREEN);
        mCorrectWordTextView.setTypeface(mButtonTypeFace);

        mButtonLayout = (LinearLayout)findViewById(R.id.button_linearlayout);

        //Use HangmanView which is in the layout file and was set to the content view
        mHangmanView = (HangmanView) findViewById(R.id.hangmanView_layout);

        //Check which category was chosen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
               int category = extras.getInt("category");
            // 0 == geeky
            if(category == 0)
                WordHandler.getInstance().makeGeekyList(this, WordHandler.getInstance().getShuffle());
            // 1 == movies
            else if(category == 1)
                WordHandler.getInstance().makeMovieListContext(this, WordHandler.getInstance().getShuffle());
            //2 == music
            else if(category == 2)
                WordHandler.getInstance().makeMusicList(this, WordHandler.getInstance().getShuffle());
        }

        //Reset everything after all questions are tried
        if(StatisticHandler.getRounds() == WordHandler.getInstance().getWordList().size()) {
            reset();
        }

        if(savedInstanceState == null) {
            WordHandler.getInstance().getWordList();
            mGuessWord = WordHandler.getInstance().getWordList().get(StatisticHandler.getRounds());
            mHiddenWord = hideWord(mGuessWord);
            mCorrectWordTextView.setText(mHiddenWord);
            setLocaleButtons();
            setFont();
            setButtonColor();
        } else {
            mCorrectWordTextView.setText(savedInstanceState.getString(getString(R.string.hangman_activity_mCurrentGuessedWord)));
            mIncorrectWordTextView.setText(savedInstanceState.getString(getString(R.string.hangman_activity_mWrongLetterString)));
            mHangmanView.updateView(savedInstanceState.getInt(getString(R.string.hangman_activity_mAttempts)));
            mCorrectGuessedButton = savedInstanceState.getStringArrayList(getString(R.string.hangman_activity_mCorrectGussedButton));
            mIncorrectGuessedButton = savedInstanceState.getStringArrayList(getString(R.string.hangman_activity_mIncorrectGussedButton));
            setLocaleButtons();
            setFont();
            setButtonColor();
        }
    }

    //Set locale button if NO language is chosen and set font on keyboard buttons
    private void setLocaleButtons(){
        if(String.valueOf(mCurrentLocale).equals(getString(R.string.hangman_activity_no))){
            TextView localeButton1 = (TextView)findViewById(R.id.locale_button_1);
            localeButton1.setTypeface(mLetterTypeFace);
            localeButton1.setTextSize(40);
            localeButton1.setPadding(7,7,7,0);
            localeButton1.setVisibility(View.VISIBLE);

            TextView localeButton2 = (TextView)findViewById(R.id.locale_button_2);
            localeButton2.setTypeface(mLetterTypeFace);
            localeButton2.setTextSize(40);
            localeButton2.setPadding(7, 7, 7, 0);
            localeButton2.setVisibility(View.VISIBLE);

            TextView localeButton3 = (TextView)findViewById(R.id.locale_button_3);
            localeButton3.setTypeface(mLetterTypeFace);
            localeButton3.setTextSize(40);
            localeButton3.setPadding(7,7,7,0);
            localeButton3.setVisibility(View.VISIBLE);
        }
    }

    //Set font on keyboard buttons
    private void setFont() {

        for (int i = 0; i< mButtonLayout.getChildCount()-1; i++) {
            ViewGroup childView = (ViewGroup) mButtonLayout.getChildAt(i);
            for (int j = 0; j < childView.getChildCount(); j++) {
                TextView textView = (TextView)childView.getChildAt(j);
                textView.setTextSize(35);
                textView.setPadding(6,6,6,0);
                textView.setTypeface(mButtonTypeFace);
            }
        }
    }

    //Set button color for wrongly and correctly chosen words on rotation
    private void setButtonColor() {
        for (int i = 0; i< mButtonLayout.getChildCount(); i++) {
            ViewGroup childView = (ViewGroup) mButtonLayout.getChildAt(i);
            for (int j = 0; j < childView.getChildCount(); j++) {
                TextView textView = (TextView)childView.getChildAt(j);
                if(mCorrectGuessedButton.contains(textView.getText().toString())) {
                    textView.setTextColor(Color.GREEN);
                    textView.setEnabled(false);
                }
                else if(mIncorrectGuessedButton.contains(textView.getText().toString())) {
                    textView.setTextColor(Color.RED);
                    textView.setEnabled(false);
                }
            }
        }
    }

    //Hide the word
    private String hideWord(String word) {
        for(int i = 0; i<word.length()-1; i++)
            mHiddenWord += "_ ";

        mHiddenWord += "_";

        return mHiddenWord;
    }

    //Unhide letter in word when guess is correct
    private String unHideWord(String guessedLetter) {
        boolean guess = mGuessWord.contains(guessedLetter);
        char[] guessWordChar = mGuessWord.toCharArray();
        char guessedCharacter = guessedLetter.charAt(0);
        if (guess) {
            for(int i = 0; i<mGuessWord.length(); i++){
                if (mGuessWord.charAt(i) == guessedCharacter) {
                    mGuessedIndexes.add(i);
                }
                else if(!mGuessedIndexes.contains(i)){
                    guessWordChar[i] = '_';
                }
            }
            return String.valueOf(guessWordChar);
        }
        return null;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGuessWord = savedInstanceState.getString(getString(R.string.hangman_activity_mGuessWord));
        mWrongGuessesLetterList = savedInstanceState.getStringArrayList(getString(R.string.hangman_activity_mWrongGuessesLetterList));
        mAttempts = savedInstanceState.getInt(getString(R.string.hangman_activity_mAttempts));
        mCurrentGuessedWord = savedInstanceState.getString(getString(R.string.hangman_activity_mCurrentGuessedWord));
        mWrongLetterString = savedInstanceState.getString(getString(R.string.hangman_activity_mWrongLetterString));
        mGuessedIndexes = savedInstanceState.getIntegerArrayList(getString(R.string.hangman_activity_mGuessedIndexes));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.hangman_activity_mGuessWord), mGuessWord);
        outState.putStringArrayList(getString(R.string.hangman_activity_mWrongGuessesLetterList), mWrongGuessesLetterList);
        outState.putString(getString(R.string.hangman_activity_mHiddenword), mHiddenWord);
        outState.putInt(getString(R.string.hangman_activity_mAttempts), mAttempts);
        outState.putString(getString(R.string.hangman_activity_mCurrentGuessedWord), mCurrentGuessedWord);
        outState.putString(getString(R.string.hangman_activity_mWrongLetterString), mWrongLetterString);
        outState.putIntegerArrayList(getString(R.string.hangman_activity_mGuessedIndexes), mGuessedIndexes);
        outState.putStringArrayList(getString(R.string.hangman_activity_mCorrectGussedButton), mCorrectGuessedButton);
        outState.putStringArrayList(getString(R.string.hangman_activity_mIncorrectGussedButton), mIncorrectGuessedButton);
    }

    //Keep dialog when phone is rotated
    private static void keepDialog(Dialog dialog){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    //Get wrong guesses fron ArrayList and convert to String
    private String wrongGuessesLettersListToString() {
            String wrongGuesses = "";
        for (String s: mWrongGuessesLetterList) {
                wrongGuesses += s + " ";
            }
        return wrongGuesses;
    }

    //Cal this method when a guess is wrong
    private void wrongGuess(TextView textViewButton, String buttonText){
        mHangmanView.increaseAttempts();
        //Change button color to red when the guess is wrong
        textViewButton.setTextColor(Color.RED);
        //Add the wrong guessed button to ArrayList
        mWrongGuessesLetterList.add(buttonText);
        //Set the wrong guessed word to TextView
        mIncorrectWordTextView.setText(wrongGuessesLettersListToString());
        //Save wrong guesses to string and use it when phone is rotated
        mWrongLetterString = mIncorrectWordTextView.getText().toString();
        //Add wrong guessed letter to ArrayList to set color back when phone is rotated
        mIncorrectGuessedButton.add(textViewButton.getText().toString());
        mAttempts++;
    }

    @Override
    public void onBackPressed() {
        //Make Alert Dialog and use dark theme
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        alertDialogBuilder.setTitle(R.string.backpressed_dialog_cancel);

        alertDialogBuilder
                .setMessage(R.string.backpressed_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.backpressed_dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StatisticHandler.resetRounds();
                        WordHandler.getInstance().setShuffle(true);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();

                    }
                })
                .setNegativeButton(R.string.backpressed_dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        keepDialog(alertDialog);
    }

    private void reset(){
        StatisticHandler.resetRounds();
        WordHandler.getInstance().setShuffle(true);

        //Make Alert Dialog and use dark theme
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        alertDialogBuilder.setTitle(R.string.reset_dialog_title);

        alertDialogBuilder
                .setMessage(R.string.reset_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.reset_dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(R.string.reset_dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
                        finish();
                        startActivity(i);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        keepDialog(alertDialog);

    }

    private void guess(View view) {
        TextView textViewButton = (TextView)view;
        String buttonText = textViewButton.getText().toString();
        //Set current word in temp variable to see if it's null
        String tempCurrentGuessedWord = unHideWord(buttonText);
        if(tempCurrentGuessedWord != null)
            mCurrentGuessedWord = tempCurrentGuessedWord;
        String textViewString;
        //Disable button after guess
        textViewButton.setEnabled(false);


        //User guessed correct
        if(tempCurrentGuessedWord != null) {
            //Change button color to yellow when the guess is right
            textViewButton.setTextColor(Color.GREEN);
            mCorrectGuessedButton.add(textViewButton.getText().toString());
            //Set space between every letter
            mCurrentGuessedWord = mCurrentGuessedWord.replaceAll("", " ");
            mCorrectWordTextView.setText(mCurrentGuessedWord);
            textViewString = mCorrectWordTextView.getText().toString();
            //Remove spaces between all letters
            textViewString = textViewString.replaceAll(" ", "");

            //User has won
            if(textViewString.equals(mGuessWord)) {
                StatisticHandler.increaseRounds();
                StatisticHandler.updateWin(this);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

                alertDialogBuilder.setTitle(R.string.win_dialog_title);

                alertDialogBuilder
                        .setMessage(R.string.win_dialog_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.win_dialog_positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(getIntent());
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                keepDialog(alertDialog);
            }

        //User has lost
        } else if(mAttempts == 5) {
            StatisticHandler.increaseRounds();
            wrongGuess(textViewButton,buttonText);
            StatisticHandler.updateLoss(this);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

            alertDialogBuilder.setTitle(R.string.lost_dialog_title);

            alertDialogBuilder
                    .setMessage(getString(R.string.lost_dialog_message) + mGuessWord)
                    .setCancelable(false)
                    .setPositiveButton(R.string.lost_dialog_positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            finish();
                            startActivity(getIntent());
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
            keepDialog(alertDialog);
        //User guessed wrong
        } else {
            wrongGuess(textViewButton,buttonText);
        }
    }

    @Override
    public void onClick(View view) {
        guess(view);
    }
}
