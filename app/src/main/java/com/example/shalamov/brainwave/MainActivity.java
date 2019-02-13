package com.example.shalamov.brainwave;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.FilesUtils;
import com.example.shalamov.brainwave.utils.ImageUtils;
import com.example.shalamov.brainwave.utils.JsonUtils;
import com.example.shalamov.brainwave.utils.LessonModel;
import com.example.shalamov.brainwave.utils.LessonsUtils;
import com.example.shalamov.brainwave.utils.WordModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private LinearLayout mMainLayout;
    private RelativeLayout relativeLayout;
    private Animation animation;
    private FloatingActionButton mActionButton;
    private FloatingActionButton mActionButtonImport;
    private Button mBtnTexts, mBtnWords, mBtnOthers;
    private int count;
    private ActionBar ab;
    private String theme = "day";


    // указатель, какой урок нужно удалить использую в контекстменю
    int positionForDelete;
    //указатель режима
    //1 - тексты 2 - слова 3 - другое
    private boolean contentWasReadedFromFileForMode2 = false;

    //кнопки на верхней панели
//    private LinearLayout mAll;
//    private LinearLayout mImportant;
//    private LinearLayout mCurrent;
//    private LinearLayout mTemp;
//    private LinearLayout mLessonBtn;
//    private LinearLayout mJwBtn;
//    private LinearLayout mSolutionsSBBtn;
//    private LinearLayout mSolutionsWBBtn;
//    private LinearLayout mBbcBtn;
//    private LinearLayout mHeadPhone;
    // filter - фильтр для уроков
    // 0 - все уроки
    // 1 - важные уроки
    // 2 - текущие
    // 3 - временные
    private String filter = "all";

    private FilesUtils filesUtils;
    private JsonUtils jsonUtils;
    private ArrayList arrayListLessons, arrayListLessonsForMode2;
    private LessonsUtils lessonsUtils;
    private ImageUtils imageUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ab = getSupportActionBar();
        ab.setSubtitle("Уроки с текстом");


        initializing();

        //загрузка списка!
        downloadContent();

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_activity_main);



//        TransitionDrawable trans = (TransitionDrawable) relativeLayout.getBackground();
//        trans.startTransition(4000);


    }


    private void addElementsToLayouts(String filter) {

        for (int i = arrayListLessons.size() - 1; i >= 0; i--) {
            LessonModel lesson = (LessonModel) arrayListLessons.get(i);
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
                Global.getImageUtils().updateLabel(((LessonModel) arrayListLessons.get(i)).getLabel(), imageView1);
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
                        builder.setTitle("Delete LessonModel?");
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
            LessonModel lesson = (LessonModel) arrayListLessons.get(i);
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
                Global.getImageUtils().updateLabel(((LessonModel) arrayListLessons.get(i)).getLabel(), imageView1);
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
                        builder.setTitle("Delete LessonModel?");
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


    private void initializing() {
        // поиск элементов во вьюхе
        mMainLayout = (LinearLayout) findViewById(R.id.layoutMainActivity);


        mActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mActionButtonImport = (FloatingActionButton) findViewById(R.id.floatingActionButtonImport);
        mActionButtonImport.setVisibility(View.INVISIBLE);

        mBtnTexts = findViewById(R.id.btn_texts);
        mBtnWords = findViewById(R.id.btn_words);
        mBtnOthers = findViewById(R.id.btn_others);

        //1 создаем инструмент работы с файлами - все они в Utils pack/
        // проверяем существует ли файл уроков в нужной директории, если нет, создаем его.
        //2 читаем файл уроков в директории
        //3 создаем инструмент работы с Json, который сразу просит строку и создает объекты lesson
        //4 инициализируем глобальные переменные
        filesUtils = new FilesUtils();
        filesUtils.setContext(MainActivity.this);
//        filesUtils.checkFile();
        arrayListLessons = new ArrayList();
        arrayListLessonsForMode2 = new ArrayList();
        Global.setLessonsList(arrayListLessons);
        Global.setLessonsListForMode2(arrayListLessonsForMode2);
        String textFromFile = filesUtils.readFile();

//        Log.d(TAG, "textFromFile = " + textFromFile);
        jsonUtils = new JsonUtils();

        lessonsUtils = new LessonsUtils();
        imageUtils = new ImageUtils();
        Global.setFilesUtils(filesUtils);
        Global.setJsonUtils(jsonUtils);

        Global.setLessonsUtils(lessonsUtils);
        Global.setImageUtils(imageUtils);
        jsonUtils.readJsonToLessonMode1(textFromFile);

        QuizLogic mQuizLogic = new QuizLogic();
        Global.setmQuizLogic(mQuizLogic);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Global.mode) {
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, BuilderLesson.class);
                        startActivityForResult(intent1, 2);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, BuilderWordsLesson.class);
                        startActivityForResult(intent2, 2);
                        break;
                    case 3:
                        break;
                }

            }
        });

        mActionButtonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Global.mode) {
                    case 1:
                        break;
                    case 2:
                        String newLesson = Global.getFilesUtils().importFile();
                        if(newLesson.length() == 0) {
                            Toast.makeText(MainActivity.this, "Файл не найден!", Toast.LENGTH_SHORT).show();
                        }else {
                            Global.getJsonUtils().importMode2(newLesson);
                            Toast.makeText(MainActivity.this, "Урок добавлен!", Toast.LENGTH_SHORT).show();

                            updateContent();
                            mActionButtonImport.setVisibility(View.INVISIBLE);
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            mActionButtonImport.startAnimation(anim);

                        }
                        break;
                    case 3:
                        break;
                }

            }
        });

        mBtnTexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.mode = 1;
                setColorsForMode();
                showTextsMode();

            }
        });

        mBtnWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.mode = 2;
                setColorsForMode();
                showWordsMode();
            }
        });

        mBtnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.mode = 3;
                setColorsForMode();
                showOtherMode();
            }
        });
//        mAll = (LinearLayout) findViewById(R.id.all_button);
//        mImportant = (LinearLayout) findViewById(R.id.important_button);
//        mCurrent = (LinearLayout) findViewById(R.id.current_button);
//        mTemp = (LinearLayout) findViewById(R.id.temp_button);
//
//        mLessonBtn = (LinearLayout) findViewById(R.id.lesson_button);
//        mJwBtn = (LinearLayout) findViewById(R.id.jw_button);
//        mSolutionsSBBtn = (LinearLayout) findViewById(R.id.solutions_sb_button);
//        mSolutionsWBBtn = (LinearLayout) findViewById(R.id.solutions_wb_button);
//        mBbcBtn = (LinearLayout) findViewById(R.id.bbc_button);
//        mHeadPhone = (LinearLayout) findViewById(R.id.headphone_button);
//
//
//
//
//        mAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "all";
//                mMainLayout.removeAllViews();
//                addElementsToLayouts();
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mAll.startAnimation(animation);
//            }
//        });
//
//        mImportant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "important";
//                mMainLayout.removeAllViews();
//                addElementsToLayouts("important");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mImportant.startAnimation(animation);
//            }
//        });
//        mCurrent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "current";
//                mMainLayout.removeAllViews();
//                addElementsToLayouts("current");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mCurrent.startAnimation(animation);
//            }
//        });
//        mTemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "temp";
//                mMainLayout.removeAllViews();
//                addElementsToLayouts("temp");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mTemp.startAnimation(animation);
//            }
//        });
//
//        mLessonBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_0";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_0");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mLessonBtn.startAnimation(animation);
//
//            }
//        });
//
//        mJwBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_18";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_18");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mJwBtn.startAnimation(animation);
//
//            }
//        });
//
//        mSolutionsSBBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_21";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_21");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mSolutionsSBBtn.startAnimation(animation);
//
//            }
//        });
//
//        mSolutionsWBBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_22";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_22");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mSolutionsWBBtn.startAnimation(animation);
//
//            }
//        });
//
//        mBbcBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_23";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_23");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mBbcBtn.startAnimation(animation);
//
//            }
//        });
//
//        mHeadPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter = "label_27";
//                mMainLayout.removeAllViews();
//                addElementsToLayoutsWithLabelFilter("label_27");
//
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
//                mHeadPhone.startAnimation(animation);
//
//            }
//        });
    }

    private void downloadContent() {
        View view1 = getLayoutInflater().inflate(R.layout.download_layout, null);
        mMainLayout.addView(view1);
        new MyAsynkTaskDownloadData().execute();
    }

    private void showTextsMode() {
        ab.setSubtitle("Уроки с текстом");
        mMainLayout.removeAllViews();
        downloadContent();
    }


    private void showWordsMode() {
        ab.setSubtitle("В уроках только слова");
        mMainLayout.removeAllViews();
        downloadContent();
    }

    private void showOtherMode() {
        ab.setSubtitle("Другое");
        mMainLayout.removeAllViews();
        downloadContent();
    }


    private void setColorsForMode() {

        switch (Global.mode) {
            case 1:
                ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                mBtnTexts.setBackgroundColor(getResources().getColor(R.color.myColorIndigo400));
                mBtnTexts.setTextColor(getResources().getColor(R.color.myColorWhite));


                mBtnWords.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));
                mBtnWords.setTextColor(getResources().getColor(R.color.myColorGrey600));

                mBtnOthers.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));
                mBtnOthers.setTextColor(getResources().getColor(R.color.myColorGrey600));

                mActionButtonImport.setVisibility(View.INVISIBLE);
                mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                break;
            case 2:
                ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorPrimaryMode2)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.myColorPrimaryDarkMode2));
                }

                mBtnWords.setBackgroundColor(getResources().getColor(R.color.myColorPrimaryLightMode2));
                mBtnWords.setTextColor(getResources().getColor(R.color.myColorWhite));


                mBtnTexts.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));
                mBtnTexts.setTextColor(getResources().getColor(R.color.myColorGrey600));

                mBtnOthers.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));
                mBtnOthers.setTextColor(getResources().getColor(R.color.myColorGrey600));

                mActionButtonImport.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                mActionButtonImport.startAnimation(anim);
                mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.myColorPrimaryMode2)));
                mActionButtonImport.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.myColorPrimaryLightMode2)));


                break;
            case 3:
                ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorPrimaryMode3)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.myColorPrimaryDarkMode3));
                }

                mBtnOthers.setBackgroundColor(getResources().getColor(R.color.myColorPrimaryLightMode3));
                mBtnOthers.setTextColor(getResources().getColor(R.color.myColorWhite));


                mBtnTexts.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));
                mBtnTexts.setTextColor(getResources().getColor(R.color.myColorGrey600));

                mBtnWords.setBackgroundColor(getResources().getColor(R.color.myColorGrey100));

                mBtnWords.setTextColor(getResources().getColor(R.color.myColorGrey600));
                mActionButtonImport.setVisibility(View.INVISIBLE);
                mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.myColorPrimaryMode3)));

                break;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateContent() {
        mMainLayout.removeAllViews();
        downloadContent();
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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Выбери действие");
        menu.add(Menu.NONE, 101, Menu.NONE, "Удалить");
        menu.add(Menu.NONE, 102, Menu.NONE, "Экспорт в файл");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case 101:
                Global.getLessonsUtils().deleteLesson(positionForDelete);
                updateContent();
                break;
            case 102:
                Global.getJsonUtils().exportToFile(positionForDelete);
                Toast.makeText(this, "Файл сохранен!", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    private class MyAsynkTaskDownloadData extends AsyncTask<String, Integer, String> {

        private ArrayList<View> viewsArray;

        private void createViewsForMode1() {

            for (int i = arrayListLessons.size() - 1; i >= 0; i--) {
                LessonModel lesson = (LessonModel) arrayListLessons.get(i);

                final int numberOfElement = i;
                final View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
                TextView mainText = (TextView) view1.findViewById(R.id.main_text);
                TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
                LinearLayout mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
                mainText.setText(lesson.getName());
                String text = Integer.toString(lesson.getArrayListText().size());
                mTextDescription.setText(text);
                ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);

                Global.getImageUtils().updateLabel(lesson.getLabel(), imageView1);
                count = i + 1;

                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));

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
                        builder.setTitle("Delete LessonModel?");
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
//            counterAnimation++;

//                viewForPush = view1;

                viewsArray.add(view1);
            }

        }

        private void createViewsForMode2() {

            for (int i = arrayListLessonsForMode2.size() - 1; i >= 0; i--) {
                WordModel lesson = (WordModel) arrayListLessonsForMode2.get(i);

                final int numberOfElement = i;
                final View view1 = getLayoutInflater().inflate(R.layout.lessons_label, null);
                TextView mainText = (TextView) view1.findViewById(R.id.main_text);
                TextView mTextDescription = (TextView) view1.findViewById(R.id.text_description);
                LinearLayout mLinear = (LinearLayout) view1.findViewById(R.id.layout_for_lesson_label);
                mainText.setText(lesson.getName());
                String text = Integer.toString(lesson.getArrayListWords().size());
                mTextDescription.setText(text);
                ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageView);

                Global.getImageUtils().updateLabel(lesson.getLabel(), imageView1);
                count = i + 1;

                mLinear.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.press_selector));

                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (Global.mode) {

                            case 1:
                                Intent intent = new Intent(MainActivity.this, ActivityBeforeTraining.class);
                                intent.putExtra("lessonNumber", Integer.toString(numberOfElement));
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, ActivityBeforeTrainingMode2.class);
                                intent2.putExtra("lessonNumber", Integer.toString(numberOfElement));
                                startActivity(intent2);
                                break;
                        }

                    }
                });

                view1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        positionForDelete = numberOfElement;
                        registerForContextMenu(view1);
                        openContextMenu(view1);

//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setCancelable(false);
//                        builder.setTitle("Delete Lesson?");
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                            Toast.makeText(MainActivity.this, "Delete " + mainText.getText().toString(), Toast.LENGTH_SHORT).show();
//                                Global.getLessonsUtils().deleteLesson(numberOfElement);
//                                updateContent();
//
//                            }
//
//                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                        builder.create().show();


                        return true;
                    }
                });


                viewsArray.add(view1);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            viewsArray = new ArrayList<>();

            switch (Global.mode) {
                case 1:
                    createViewsForMode1();
                    break;
                case 2:
                    if (!contentWasReadedFromFileForMode2) {

                        String text = filesUtils.readFileForMode2();
                        contentWasReadedFromFileForMode2 = true;
                        if (text.length() != 0) {
                            jsonUtils.readJsonToLessonMode2(text);
                            createViewsForMode2();
                        }

                    } else {
                        createViewsForMode2();
                    }
                    break;
                case 3:
                    break;

            }

            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mMainLayout.removeAllViews();
            if (viewsArray.size() != 0) {
                for (int i = 0; i < viewsArray.size(); i++) {
                    mMainLayout.addView(viewsArray.get(i));
                }
            } else {
                Toast.makeText(MainActivity.this, "Список пуст!", Toast.LENGTH_SHORT).show();
            }

            // Do things like update the progress bar
        }


    }
}
