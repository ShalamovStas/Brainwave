package com.example.shalamov.brainwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shalamov.brainwave.utils.LessonModel;

public class TextEditorActivity extends AppCompatActivity {

    int currentSentenceIndex, lessonNumber;
    Button mBtnSave, mBtnCancel, mBtnCleanText;
    QuizLogic mQuizLogic;
    EditText mEditTextEng, mEditTextRu;
    LessonModel lesson;

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

        String[] allTextArray = mQuizLogic.getCurrentSentenceString().split("[=>]");

        mEditTextEng.setText(allTextArray[0]);
        if(allTextArray.length >= 3) {
            mEditTextRu.setText(allTextArray[2]);
        }else{
            mEditTextRu.setText("");
        }
    }

    private void initialize() {
        mQuizLogic = Global.getmQuizLogic();
        lesson = (LessonModel) Global.getLessonsList().get(lessonNumber);
        mEditTextEng = (EditText) findViewById(R.id.edit_text_english);
        mEditTextRu = (EditText) findViewById(R.id.edit_text_russian);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCleanText = (Button) findViewById(R.id.btn_clean);


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = "";
                if(mEditTextRu.getText().toString().length() != 0){
                    newText = mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString();
                }else{
                    newText = mEditTextEng.getText().toString();
                }

                Global.getLessonsUtils().changeSentence(lesson, mQuizLogic.getCurrentSentenceString(), newText);

                finish();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnCleanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextEng.setText("");
                mEditTextRu.setText("");
            }
        });

    }



}
