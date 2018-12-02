package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shalamov.brainwave.jsonUtils.JsonUtilsOld;

import java.util.ArrayList;

public class BuilderLesson extends AppCompatActivity {

    LinearLayout mButtonChooseLabel;
    ImageView mLabelLesson;
    String labelForLesson;
    String valuesFromActivity;

    EditText lesson_name;
    EditText lesson_text;
    ArrayList mListLessons;
    EditText ediTextForDeleting;
    Button buttonDelete;

    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder_lesson);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lesson_name = (EditText) findViewById(R.id.lesson_name);
        lesson_text = (EditText) findViewById(R.id.text_lesson);
        labelForLesson = "label_0";
        mButtonChooseLabel = (LinearLayout) findViewById(R.id.btn_choose_label);
        mLabelLesson = (ImageView) findViewById(R.id.image_for_lesson);
        spinner = (Spinner) findViewById(R.id.spinner);
        Global.getImageUtils().updateLabel("label_0", mLabelLesson);

        ediTextForDeleting = (EditText) findViewById(R.id.text_for_delete);
        buttonDelete = (Button) findViewById(R.id.btn_delete);

        mButtonChooseLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChooseLabel.class);
                startActivityForResult(i, 2);

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ediTextForDeleting.getText().length() != 0 && lesson_text.getText().length()!= 0){
                    improveText();
                }
            }
        });

    }

    private void improveText() {
        String newText = Global.getLessonsUtils().deleteElementInString(lesson_text.getText().toString(), ediTextForDeleting.getText().toString());
        lesson_text.setText(newText);
        ediTextForDeleting.setText("");
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

                boolean bool1 = lesson_name.getText().toString() != "";

                Global.getLessonsUtils().createLesson(Global.getLessonsList().size() + 1, lesson_name.getText().toString(), lesson_text.getText().toString(), "", this.getSpinnerSelect(), "description2", "description3", "description1",
                        labelForLesson, "progress 1");

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

    private String getSpinnerSelect() {
        String selectedSpinner = spinner.getSelectedItem().toString();
        String category = "";
        switch (selectedSpinner) {
            case "Temp":
                category = "temp";
                break;
            case "Current":
                category = "current";
                break;
            case "Important":
                category = "important";
                break;
            default:
                category = "temp";
                break;
        }
        return category;
    }


}



