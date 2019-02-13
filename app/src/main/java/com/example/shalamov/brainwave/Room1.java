package com.example.shalamov.brainwave;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.LessonModel;

import java.util.Locale;

public class Room1 extends AppCompatActivity {

    private QuizLogic mQuizLogic;
    int currentSentenceIndex, lessonNumber, showOnlyFavoriteSentences;
    private ActionBar ab;
    private TextView textFieldForLearning;
    private LessonModel lesson;
    private LinearLayout layoutForClick;
    private LinearLayout mLayoutAddToFavorite;
    private ImageView mImageForLayoutAddToFavorite;
    private TextToSpeech textToSpeech;
    private ClipboardManager clipboard;
    private ImageView mImageFavoriteOrNot;
    final String TAG = "Room1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room1);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        currentSentenceIndex = Integer.parseInt(getIntent().getStringExtra("currentSentenceIndex"));
        showOnlyFavoriteSentences = Integer.parseInt(getIntent().getStringExtra("onlyFavorite"));
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        initializing();


    }

    private void initializing() {
        lesson = (LessonModel) Global.getLessonsList().get(lessonNumber);
        mQuizLogic = Global.getmQuizLogic();

        mQuizLogic.setCurrSentence(currentSentenceIndex);
        mQuizLogic.createArrayCurrentSentence(currentSentenceIndex);
        if(showOnlyFavoriteSentences == 1){
            ab.setTitle("Favorite");
        }else{
            ab.setTitle("Text");
        }
        ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());

        layoutForClick = findViewById(R.id.layout_tap_quiz); //первый лейоут где располагается главный текст и по нему отслеживаются нажатия
        LinearLayout layoutForClickPrevious = findViewById(R.id.layout_btn_previous);
        LinearLayout layoutForClickNext = findViewById(R.id.layout_btn_next);
        LinearLayout mLayoutCleanText = findViewById(R.id.btn_clean_text);
        LinearLayout mLayoutGoToTranslator = findViewById(R.id.btn_go_to_translator);
        LinearLayout mLayoutCorrectText = findViewById(R.id.btn_correct_text);
        LinearLayout mLayoutPaste = findViewById(R.id.btn_paste);
        LinearLayout mLayoutGoToChooseWordForVocabulary = findViewById(R.id.btn_word_vocabulary_builder);
        mLayoutAddToFavorite = findViewById(R.id.btn_add_to_favorite_activity_room1);
        mImageForLayoutAddToFavorite = findViewById(R.id.image_btn_add_to_favorite_activity_room1);
        BottomNavigationView bottomNavigationItemView = findViewById(R.id.navigation_bottom);
        textFieldForLearning = findViewById(R.id.text_field_for_learning);

        mImageFavoriteOrNot = (ImageView) findViewById(R.id.image_favorite_or_not);

        checkFavoriteAndUpdateLabels();

        textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.allWord())));

        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.previous:
                        textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWeb(mQuizLogic.previousSentence())));
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        checkFavoriteAndUpdateLabels();
                        break;
                    case R.id.ok:
                        textFieldForLearning.setText(mQuizLogic.setCurrWordNull());
                        break;
                    case R.id.next:
                        textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWeb(mQuizLogic.nextSentence())));

                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        checkFavoriteAndUpdateLabels();
                        break;
                }
                return true;
            }

        });

        layoutForClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mQuizLogic.nextWord();

                if (text != "") {
                    textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(text)));
                }

//                textFieldForLearning.setText(textFieldForLearning.getText().toString() + " Add text ");
            }
        });

        layoutForClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String text = mQuizLogic.allWord();
                textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(text)));

                return true;
            }
        });

        layoutForClickPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.previousSentence())));

                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                checkFavoriteAndUpdateLabels();
            }
        });

        layoutForClickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.nextSentence())));

                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                checkFavoriteAndUpdateLabels();
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        mLayoutGoToTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleTranslator(mQuizLogic.allWord());
            }
        });


        mLayoutPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteText();
            }
        });

        mLayoutCorrectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctText();
            }
        });

        mLayoutCleanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textForClean = Global.getLessonsUtils().cleanText(mQuizLogic.getCurrentSentenceString());
//                Log.d(TAG, "textForClean = " + textForClean);
                copyData(mQuizLogic.getCurrentSentenceString());
                Global.getLessonsUtils().changeSentence(lesson, mQuizLogic.getCurrentSentenceString(), textForClean);
                textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.getCurrentSentenceString())));


            }
        });

        mLayoutGoToChooseWordForVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Room1.this, ChooseWordForVocabularyActivity.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                intent.putExtra("currentSentenceIndex", Integer.toString(mQuizLogic.getCurrentSentenceIndex()));
                intent.putExtra("addNewWordFlag", "0");
                startActivity(intent);
            }
        });

        mLayoutAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Global.getLessonsUtils().checkIfExistThisSentenceInFavorite(lesson, mQuizLogic.getCurrentSentenceString())){
                    Global.getLessonsUtils().deleteFavoriteSentence(lesson, mQuizLogic.getCurrentSentenceString());
                    Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
                    Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageForLayoutAddToFavorite);
                }else{
                    Global.getLessonsUtils().addFavoriteSentence(lesson, mQuizLogic.getCurrentSentenceString());
                    Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
                    Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageForLayoutAddToFavorite);
                }

            }
        });
    }

    private void checkFavoriteAndUpdateLabels() {
        if(Global.getLessonsUtils().checkIfExistThisSentenceInFavorite(lesson, mQuizLogic.getCurrentSentenceString())){
            Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
            Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageForLayoutAddToFavorite);
        }else{
            Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
            Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageForLayoutAddToFavorite);
        }
    }

    private void copyData(String textForClean) {

        String[] text = textForClean.split("[=>]");

        if(text.length >= 3){
            ClipData myClip = ClipData.newPlainText("text", text[2]);
            clipboard.setPrimaryClip(myClip);

        }
    }


    private void correctText() {

        Intent intent = new Intent(Room1.this, TextEditorActivity.class);
        intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
        intent.putExtra("currentSentenceIndex", Integer.toString(currentSentenceIndex));
        startActivity(intent);

    }

    private void pasteText() {
        ClipData abc = clipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String text = item.getText().toString();

        Log.d(TAG, "\npaste text:[" + text + "]" + "\nmQuizLogic.getCurrentSentenceString() = " + mQuizLogic.getCurrentSentenceString());

        Global.getLessonsUtils().changeSentence(lesson, mQuizLogic.getCurrentSentenceString(), mQuizLogic.getCurrentSentenceString() + "=>" + text);

        textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.getSentenceString(currentSentenceIndex))));


    }


    private void openGoogleTranslator(String text) {
        try {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.google.com/#en/ru/" + text));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_room1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {

            textToSpeech.setSpeechRate(1f);
            textToSpeech.speak(textFieldForLearning.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }

        return true;
    }

    @Override
    protected void onResume() {
        updateContent();
        super.onResume();


    }

    private void updateContent() {
        textFieldForLearning.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.getCurrentSentenceString())));
    }
}
