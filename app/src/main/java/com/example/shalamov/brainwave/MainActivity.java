package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.jsonUtils.JsonUtilsOld;
import com.example.shalamov.brainwave.utils.FilesUtils;
import com.example.shalamov.brainwave.utils.ImageUtils;
import com.example.shalamov.brainwave.utils.JsonUtils;
import com.example.shalamov.brainwave.utils.Lesson;
import com.example.shalamov.brainwave.utils.LessonsUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mMainLayout, mLessonBuilderLayout, mLessonChooseLayout;
    private RelativeLayout mRelativeLayout_level_0;
    private ScrollView mScrollView_level_1;
    private int count;
    private JsonUtilsOld mJsonUtilsOld;
    private Animation animation;
    private FloatingActionButton mActionButton;
    private MainActivityLogic mMainActivityLogic;
    ActionBar ab;
    private String theme = "day";


    private LinearLayout mTextViewFavorite;

    private FilesUtils filesUtils;
    private JsonUtils jsonUtils;
    private String textFromFile; // текст файла
    private ArrayList arrayListLessons;
    private LessonsUtils lessonsUtils;
    private ImageUtils imageUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(true);
        ab.setSubtitle("Lessons");

        initializing();
//        setSettingsToThisActivity();
        addElementsToLayouts();

    }



//    private void setSettingsToThisActivity() {
//        theme = mJsonUtilsOld.readSettings();
//        if (theme == "no settings") {
//            theme = "day";
//        }
//
//
//        if (theme.equalsIgnoreCase("night")) {
//
//
//            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColorGrey800));
//
////            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColorNight500));
//            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorGrey800)));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(getResources().getColor(R.color.myColorGrey900));
//            }
//        } else {
//            mRelativeLayout_level_0.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.myColor3));
//            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
//        }
//
//    }

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
        for (int i = arrayListLessons.size() - 1; i >= 0; i--) {


            final int numberOfElement = i;
            View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
            final TextView mainText = (TextView) view1.findViewById(R.id.main_text);
            final TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
            final LinearLayout  mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
            final Lesson lesson = (Lesson) arrayListLessons.get(i);
            mainText.setText(lesson.getName());
            mTextDescription.setText(mMainActivityLogic.getNumberOfSentences(lesson.getText()));
            ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);
            Global.getImageUtils().updateLabel(((Lesson) arrayListLessons.get(i)).getLabel(), imageView1);
            count = i + 1;
            if (theme.equalsIgnoreCase("night")) {
                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector_night));
            }else{
                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));
            }
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ActivityNavigation.class);
                    intent.putExtra("number", Integer.toString(numberOfElement));

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
                            Global.getLessonsUtils().deleteLesson(numberOfElement);
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
//            animation.setStartOffset(10 + counterAnimation * 50);
            mMainLayout.addView(view1);
            view1.startAnimation(animation);
//            counterAnimation++;

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

    private void initializing() {
        // поиск элементов во вьюхе
        mMainActivityLogic = new MainActivityLogic();
        mMainLayout = (LinearLayout) findViewById(R.id.layoutMainActivity);
        mLessonChooseLayout = (LinearLayout) findViewById(R.id.lesson_chose_layout);
        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mRelativeLayout_level_0 = (RelativeLayout) findViewById(R.id.relative_layout_level_0);
        mScrollView_level_1 = (ScrollView) findViewById(R.id.scroll_view_level_1);

        //1 создаем инструмент работы с файлами - все они в Utils pack/
        // проверяем существует ли файл уроков в нужной директории, если нет, создаем его.
        //2 читаем файл уроков в директории
        //3 создаем инструмент работы с Json, который сразу просит строку и создает объекты lesson
        //4 инициализируем глобальные переменные
        filesUtils = new FilesUtils();
        filesUtils.checkFile();
        arrayListLessons = new ArrayList();
        textFromFile = filesUtils.readFile();
        jsonUtils = new JsonUtils(textFromFile);

        lessonsUtils = new LessonsUtils();
        imageUtils = new ImageUtils();
        Global.setFilesUtils(filesUtils);
        Global.setJsonUtils(jsonUtils);
        Global.setLessonsList(arrayListLessons);
        Global.setLessonsUtils(lessonsUtils);
        Global.setImageUtils(imageUtils);
        jsonUtils.readJsonToLesson();

//        mJsonUtilsOld = new JsonUtilsOld(this, mListLessons);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuilderLesson.class);
                startActivityForResult(intent, 2);
            }
        });

        mTextViewFavorite = (LinearLayout) findViewById(R.id.favorite_button);
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
                mJsonUtilsOld.saveSettings("day");
//                setSettingsToThisActivity();
                updateColorContent();
                return true;

            case R.id.night:
                theme = "night";
                mJsonUtilsOld.saveSettings("night");
//                setSettingsToThisActivity();
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
//        setSettingsToThisActivity();
    }
}
