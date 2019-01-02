package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;

import java.util.ArrayList;

public class QuizWordsActivity extends AppCompatActivity {
    String TAG = "QuizWordsActivity";
    private Lesson lesson;
    private int lessonNumber;
    private LinearLayout mLayoutNext, mLayoutPrevious;
    private RelativeLayout basicLayout;
    private TextView textViewFirst, textViewSecond;
    private ArrayList arrayListWords;
    private ActionBar ab;
    private TextToSpeech mTTS;
    private boolean speechIsPossible;
    private ProgressBar progressBar;

    private ArrayList<String> wordsRusArray, wordsEngArray, examplesOfTheUsageArray;
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
//        mTTS = new TextToSpeech(this, this);
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        arrayListWords = lesson.getArrayListWords();
        currentIndex = 0;
        mode = 0;
        translationIsNotShown = false;
        createArrays();

        basicLayout = (RelativeLayout) findViewById(R.id.basic_layout_words_activity);
        textViewFirst = (TextView) findViewById(R.id.text_first_words_activity);
        textViewSecond = (TextView) findViewById(R.id.text_second_words_activity);

        mLayoutNext = (LinearLayout) findViewById(R.id.btn_next_quiz_words);
        mLayoutPrevious = (LinearLayout) findViewById(R.id.btn_previous_quiz_words);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setProgressBarData();


        mLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (mode) {
                    case 0:
                        clickNextWordMode0();
                        setProgressBarData();
                        break;
                    case 1:
                        clickNextWordMode1();
                        setProgressBarData();
                        break;
                    case 2:
                        clickNextWordMode2();
                        setProgressBarData();
                        break;
                }
                ab.setSubtitle("#" + (currentIndex + 1) + " from " + wordsEngArray.size());
            }
        });

        mLayoutNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (!((currentIndex + 10) > wordsEngArray.size())) {
                    currentIndex = currentIndex + 9;

                    switch (mode) {
                        case 0:
                            clickNextWordMode0();
                            setProgressBarData();
                            break;
                        case 1:
                            clickNextWordMode1();
                            setProgressBarData();
                            break;
                        case 2:
                            clickNextWordMode2();
                            setProgressBarData();
                            break;
                    }
                    ab.setSubtitle("#" + (currentIndex + 1) + " from " + wordsEngArray.size());

                }
                return true;
            }
        });

        mLayoutPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (mode) {
                    case 0:
                        clickPreviousWordMode0();
                        setProgressBarData();
                        break;
                    case 1:
                        clickPreviousWordMode1();
                        setProgressBarData();
                        break;
                    case 2:
                        clickPreviousWordMode2();
                        setProgressBarData();
                        break;
                }
                ab.setSubtitle("#" + (currentIndex + 1) + " from " + wordsEngArray.size());
            }
        });

        mLayoutPrevious.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                showExampleOfUsage();
                return true;
            }
        });

    }

    private void setProgressBarData() {
        double value1 = 100 / wordsEngArray.size();
        int progress = (int) value1 * (currentIndex + 1);
        if (currentIndex == (wordsEngArray.size() - 1)) {
            progress = 100;
        }
        progressBar.setProgress(progress);
    }

    private void createArrays() {
        wordsEngArray = new ArrayList();
        wordsRusArray = new ArrayList();
        examplesOfTheUsageArray = new ArrayList<>();

        for (int i = 0; i < arrayListWords.size(); i++) {
            String[] wordsArrayAfterSplit = arrayListWords.get(i).toString().split("[=>]");

            if (wordsArrayAfterSplit.length == 7) {
                if (wordsArrayAfterSplit[6].equalsIgnoreCase("1")) {
                    wordsEngArray.add(wordsArrayAfterSplit[0]);
                    wordsRusArray.add(wordsArrayAfterSplit[2]);
                    examplesOfTheUsageArray.add(wordsArrayAfterSplit[4]);
                }
            } else {
                wordsEngArray.add(wordsArrayAfterSplit[0]);
                wordsRusArray.add(wordsArrayAfterSplit[2]);
                if(wordsArrayAfterSplit.length > 4){
                    examplesOfTheUsageArray.add(wordsArrayAfterSplit[4]);
                }else{
                    examplesOfTheUsageArray.add("- ");
                }
            }


        }
    }


    private void clickNextWordMode1() {

        if (translationIsNotShown) {
            textViewSecond.setText(wordsRusArray.get(currentIndex));
            translationIsNotShown = false;
        } else {
            currentIndex++;
            checkOutOfArray();
            textViewFirst.setText(wordsEngArray.get(currentIndex));
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickPreviousWordMode1() {

        if (translationIsNotShown) {
            textViewSecond.setText(wordsRusArray.get(currentIndex));
            translationIsNotShown = false;
        } else {
            currentIndex--;
            checkOutOfArray();
            textViewFirst.setText(wordsEngArray.get(currentIndex));
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickNextWordMode0() {

        currentIndex++;
        checkOutOfArray();
        Log.d(TAG, "clickNextWordMode0()\n===============\n" + "currentIndex = " + currentIndex);
        textViewFirst.setText(wordsEngArray.get(currentIndex));
        textViewSecond.setText(wordsRusArray.get(currentIndex));
    }

    private void clickPreviousWordMode0() {
        currentIndex--;
        checkOutOfArray();
        textViewFirst.setText(wordsEngArray.get(currentIndex));
        textViewSecond.setText(wordsRusArray.get(currentIndex));
    }

    private void clickNextWordMode2() {

        if (translationIsNotShown) {
            textViewSecond.setText(wordsEngArray.get(currentIndex));
            translationIsNotShown = false;
        } else {
            currentIndex++;
            checkOutOfArray();
            textViewFirst.setText(wordsRusArray.get(currentIndex));
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void clickPreviousWordMode2() {

        if (translationIsNotShown) {
            textViewSecond.setText(wordsEngArray.get(currentIndex));
            translationIsNotShown = false;
        } else {
            currentIndex--;
            checkOutOfArray();
            textViewFirst.setText(wordsRusArray.get(currentIndex));
            textViewSecond.setText("");
            translationIsNotShown = true;
        }
    }

    private void showNextWord() {

        checkOutOfArray();


//        String[] wordsArrayAfterSplit = splitWord(arrayListWords.get(currentIndex).toString());

        switch (mode) {
            case 0:
                textViewFirst.setText(wordsEngArray.get(currentIndex));
                textViewSecond.setText(wordsRusArray.get(currentIndex));
                break;
            case 1:
                if (translationIsNotShown) {
                    textViewSecond.setText(wordsRusArray.get(currentIndex));
                    translationIsNotShown = false;
                } else {
                    currentIndex++;
                    checkOutOfArray();
                    textViewFirst.setText(wordsEngArray.get(currentIndex));
                    textViewSecond.setText("");
                    translationIsNotShown = true;
                }
                break;
            case 2:
                if (translationIsNotShown) {
                    textViewSecond.setText(wordsEngArray.get(currentIndex));
                    translationIsNotShown = false;
                } else {
                    currentIndex++;
                    checkOutOfArray();
                    textViewFirst.setText(wordsRusArray.get(currentIndex));
                    textViewSecond.setText("");
                    translationIsNotShown = true;
                }
                break;

        }

        ab.setSubtitle("#" + (currentIndex + 1) + " from " + wordsEngArray.size());
    }

    private void checkOutOfArray() {
        if (currentIndex >= wordsRusArray.size()) {
            currentIndex = 0;
        }
        if (currentIndex < 0) {
//            currentIndex = arrayListWords.size() - 1;
            currentIndex = 0;
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
            case R.id.action_play:
//                if(speechIsPossible) {
//
//                    String text = "А Васька слушает да ест";
//                    mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//                }else{
//                    Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
//                }

                break;
            case R.id.action_example:
                showExampleOfUsageAlertDialog();
                break;
        }

        return true;
    }


    private void showExampleOfUsageAlertDialog() {
        String[] text = arrayListWords.get(currentIndex).toString().split("[=>]");
        if (text.length >= 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizWordsActivity.this);
            builder.setTitle("Пример использования слова");
            EditText editText = new EditText(QuizWordsActivity.this);
            editText.setText(text[4]);
            editText.setFocusable(false);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            builder.setView(editText);
            builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            builder.create().show();
        } else {
            Toast.makeText(this, "Пример отсутствует", Toast.LENGTH_SHORT).show();
        }

    }

    private void showExampleOfUsage() {
        String text = examplesOfTheUsageArray.get(currentIndex);
        textViewSecond.setText(Html.fromHtml(getFormattedExampleOfUsage(text, wordsEngArray.get(currentIndex))));
    }

    private String getFormattedExampleOfUsage(String text, String word) {
        // text - английское предложение
        Log.d(TAG, "getFormattedExampleOfUsage(String text, String word)\n");
        StringBuilder finalText = new StringBuilder();
        String[] splitText = text.split(word);

        finalText.append("<b><font color=#2e7d32>");
        for (int i = 0; i < splitText.length; i++) {
            Log.d(TAG, "splitText[i] = [" + splitText[i] + "]");
            finalText.append(splitText[i]);

            if (i == 0) {
                finalText.append("</font></b>");
                finalText.append("<font color=#9f3924>" + word + "</font>");
                finalText.append("<b><font color=#2e7d32>");
            }

            if (i == 1 & splitText.length > 2) {
                finalText.append("</font></b>");
                finalText.append("<font color=#9f3924>" + word + "</font>");
                finalText.append("<b><font color=#205128>");
            }

        }
        finalText.append("</font></b>");

        return finalText.toString();
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
