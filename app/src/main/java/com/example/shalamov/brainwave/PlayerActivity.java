package com.example.shalamov.brainwave;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;

import java.util.ArrayList;
import java.util.Locale;

public class PlayerActivity extends AppCompatActivity {

    String TAG = "PlayerActivity";
    private ImageView mBtnPlayStop;

    private int lessonNumber;
    private Lesson lesson;
    private ArrayList arrayListWords;
    private TextToSpeech mTTSRu;
    private TextToSpeech mTTSEng;

    private ArrayList<String> wordsRusArray, wordsEngArray;

    private boolean speechIsPossible, speechPlays;

    private CountDownTimer timer;
    private TextView textViewLog;

    private TextToSpeech TTSEng;
    TextToSpeech TTSRu;
    private boolean needStopPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        init();
        setListeners();


    }


    private void setListeners() {
        mBtnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speechPlays) {
                    mBtnPlayStop.setBackgroundResource(R.drawable.ic_play_color);
                    speechPlays = false;
                    needStopPlayer = true;
                } else {
                    mBtnPlayStop.setBackgroundResource(R.drawable.ic_pause_color);
                    new MyAsinkTask().execute();
                    speechPlays = true;
                    needStopPlayer = false;

                }


//                mTTSRu.speak("Это пример", TextToSpeech.QUEUE_FLUSH, null);



            }
        });
    }



    private void sayEnglish(int index) {
        TTSEng.speak(wordsEngArray.get(index), TextToSpeech.QUEUE_FLUSH, null);

    }

    private void sayRussian(int index) {
        TTSRu.speak(wordsRusArray.get(index), TextToSpeech.QUEUE_FLUSH, null);

    }


    private void init() {
//        mTTSRu = new TextToSpeech(this, this);
//        mTTSEng = new TextToSpeech(this, this);
        mBtnPlayStop = (ImageView) findViewById(R.id.play_pause);
        textViewLog = (TextView) findViewById(R.id.log_text_view);
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        arrayListWords = lesson.getArrayListWords();
        createArrays();
        speechPlays = false;
        needStopPlayer = true;


        TTSEng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    TTSEng.setLanguage(Locale.UK);
                }
            }
        });

        TTSRu = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    Locale locale = new Locale("ru");
                    TTSRu.setLanguage(locale);
                }
            }
        });

    }

    private void createArrays() {
        wordsEngArray = new ArrayList();
        wordsRusArray = new ArrayList();

        for (int i = 0; i < arrayListWords.size(); i++) {
            String[] wordsArrayAfterSplit = arrayListWords.get(i).toString().split("[=>]");

            if(wordsArrayAfterSplit.length == 7){
                if(wordsArrayAfterSplit[6].equalsIgnoreCase("1")){
                    wordsEngArray.add(wordsArrayAfterSplit[0]);
                    wordsRusArray.add(wordsArrayAfterSplit[2]);
                }
            }else {
                wordsEngArray.add(wordsArrayAfterSplit[0]);
                wordsRusArray.add(wordsArrayAfterSplit[2]);
            }

        }
    }

    @Override
    protected void onDestroy() {

        if (TTSEng != null) {
            TTSEng.stop();
            TTSEng.shutdown();
        }

        if (TTSRu != null) {
            TTSRu.stop();
            TTSRu.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        needStopPlayer = true;
        finish();

    }


    private class MyAsinkTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(PlayerActivity.this, "start", Toast.LENGTH_SHORT).show();
            // Do something like display a progress bar
        }

        @Override
        protected String doInBackground(String... strings) {

            int i = 0;

            while(!needStopPlayer) {

                for (int j = 0; j < 5; j++) {
                    if (needStopPlayer) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (needStopPlayer) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (needStopPlayer) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (needStopPlayer) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (needStopPlayer) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (needStopPlayer) {
                        break;
                    }


                    if (j == 1 || j == 3) {
                        publishProgress(i, j);
                    }
                }

                i++;

                if(i == wordsEngArray.size()){
                    i = 0;
                }
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            textViewLog.setText("currentIndex = " + values[0]);
            if (values[1] == 1){
                sayEnglish(values[0]);
            }
            if (values[1] == 3){
                sayRussian(values[0]);
            }
            // Do things like update the progress bar
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Toast.makeText(PlayerActivity.this, "finish", Toast.LENGTH_SHORT).show();
            mBtnPlayStop.setBackgroundResource(R.drawable.ic_play_color);
            speechPlays = false;
            needStopPlayer = false;
            textViewLog.setText("--log--" );

            // Do things like hide the progress bar or change a TextView
        }
    }


}


