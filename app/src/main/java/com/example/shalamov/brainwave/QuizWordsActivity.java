package com.example.shalamov.brainwave;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shalamov.brainwave.utils.Lesson;

import java.util.ArrayList;

public class QuizWordsActivity extends AppCompatActivity {
    private Lesson lesson;
    private int lessonNumber;
    private LinearLayout mLayoutNext, mLayoutPrevious;
    private RelativeLayout basicLayout;
    private TextView textViewFirst, textViewSecond;
    private ArrayList arrayListwords;
    private ActionBar ab;

    private String[] wordsRusArray, wordsEngArray;
    //логика работы
    int currentIndex;
    byte mode;
    boolean translationIsNotShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_words);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        init();
        showNextWord();
    }

    private void init() {
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        arrayListwords = lesson.getArrayListWords();
        currentIndex = 0;
        mode = 0;
        translationIsNotShown = false;
        createArrays();

        basicLayout = (RelativeLayout) findViewById(R.id.basic_layout_words_activity);
        textViewFirst = (TextView) findViewById(R.id.text_first_words_activity);
        textViewSecond = (TextView) findViewById(R.id.text_second_words_activity);

        mLayoutNext = (LinearLayout) findViewById(R.id.btn_next_quiz_words);
        mLayoutPrevious = (LinearLayout) findViewById(R.id.btn_previous_quiz_words);


        mLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (mode){
                    case 0:
                        clickNextWordMode0();
                        break;
                    case 1:
                        clickNextWordMode1();
                        break;
                    case 2:
                        clickNextWordMode2();
                        break;
                }
                ab.setSubtitle("#" + (currentIndex + 1) + " from " + arrayListwords.size());
            }
        });

        mLayoutPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (mode){
                    case 0:
                        clickPreviousWordMode0();
                        break;
                    case 1:
                        clickPreviousWordMode1();
                        break;
                    case 2:
                        clickPreviousWordMode2();
                        break;
                }
                ab.setSubtitle("#" + (currentIndex + 1) + " from " + arrayListwords.size());
            }
        });

    }

    private void createArrays() {
        wordsEngArray = new String[arrayListwords.size()];
        wordsRusArray = new String[arrayListwords.size()];

        for (int i = 0; i < arrayListwords.size(); i++) {
            String[] wordsArrayAfterSplit = arrayListwords.get(i).toString().split("[=>]");

            wordsEngArray[i] = wordsArrayAfterSplit[0];
            wordsRusArray[i] = wordsArrayAfterSplit[2];

        }
    }

    private void clickNextWordMode1(){

        if(translationIsNotShown){
            textViewSecond.setText(wordsRusArray[currentIndex]);
            translationIsNotShown = false;
        }else{
            currentIndex++;
            checkOutOfArray();
            textViewFirst.setText(wordsEngArray[currentIndex]);
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickPreviousWordMode1(){

        if(translationIsNotShown){
            textViewSecond.setText(wordsRusArray[currentIndex]);
            translationIsNotShown = false;
        }else{
            currentIndex--;
            checkOutOfArray();
            textViewFirst.setText(wordsEngArray[currentIndex]);
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickNextWordMode0(){
            currentIndex++;
            checkOutOfArray();
        textViewFirst.setText(wordsEngArray[currentIndex]);
        textViewSecond.setText(wordsRusArray[currentIndex]);
    }

    private void clickPreviousWordMode0(){
        currentIndex--;
        checkOutOfArray();
        textViewFirst.setText(wordsEngArray[currentIndex]);
        textViewSecond.setText(wordsRusArray[currentIndex]);
    }

    private void clickNextWordMode2(){

        if(translationIsNotShown){
            textViewSecond.setText(wordsEngArray[currentIndex]);
            translationIsNotShown = false;
        }else{
            currentIndex++;
            checkOutOfArray();
            textViewFirst.setText(wordsRusArray[currentIndex]);
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickPreviousWordMode2(){

        if(translationIsNotShown){
            textViewSecond.setText(wordsEngArray[currentIndex]);
            translationIsNotShown = false;
        }else{
            currentIndex--;
            checkOutOfArray();
            textViewFirst.setText(wordsRusArray[currentIndex]);
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void showNextWord() {

        checkOutOfArray();


//        String[] wordsArrayAfterSplit = splitWord(arrayListwords.get(currentIndex).toString());

        switch (mode) {
            case 0:
                textViewFirst.setText(wordsEngArray[currentIndex]);
                textViewSecond.setText(wordsRusArray[currentIndex]);
                break;
            case 1:
                if(translationIsNotShown){
                    textViewSecond.setText(wordsRusArray[currentIndex]);
                    translationIsNotShown = false;
                }else{
                    currentIndex++;
                    checkOutOfArray();
                    textViewFirst.setText(wordsEngArray[currentIndex]);
                    textViewSecond.setText("");
                    translationIsNotShown = true;
                }
                break;
            case 2:
                if(translationIsNotShown){
                    textViewSecond.setText(wordsEngArray[currentIndex]);
                    translationIsNotShown = false;
                }else{
                    currentIndex++;
                    checkOutOfArray();
                    textViewFirst.setText(wordsRusArray[currentIndex]);
                    textViewSecond.setText("");
                    translationIsNotShown = true;
                }
                break;

        }

        ab.setSubtitle("#" + (currentIndex + 1) + " from " + arrayListwords.size());
    }

    private void checkOutOfArray(){
        if (currentIndex >= arrayListwords.size() ) {
            currentIndex = 0;
        }
        if (currentIndex < 0) {
            currentIndex = arrayListwords.size() - 1;
        }
    }

    private String[] splitWord(String temp) {
        String[] wordsArrayAfterSplit = temp.split("[=>]");
        return wordsArrayAfterSplit;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_quiz_words, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
                setOptionIcon(item);
                break;
        }

        return true;
    }

    private void setOptionIcon(MenuItem item) {

        switch (mode) {
            case 0:
                item.setIcon(R.drawable.ic_eng);
                mode = 1;
                translationIsNotShown = false;
//                clickNextWordMode1();
                break;
            case 1:
                item.setIcon(R.drawable.ic_ru);
                mode = 2;
                translationIsNotShown = false;
//                clickNextWordMode2();
                break;
            case 2:
                item.setIcon(R.drawable.ic_eng_ru);
                mode = 0;
                translationIsNotShown = false;
//                clickNextWordMode0();
                break;
        }
    }
}
