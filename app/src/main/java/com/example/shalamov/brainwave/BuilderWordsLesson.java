package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BuilderWordsLesson extends AppCompatActivity {

    private LinearLayout mButtonChooseLabel;
    private String labelForLesson;
    private ImageView mLabelLesson;
    EditText lesson_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder_words_lesson);

        mButtonChooseLabel = (LinearLayout) findViewById(R.id.btn_choose_label);
        mLabelLesson = (ImageView) findViewById(R.id.image_for_lesson);
        Global.getImageUtils().updateLabel("label_0", mLabelLesson);
        labelForLesson = "label_0";
        lesson_name = (EditText) findViewById(R.id.lesson_name);

        mButtonChooseLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChooseLabel.class);
                startActivityForResult(i, 2);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        menu.add(0, 1, 0, "save").setIcon(R.drawable.ic_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == 1) {

            if(lesson_name.getText().toString().length() != 0){

                Global.getLessonsUtils().createLessonMode2(Global.getLessonsListForMode2().size() + 1, lesson_name.getText().toString(),"", labelForLesson);
                Intent i = new Intent();
                setResult(2, i);
                this.finish();

            }else{

                Toast.makeText(this, "Input text into text fields", Toast.LENGTH_SHORT).show();

            }


        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            labelForLesson = data.getStringExtra("Label");
            Global.getImageUtils().updateLabel(labelForLesson, mLabelLesson);

        }
    }
}
