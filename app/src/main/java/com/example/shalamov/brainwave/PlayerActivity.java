package com.example.shalamov.brainwave;

import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    private String[] wordsRusArray, wordsEngArray;

    private boolean speechIsPossible, speechPlays;

    private CountDownTimer timer;


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

                    mBtnPlayStop.setImageResource(R.drawable.ic_play_color);
                    speechPlays = false;

                } else {
                    speechPlays = true;
                    mBtnPlayStop.setImageResource(R.drawable.ic_pause_color);

                    Thread thread = new Thread() {
                        public void run() {
                            Log.d(TAG, "\nstart thread");
                            byte count = 0;
                            while (speechPlays) {
                                Log.d(TAG, "\nthread" + count);
                                count++;

                                try {
                                    sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(count == 10){
                                    speechPlays = false;
                                }
                            }
                            Log.d(TAG, "\nfinish thread");
                        }
                    };

                    thread.run();

                    Log.d(TAG, "\nafter thread.run();");

                }
            }
        });
    }

    private void stopPlayer() {

        if (timer != null) {
            timer.cancel();
        }
    }

    private void startPlayer(int milisInFuture, int countDownInterval) {
        final int mIF = milisInFuture;
        timer = new CountDownTimer(milisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {

                int progress = (int) (mIF / 1000);
                if (progress == 2) {
                    mTTSRu.speak("Пример", TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void onFinish() {
                mTTSRu.speak("Пример", TextToSpeech.QUEUE_FLUSH, null);
                mBtnPlayStop.setImageResource(R.drawable.ic_play_color);
            }
        }.start();
    }

    private void init() {
//        mTTSRu = new TextToSpeech(this, this);
//        mTTSEng = new TextToSpeech(this, this);
        mBtnPlayStop = (ImageView) findViewById(R.id.play_pause);
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        arrayListWords = lesson.getArrayListWords();
        createArrays();
        speechPlays = false;
    }

    private void createArrays() {
        wordsEngArray = new String[arrayListWords.size()];
        wordsRusArray = new String[arrayListWords.size()];

        for (int i = 0; i < arrayListWords.size(); i++) {
            String[] wordsArrayAfterSplit = arrayListWords.get(i).toString().split("[=>]");

            wordsEngArray[i] = wordsArrayAfterSplit[0];
            wordsRusArray[i] = wordsArrayAfterSplit[2];

        }
    }


//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//
//            Locale localeRu = new Locale("ru");
//            Locale localeEng = new Locale("en-US");
//
//            int result1 = mTTSRu.setLanguage(localeRu);
//            int result2 = mTTSEng.setLanguage(localeEng);
//
//            //int result = mTTSRu.setLanguage(Locale.getDefault());
//
//            if (result1 == TextToSpeech.LANG_MISSING_DATA
//                    || result1 == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Toast.makeText(this, "Этот язык не поддерживается", Toast.LENGTH_SHORT).show();
//                Log.e("TTS", "Извините, этот язык не поддерживается");
//            } else {
//
//                speechIsPossible = true;
//            }
//
//        } else {
//            Log.e("TTS", "Ошибка!");
//        }
//    }
}
