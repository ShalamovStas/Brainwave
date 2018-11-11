package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.jsonUtils.JsonUtils;
import com.example.shalamov.brainwave.jsonUtils.Lesson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mMainLayout, mLessonBuilderLayout, mLessonChooseLayout;
    private RelativeLayout mRelativeLayout_level_0;
    private ScrollView mScrollView_level_1;
    private int count;
    private ArrayList mListLessons;
    private JsonUtils mJsonUtils;
    private Animation animation;
    private FloatingActionButton mActionButton;
    private MainActivityLogic mMainActivityLogic;
    ActionBar ab;
    private String theme;

    public JsonUtils getJsonUtils() {
        return mJsonUtils;
    }

    private TextView mTextViewFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(true);
        ab.setSubtitle("Lessons");

        identification();
        setSettingsToThisActivity();
        mJsonUtils.readJsonStringToArrayList(mJsonUtils.readFile());
        addElementsToLayouts();

    }



    private void setSettingsToThisActivity() {
        theme = mJsonUtils.readSettings();
        if (theme == "no settings") {
            theme = "day";
        }


        if (theme.equalsIgnoreCase("night")) {


            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColorGrey800));

//            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColorNight500));
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorGrey800)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.myColorGrey900));
            }
        } else {
            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColor3));
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

    }

    private void addElementsToLayouts() {
        int counterAnimation = 0;

//        View lessonBuilderLayout = getLayoutInflater().inflate(R.layout.lesson_builder_label, null);
//        ImageView labelLessonBuilderLayout = (ImageView) lessonBuilderLayout.findViewById(R.id.imageViewLessonBuilder);
//        labelLessonBuilderLayout.setImageResource(R.drawable.ic_think);
//        mMainLayout.addView(lessonBuilderLayout);
//        lessonBuilderLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, BuilderLesson.class);
//                startActivityForResult(intent, 2);
//            }
//        });

//        Toast.makeText(this, "Size of Array " +  mListLessons.size(), Toast.LENGTH_SHORT).show();
        for (int i = mListLessons.size() - 1; i >= 0; i--) {


            final int numberOfElement = i;
            View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
            final TextView mainText = (TextView) view1.findViewById(R.id.main_text);
            final TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
            final LinearLayout  mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
            final Lesson lesson = (Lesson) mListLessons.get(i);
            mainText.setText(lesson.getName());
            mTextDescription.setText(mMainActivityLogic.getNumberOfSentences(lesson.getText()));
            ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);
            mJsonUtils.updateLabel(((Lesson) mListLessons.get(i)).getLabel(), imageView1);
            count = i + 1;
            if (theme.equalsIgnoreCase("night")) {
                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector_night));
            }else{
                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));
            }
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(MainActivity.this, "Выбран " + mainText.getText() + " урок", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
//                    intent.putExtra("jsonArray", mJsonUtils.readFile());
//                    intent.putExtra("name_lesson", mainText.getText().toString());
//
//                    startActivity(intent);

                    Intent intent = new Intent(MainActivity.this, ActivityNavigation.class);
                    intent.putExtra("jsonArray", mJsonUtils.readFile());
                    intent.putExtra("name_lesson", mainText.getText().toString());
                    intent.putExtra("theme", theme);

                    startActivity(intent);

                }
            });
            view1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Delete Lesson?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(MainActivity.this, "Delete " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
                            mJsonUtils.deleteLesson(mainText.getText().toString());

                            updateContent();

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

            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            animation.setStartOffset(10 + counterAnimation * 50);
            mMainLayout.addView(view1);
            view1.startAnimation(animation);
            counterAnimation++;
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select the action");
        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "Delete") {
        }
        return true;
    }

    private void identification() {
        mMainActivityLogic = new MainActivityLogic();
        mMainLayout = (LinearLayout) findViewById(R.id.layoutMainActivity);
        mLessonChooseLayout = (LinearLayout) findViewById(R.id.lesson_chose_layout);
        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mRelativeLayout_level_0 = (RelativeLayout) findViewById(R.id.relative_layout_level_0);
        mScrollView_level_1 = (ScrollView) findViewById(R.id.scroll_view_level_1);
        mListLessons = new ArrayList();
        mJsonUtils = new JsonUtils(this, mListLessons);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuilderLesson.class);
                startActivityForResult(intent, 2);
            }
        });

        mTextViewFavorite = (TextView) findViewById(R.id.favorite_button);
        mTextViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Favorite", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day:
                theme = "day";
                mJsonUtils.saveSettings("day");
                setSettingsToThisActivity();
                updateColorContent();
                return true;

            case R.id.night:
                theme = "night";
                mJsonUtils.saveSettings("night");
                setSettingsToThisActivity();
                updateColorContent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateColorContent(){
        mMainLayout.removeAllViews();
        addElementsToLayouts();
    }

    private void updateContent() {
        mMainLayout.removeAllViews();
        mJsonUtils.readJsonStringToArrayList(mJsonUtils.readFile());
        addElementsToLayouts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            updateContent();
//            MainActivity.this.recreate();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContent();
        setSettingsToThisActivity();
    }
}
