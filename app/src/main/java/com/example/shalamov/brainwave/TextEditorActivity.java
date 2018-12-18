package com.example.shalamov.brainwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shalamov.brainwave.utils.Lesson;

public class TextEditorActivity extends AppCompatActivity {

    int currentSentenceIndex, lessonNumber;
    Button mBtnSave, mBtnCancel;
    QuizLogic mQuizLogic;
    EditText mEditText;
    Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);


        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        currentSentenceIndex = Integer.parseInt(getIntent().getStringExtra("currentSentenceIndex"));
        initialize();
        setTextToField();
    }

    private void setTextToField() {
        mEditText.setText(mQuizLogic.getCurrentSentenceString());
    }

    private void initialize() {
        mQuizLogic = Global.getmQuizLogic();
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.getLessonsUtils().changeSentence(lesson, mQuizLogic.getCurrentSentenceString(), mEditText.getText().toString());

                finish();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



}
