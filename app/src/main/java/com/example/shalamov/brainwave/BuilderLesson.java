package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.jsonUtils.JsonUtils;

import java.util.ArrayList;

public class BuilderLesson extends AppCompatActivity {

    LinearLayout mButtonChooseLabel;
    ImageView mLabelLesson;
    String labelForLesson;
    String valuesFromActivity;
    JsonUtils jsonUtils;
    EditText lesson_name;
    EditText lesson_text;
    ArrayList mListLessons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder_lesson);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        jsonUtils = new JsonUtils(this);
        lesson_name = (EditText) findViewById(R.id.lesson_name);
        lesson_text = (EditText) findViewById(R.id.text_lesson);
        labelForLesson = "label_0";
        mButtonChooseLabel = (LinearLayout) findViewById(R.id.btn_choose_label);
        mLabelLesson = (ImageView) findViewById(R.id.image_for_lesson);
        jsonUtils.updateLabel("label_0", mLabelLesson);

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

        menu.add(0, 1, 0, "save").setIcon(R.drawable.ic_save_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == 1) {

            if(lesson_name.getText().toString().length() != 0 && lesson_text.getText().toString().length() != 0){

                Boolean bool1 = lesson_name.getText().toString() != "";
                jsonUtils.insertTextIntoJson(lesson_name.getText().toString(), lesson_text.getText().toString(), labelForLesson);
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
            System.out.println(labelForLesson);
            jsonUtils.updateLabel(labelForLesson, mLabelLesson);

        }
    }


}



