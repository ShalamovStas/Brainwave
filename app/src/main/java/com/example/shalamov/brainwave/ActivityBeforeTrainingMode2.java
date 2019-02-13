package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.WordModel;

public class ActivityBeforeTrainingMode2 extends AppCompatActivity {

    private LinearLayout mBtnListWords, mBtnTraining, mBtnAudioTraining;
    private int lessonNumber;

    private WordModel lesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_training_mode2);

        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (WordModel) Global.getLessonsListForMode2().get(lessonNumber);

        init();
        setListeners();

    }

    private void init() {

        mBtnListWords = findViewById(R.id.btn_words_list);
        mBtnTraining = findViewById(R.id.btn_training);
        mBtnAudioTraining = findViewById(R.id.btn_audio_training);
    }

    private void setListeners() {

        mBtnListWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBeforeTrainingMode2.this, ListWordsActivity.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                startActivity(intent);
            }
        });

        mBtnTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lesson.getArrayListWords().size() != 0) {
                    Intent intent = new Intent(ActivityBeforeTrainingMode2.this, QuizWordsActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTrainingMode2.this, "Нет слов для изучения!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnAudioTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lesson.getArrayListWords().size() != 0) {
                    Intent intent = new Intent(ActivityBeforeTrainingMode2.this, PlayerActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityBeforeTrainingMode2.this, "Нет слов для изучения!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    @Override
//    public void onBackPressed() {
//        Global.getJsonUtils().saveFromModelToFileMode2();
////        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
//        finish();
//    }

}
