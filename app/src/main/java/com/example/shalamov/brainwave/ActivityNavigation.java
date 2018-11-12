package com.example.shalamov.brainwave;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.jsonUtils.JsonUtils;
import com.example.shalamov.brainwave.jsonUtils.Lesson;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityNavigation  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Quiz Это базовый Layout для добавления контента
    View mLayoutLearnText, mLayoutBulder, layoutNote, mLayoutMainTraining2;
    LinearLayout layoutQuizForAddContent, mNotePadButton, mTreaningButton, mSettingsButton, mEditorButton, mNavigationLayout;
    LinearLayout layoutNotepadForAddContent;
    TextView textNoteForSentence, mNumberSentence, mTextViewForConcat;
    View layoutForSentence;
    RelativeLayout layoutForClick;
    JsonUtils mJsonUtils;
    String jsonArrayText, nameLesson, labelLesson;
    QuizLogic mQuizLogic;
    ActionBar ab;
    String labelForLesson;
    ImageView mLabelLesson;
    EditText nameLessonChange;
    EditText mainTextLessonChange;
    String allTextForLesson;
    String oldNameLesson;
    ImageView mNotepadImage, mTrainingImage, mSettingsImage, mEditorImage;
    ScrollView mScroll;
    View focused;
    int updateTextMarkColor = 500;


    //global for all layouts
    boolean notePadShow;
    boolean previousLayoutIsTraining;
    String[] history = {"null", "null"};
    TextToSpeech t1;
    boolean dayTheme; // дневная тема если true, режим день
    String theme;


    //saving position for scroll
    int scrollYPosition = 0;

    // settings layout variables

    View layoutSettings;
    int settingsSizeText;
    boolean boolBoltText;
    boolean boolItalicText;
    TextView mTextExample;
    Switch mSwitchBolt;
    Switch mSwitchDayNight;
    SeekBar mSeekBar;
    Button mButtonDefaultValues;


    //Values for training layout
    TextView textFieldForLearning;
    int currentSentenceIndex = 0;

    //animation work

    View mLayoutAnimation;
    int animDirection = 0;
    AnimLogic mAnimLogic;

    //Voice
    boolean voice = false;
    Switch mVoice;

    //training2
    LinearLayout mLayoutPiecesLine1, mLayoutPiecesLine2, mLayoutPiecesLine3, mLayoutPiecesLine4, mLayoutPiecesLine5, mLayoutPiecesLine6, mLayoutPiecesLine7, mLayoutPiecesLine8, mLayoutPiecesLine9, mLayoutPiecesLine10, mLayoutPiecesLine11, mLayoutPiecesLine12;
    TextView mTextQuestionLabel;
    ImageView mBtnEye, mBtnDialogue, mBtnBackspace;
    LogicTraining2 mLogicTraining2;

    int currentLine;
    ArrayList<TextView> mArrayListTextInButtonsTraining2;
    ArrayList<View> mArrayListButtonsTraining2;
    ArrayList<Integer> mArrayStateOfButtonsTraining2;
    //    ArrayList<Integer> clickStateInTraining2;
    int countNumberOfWords;

    int rotation;
    int sizeScreen;

    AudioManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //настройки окна
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        jsonArrayText = getIntent().getExtras().getString("jsonArray");
        nameLesson = getIntent().getExtras().getString("name_lesson");



        oldNameLesson = nameLesson;
        mJsonUtils = new JsonUtils(this);
        createQuiz();

        ab.setTitle("Quiz");
        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
        initializeBasicLayout();
        getSettingsForLesson();


        showNotePad();
//        showTraining2();
//        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        am.registerMediaButtonEventReceiver(am);

        RemoteControlReceiver receiver = new RemoteControlReceiver();
        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        manager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));

        Global.sNavigation = ActivityNavigation.this;

    }


    private void getSettingsForLesson() {

        theme = mJsonUtils.readSettings();

        //глобальные настройки
        if (theme.equalsIgnoreCase("night")) {
            dayTheme = false;
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorGrey800)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.myColorGrey900));
            }
        } else {
            dayTheme = true;
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }


        //size text in training
        settingsSizeText = 25;
        boolBoltText = true;
        boolItalicText = false;

        //settings for quizLogic
        currentSentenceIndex = 0;
        mQuizLogic.setCurrSentenceNull();
        mQuizLogic.setCurrWordNull();
        mQuizLogic.createArrayCurrentSentence(0);

        //settings train2
        mArrayListTextInButtonsTraining2 = new ArrayList<>();
        mArrayListButtonsTraining2 = new ArrayList<>();
        mArrayStateOfButtonsTraining2 = new ArrayList<>();

    }

    private void initializeBasicLayout() {

//        mNavigationLayout = (LinearLayout) findViewById(R.id.navigation);

//        mNotepadImage = (ImageView) findViewById(R.id.notepad_image_button);
//        mTrainingImage = (ImageView) findViewById(R.id.training_image_button);
//        mSettingsImage = (ImageView) findViewById(R.id.settings_image_button);
//        mEditorImage = (ImageView) findViewById(R.id.editor_image_button);

//        mNotepadText = (TextView) findViewById(R.id.notepad_text);
//        mTrainingText = (TextView) findViewById(R.id.training_text);
//        mSettingsText = (TextView) findViewById(R.id.settings_text);
//        mEditorText = (TextView) findViewById(R.id.editor_text);


        layoutQuizForAddContent = (LinearLayout) findViewById(R.id.layout_for_add_content);
//        mNotePadButton = (LinearLayout) findViewById(R.id.notepad_button);
//        mTreaningButton = (LinearLayout) findViewById(R.id.training_button);
//        mSettingsButton = (LinearLayout) findViewById(R.id.settings_button);
//        mEditorButton = (LinearLayout) findViewById(R.id.editor_button);

//
//        mNotePadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showNotePad();
//
//            }
//        });
//
//        mTreaningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showQuiz();
//
//            }
//        });
//
//        mSettingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showSettings();
//
//            }
//        });
//
//        mEditorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showEditor();
//            }
//        });

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

    }

    private void saveStateNotePadLayout() {
        if (mScroll != null) {
            scrollYPosition = mScroll.getScrollY();

        }
    }

    private void showAnimation() {
        mLayoutAnimation = getLayoutInflater().inflate(R.layout.layout_animation_quiz, null);
        RelativeLayout relativeLayout = (RelativeLayout) mLayoutAnimation.findViewById(R.id.basic_layout);
        final ImageView image = (ImageView) mLayoutAnimation.findViewById(R.id.imageView2);

        for (int i = 0; i < mAnimLogic.getArraySize(); i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params.setMarginStart(i * 10);

            params.setMargins(i * 10, i * 10, i * 10, i * 10);

            final Button button = new Button(this);
            button.setText(mAnimLogic.getTextUsingIndex(i));
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(button, "translationX", 200f);
                    animation.setDuration(2000);
                    animation.start();
                }
            });

            relativeLayout.addView(button);

        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (animDirection == 0) {
                    animDirection = 1;
                    ObjectAnimator animation1 = ObjectAnimator.ofFloat(image, "translationX", 300f);
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(image, "translationY", 300f);
                    animation1.setDuration(2000);
                    animation2.setDuration(2000);
                    animation1.start();
                    animation2.start();
                } else {
                    animDirection = 0;
                    ObjectAnimator animation = ObjectAnimator.ofFloat(image, "translationX", 0);
                    animation.setDuration(2000);
                    animation.start();
                }
            }
        });


        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutAnimation);

    }


    private void showSettings() {
        writeHistory("Settings");
        notePadShow = false;

        saveStateNotePadLayout();
        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can create your own style");

        layoutQuizForAddContent.removeAllViews();


        layoutSettings = getLayoutInflater().inflate(R.layout.layout_settings, null);


        mTextExample = (TextView) layoutSettings.findViewById(R.id.text_example);

        mSwitchBolt = (Switch) layoutSettings.findViewById(R.id.bolt_switch);
        mSwitchDayNight = (Switch) layoutSettings.findViewById(R.id.night_switch);
        mSeekBar = (SeekBar) layoutSettings.findViewById(R.id.seek_bar);
        mButtonDefaultValues = (Button) layoutSettings.findViewById(R.id.default_button);
        mVoice = (Switch) layoutSettings.findViewById(R.id.voice);

        mSwitchBolt.setChecked(boolBoltText);
        mSwitchDayNight.setChecked(!dayTheme);
        mVoice.setChecked(voice);
        mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeText);
        mTextExample.setText("Text size: " + settingsSizeText);
        mSeekBar.setProgress(settingsSizeText);

        if (boolBoltText) {
            mTextExample.setTypeface(mTextExample.getTypeface(), Typeface.BOLD);
        } else {
            mTextExample.setTypeface(mTextExample.getTypeface(), Typeface.NORMAL);
        }

        mSwitchBolt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mSwitchBolt.isChecked()) {
                    boolBoltText = true;
                    mTextExample.setTypeface(mTextExample.getTypeface(), Typeface.BOLD);
                } else {
                    boolBoltText = false;
                    mTextExample.setTypeface(mTextExample.getTypeface(), Typeface.NORMAL);
                }
            }
        });

        mVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mVoice.isChecked()) {
                    voice = true;
                } else {
                    voice = false;
                }
            }
        });

        mSwitchDayNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mSwitchDayNight.isChecked()) {
                    dayTheme = false;
                    ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.myColorGrey800)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.myColorGrey900));
                    }

                    mJsonUtils.saveSettings("night");

                } else {
                    dayTheme = true;
                    ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
//                    ab.setBackgroundDrawable(new ColorDrawable(getResources()
//                            .getColor(R.color.myColorNight900)));

                    mJsonUtils.saveSettings("day");

                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                settingsSizeText = i;
                mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeText);
                mTextExample.setText("Text size: " + settingsSizeText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButtonDefaultValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsSizeText = 30;
                boolBoltText = true;
                boolItalicText = false;

                mSwitchBolt.setChecked(boolBoltText);
                mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeText);
                mTextExample.setText("Text size: " + settingsSizeText);
                mSeekBar.setProgress(settingsSizeText);

            }
        });

        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(layoutSettings);

    }


    private void showQuiz() {
        writeHistory("Training");
        notePadShow = false;

        saveStateNotePadLayout();

        mQuizLogic.setCurrSentence(currentSentenceIndex);
        mQuizLogic.createArrayCurrentSentence(currentSentenceIndex);

        ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());


//        mNotepadText.setTextColor(Color.parseColor("#000000"));
//        mTrainingText.setTextColor(Color.parseColor("#FFFFFF"));
//        mSettingsText.setTextColor(Color.parseColor("#000000"));
//        mEditorText.setTextColor(Color.parseColor("#000000"));
//
//        mNotepadImage.setImageResource(R.drawable.ic_notepad);
//        mTrainingImage.setImageResource(R.drawable.ic_japanese_white);
//        mSettingsImage.setImageResource(R.drawable.ic_settings);
//        mEditorImage.setImageResource(R.drawable.ic_edit);

//        if (dayTheme) {
//            mNotePadButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//            mTreaningButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimary));
//            mSettingsButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//            mEditorButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        } else {
//            mNotePadButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNight800));
//            mTreaningButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNight900));
//            mSettingsButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNight800));
//            mEditorButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNight800));
//        }

        mLayoutLearnText = getLayoutInflater().inflate(R.layout.train_layout, null); //тренировочный лайоут с текстовым полем и кнопками внизу
        layoutForClick = (RelativeLayout) mLayoutLearnText.findViewById(R.id.layout_tap_quiz); //первый лейоут где располагается главный текст и по нему отслеживаются нажатия
        LinearLayout mLayoutGoToTraining2 = (LinearLayout) mLayoutLearnText.findViewById(R.id.btn_go_to_train2);
        LinearLayout mLayoutGoToTranslator = (LinearLayout) mLayoutLearnText.findViewById(R.id.btn_go_to_translator);
        LinearLayout mLayoutGoToSettings = (LinearLayout) mLayoutLearnText.findViewById(R.id.btn_go_to_settings);
        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) mLayoutLearnText.findViewById(R.id.navigation_bottom);
        textFieldForLearning = (TextView) mLayoutLearnText.findViewById(R.id.text_field_for_learning);

        if (!dayTheme) {
            textFieldForLearning.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorDark));
            layoutForClick.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey600));
            bottomNavigationItemView.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey800));
        }

        textFieldForLearning.setText(mQuizLogic.allWord());

        //set settings
        textFieldForLearning.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeText);
        if (boolBoltText) {
            textFieldForLearning.setTypeface(textFieldForLearning.getTypeface(), Typeface.BOLD);
        } else {
            textFieldForLearning.setTypeface(textFieldForLearning.getTypeface(), Typeface.NORMAL);
        }

        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.previous:
                        textFieldForLearning.setText(mQuizLogic.previousSentence());
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        if (voice) {
                            t1.speak(mQuizLogic.getSentenceString(currentSentenceIndex), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        break;
                    case R.id.ok:
                        textFieldForLearning.setText(mQuizLogic.setCurrWordNull());
                        break;
                    case R.id.next:
                        textFieldForLearning.setText(mQuizLogic.nextSentence());
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        if (voice) {
                            t1.speak(mQuizLogic.getSentenceString(currentSentenceIndex), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        break;
//                    case R.id.next_plus:
//                        mQuizLogic.nextSentencePlus();
//                        textFieldForLearning.setText(mQuizLogic.nextWord());
//                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
//                        break;
                }
                return true;
            }

        });

        layoutForClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mQuizLogic.nextWord();

                if (text != "") {
                    textFieldForLearning.setText(text);
                }

//                textFieldForLearning.setText(textFieldForLearning.getText().toString() + " Add text ");
            }
        });

        layoutForClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String text = mQuizLogic.allWord();
                textFieldForLearning.setText(text);
                return true;
            }
        });

        mLayoutGoToTraining2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTraining2();
            }
        });

        mLayoutGoToTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String sent = textFieldForLearning.getText().toString();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.google.com/#en/ru/" + sent));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        mLayoutGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettings();
            }
        });


        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutLearnText);

    }

    private void findSettingsForTraining2() {
        Configuration configuration = ActivityNavigation.this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

    }

    private void showTraining2() {


        writeHistory("Training2");
        notePadShow = false;

        mQuizLogic.setCurrSentence(currentSentenceIndex);
        mQuizLogic.createArrayCurrentSentence(currentSentenceIndex);
        ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());

        countNumberOfWords = 0;
        mArrayListTextInButtonsTraining2.clear();
        mArrayListButtonsTraining2.clear();
        mArrayStateOfButtonsTraining2.clear();


        mLayoutMainTraining2 = getLayoutInflater().inflate(R.layout.train_layout1, null);
//        mInfiLayout = getLayoutInflater().inflate(R.layout.element_2, null);
//        mLayoutInfo = (LinearLayout) mInfiLayout.findViewById(R.id.info_layout);
//        mTextInfo = (LinearLayout) mInfiLayout.findViewById(R.id.info_layout);
        BottomNavigationView bottomNavigationItemView1 = (BottomNavigationView) mLayoutMainTraining2.findViewById(R.id.navigation_bottom_1);
        mTextViewForConcat = (TextView) mLayoutMainTraining2.findViewById(R.id.text_for_concat);

        mBtnEye = (ImageView) mLayoutMainTraining2.findViewById(R.id.btn_eye);
        mBtnDialogue = (ImageView) mLayoutMainTraining2.findViewById(R.id.btn_dialogue);
        mBtnBackspace = (ImageView) mLayoutMainTraining2.findViewById(R.id.btn_backspace);
        LinearLayout mShowWord = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.btn_show_next_word);

        mLayoutPiecesLine1 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_1);
        mLayoutPiecesLine2 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_2);
        mLayoutPiecesLine3 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_3);
        mLayoutPiecesLine4 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_4);
        mLayoutPiecesLine5 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_5);
        mLayoutPiecesLine6 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_6);
        mLayoutPiecesLine7 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_7);
        mLayoutPiecesLine8 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_8);
        mLayoutPiecesLine9 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_9);
        mLayoutPiecesLine10 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_10);
        mLayoutPiecesLine11 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_11);
        mLayoutPiecesLine12 = (LinearLayout) mLayoutMainTraining2.findViewById(R.id.layout_for_add_piece_line_12);

        mTextViewForConcat.setText("");


        mLogicTraining2.setParametersLogicTraining2(mQuizLogic.getCurrentSentenceArray(), sizeScreen);


        boolean flag = true;
        while (flag) {
            currentLine++;
            flag = addLineInShowTrainig2();
        }


        bottomNavigationItemView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.previous:
                        mArrayListTextInButtonsTraining2.clear();
                        mArrayListButtonsTraining2.clear();
                        mArrayStateOfButtonsTraining2.clear();

                        countNumberOfWords = 0;
                        mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimaryDark));
                        mTextViewForConcat.setText("");
                        mQuizLogic.previousSentence();
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        mLayoutPiecesLine1.removeAllViews();
                        mLayoutPiecesLine2.removeAllViews();
                        mLayoutPiecesLine3.removeAllViews();
                        mLayoutPiecesLine4.removeAllViews();
                        mLayoutPiecesLine5.removeAllViews();
                        mLayoutPiecesLine6.removeAllViews();
                        mLayoutPiecesLine7.removeAllViews();
                        mLayoutPiecesLine8.removeAllViews();
                        mLayoutPiecesLine9.removeAllViews();
                        mLayoutPiecesLine10.removeAllViews();
                        mLayoutPiecesLine11.removeAllViews();
                        mLayoutPiecesLine12.removeAllViews();

                        mLogicTraining2.setParametersLogicTraining2(mQuizLogic.getCurrentSentenceArray(), sizeScreen);


                        currentLine = 0;
                        boolean flag = true;

                        while (flag) {
                            currentLine++;
                            flag = addLineInShowTrainig2();

                        }
                        break;
                    case R.id.ok:

                        for (int i = 0; i < mArrayListTextInButtonsTraining2.size(); i++) {
                            mArrayListTextInButtonsTraining2.get(i).setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorDark));
                            mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimaryDark));
                            mArrayListButtonsTraining2.get(i).setEnabled(true);
                            mArrayListButtonsTraining2.get(i).setEnabled(true);
                            mArrayStateOfButtonsTraining2.set(i, 0);


                            countNumberOfWords = 0;
                        }

                        mTextViewForConcat.setText("");

                        break;
                    case R.id.next:
                        mArrayListTextInButtonsTraining2.clear();
                        mArrayListButtonsTraining2.clear();
                        mArrayStateOfButtonsTraining2.clear();

                        countNumberOfWords = 0;
                        mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimaryDark));
                        mTextViewForConcat.setText("");
                        mQuizLogic.nextSentence();
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());

                        mLayoutPiecesLine1.removeAllViews();
                        mLayoutPiecesLine2.removeAllViews();
                        mLayoutPiecesLine3.removeAllViews();
                        mLayoutPiecesLine4.removeAllViews();
                        mLayoutPiecesLine5.removeAllViews();
                        mLayoutPiecesLine6.removeAllViews();
                        mLayoutPiecesLine7.removeAllViews();
                        mLayoutPiecesLine8.removeAllViews();
                        mLayoutPiecesLine9.removeAllViews();
                        mLayoutPiecesLine10.removeAllViews();
                        mLayoutPiecesLine11.removeAllViews();
                        mLayoutPiecesLine12.removeAllViews();

                        mLogicTraining2.setParametersLogicTraining2(mQuizLogic.getCurrentSentenceArray(), sizeScreen);


                        currentLine = 0;
                        boolean flag1 = true;

                        while (flag1) {
                            currentLine++;
                            flag1 = addLineInShowTrainig2();

                        }
                        break;
                }
                return true;
            }
        });


        mBtnDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNavigation.this);
                builder.setCancelable(false);
                builder.setTitle("Sentence");
                builder.setIcon(R.drawable.ic_think);
                builder.setMessage(mLogicTraining2.getCorrectSentence());
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });
                builder.create().show();
            }
        });

        mBtnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewForConcat.setText("");

                mTextViewForConcat.setText(mLogicTraining2.getCorrectSentence());

                for (int i = 0; i < mArrayListTextInButtonsTraining2.size(); i++) {
                    mArrayListTextInButtonsTraining2.get(i).setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey400));
                    mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorCorrectTextGreen800));
                    mArrayListButtonsTraining2.get(i).setEnabled(false);
                    countNumberOfWords = 0;
                }

            }
        });

        mBtnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBackspaceTraining2();
            }
        });

        mShowWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayTextCurrent; // массив слов который введен в поле для конкатенации
                int numberWord = 0;       // индекс следующего слова
                String speech = "";         // слово для показа
                String textcurrent;// словa который введен в поле для конкатенации

                textcurrent = mTextViewForConcat.getText().toString();
                if (textcurrent.equalsIgnoreCase(" ")) {
                    textcurrent = "";
                    numberWord = 0;
                } else {
                    arrayTextCurrent = textcurrent.split("[ ]");
                    numberWord = arrayTextCurrent.length;
                }

                String[] arrayTextOriginal = mQuizLogic.getCurrentSentenceArray(); // текущее предложение

                if (arrayTextOriginal.length != numberWord) {
                    speech = arrayTextOriginal[numberWord];
                }

                for (int i = 0; i < mArrayListTextInButtonsTraining2.size(); i++) {

                    if (mArrayListTextInButtonsTraining2.get(i).getText().toString().equalsIgnoreCase(speech) && mArrayStateOfButtonsTraining2.get(i) == 0) {
                        mArrayListTextInButtonsTraining2.get(i).setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNotCorrectTextOrange800));
                        i = mArrayListTextInButtonsTraining2.size();
                    }

                }
            }
        });


        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutMainTraining2);


    }


    private void findDisplayOrientation() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;
        rotation = display.getRotation();
        sizeScreen = w;

    }

    private void clickBackspaceTraining2() {
        if (countNumberOfWords != 0) {
            String newText = "";
            String[] arrayText;
//            if (mTextViewForConcat.getText().toString().length() == 1) {
//                arrayText = new String[1];
//                arrayText[0] = mTextViewForConcat.getText().toString();
//            } else {
//                arrayText = mTextViewForConcat.getText().toString().split("[ ]");
//            }


            arrayText = mTextViewForConcat.getText().toString().split("[ ]");


            //находим последнюю кнопку и делаем ее активной
            //флаг для того чтобы изменить состояние кнопки один раз
            boolean flag = true;
            for (int i = 0; i < mArrayListTextInButtonsTraining2.size(); i++) {
                //Текст в кнопке
                String text;
                //проверка если текст в кнопке _і_ то действительный текст это і
                if (mArrayListTextInButtonsTraining2.get(i).getText().toString().equalsIgnoreCase(" I ")) {
                    text = "I";
                } else {
                    text = mArrayListTextInButtonsTraining2.get(i).getText().toString();
                }
                //
                if (text.equalsIgnoreCase(arrayText[arrayText.length - 1]) && flag) {

                    if (mArrayStateOfButtonsTraining2.get(i) == 1) {
                        mArrayListTextInButtonsTraining2.get(i).setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorDark));
                        mArrayListButtonsTraining2.get(i).setEnabled(true);
                        countNumberOfWords--;
                        mArrayStateOfButtonsTraining2.set(i, 0);
                        flag = false;
                    }
                }
            }

            //удаляем последнее слово в поле для ввода

            if (arrayText.length == 1) {
                mTextViewForConcat.setText("");
            } else {

                arrayText[arrayText.length - 1] = "";
                for (int i = 0; i < arrayText.length - 1; i++) {
                    newText = newText + arrayText[i] + " ";
                }
                mTextViewForConcat.setText("");
                mTextViewForConcat.setText(newText);
            }


        }
    }


    private boolean addLineInShowTrainig2() {

        countNumberOfWords = 0;


        ArrayList<String> line = mLogicTraining2.nextLine();
        if (line.size() != 0) {

            for (int i = 0; i < line.size(); i++) {
                final View mLayout = getLayoutInflater().inflate(R.layout.element_1, null);
                final LinearLayout mLayoutPieceOfSentence = (LinearLayout) mLayout.findViewById(R.id.buttonPieceOfSentence);

                final TextView mTextPieceOfSentence = (TextView) mLayoutPieceOfSentence.findViewById(R.id.text_piece_of_sentence);
                final int indexOfElement;


                mTextPieceOfSentence.setText(line.get(i));


                mArrayListTextInButtonsTraining2.add(mTextPieceOfSentence);
                mArrayListButtonsTraining2.add(mLayoutPieceOfSentence);

                indexOfElement = mArrayStateOfButtonsTraining2.size();
                mArrayStateOfButtonsTraining2.add(0);

                mLayoutPieceOfSentence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        countNumberOfWords++;
                        mTextPieceOfSentence.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey400));

                        if (mTextPieceOfSentence.getText().toString() == " I ") {
                            mTextViewForConcat.setText(mTextViewForConcat.getText().toString() + "I" + " ");
                        } else {
                            mTextViewForConcat.setText(mTextViewForConcat.getText().toString() + mTextPieceOfSentence.getText() + " ");
                        }

                        mLayoutPieceOfSentence.setEnabled(false);

                        if (countNumberOfWords == mLogicTraining2.getNumberOfWordsInSentence()) {
                            countNumberOfWords = 0;
                            if (mLogicTraining2.checkSentence(mTextViewForConcat.getText().toString())) {
                                mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorCorrectTextGreen800));
                            } else {
                                mTextViewForConcat.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorNotCorrectTextOrange800));
                            }

                        }

                        mArrayStateOfButtonsTraining2.set(indexOfElement, 1);


                    }
                });

                if (currentLine == 1) {
                    mLayoutPiecesLine1.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 2) {
                    mLayoutPiecesLine2.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 3) {
                    mLayoutPiecesLine3.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 4) {
                    mLayoutPiecesLine4.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 5) {
                    mLayoutPiecesLine5.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 6) {
                    mLayoutPiecesLine6.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 7) {
                    mLayoutPiecesLine7.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 8) {
                    mLayoutPiecesLine8.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 9) {
                    mLayoutPiecesLine9.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 10) {
                    mLayoutPiecesLine10.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 11) {
                    mLayoutPiecesLine11.addView(mLayoutPieceOfSentence);
                }
                if (currentLine == 12) {
                    mLayoutPiecesLine12.addView(mLayoutPieceOfSentence);
                }
            }

            return true;
        } else {
            currentLine = 0;
            countNumberOfWords = 0;
            return false;
        }
    }

    private void showNotePad() {

        writeHistory("notePad");

        notePadShow = true;


        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can change, add and remove sentences");
//        mNotepadText.setTextColor(Color.parseColor("#FFFFFF"));
//        mTrainingText.setTextColor(Color.parseColor("#000000"));
//        mSettingsText.setTextColor(Color.parseColor("#000000"));
//        mEditorText.setTextColor(Color.parseColor("#000000"));

//        mNotepadImage.setImageResource(R.drawable.ic_notepad_white);
//        mTrainingImage.setImageResource(R.drawable.ic_japanese);
//        mSettingsImage.setImageResource(R.drawable.ic_settings);
//        mEditorImage.setImageResource(R.drawable.ic_edit);
//
//
//        mNotePadButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimary));
//        mTreaningButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        mSettingsButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        mEditorButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));

        layoutNote = getLayoutInflater().inflate(R.layout.note_layout, null);
        mScroll = (ScrollView) layoutNote.findViewById(R.id.scroll);
        mScroll.setFillViewport(true);
        layoutNotepadForAddContent = (LinearLayout) layoutNote.findViewById(R.id.note_layout_for_add);
        mQuizLogic.setCurrSentenceNull();
        for (int i = 0; i < mQuizLogic.getNumberOfSentences(); i++) {
            final int indexSentence = i;
            layoutForSentence = getLayoutInflater().inflate(R.layout.layout_sentence, null);
            LinearLayout layoutRextAndNumber = (LinearLayout) layoutForSentence.findViewById(R.id.layout_sentence_and_number);
            textNoteForSentence = (TextView) layoutForSentence.findViewById(R.id.text_for_sentence);
            mNumberSentence = (TextView) layoutForSentence.findViewById(R.id.number_sentence);
            mNumberSentence.setText("#" + (i + 1));
            textNoteForSentence.setText(mQuizLogic.getCurrentSentenceString());
            layoutNotepadForAddContent.addView(layoutForSentence);

            if (!dayTheme) {
                layoutRextAndNumber.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey600));
                layoutNote.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey800));
            }

            if (updateTextMarkColor == i) {
//                textNoteForSentence.setTextColor((Color.parseColor("#0097a7")));
                textNoteForSentence.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor2));
                textNoteForSentence.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
//                textNoteForSentence.setTextColor((Color.parseColor("#303f9f")));
                if (!dayTheme) {
                    textNoteForSentence.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorDark));
                } else {
                    textNoteForSentence.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimaryDark));
                }
            }


            layoutForSentence.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNavigation.this);
                    builder.setCancelable(false);
                    builder.setTitle("Correcting");
                    final EditText editText = new EditText(ActivityNavigation.this);
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    editText.setText(mQuizLogic.getSentenceString(indexSentence));
                    builder.setView(editText);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            saveStateNotePadLayout();

                            if (editText.getText().length() == 0) {
                                Toast.makeText(ActivityNavigation.this, "Text can`t be empty!", Toast.LENGTH_SHORT).show();
                            } else {
                                focused = layoutForSentence;
                                updateTextMarkColor = indexSentence;

                                mJsonUtils.updateSentence(jsonArrayText, nameLesson, mQuizLogic.changeSentence(editText.getText().toString(), indexSentence));

                                updateContent();

                                ab.setSubtitle("#" + (indexSentence + 1) + " changed!");


                            }
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setNeutralButton("Delete sentence", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityNavigation.this);
                            builder1.setCancelable(false);
                            builder1.setTitle("Delete sentence?");
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveStateNotePadLayout();
                                    updateTextMarkColor = indexSentence;
                                    mJsonUtils.updateSentence(jsonArrayText, nameLesson, mQuizLogic.deleteSentence(indexSentence));
                                    updateContent();

                                }

                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder1.create().show();

                        }
                    });
                    builder.create().show();

                    return true;
                }
            });

            layoutForSentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentSentenceIndex = indexSentence;
                    showQuiz();
                }
            });

        }


        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(layoutNote);


        mScroll.post(new Runnable() {
            @Override
            public void run() {

                mScroll.smoothScrollBy(0, scrollYPosition);
            }
        });


    }

    private void writeHistory(String currentPosition) {
        history[0] = history[1];
        history[1] = currentPosition;
    }

    private void showEditor() {
        writeHistory("Editor");
        notePadShow = false;

        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can change, add and remove sentences");
//        mNotepadText.setTextColor(Color.parseColor("#000000"));
//        mTrainingText.setTextColor(Color.parseColor("#000000"));
//        mSettingsText.setTextColor(Color.parseColor("#000000"));
//        mEditorText.setTextColor(Color.parseColor("#FFFFFF"));
//
//        mNotepadImage.setImageResource(R.drawable.ic_notepad);
//        mTrainingImage.setImageResource(R.drawable.ic_japanese);
//        mSettingsImage.setImageResource(R.drawable.ic_settings);
//        mEditorImage.setImageResource(R.drawable.ic_edit_white);
//
//        mNotePadButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        mTreaningButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        mSettingsButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColor1));
//        mEditorButton.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.colorPrimary));


        mLayoutBulder = getLayoutInflater().inflate(R.layout.builder_layout, null);


        LinearLayout layoutChangeLabel = (LinearLayout) mLayoutBulder.findViewById(R.id.btn_choose_label_builder);
        LinearLayout saveLesson = (LinearLayout) mLayoutBulder.findViewById(R.id.save_lesson_builder);
        nameLessonChange = (EditText) mLayoutBulder.findViewById(R.id.lesson_name_builder);
        mainTextLessonChange = (EditText) mLayoutBulder.findViewById(R.id.text_lesson_builder);

        labelForLesson = "label_1";
        mLabelLesson = (ImageView) mLayoutBulder.findViewById(R.id.image_for_lesson_builder);
        mJsonUtils.updateLabel("label_1", mLabelLesson);

        nameLessonChange.setText(nameLesson);
        mainTextLessonChange.setText(allTextForLesson);

        layoutChangeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChooseLabel.class);
                startActivityForResult(i, 2);

            }
        });

        saveLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameLesson = nameLessonChange.getText().toString();
                if (nameLessonChange.getText().toString().length() != 0 && mainTextLessonChange.getText().toString().length() != 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNavigation.this);
                    builder.setCancelable(false);
                    builder.setTitle("Save Lesson?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mJsonUtils.deleteLesson(oldNameLesson);
                            mJsonUtils.insertTextIntoJson(nameLessonChange.getText().toString(), mainTextLessonChange.getText().toString(), labelForLesson);
                            updateContent();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();


                } else {
                    Toast.makeText(ActivityNavigation.this, "Input text into text fields", Toast.LENGTH_SHORT).show();


                }
            }
        });
        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutBulder);


    }



    @Override
    protected void onResume() {
        findDisplayOrientation();
        super.onResume();
    }

    private void updateContent() {

        String mainText = mJsonUtils.readFile();

        allTextForLesson = mJsonUtils.getAllTextForLesson(mainText, nameLesson);


        mQuizLogic.setNull(allTextForLesson);

        showNotePad();

    }

    private void createQuiz() {
        allTextForLesson = mJsonUtils.getAllTextForLesson(jsonArrayText, nameLesson);
        mQuizLogic = new QuizLogic(allTextForLesson);
        mLogicTraining2 = new LogicTraining2();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {

            if (!notePadShow) {


                if (history[0] == "Training") {

                    if (history[1] == "Training") {
                        showNotePad();
                    }
                    showQuiz();
                } else {
                    showNotePad();
                }

            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (!notePadShow) {
                showNotePad();
            } else {
                finish();
            }
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play) {

            String historyCase = "";
            historyCase = history[1];

            switch (historyCase) {
                case "Training":
                    t1.setSpeechRate(1f);
                    t1.speak(textFieldForLearning.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case "Training2":
                    String[] arrayTextCurrent; // массив слов который введен в поле для конкатенации
                    int numberWords = 0;       // количество слов которые нужно сказать из текущего предложения
                    String[] arrayTextToSpeech; // массив слов которые нужно сказать
                    String speech = "";         // текст для озвучивания
                    arrayTextCurrent = mTextViewForConcat.getText().toString().split("[ ]");
                    numberWords = arrayTextCurrent.length + 1;
                    arrayTextToSpeech = mQuizLogic.getCurrentSentenceArray();
                    if (arrayTextToSpeech.length >= numberWords) {
                        for (int i = 0; i < numberWords; i++) {
                            speech = speech + arrayTextToSpeech[i];
                        }
                        speech = speech + ".";
                        t1.setSpeechRate(0.5f);
                        t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

                    }
                    break;
                default:
                    t1.setSpeechRate(1f);
                    t1.speak(mQuizLogic.getSentenceString(currentSentenceIndex), TextToSpeech.QUEUE_FLUSH, null);
            }


//                    if (history[1].equalsIgnoreCase("Training")) {
//                        t1.setSpeechRate(1f);
//                        t1.speak(textFieldForLearning.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                    if (history[1].equalsIgnoreCase("Training2")) {
//                        String[] arrayTextCurrent; // массив слов который введен в поле для конкатенации
//                        int numberWords = 0;       // количество слов которые нужно сказать из текущего предложения
//                        String[] arrayTextToSpeech; // массив слов которые нужно сказать
//                        String speech = "";         // текст для озвучивания
//                        arrayTextCurrent = mTextViewForConcat.getText().toString().split("[ ]");
//                        numberWords = arrayTextCurrent.length + 1;
//                        arrayTextToSpeech = mQuizLogic.getCurrentSentenceArray();
//                        if (arrayTextToSpeech.length >= numberWords) {
//                            for (int i = 0; i < numberWords; i++) {
//                                speech = speech + arrayTextToSpeech[i];
//                            }
//                            speech = speech + ".";
//                            t1.setSpeechRate(0.5f);
//                            t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
//                        }
//
//                    } else {
//                        t1.setSpeechRate(1f);
//                        t1.speak(mQuizLogic.getSentenceString(currentSentenceIndex), TextToSpeech.QUEUE_FLUSH, null);
//                    }
                    return true;
            }

            return true;
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_note_pad) {

                showNotePad();
            } else if (id == R.id.nav_training) {
                showQuiz();
            } else if (id == R.id.nav_editor) {
                showEditor();
            } else if (id == R.id.nav_settings) {
                showSettings();
            } else if (id == R.id.nav_training2) {
                showTraining2();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.END);
            return true;
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == 2) {
                labelForLesson = data.getStringExtra("Label");
                mJsonUtils.updateLabel(labelForLesson, mLabelLesson);

            }
        }

        @Override
        protected void onDestroy () {

            if (t1 != null) {
                t1.stop();
                t1.shutdown();
            }

            manager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));
            super.onDestroy();
        }
}
