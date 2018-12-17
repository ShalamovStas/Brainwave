package com.example.shalamov.brainwave;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalamov.brainwave.utils.Lesson;
import com.example.shalamov.brainwave.utils.ToolForNotepad;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Quiz Это базовый Layout для добавления контента
    View mLayoutLearnText, mLayoutBulder, layoutNote, mLayoutMainTraining2;
    LinearLayout layoutQuizForAddContent;
    //    mNotePadButton, mTreaningButton, mSettingsButton, mEditorButton, mNavigationLayout;
    LinearLayout layoutNotepadForAddContent;
    TextView textNoteForSentence, mNumberSentence, mTextViewForConcat;
    //    WebView textNoteForSentenceWeb;
    View layoutForSentence;
    LinearLayout layoutForClick;
    //  JsonUtilsOld mJsonUtilsOld;
    String nameLesson;
    QuizLogic mQuizLogic;
    ActionBar ab;
    String labelForLesson;
    ImageView mLabelLesson;
    EditText nameLessonChange;
    EditText mainTextLessonChange;
    String allTextForLesson;
    //  String oldNameLesson;
//    ImageView mNotepadImage, mTrainingImage, mSettingsImage, mEditorImage;
    ScrollView mScroll;

    int updateTextMarkColor = 500;


    //global for all layouts
    boolean notePadShow;
    //    boolean previousLayoutIsTraining;
    String[] history = {"null", "null"};
    TextToSpeech t1;
    boolean dayTheme; // дневная тема если true, режим день
    String theme;


    //saving position for scroll
    int scrollYPosition = 0;

    // settings layout variables

    View layoutSettings;
    int settingsSizeTextQuiz, settingsSizeTextNote;
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
    //    TextView mTextQuestionLabel;
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

    int lessonNumber;
    Lesson lesson;
    boolean wasChanged = false;

    private Spinner spinner;
    Animation animation;

    ClipboardManager clipboard;


    //добавление элементов этапами
    private ToolForNotepad toolForNotepad;
    private Button mBtnFavoriteTextBottomNavigation;
    private Button mBtnAllTextBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //настройки окна
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);


        lessonNumber = Integer.parseInt(getIntent().getStringExtra("number"));
        lesson = (Lesson) Global.getLessonsList().get(lessonNumber);


//        oldNameLesson = nameLesson;

        createQuiz();

        ab.setTitle("Quiz");
        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        initializeBasicLayout();
        getSettingsForLesson();


        showNotePad();
//        showTraining2();
//        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        am.registerMediaButtonEventReceiver(am);

        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        manager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));

        Global.setActivityNavigation(this);

    }


    private void getSettingsForLesson() {

        theme = "day";

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
        settingsSizeTextQuiz = 25;
        settingsSizeTextNote = 20;
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


        layoutQuizForAddContent = findViewById(R.id.layout_for_add_content);
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

//    private void showAnimation() {
//        mLayoutAnimation = getLayoutInflater().inflate(R.layout.layout_animation_quiz, null);
//        RelativeLayout relativeLayout = (RelativeLayout) mLayoutAnimation.findViewById(R.id.basic_layout);
//        final ImageView image = (ImageView) mLayoutAnimation.findViewById(R.id.imageView2);
//
//        for (int i = 0; i < mAnimLogic.getArraySize(); i++) {
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////            params.setMarginStart(i * 10);
//
//            params.setMargins(i * 10, i * 10, i * 10, i * 10);
//
//            final Button button = new Button(this);
//            button.setText(mAnimLogic.getTextUsingIndex(i));
//            button.setLayoutParams(params);
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ObjectAnimator animation = ObjectAnimator.ofFloat(button, "translationX", 200f);
//                    animation.setDuration(2000);
//                    animation.start();
//                }
//            });
//
//            relativeLayout.addView(button);
//
//        }
//
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (animDirection == 0) {
//                    animDirection = 1;
//                    ObjectAnimator animation1 = ObjectAnimator.ofFloat(image, "translationX", 300f);
//                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(image, "translationY", 300f);
//                    animation1.setDuration(2000);
//                    animation2.setDuration(2000);
//                    animation1.start();
//                    animation2.start();
//                } else {
//                    animDirection = 0;
//                    ObjectAnimator animation = ObjectAnimator.ofFloat(image, "translationX", 0);
//                    animation.setDuration(2000);
//                    animation.start();
//                }
//            }
//        });
//
//
//        layoutQuizForAddContent.removeAllViews();
//        layoutQuizForAddContent.addView(mLayoutAnimation);
//
//    }


    private void showSettings() {
        writeHistory("Settings");
        notePadShow = false;

        saveStateNotePadLayout();
        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can create your own style");

        layoutQuizForAddContent.removeAllViews();


        layoutSettings = getLayoutInflater().inflate(R.layout.layout_settings, null);


        mTextExample = layoutSettings.findViewById(R.id.text_example);
        final TextView mTextExampleForNotepad = layoutSettings.findViewById(R.id.text_example_notepad);

        mSwitchBolt = layoutSettings.findViewById(R.id.bolt_switch);
        mSwitchDayNight = layoutSettings.findViewById(R.id.night_switch);
        mSeekBar = layoutSettings.findViewById(R.id.seek_bar);
        SeekBar mSeekBarForNotepad = layoutSettings.findViewById(R.id.seek_bar_for_notepad);
        mButtonDefaultValues = layoutSettings.findViewById(R.id.default_button);
        mVoice = layoutSettings.findViewById(R.id.voice);

        mSwitchBolt.setChecked(boolBoltText);
        mSwitchDayNight.setChecked(!dayTheme);
        mVoice.setChecked(voice);

        mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextQuiz);
        mTextExample.setText("Text size: " + settingsSizeTextQuiz);
        mSeekBar.setProgress(settingsSizeTextQuiz);

        mTextExampleForNotepad.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextNote);
        mTextExampleForNotepad.setText("Text size: " + settingsSizeTextNote);
        mSeekBarForNotepad.setProgress(settingsSizeTextNote);

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

//                    mJsonUtilsOld.saveSettings("night");

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

//                    mJsonUtilsOld.saveSettings("day");

                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                settingsSizeTextQuiz = i;
                mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextQuiz);
                mTextExample.setText("Text size: " + settingsSizeTextQuiz);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarForNotepad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                settingsSizeTextNote = i;
                mTextExampleForNotepad.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextNote);
                mTextExampleForNotepad.setText("Text size: " + settingsSizeTextNote);
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
                settingsSizeTextQuiz = 30;
                settingsSizeTextNote = 20;
                boolBoltText = true;
                boolItalicText = false;

                mSwitchBolt.setChecked(boolBoltText);
                mTextExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextQuiz);
                mTextExample.setText("Text size: " + settingsSizeTextQuiz);
                mSeekBar.setProgress(settingsSizeTextQuiz);

                mTextExampleForNotepad.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextNote);
                mTextExampleForNotepad.setText("Text size: " + settingsSizeTextNote);
                mSeekBar.setProgress(settingsSizeTextNote);

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
        layoutForClick = mLayoutLearnText.findViewById(R.id.layout_tap_quiz); //первый лейоут где располагается главный текст и по нему отслеживаются нажатия
        LinearLayout layoutForClickPrevious = mLayoutLearnText.findViewById(R.id.layout_btn_previous);
        LinearLayout layoutForClickNext = mLayoutLearnText.findViewById(R.id.layout_btn_next);
        LinearLayout mLayoutGoToTraining2 = mLayoutLearnText.findViewById(R.id.btn_go_to_train2);
        LinearLayout mLayoutGoToTranslator = mLayoutLearnText.findViewById(R.id.btn_go_to_translator);
        LinearLayout mLayoutGoToSettings = mLayoutLearnText.findViewById(R.id.btn_go_to_settings);
        LinearLayout mLayoutAddRus = mLayoutLearnText.findViewById(R.id.btn_add_rus);
        BottomNavigationView bottomNavigationItemView = mLayoutLearnText.findViewById(R.id.navigation_bottom);
        textFieldForLearning = mLayoutLearnText.findViewById(R.id.text_field_for_learning);

        final ImageView mImageFavoriteOrNot = (ImageView) mLayoutLearnText.findViewById(R.id.image_favorite_or_not);


        if (!dayTheme) {
            textFieldForLearning.setTextColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorDark));
            layoutForClick.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey600));
            bottomNavigationItemView.setBackgroundColor(ContextCompat.getColor(ActivityNavigation.this, R.color.myColorGrey800));
        }

        textFieldForLearning.setText(mQuizLogic.allWord());

        if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
            if (mQuizLogic.checkFavoriteSentence(lesson, mQuizLogic.getCurrentSentenceString())) {
                Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
            } else {
                Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
            }
        }

        //set settings
        textFieldForLearning.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextQuiz);
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

                        if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                            if (mQuizLogic.checkFavoriteSentence(lesson, mQuizLogic.getSentenceString(currentSentenceIndex))) {
                                Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
                            } else {
                                Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
                            }
                        }

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
                        if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                            if (mQuizLogic.checkFavoriteSentence(lesson, mQuizLogic.getSentenceString(currentSentenceIndex))) {
                                Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
                            } else {
                                Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
                            }
                        }
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

        layoutForClickPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFieldForLearning.setText(mQuizLogic.previousSentence());
                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());

                if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                    if (mQuizLogic.checkFavoriteSentence(lesson, mQuizLogic.getSentenceString(currentSentenceIndex))) {
                        Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
                    } else {
                        Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
                    }
                }
            }
        });

        layoutForClickNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFieldForLearning.setText(mQuizLogic.nextSentence());
                currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                    if (mQuizLogic.checkFavoriteSentence(lesson, mQuizLogic.getSentenceString(currentSentenceIndex))) {
                        Global.getImageUtils().updateLabel("favorite_sentence_quiz", mImageFavoriteOrNot);
                    } else {
                        Global.getImageUtils().updateLabel("not_favorite_sentence_quiz", mImageFavoriteOrNot);
                    }
                }
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
                openGoogleTranslator(mQuizLogic.allWord());
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

        long time = Calendar.getInstance().getTimeInMillis();


        writeHistory("notePad");

        notePadShow = true;


        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
        toolForNotepad.setStartPosition();
        ab.setSubtitle(lesson.getName());
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


        //кнопки внизу для навигации
        mBtnFavoriteTextBottomNavigation = (Button) layoutNote.findViewById(R.id.btn_favorite_text_bottom_navigation);
        mBtnAllTextBottomNavigation = (Button) layoutNote.findViewById(R.id.btn_all_text_bottom_navigation);


        mQuizLogic.setCurrSentenceNull();

        final LinearLayout mBtnFavoriteText = (LinearLayout) layoutNote.findViewById(R.id.btn_favorite);
        final LinearLayout mBtnAllText = (LinearLayout) layoutNote.findViewById(R.id.btn_all_text);

        final LinearLayout mBtnSettings = (LinearLayout) layoutNote.findViewById(R.id.btn_settings);
        final LinearLayout mBtnTranslator = (LinearLayout) layoutNote.findViewById(R.id.btn_translator);
        final LinearLayout mBtnOpenApp = (LinearLayout) layoutNote.findViewById(R.id.btn_open_app);

        Log.d("brain", "инициализация " + (Calendar.getInstance().getTimeInMillis() - time));

        mBtnFavoriteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scrollYPosition = 0;
                removeContentFromNotepad();
                mQuizLogic.setCurrSentenceNull();
                showFavoriteTextInNotePad();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnFavoriteText.startAnimation(animation);
            }
        });

        mBtnAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scrollYPosition = 0;
                removeContentFromNotepad();
                mQuizLogic.setCurrSentenceNull();
                toolForNotepad.setStartPosition();
                showAllTextInNotePad();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnAllText.startAnimation(animation);
            }
        });

        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollYPosition = 0;
                showSettings();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnSettings.startAnimation(animation);
            }
        });

        mBtnTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollYPosition = 0;
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnTranslator.startAnimation(animation);

                openGoogleTranslator(lesson.getText());

            }
        });

        mBtnOpenApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollYPosition = 0;
                Intent intent = getPackageManager().getLaunchIntentForPackage("air.ru.uchimslova.words");
                startActivity(intent);
            }
        });


        //навигация внизу страницы
        mBtnFavoriteTextBottomNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scrollYPosition = 0;
                removeContentFromNotepad();
                mQuizLogic.setCurrSentenceNull();
                showFavoriteTextInNotePad();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnFavoriteText.startAnimation(animation);
            }
        });

        mBtnAllTextBottomNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollYPosition = 0;
                removeContentFromNotepad();
                mQuizLogic.setCurrSentenceNull();
                toolForNotepad.setStartPosition();
                showAllTextInNotePad();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                mBtnAllText.startAnimation(animation);
            }
        });


        // при достижении конца списка доюавляются новые элементы


        mScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int maxSize = mScroll.getChildAt(0).getHeight() - mScroll.getHeight();

                // DO SOMETHING WITH THE SCROLL COORDINATES
//                Log.d("brain", "ActivityNavig-onScrollChanged - was called");
//                Log.d("brain", "mScroll.getScrollY() = " + mScroll.getScrollY() +
//                "\nmaxSize = " + maxSize);

                boolean flag = history[1].equalsIgnoreCase("Editor");
                Log.d("ActivityNavigation", "==========onScrollChanged===========\n" + "flag = " + flag + "\n" +
                        "history[0] = " + history[1]);

                if (mScroll.getScrollY() == maxSize && !(history[1].equalsIgnoreCase("Editor"))) {
                    if (!(history[1].equalsIgnoreCase("NotePadFavorite"))) {
                        showAllTextInNotePad();
                    }

                }
            }


        });

        Log.d("brain", "Точка перед методом  showAllTextInNotePad()" + (Calendar.getInstance().getTimeInMillis() - time));
        showAllTextInNotePad();

        Log.d("brain", "lesson.getTextFavorite(): [" + lesson.getTextFavorite() + "]");

        Log.d("brain", "Время срабатывания метода " + (Calendar.getInstance().getTimeInMillis() - time));
    }

    private void removeContentFromNotepad() {
        layoutNotepadForAddContent.removeAllViews();
    }

    private void showFavoriteTextInNotePad() {
        writeHistory("NotePadFavorite");
        toolForNotepad.setStartPosition();
        if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {

            for (int i = 0; i < mQuizLogic.getNumberOfSentences(); i++) {

                final int indexSentence = i;
                final String textSentence = mQuizLogic.getCurrentSentenceString();
                if (mQuizLogic.checkFavoriteSentence(lesson, textSentence)) {

                    layoutForSentence = getLayoutInflater().inflate(R.layout.layout_sentence, null);
                    LinearLayout layoutRextAndNumber = (LinearLayout) layoutForSentence.findViewById(R.id.layout_sentence_and_number);
                    final ImageView btnStar = (ImageView) layoutForSentence.findViewById(R.id.btn_star);
                    final ImageView btnTranslateCurrentSentence = (ImageView) layoutForSentence.findViewById(R.id.btn_translate_current_sentence);
                    textNoteForSentence = (TextView) layoutForSentence.findViewById(R.id.text_for_sentence);
                    mNumberSentence = (TextView) layoutForSentence.findViewById(R.id.number_sentence);
                    mNumberSentence.setText("#" + (i + 1));

                    textNoteForSentence.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextNote);
                    textNoteForSentence.setText(Html.fromHtml(formTextForWeb(textSentence)));
                    layoutNotepadForAddContent.addView(layoutForSentence);

                    layoutForSentence.getAnimation();


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


                    if (mQuizLogic.checkFavoriteSentence(lesson, textSentence)) {
                        Global.getImageUtils().updateLabel("star_active", btnStar);
                    } else {
                        Global.getImageUtils().updateLabel("star_inactive", btnStar);
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

                                    // сохраняем положение экрана для того чтобы не перематывать заново
                                    saveStateNotePadLayout();
                                    // текст не может быть пустой
                                    if (editText.getText().length() == 0) {
                                        Toast.makeText(ActivityNavigation.this, "Text can`t be empty!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //запоминаем какой текст подсвечивать
                                        updateTextMarkColor = indexSentence;
                                        //изменяем предложение в уроке
                                        // флаг присутствовали изменения
                                        wasChanged = true;
                                        Global.getLessonsUtils().changeSentence(lesson, textSentence, editText.getText().toString());
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
                                            wasChanged = true;
                                            Global.getLessonsUtils().deleteSentence(lesson, textSentence);
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

                    btnStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean flag;

                            if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                                flag = mQuizLogic.checkFavoriteSentence(lesson, textSentence);

                                if (flag) {
                                    Global.getImageUtils().updateLabel("star_inactive", btnStar);
                                    Global.getLessonsUtils().deleteFavoriteSentence(lesson, textSentence);
                                    wasChanged = true;
                                } else {
                                    Global.getImageUtils().updateLabel("star_active", btnStar);
                                    Global.getLessonsUtils().addFavoriteSentence(lesson, textSentence);
                                    wasChanged = true;
                                }
                            } else {
                                Global.getImageUtils().updateLabel("star_active", btnStar);
                                Global.getLessonsUtils().addFavoriteSentence(lesson, textSentence);
                                wasChanged = true;
                            }


                        }
                    });

                    btnTranslateCurrentSentence.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openGoogleTranslator(textSentence);
                        }
                    });

                }


            }


            layoutQuizForAddContent.removeAllViews();
            layoutQuizForAddContent.addView(layoutNote);

        } else {
            Toast.makeText(this, "There are not any favorite sentences", Toast.LENGTH_SHORT).show();
        }
        mQuizLogic.setCurrSentenceNull();


    }


    private void showAllTextInNotePad() {
        writeHistory("NotePad");

        Integer[] indexes = toolForNotepad.getIndexes();
        Log.d("brain", "ActivityNavigation-showAllTextInNotePad-indexes[0] = " + indexes[0] + "\nindexes[1]=" + indexes[1]);

        if (indexes[0] != indexes[1]) {
            for (int i = indexes[0]; i < indexes[1]; i++) {


                final int indexSentence = i;
                final String textSentence = mQuizLogic.getCurrentSentenceString();
                layoutForSentence = getLayoutInflater().inflate(R.layout.layout_sentence, null);
                LinearLayout layoutRextAndNumber = (LinearLayout) layoutForSentence.findViewById(R.id.layout_sentence_and_number);
                final ImageView btnStar = (ImageView) layoutForSentence.findViewById(R.id.btn_star);
                final ImageView btnTranslateCurrentSentence = (ImageView) layoutForSentence.findViewById(R.id.btn_translate_current_sentence);
                textNoteForSentence = (TextView) layoutForSentence.findViewById(R.id.text_for_sentence);
//                textNoteForSentenceWeb = (WebView) layoutForSentence.findViewById(R.id.text_for_sentence_web);
                mNumberSentence = (TextView) layoutForSentence.findViewById(R.id.number_sentence);
                mNumberSentence.setText("#" + (i + 1));

                textNoteForSentence.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsSizeTextNote);

                textNoteForSentence.setText(Html.fromHtml(formTextForWeb(textSentence)));


//                textNoteForSentenceWeb.loadData(formTextForWeb(textSentence), "text/html; charset=utf-8", "utf-8");


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


                if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                    if (mQuizLogic.checkFavoriteSentence(lesson, textSentence)) {
                        Global.getImageUtils().updateLabel("star_active", btnStar);
                    } else {
                        Global.getImageUtils().updateLabel("star_inactive", btnStar);
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

                                // сохраняем положение экрана для того чтобы не перематывать заново
                                saveStateNotePadLayout();
                                // текст не может быть пустой
                                if (editText.getText().length() == 0) {
                                    Toast.makeText(ActivityNavigation.this, "Text can`t be empty!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //запоминаем какой текст подсвечивать
                                    updateTextMarkColor = indexSentence;
                                    //изменяем предложение в уроке
                                    // флаг присутствовали изменения
                                    wasChanged = true;
                                    Global.getLessonsUtils().changeSentence(lesson, textSentence, editText.getText().toString());
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
                                        wasChanged = true;
                                        Global.getLessonsUtils().deleteSentence(lesson, textSentence);
                                        updateContent();

                                    }

                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder1.create().show();

                            }
                        }).setNeutralButton("Paste", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        ClipData abc = clipboard.getPrimaryClip();
                                        ClipData.Item item = abc.getItemAt(0);
                                        String text = item.getText().toString();
                                        editText.setText(editText.getText() + "=>" + text);


                                        // сохраняем положение экрана для того чтобы не перематывать заново
                                        saveStateNotePadLayout();
                                        // текст не может быть пустой
                                        if (editText.getText().length() == 0) {
                                            Toast.makeText(ActivityNavigation.this, "Text can`t be empty!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //запоминаем какой текст подсвечивать
                                            updateTextMarkColor = indexSentence;
                                            //изменяем предложение в уроке
                                            // флаг присутствовали изменения
                                            wasChanged = true;
                                            Global.getLessonsUtils().changeSentence(lesson, textSentence, editText.getText().toString());
                                            updateContent();

                                            ab.setSubtitle("#" + (indexSentence + 1) + " changed!");

                                        }
                                    }
                                }
                        );
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

                btnStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean flag;

                        if (!mQuizLogic.checkIfFavoriteTextEmpty(lesson)) {
                            flag = mQuizLogic.checkFavoriteSentence(lesson, textSentence);

                            if (flag) {
                                Global.getImageUtils().updateLabel("star_inactive", btnStar);
                                Global.getLessonsUtils().deleteFavoriteSentence(lesson, textSentence);
                                wasChanged = true;
                            } else {
                                Global.getImageUtils().updateLabel("star_active", btnStar);
                                Global.getLessonsUtils().addFavoriteSentence(lesson, textSentence);
                                wasChanged = true;
                            }
                        } else {
                            Global.getImageUtils().updateLabel("star_active", btnStar);
                            Global.getLessonsUtils().addFavoriteSentence(lesson, textSentence);
                            wasChanged = true;
                        }


                    }
                });
                layoutNotepadForAddContent.addView(layoutForSentence);

                btnTranslateCurrentSentence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGoogleTranslator(textSentence);

                    }
                });
            }
        }

        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(layoutNote);

//        mScroll.post(new Runnable() {
//            @Override
//            public void run() {
//
//                mScroll.smoothScrollBy(0, scrollYPosition);
//            }
//        });
    }

    private String formTextForWeb(String textSentence) {
        String[] allTextArray = textSentence.split("[=>]");
        int size = allTextArray.length;
        String finalText = "";
        switch (size) {
            case 0:
                finalText = "<b><font color=#FF0000>ERROR</font></b>";
                break;
            case 1:
                finalText = "<font color=#082779>" + allTextArray[0] + "</font>";
                break;
            case 3:
                finalText = "<font color=#082779>" + allTextArray[0] + "</font>" +
                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>";
                break;
            default:
                finalText = "<font color=#082779>" + textSentence + "</font>";
                break;

        }

        return finalText;
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

        spinner = (Spinner) mLayoutBulder.findViewById(R.id.spinner);
        LinearLayout layoutChangeLabel = (LinearLayout) mLayoutBulder.findViewById(R.id.btn_choose_label_builder);
        LinearLayout saveLesson = (LinearLayout) mLayoutBulder.findViewById(R.id.save_lesson_builder);
        nameLessonChange = (EditText) mLayoutBulder.findViewById(R.id.lesson_name_builder);
        mainTextLessonChange = (EditText) mLayoutBulder.findViewById(R.id.text_lesson_builder);

        labelForLesson = lesson.getLabel();
        mLabelLesson = (ImageView) mLayoutBulder.findViewById(R.id.image_for_lesson_builder);
        Global.getImageUtils().updateLabel(lesson.getLabel(), mLabelLesson);

        nameLessonChange.setText(lesson.getName());
        mainTextLessonChange.setText(lesson.getText());

        setSpinner(lesson.getDescription1());

        final EditText ediTextForDeleting = (EditText) mLayoutBulder.findViewById(R.id.text_for_delete);
        Button buttonDelete = (Button) mLayoutBulder.findViewById(R.id.btn_delete);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ediTextForDeleting.getText().length() != 0 && mainTextLessonChange.getText().length() != 0) {
                    String newText = Global.getLessonsUtils().deleteElementInString(mainTextLessonChange.getText().toString(), ediTextForDeleting.getText().toString());
                    mainTextLessonChange.setText(newText);
                    ediTextForDeleting.setText("");
                }
            }
        });

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
                            wasChanged = true;
                            Global.getLessonsUtils().changeLesson(lessonNumber, nameLessonChange.getText().toString(), mainTextLessonChange.getText().toString(), ActivityNavigation.this.getSpinnerSelect(), "description2", "description3", "description1",
                                    lesson.getLabel(), "progress 1");
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

        allTextForLesson = lesson.getText();
        mQuizLogic.setNull();
        toolForNotepad.setNumberOfSentences(lesson.getArrayListText().size());

        showNotePad();

    }

    private void createQuiz() {

        allTextForLesson = lesson.getText();
        mQuizLogic = new QuizLogic(lesson);
        mLogicTraining2 = new LogicTraining2();
        toolForNotepad = new ToolForNotepad();
        toolForNotepad.setNumberOfSentences(mQuizLogic.getNumberOfSentences());
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
                if (wasChanged) {
                    Global.getJsonUtils().saveFromModelToFile();
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
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
                if (wasChanged) {
                    Global.getJsonUtils().saveFromModelToFile();
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {

            mLabelLesson = (ImageView) mLayoutBulder.findViewById(R.id.image_for_lesson_builder);
            labelForLesson = data.getStringExtra("Label");
            lesson.setLabel(labelForLesson);
            Global.getImageUtils().updateLabel(lesson.getLabel(), mLabelLesson);
//                mJsonUtilsOld.updateLabel(labelForLesson, mLabelLesson);

        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

        manager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));
        super.onDestroy();
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

    private void setSpinner(String category) {
        switch (category) {
            case "temp":
                spinner.setSelection(0);
                break;
            case "current":
                spinner.setSelection(1);
                break;
            case "important":
                spinner.setSelection(2);
                break;
        }

    }

    private void openGoogleTranslator(String text) {
        try {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.google.com/#en/ru/" + text));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplication(), "Sorry, No Google Translation Installed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
