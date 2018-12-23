package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    ActionBar ab;
    private String theme = "day";

//кнопки на верхней панели
    private LinearLayout mAll;
    private LinearLayout mImportant;
    private LinearLayout mCurrent;
    private LinearLayout mTemp;
    private LinearLayout mLessonBtn;
    private LinearLayout mJwBtn;
    private LinearLayout mSolutionsSBBtn;
    private LinearLayout mSolutionsWBBtn;
    private LinearLayout mBbcBtn;
    private LinearLayout mHeadPhone;
    // filter - фильтр для уроков
    // 0 - все уроки
    // 1 - важные уроки
    // 2 - текущие
    // 3 - временные
    private String filter = "all";

    private FilesUtils filesUtils;
    private JsonUtils jsonUtils;
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

        Log.d("brain", "Main Activity onCreate ");
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

        Log.d("brain", "addElementsToLayouts() " + arrayListLessons.size());

        for (int i = arrayListLessons.size() - 1; i >= 0; i--) {
            Lesson lesson = (Lesson) arrayListLessons.get(i);


            final int numberOfElement = i;
            View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
            TextView mainText = (TextView) view1.findViewById(R.id.main_text);
            TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
            LinearLayout  mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
            mainText.setText(lesson.getName());
            mTextDescription.setText("?");
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
                    Intent intent = new Intent(MainActivity.this, ActivityBeforeTraining.class);
                    intent.putExtra("lessonNumber", Integer.toString(numberOfElement));

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

//            animation.setStartOffset(10 + counterAnimation * 50);
            mMainLayout.addView(view1);
//            counterAnimation++;
        }

    }

    private void addElementsToLayouts(String filter) {

        for (int i = arrayListLessons.size() - 1; i >= 0; i--) {
            Lesson lesson = (Lesson) arrayListLessons.get(i);
            String filterFromLesson = lesson.getDescription1();

            if (filter.equalsIgnoreCase(filterFromLesson)) {

                final int numberOfElement = i;
                View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
                TextView mainText = (TextView) view1.findViewById(R.id.main_text);
                TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
                LinearLayout mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
                mainText.setText(lesson.getName());
                mTextDescription.setText("?");
                ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);
                Global.getImageUtils().updateLabel(((Lesson) arrayListLessons.get(i)).getLabel(), imageView1);
                count = i + 1;
                if (theme.equalsIgnoreCase("night")) {
                    mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector_night));
                } else {
                    mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));
                }
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ActivityBeforeTraining.class);
                        intent.putExtra("lessonNumber", Integer.toString(numberOfElement));

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

    }

    private void addElementsToLayoutsWithLabelFilter(String filter) {

        for (int i = arrayListLessons.size() - 1; i >= 0; i--) {
            Lesson lesson = (Lesson) arrayListLessons.get(i);
            String filterFromLesson = lesson.getLabel();

            if (filter.equalsIgnoreCase(filterFromLesson)) {

                final int numberOfElement = i;
                View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
                TextView mainText = (TextView) view1.findViewById(R.id.main_text);
                TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
                LinearLayout mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
                mainText.setText(lesson.getName());
                mTextDescription.setText("?");
                ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);
                Global.getImageUtils().updateLabel(((Lesson) arrayListLessons.get(i)).getLabel(), imageView1);
                count = i + 1;
                if (theme.equalsIgnoreCase("night")) {
                    mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector_night));
                } else {
                    mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));
                }
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ActivityBeforeTraining.class);
                        intent.putExtra("lessonNumber", Integer.toString(numberOfElement));

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
        Global.setLessonsList(arrayListLessons);
        String textFromFile = filesUtils.readFile();

        Log.d("brain", "MainActivity-initializing: textFromFile:" + textFromFile);
        jsonUtils = new JsonUtils(textFromFile);

        lessonsUtils = new LessonsUtils();
        imageUtils = new ImageUtils();
        Global.setFilesUtils(filesUtils);
        Global.setJsonUtils(jsonUtils);

        Global.setLessonsUtils(lessonsUtils);
        Global.setImageUtils(imageUtils);
        jsonUtils.readJsonToLesson();

        QuizLogic mQuizLogic = new QuizLogic();
        Global.setmQuizLogic(mQuizLogic);

//        mJsonUtilsOld = new JsonUtilsOld(this, mListLessons);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuilderLesson.class);
                startActivityForResult(intent, 2);
            }
        });

        mAll = (LinearLayout) findViewById(R.id.all_button);
        mImportant = (LinearLayout) findViewById(R.id.important_button);
        mCurrent = (LinearLayout) findViewById(R.id.current_button);
        mTemp = (LinearLayout) findViewById(R.id.temp_button);

        mLessonBtn = (LinearLayout) findViewById(R.id.lesson_button);
        mJwBtn = (LinearLayout) findViewById(R.id.jw_button);
        mSolutionsSBBtn = (LinearLayout) findViewById(R.id.solutions_sb_button);
        mSolutionsWBBtn = (LinearLayout) findViewById(R.id.solutions_wb_button);
        mBbcBtn = (LinearLayout) findViewById(R.id.bbc_button);
        mHeadPhone = (LinearLayout) findViewById(R.id.headphone_button);




        mAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "all";
                mMainLayout.removeAllViews();
                addElementsToLayouts();

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mAll.startAnimation(animation);
            }
        });

        mImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "important";
                mMainLayout.removeAllViews();
                addElementsToLayouts("important");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mImportant.startAnimation(animation);
            }
        });
        mCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "current";
                mMainLayout.removeAllViews();
                addElementsToLayouts("current");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mCurrent.startAnimation(animation);
            }
        });
        mTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "temp";
                mMainLayout.removeAllViews();
                addElementsToLayouts("temp");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mTemp.startAnimation(animation);
            }
        });

        mLessonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_0";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_0");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mLessonBtn.startAnimation(animation);

            }
        });

        mJwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_18";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_18");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mJwBtn.startAnimation(animation);

            }
        });

        mSolutionsSBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_21";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_21");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mSolutionsSBBtn.startAnimation(animation);

            }
        });

        mSolutionsWBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_22";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_22");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mSolutionsWBBtn.startAnimation(animation);

            }
        });

        mBbcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_23";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_23");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBbcBtn.startAnimation(animation);

            }
        });

        mHeadPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "label_27";
                mMainLayout.removeAllViews();
                addElementsToLayoutsWithLabelFilter("label_27");

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mHeadPhone.startAnimation(animation);

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

//        setSettingsToThisActivity();
    }
}
