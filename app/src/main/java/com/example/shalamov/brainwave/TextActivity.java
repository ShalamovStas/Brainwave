package com.example.shalamov.brainwave;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;

public class TextActivity extends AppCompatActivity {

    private TextView textView;
    private int lessonNumber;
    private Lesson lesson;
    private ActionBar ab;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle("Text");

        textView = (TextView) findViewById(R.id.text_activity_text);
        scrollView = (ScrollView) findViewById(R.id.scroll_text_activity);

        lessonNumber = Integer.parseInt(getIntent().getStringExtra("lessonNumber"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);

        setTextToView();
    }

    private void setTextToView() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < lesson.getArrayListText().size(); i++) {

            stringBuilder.append(Global.getLessonsUtils().cleanText(lesson.getArrayListText().get(i)));
            stringBuilder.append("\n\n");
        }

        textView.setText(stringBuilder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_day:
                setDay();
                break;
            case R.id.action_night:
                setNight();
                break;

        }

        return true;
    }

    private void setDay() {
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        scrollView.setBackgroundColor(ContextCompat.getColor(TextActivity.this, R.color.myColorGrey100));
        textView.setTextColor(ContextCompat.getColor(TextActivity.this, R.color.colorPrimary));

        textView.setText("");
        setTextToView();

    }

    private void setNight() {
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorGrey800)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.myColorGrey900));
        }
        scrollView.setBackgroundColor(ContextCompat.getColor(TextActivity.this, R.color.myColorGrey600));
        textView.setTextColor(ContextCompat.getColor(TextActivity.this, R.color.myColorDark));

        textView.setText("");
        setTextToView();

    }
}
