package com.example.shalamov.brainwave;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
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
    private TextView mEditTextExampleUsage;
    private LinearLayout mLayoutForAddContent;
    private int lessonNumber, addNewWordFlag, mode;
    private int currentSentenceIndex;
    private ClipboardManager clipboard;
    private QuizLogic mQuizLogic;
    private Lesson lesson;
    private String oldText, exampleOfUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_for_vocabulary);


        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        currentSentenceIndex = Integer.parseInt(getIntent().getStringExtra("currentSentenceIndex"));
        addNewWordFlag = Integer.parseInt(getIntent().getStringExtra("addNewWordFlag"));
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        init();
        setListeners();

        switch (addNewWordFlag) {
            //ДОБАВЛЕНИЕ НОВОГО СЛОВА
            case 1:
                mode = 1;
                mEditTextEng.setFocusableInTouchMode(true);
                mEditTextRu.setFocusableInTouchMode(true);
                setDataToLayoutAddNewWordMode();
                break;
            //РЕДАКТИРОВАНИЕ СУЩЕСТВУЮЩЕГО
            case 2:
                mode = 2;
                oldText = getIntent().getStringExtra("text");
                setDataToLayoutCorrectingMode(oldText);
                mEditTextEng.setFocusableInTouchMode(true);
                mEditTextRu.setFocusableInTouchMode(true);
                break;
            //ДОБАВЛЕНИЕ НОВОГО слова ИЗ существующего ТЕКСТА
            default:
                mode = 3;
                setDataToLayout();
                break;
        }

    }

    private void setDataToLayoutAddNewWordMode() {
        View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab_for_example, null);
        mEditTextExampleUsage = (TextView) mLayout.findViewById(R.id.example_of_usage_word);
        LinearLayout btnContainerExampleOfUsage = (LinearLayout)  mLayout.findViewById(R.id.button_container_example_of_usage_word);
       addListenerToBtnContainerExampleOfUsage(btnContainerExampleOfUsage, mEditTextExampleUsage, mEditTextExampleUsage.getText().toString());
        mLayoutForAddContent.addView(mLayout);
    }

    private void setDataToLayoutCorrectingMode(String text) {
        View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab_for_example, null);
        mEditTextExampleUsage = (TextView) mLayout.findViewById(R.id.example_of_usage_word);
        LinearLayout btnContainerExampleOfUsage = (LinearLayout)  mLayout.findViewById(R.id.button_container_example_of_usage_word);
        addListenerToBtnContainerExampleOfUsage(btnContainerExampleOfUsage, mEditTextExampleUsage, mEditTextExampleUsage.getText().toString());
        mLayoutForAddContent.addView(mLayout);

        String[] wordsRusEng = text.split("[=>]");
        mEditTextEng.setText(wordsRusEng[0]);
        mEditTextRu.setText(wordsRusEng[2]);

        if (wordsRusEng.length >= 5) {
            mEditTextExampleUsage.setText(wordsRusEng[4]);
        }

    }

    private void setDataToLayout() {
        mLayoutForAddContent.removeAllViews();
        String[] words = getEngWordsFromSentence();

        View mLayoutExampleOfUsage = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab_for_example, null);
        mEditTextExampleUsage = (TextView) mLayoutExampleOfUsage.findViewById(R.id.example_of_usage_word);
        mEditTextExampleUsage.setText(getEngSentenceForExampleOfUsage());
        LinearLayout btnContainerExampleOfUsage = (LinearLayout)  mLayoutExampleOfUsage.findViewById(R.id.button_container_example_of_usage_word);
        addListenerToBtnContainerExampleOfUsage(btnContainerExampleOfUsage, mEditTextExampleUsage, mEditTextExampleUsage.getText().toString());
        mLayoutForAddContent.addView(mLayoutExampleOfUsage);


        for (int i = 0; i < words.length; i++) {
            final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence);

            mTextPieceOfSentence.setText(words[i]);

            mLayoutPieceOfSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTextPieceOfSentence.setTextColor(Color.parseColor("#3F51B5"));

                    if (mEditTextEng.getText().length() == 0) {
                        mEditTextEng.setText(mTextPieceOfSentence.getText().toString());
                    } else {
                        mEditTextEng.setText(mEditTextEng.getText().toString() + " " + mTextPieceOfSentence.getText().toString());
                    }
                    mEditTextEng.setFocusableInTouchMode(true);
                    mEditTextRu.setFocusableInTouchMode(true);
                }
            });


            mLayoutForAddContent.addView(mLayout);
        }


    }

    private void addListenerToBtnContainerExampleOfUsage(LinearLayout btnContainerExampleOfUsage, TextView mEditText, final String oldText) {
        final TextView finalEditText = mEditText;
        btnContainerExampleOfUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordForVocabularyActivity.this);
                builder.setTitle("Пример использования слова");
                final EditText editText = new EditText(ChooseWordForVocabularyActivity.this);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                if(finalEditText.getText().toString().length() != 0){
                    editText.setText(finalEditText.getText().toString());
                }else{
                    editText.setHint("Введите пример");
                }
                builder.setView(editText);
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finalEditText.setText(editText.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });
    }

    private String[] getEngWordsFromSentence() {
        String sentence = mQuizLogic.getSentenceString(currentSentenceIndex);
        String[] engAndRusArray = sentence.split("[=>]");
        return engAndRusArray[0].split("[ ]");
    }

    private String getEngSentenceForExampleOfUsage() {
        String sentence = mQuizLogic.getSentenceString(currentSentenceIndex);
        String[] engAndRusArray = sentence.split("[=>]");
        return engAndRusArray[0];
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

                switch (mode) {
                    case 1:
                        addNewWord();
                        break;
                    case 2:
                        correctingWord();
                        break;
                    case 3:
                        addNewWord();
                        break;
                }


            }
        });
    }

    private void correctingWord() {
        if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
            String newText = mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString()
                    + "=>" + getExampleOfUsageWord();
            Global.getLessonsUtils().changeWord(lesson, oldText, newText);
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        } else {
            Toast.makeText(ChooseWordForVocabularyActivity.this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
        }

    }


    private void addNewWord() {
        if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
            boolean flag = false;
            flag = Global.getLessonsUtils().addWord(lesson, mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString()
                    + "=>" + getExampleOfUsageWord());

            if (flag) {
                Toast.makeText(ChooseWordForVocabularyActivity.this, mEditTextEng.getText().toString() + " saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            } else {
                Toast.makeText(ChooseWordForVocabularyActivity.this, "Слово уже добавлено!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ChooseWordForVocabularyActivity.this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
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

    private String getExampleOfUsageWord() {
        if (mEditTextExampleUsage != null) {
            return mEditTextExampleUsage.getText().toString();
        } else {
            return "";
        }
    }
}
