package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.preference.DialogPreference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_words);
        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);
        init();
        setDataForLayout();
    }



    private void setDataForLayout() {

        ArrayList words = lesson.getArrayListWords();

        for (int i = 0; i < words.size(); i++) {
            final String text = words.get(i).toString();
            final View mLayout = getLayoutInflater().inflate(R.layout.element_choose_world_for_vocab, null);
            final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence);
            final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence);

            mTextPieceOfSentence.setText(Html.fromHtml(Global.getLessonsUtils().formTextForWebBoltFormat(words.get(i).toString())));
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
        mLayoutForAdd = (LinearLayout) findViewById(R.id.layout_for_add_content_list_words);
    }
}
