package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shalamov.brainwave.utils.Lesson;

import java.util.ArrayList;

public class ListWordsActivity extends AppCompatActivity {

    private LinearLayout mLayoutForAdd;
    private Lesson lesson;
    private int lessonNumber;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_words);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
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

        mLayoutForAdd.removeAllViews();
        ArrayList words = lesson.getArrayListWords();

        for (int i = 0; i < words.size(); i++) {
            final String text = words.get(i).toString();
            final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence);

            mTextPieceOfSentence.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWordList(words.get(i).toString())));
           if(i % 2 == 0){
               mLayoutPieceOfSentence.setBackgroundColor(ContextCompat.getColor(ListWordsActivity.this, R.color.myColorWhite));
           }

            mLayoutPieceOfSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListWordsActivity.this, ChooseWordForVocabularyActivity.class);
                    intent.putExtra("lessonNumber", Integer.toString(lessonNumber));
                    intent.putExtra("currentSentenceIndex", "0");
                    intent.putExtra("addNewWordFlag", "2");
                    intent.putExtra("text", text);
                    startActivityForResult(intent, 1);
                }
            });

            mLayoutPieceOfSentence.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListWordsActivity.this);
                    builder.setTitle("Delete word?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Global.getLessonsUtils().deleteWord(lesson, text);
                            mLayoutForAdd.removeView(mLayout);
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

            mLayoutForAdd.addView(mLayout);
        }
    }

    private void init() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mLayoutForAdd = (LinearLayout) findViewById(R.id.layout_for_add_content_list_words);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            setDataForLayout();
        }
    }


}
