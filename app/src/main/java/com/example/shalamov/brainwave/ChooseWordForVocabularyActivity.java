package com.example.shalamov.brainwave;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;

public class ChooseWordForVocabularyActivity extends AppCompatActivity {
    private EditText mEditTextEng, mEditTextRu;
    private Button mBtnDelete, mBtnSave, mBtnGoToDictionary, mBtnPaste;
    private LinearLayout mLayoutForAddContent;
    private int lessonNumber;
    private int currentSentenceIndex;
    private ClipboardManager clipboard;
    private QuizLogic mQuizLogic;
    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_for_vocabulary);

        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        currentSentenceIndex = Integer.parseInt(getIntent().getStringExtra("currentSentenceIndex"));
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        init();
        setListeners();
        setDataToLayout();
    }

    private void setDataToLayout() {
        String[] words = mQuizLogic.getCurrentSentenceArray();
        mLayoutForAddContent.removeAllViews();
        for (int i = 0; i < words.length; i++) {
            final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence);

            mTextPieceOfSentence.setText(words[i]);

            mLayoutPieceOfSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEditTextEng.setText(mEditTextEng.getText().toString() + " " + mTextPieceOfSentence.getText().toString());
                    mEditTextEng.setFocusableInTouchMode(true);
                    mEditTextRu.setFocusableInTouchMode(true);
                }
            });


            mLayoutForAddContent.addView(mLayout);
        }


    }


    private void init() {
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        mQuizLogic = Global.getmQuizLogic();
        mEditTextEng = (EditText) findViewById(R.id.eng_text_choose_word_vocabulary);
        mEditTextRu = (EditText) findViewById(R.id.ru_text_choose_word_vocabulary);
        mBtnDelete = (Button) findViewById(R.id.btn_delete_choose_word_vocabulary);
        mBtnPaste = (Button) findViewById(R.id.btn_paste_choose_word_vocabulary);

        mBtnSave = (Button) findViewById(R.id.btn_save_choose_word_vocabulary);
        mBtnGoToDictionary = (Button) findViewById(R.id.btn_gotodictionary_choose_word_vocabulary);
        mLayoutForAddContent = (LinearLayout) findViewById(R.id.layout_for_add_content_choose_word_vocabulary);


        mEditTextEng.setFocusableInTouchMode(false);
        mEditTextRu.setFocusableInTouchMode(false);




    }

    private void setListeners() {

        mBtnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteText();
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextEng.setText("");
                mEditTextRu.setText("");
            }
        });
        mBtnGoToDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextEng.getText().toString().length() != 0) {
                    openGoogleTranslator(mEditTextEng.getText().toString());
                } else {
                    Toast.makeText(ChooseWordForVocabularyActivity.this, "Заполните поле!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
                    saveWord();
                }

            }
        });
    }

    private void saveWord() {
        boolean flag = Global.getLessonsUtils().addWord(lesson, mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString());

        if (flag) {
            Toast.makeText(ChooseWordForVocabularyActivity.this, mEditTextEng.getText().toString() + " saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(ChooseWordForVocabularyActivity.this, "Слово уже добавлено!", Toast.LENGTH_SHORT).show();
        }
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

    private void pasteText() {
        ClipData abc = clipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String text = item.getText().toString();
        mEditTextRu.setText(text);

    }
}
