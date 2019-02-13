package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shalamov.brainwave.utils.LessonModel;

public class VocabularyBuilderActivity extends AppCompatActivity {

    private TextView text;
    private LinearLayout mBtnPrevious, mBtnNext, mBtnCreateVocabulary;
    private int lessonNumber, currentSentenceIndex;
    private LessonModel lesson;
    private QuizLogic mQuizLogic;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_builder);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));

        init();
        setListeners();
        text.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(mQuizLogic.getCurrentSentenceString())));
        ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());
    }



    private void init() {
        currentSentenceIndex = 0;
        lesson = (LessonModel) Global.getLessonsList().get(lessonNumber);
        mQuizLogic = Global.getmQuizLogic();
        mQuizLogic.setCurrSentence(currentSentenceIndex);
        mQuizLogic.createArrayCurrentSentence(currentSentenceIndex);
        text = (TextView) findViewById(R.id.text_vocabulary_builder);
        mBtnPrevious = (LinearLayout) findViewById(R.id.btn_previous_vocabulary_builder);
        mBtnNext = (LinearLayout) findViewById(R.id.btn_next_vocabulary_builder);
        mBtnCreateVocabulary = (LinearLayout) findViewById(R.id.btn_word_vocabulary_builder);
    }

    private void setListeners() {
        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWeb(mQuizLogic.previousSentence())));
                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());

            }
        });


        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWeb(mQuizLogic.nextSentence())));
                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());
            }
        });

        mBtnCreateVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VocabularyBuilderActivity.this, ChooseWordForVocabularyActivity.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                intent.putExtra("currentSentenceIndex", Integer.toString(mQuizLogic.getCurrentSentenceIndex()));
                intent.putExtra("addNewWordFlag", "0");
                startActivity(intent);
            }
        });


    }
}
