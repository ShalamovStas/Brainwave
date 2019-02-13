package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shalamov.brainwave.utils.LessonModel;
import com.example.shalamov.brainwave.utils.WordModel;

import java.util.ArrayList;

public class ListWordsActivity extends AppCompatActivity {

    String TAG = "ListWordsActivity";
    private LinearLayout mLayoutForAddContent;
    private ActionBar ab;
    private LessonModel lessonMode1;
    private WordModel lessonMode2;
    private int lessonNumber;
    private FloatingActionButton floatingActionButton;
    private boolean needToSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_words);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));

        ab = getSupportActionBar();
        ab.setTitle("Слова");
        init();
        setListeners();
        setDataForLayout();
    }

    private void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListWordsActivity.this, ChooseWordForVocabularyActivity.class);
                intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                intent.putExtra("currentSentenceIndex", "0");
                intent.putExtra("addNewWordFlag", "1");
                startActivityForResult(intent, 1);

            }
        });
    }


    private void setDataForLayout() {
        mLayoutForAddContent.removeAllViews();
        ArrayList words = null;
        switch (Global.mode){
            case 1:
                lessonMode1 = (LessonModel) Global.getLessonsList().get(lessonNumber);
                words = lessonMode1.getArrayListWords();
                break;
            case 2:
                lessonMode2 = (WordModel) Global.getLessonsListForMode2().get(lessonNumber);
                words = lessonMode2.getArrayListWords();
                break;
            case 3:
                break;

        }


        for (int i = 0; i < words.size(); i++) {
            final int index = i;
            final String text = words.get(i).toString();
            final View mLayout = getLayoutInflater().inflate(R.layout.element_for_list_words_activity, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence_list_word_activity);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence_list_word_activity);

            final ImageView mImageLearnNotLearn = (ImageView) mLayout.findViewById(R.id.image_learn_not_learn_list_word_activity);
            final LinearLayout mLayoutLearnNotLearn = (LinearLayout) mLayout.findViewById(R.id.btn_learn_not_learn);

            String[] informationAboutObject = Global.getLessonsUtils().formTextForWordList(text);

            mTextPieceOfSentence.setText(Html.fromHtml(informationAboutObject[0]));
            if (i % 2 == 0) {
                mLayoutPieceOfSentence.setBackgroundColor(ContextCompat.getColor(ListWordsActivity.this, R.color.myColorWhite));
            }

            if(informationAboutObject[1].equals("1")){
                mImageLearnNotLearn.setBackgroundResource(R.drawable.ic_play_color);

            }else{
                mImageLearnNotLearn.setBackgroundResource(R.drawable.ic_pause_color);

            }

            mLayoutPieceOfSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListWordsActivity.this, ChooseWordForVocabularyActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    intent.putExtra("currentSentenceIndex", "0");
                    intent.putExtra("addNewWordFlag", "2");
                    if(Global.mode == 1) {
                        intent.putExtra("text", lessonMode1.getArrayListWords().get(index));
                    }
                    if(Global.mode == 2) {
                        intent.putExtra("text", lessonMode2.getArrayListWords().get(index));
                    }
                    startActivityForResult(intent, 1);
                }
            });

            mLayoutPieceOfSentence.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListWordsActivity.this);
                    builder.setTitle("Удалить слово?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(Global.mode == 1) {
                                Global.getLessonsUtils().deleteWord(lessonMode1, text);
                            }
                            if(Global.mode == 2) {
                                Global.getLessonsUtils().deleteWord(lessonMode2, text);
                            }
                            mLayoutForAddContent.removeView(mLayout);

                            if(Global.mode == 1) {
                                Global.getJsonUtils().saveFromModelToFile();

                            }
                            if(Global.mode == 2) {
                                Global.getJsonUtils().saveFromModelToFileMode2();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.create().show();


                    return true;
                }
            });

            mLayoutLearnNotLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    needToSave = true;
                    boolean flag = false;
                    if(Global.mode == 1) {
                        flag = Global.getLessonsUtils().checkFlagLearnNotLearn(lessonMode1, index);
                    }
                    if(Global.mode == 2) {
                        flag = Global.getLessonsUtils().checkFlagLearnNotLearn( lessonMode2, index);
                    }


                    if(flag){
                        mImageLearnNotLearn.setBackgroundResource(R.drawable.ic_pause_color);
                        if(Global.mode == 1) {
                            Global.getLessonsUtils().updateWordLearnNotLearn(lessonMode1, index, "0");
                        }
                        if(Global.mode == 2) {
                            Global.getLessonsUtils().updateWordLearnNotLearn(lessonMode2, index, "0");
                        }


                    }else{
                        mImageLearnNotLearn.setBackgroundResource(R.drawable.ic_play_color);
                        if(Global.mode == 1) {
                            Global.getLessonsUtils().updateWordLearnNotLearn(lessonMode1, index, "1");
                        }
                        if(Global.mode == 2) {
                            Global.getLessonsUtils().updateWordLearnNotLearn(lessonMode2, index, "1");
                        }

                    }

                }
            });

            mLayoutForAddContent.addView(mLayout);
        }
    }

    private void init() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mLayoutForAddContent = (LinearLayout) findViewById(R.id.layout_for_add_content_list_words);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setDataForLayout();
        }
    }

        @Override
    public void onBackPressed() {

        if(needToSave) {
            if (Global.mode == 1) {
                Global.getJsonUtils().saveFromModelToFile();
            }
            if (Global.mode == 2) {
                Global.getJsonUtils().saveFromModelToFileMode2();
            }
        }

        finish();
    }


}
