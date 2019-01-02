package com.example.shalamov.brainwave;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;

import org.w3c.dom.Text;

public class ActivityBeforeTraining extends AppCompatActivity {
    private int lessonNumber, currentSentenceIndex, numberOfFavoriteSentences;
    private Lesson lesson;


    LinearLayout mBtnOnlyText, mBtnList, mBtnFavorite, mBtnTextWithTranslation, mBtnWordsList, mBtnQuizForWords, mBtnPlayer;
    TextView mTextNumberOfSentences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_choose_training);

        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);

        init();
        setListeners();

        TransitionDrawable trans = (TransitionDrawable) mBtnOnlyText.getBackground();
        trans.startTransition(4000);

        TransitionDrawable trans2 = (TransitionDrawable) mBtnTextWithTranslation.getBackground();
        trans2.startTransition(1000);

        TransitionDrawable trans3 = (TransitionDrawable) mBtnQuizForWords.getBackground();
        trans3.startTransition(3000);

//        setParameters();
    }




    private void setParameters() {
        mTextNumberOfSentences.setText("Количество предложений: " + lesson.getArrayListText().size());

    }

    private void init() {
        currentSentenceIndex = 0;
        Global.getmQuizLogic().setLesson(lesson);
        mBtnOnlyText = (LinearLayout) findViewById(R.id.btn_only_text);
        mBtnList = (LinearLayout) findViewById(R.id.btn_list);
        mBtnFavorite = (LinearLayout) findViewById(R.id.btn_favorite);
        mBtnTextWithTranslation = (LinearLayout) findViewById(R.id.btn_text_with_translation);
//        mTextNumberOfSentences = (TextView) findViewById(R.id.number_of_sentences);
        mBtnWordsList = (LinearLayout) findViewById(R.id.btn_words_list);
        mBtnQuizForWords = (LinearLayout) findViewById(R.id.btn_quiz_for_words);
        mBtnPlayer = (LinearLayout) findViewById(R.id.btn_player);
    }

    private void setListeners() {
        mBtnOnlyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBeforeTraining.this, TextActivity.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                startActivity(intent);
            }
        });

        //предложения по очереди
        mBtnTextWithTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.getmQuizLogic().setUseFavorite(false);
                Global.getmQuizLogic().setNull();
                Intent intent = new Intent(ActivityBeforeTraining.this, Room1.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                intent.putExtra("currentSentenceIndex", Integer.toString(currentSentenceIndex));
                intent.putExtra("onlyFavorite", "0");
                startActivity(intent);
            }
        });

        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBeforeTraining.this, ActivityNavigation.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                intent.putExtra("currentSentenceIndex", Integer.toString(currentSentenceIndex));
                startActivity(intent);
            }
        });

        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfFavoriteSentences = lesson.getArrayListTextFavorite().size();
                if(numberOfFavoriteSentences != 0) {
                    Global.getmQuizLogic().setUseFavorite(true);
                    Global.getmQuizLogic().setNull();

                    Intent intent = new Intent(ActivityBeforeTraining.this, Room1.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    intent.putExtra("currentSentenceIndex", Integer.toString(currentSentenceIndex));
                    intent.putExtra("onlyFavorite", "1");
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTraining.this, "The number of favorite sentences = 0", Toast.LENGTH_SHORT).show();
                }
            }
        });



        mBtnWordsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lesson.getArrayListWords().size() != 0) {
                    Intent intent = new Intent(ActivityBeforeTraining.this, ListWordsActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTraining.this, "Словарь пуст!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mBtnQuizForWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lesson.getArrayListWords().size() != 0) {
                    Intent intent = new Intent(ActivityBeforeTraining.this, QuizWordsActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTraining.this, "Нет слов для изучения!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lesson.getArrayListWords().size() != 0) {
                    Intent intent = new Intent(ActivityBeforeTraining.this, PlayerActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTraining.this, "Нет слов для изучения!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            return true;
        }

        if (id == R.id.action_save) {

            Global.getJsonUtils().saveFromModelToFile();
//            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Global.getJsonUtils().saveFromModelToFile();
//        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}

