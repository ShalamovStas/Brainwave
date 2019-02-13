package com.example.shalamov.brainwave;

import android.app.Activity;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.LessonModel;
import com.example.shalamov.brainwave.utils.WordModel;

public class ChooseWordForVocabularyActivity extends AppCompatActivity {
    String TAG = "ChooseWordForVocabularyActivity";
    private EditText mEditTextEng, mEditTextRu;
    private Button mBtnDelete, mBtnSave, mBtnGoToDictionary, mBtnPaste;
    private TextView mTextViewExampleOfUsage;
    private LinearLayout mLayoutForAddContent, mLayoutContainerExampleOfUsage;
    private int lessonNumber, addNewWordFlag, mode;
    private int currentSentenceIndex;
    private ClipboardManager clipboard;
    private QuizLogic mQuizLogic;
    private LessonModel lessonGlobalMode1;
    private WordModel lessonGlobalMode2;
    private String oldText, exampleOfUsage;

    //в режиме создания нового слова 1 нажатие на paste - вставляем текст в example of usage
    //второе - вставляем скопированный перевод

    private byte countSaveClick;

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



    private void setDataToLayoutCorrectingMode(String text) {

        String[] wordsRusEng = text.split("[=>]");
        mEditTextEng.setText(wordsRusEng[0]);
        mEditTextRu.setText(wordsRusEng[2]);

        if (wordsRusEng.length >= 5) {
            mTextViewExampleOfUsage.setText(wordsRusEng[4]);
        }

    }

    private void setDataToLayout() {
        mLayoutForAddContent.removeAllViews();
        String[] words = getEngWordsFromSentence();

        mTextViewExampleOfUsage.setText(getEngSentenceForExampleOfUsage());


        for (int i = 0; i < words.length; i++) {
            final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence_list_word_activity);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence_list_word_activity);

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

//    private void addListenerToBtnContainerExampleOfUsage(LinearLayout btnContainerExampleOfUsage, TextView mEditText, final String oldText) {
//        final TextView finalEditText = mEditText;
//        btnContainerExampleOfUsage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordForVocabularyActivity.this);
//                builder.setTitle("Пример использования слова");
//                final EditText editText = new EditText(ChooseWordForVocabularyActivity.this);
//                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
//                if(finalEditText.getText().toString().length() != 0){
//                    editText.setText(finalEditText.getText().toString());
//                }else{
//                    editText.setHint("Введите пример");
//                }
//                builder.setView(editText);
//                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finalEditText.setText(editText.getText().toString());
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                builder.create().show();
//            }
//        });
//    }

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
        if (Global.mode == 1) {
            lessonGlobalMode1 = (LessonModel) Global.getLessonsList().get(lessonNumber);
        }

        if (Global.mode == 2) {
            lessonGlobalMode2 = (WordModel) Global.getLessonsListForMode2().get(lessonNumber);
        }
        mQuizLogic = Global.getmQuizLogic();
        mEditTextEng = (EditText) findViewById(R.id.eng_text_choose_word_vocabulary);
        mEditTextRu = (EditText) findViewById(R.id.ru_text_choose_word_vocabulary);
        mBtnDelete = (Button) findViewById(R.id.btn_delete_choose_word_vocabulary);
        mBtnPaste = (Button) findViewById(R.id.btn_paste_choose_word_vocabulary);

        mBtnSave = (Button) findViewById(R.id.btn_save_choose_word_vocabulary);
        mBtnGoToDictionary = (Button) findViewById(R.id.btn_gotodictionary_choose_word_vocabulary);
        mLayoutForAddContent = (LinearLayout) findViewById(R.id.layout_for_add_content_choose_word_vocabulary);
        mLayoutContainerExampleOfUsage = (LinearLayout) findViewById(R.id.container_example_of_usage_word); // контейнер для англ предложения
        mTextViewExampleOfUsage = (TextView) findViewById(R.id.example_of_usage_text); //текст пример английского предложения

        mEditTextEng.setFocusableInTouchMode(false);
        mEditTextRu.setFocusableInTouchMode(false);

        countSaveClick = 0;

    }

    private void setListeners() {

        mLayoutContainerExampleOfUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordForVocabularyActivity.this);
                builder.setTitle("Пример использования слова");
                final EditText editText = new EditText(ChooseWordForVocabularyActivity.this);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                if (mTextViewExampleOfUsage.getText().toString().length() != 0) {
                    editText.setText(mTextViewExampleOfUsage.getText().toString());
                } else {
                    editText.setHint("Введите пример");
                }
                builder.setView(editText);
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTextViewExampleOfUsage.setText(editText.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();

            }
        });

        mBtnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mode == 1) {
                    if (countSaveClick == 0) {
                        countSaveClick++;
                        pasteSentenceForExampleOfTheUsageAndCreatePartsOfSentence();

                        InputMethodManager imm = (InputMethodManager) ChooseWordForVocabularyActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    } else {
                        pasteText();
                    }
                } else {
                    pasteText();
                }

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
                        if (Global.mode == 1) {
                            addNewWordGlobalMode1();
                        }
                        if (Global.mode == 2) {
                            addNewWordGlobalMode2();
                        }
                        break;
                    case 2:
                        correctingWord();
                        break;
                    case 3:
                        if (Global.mode == 1) {
                            addNewWordGlobalMode1();
                        }
                        if (Global.mode == 2) {
                            addNewWordGlobalMode2();
                        }
                        break;
                }


                if(Global.mode == 1){
                    Global.getJsonUtils().saveFromModelToFile();
                }
                if(Global.mode == 2){
                    Global.getJsonUtils().saveFromModelToFileMode2();
                }
            }
        });
    }

    private void pasteSentenceForExampleOfTheUsageAndCreatePartsOfSentence() {

        ClipData abc = clipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String dirtySentence = item.getText().toString();
        if(dirtySentence.length() < 8000) {

            String cleanSentence = cleanText(dirtySentence);

            mTextViewExampleOfUsage.setText(cleanSentence);


            String[] engAndRusArray = cleanSentence.split("[=>]");
            String[] words = engAndRusArray[0].split("[ ]");

            for (int i = 0; i < words.length; i++) {
                final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
                final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence_list_word_activity);
                final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence_list_word_activity);
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

    }

    private String cleanText(String dirtySentence) {
        String[] allTextArray = dirtySentence.split("[.\\?\\!\\\n]");
        StringBuilder cleanText1 = new StringBuilder();
        for (int i = 0; i < allTextArray.length; i++) {
            cleanText1.append(allTextArray[i]);
            if(i != allTextArray.length-1 && !allTextArray[i].equalsIgnoreCase(" ")){
                cleanText1.append(" ");
            }
        }

        StringBuilder cleanText2 = new StringBuilder();
        String[] allTextArray2 = cleanText1.toString().split("[ ]");

        for (int i = 0; i < allTextArray2.length; i++) {
            cleanText2.append(allTextArray2[i] + " ");

        }
        return cleanText2.toString();
    }

    private void correctingWord() {
        if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
            String newText = mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString()
                    + "=>" + getExampleOfUsageWord() + "=>1";
            if (Global.mode == 1) {
                Global.getLessonsUtils().changeWord(lessonGlobalMode1, oldText, newText);
            }
            if (Global.mode == 2) {
                Global.getLessonsUtils().changeWord(lessonGlobalMode2, oldText, newText);
            }
            Log.d(TAG, "\noldText = " + oldText);
            Log.d(TAG, "\nnewText = " + newText);
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        } else {
            Toast.makeText(ChooseWordForVocabularyActivity.this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
        }

    }


    private void addNewWordGlobalMode1() {
        if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
            boolean flag = false;
            flag = Global.getLessonsUtils().addWord(lessonGlobalMode1, mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString()
                    + "=>" + getExampleOfUsageWord() + "=>1");

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

    private void addNewWordGlobalMode2() {
        if (mEditTextEng.getText().toString().length() != 0 & mEditTextRu.getText().toString().length() != 0) {
            boolean flag = false;
            flag = Global.getLessonsUtils().addWord(lessonGlobalMode2, mEditTextEng.getText().toString() + "=>" + mEditTextRu.getText().toString()
                    + "=>" + getExampleOfUsageWord() + "=>1");

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
        String text;
        if (mTextViewExampleOfUsage != null) {
            if (mTextViewExampleOfUsage.getText().toString().equalsIgnoreCase("")) {
                text = "-";
            } else {
                text = mTextViewExampleOfUsage.getText().toString();
            }
            return mTextViewExampleOfUsage.getText().toString();
        } else {
            text = "-";
        }

        return text;
    }



}
