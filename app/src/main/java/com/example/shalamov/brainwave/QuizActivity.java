package com.example.shalamov.brainwave;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.example.shalamov.brainwave.jsonUtils.JsonUtilsOld;

import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    //Quiz Это базовый Layout для добавления контента
    View mLayoutLearnText, mLayoutBulder, layoutNote;
    LinearLayout layoutQuizForAddContent, mNotePadButton, mTreaningButton, mSettingsButton, mEditorButton, mNavigationLayout;
    LinearLayout layoutNotepadForAddContent;
    TextView textNoteForSentence, mNumberSentence, mEditorText, mSettingsText, mTrainingText, mNotepadText;
    View layoutForSentence;
    RelativeLayout layoutForClick;
    JsonUtilsOld mJsonUtilsOld;
    String jsonArrayText, nameLesson;
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
    TextToSpeech t1;
    boolean dayTheme; // дневная тема если true, режим день


    //saving position for scrool
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        jsonArrayText = getIntent().getExtras().getString("jsonArray");
        nameLesson = getIntent().getExtras().getString("name_lesson");
        oldNameLesson = nameLesson;
        mJsonUtilsOld = new JsonUtilsOld(this);
        createQuiz();
        createAnimLogic();
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle("Quiz");
        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
        initializeBasicLayout();
        getSettingsForLesson();
//        showAnimation();
        showNotePad();
//        initializeQuizLayout();
//        initializeBuilderLayout();
//        initializeNoteLayout();

//        showNotePad();
//        showQuiz();
//        showBuilder();

//        layoutQuizForAddContent.removeAllViewsInLayout();
//        layoutQuizForAddContent.addView(layoutNote);


    }

    private void createAnimLogic() {
        mAnimLogic = new AnimLogic();
    }

    private void getSettingsForLesson() {

        //глобальные настройки
        dayTheme = true;

        //size text in training
        settingsSizeText = 25;
        boolBoltText = true;
        boolItalicText = false;

        //settings for quizLogic
        currentSentenceIndex = 0;
        mQuizLogic.setCurrSentenceNull();
        mQuizLogic.setCurrWordNull();
        mQuizLogic.createArrayCurrentSentence(0);

    }

    private void initializeBasicLayout() {

        mNavigationLayout = (LinearLayout)  findViewById(R.id.navigation);

        mNotepadImage = (ImageView) findViewById(R.id.notepad_image_button);
        mTrainingImage = (ImageView) findViewById(R.id.training_image_button);
        mSettingsImage = (ImageView) findViewById(R.id.settings_image_button);
        mEditorImage = (ImageView) findViewById(R.id.editor_image_button);

        mNotepadText = (TextView) findViewById(R.id.notepad_text);
        mTrainingText = (TextView) findViewById(R.id.training_text);
        mSettingsText = (TextView) findViewById(R.id.settings_text);
        mEditorText = (TextView) findViewById(R.id.editor_text);


        layoutQuizForAddContent = (LinearLayout) findViewById(R.id.layout_for_add_content);
        mNotePadButton = (LinearLayout) findViewById(R.id.notepad_button);
        mTreaningButton = (LinearLayout) findViewById(R.id.training_button);
        mSettingsButton = (LinearLayout) findViewById(R.id.settings_button);
        mEditorButton = (LinearLayout) findViewById(R.id.editor_button);


        mNotePadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showNotePad();

            }
        });

        mTreaningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showQuiz();

            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSettings();

            }
        });

        mEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditor();
            }
        });

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

        saveStateNotePadLayout();
        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can create your own style");

        mNotepadText.setTextColor(Color.parseColor("#000000"));
        mTrainingText.setTextColor(Color.parseColor("#000000"));
        mSettingsText.setTextColor(Color.parseColor("#FFFFFF"));
        mEditorText.setTextColor(Color.parseColor("#000000"));

        mNotepadImage.setImageResource(R.drawable.ic_notepad);
        mTrainingImage.setImageResource(R.drawable.ic_japanese);
        mSettingsImage.setImageResource(R.drawable.ic_settings_white);
        mEditorImage.setImageResource(R.drawable.ic_edit);

        mNotePadButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mTreaningButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mSettingsButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.colorPrimary));
        mEditorButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        layoutQuizForAddContent.removeAllViews();


        layoutSettings = getLayoutInflater().inflate(R.layout.layout_settings, null);
        mTextExample = (TextView) layoutSettings.findViewById(R.id.text_example);

        mSwitchBolt = (Switch) layoutSettings.findViewById(R.id.bolt_switch);
        mSwitchDayNight = (Switch) layoutSettings.findViewById(R.id.night_switch);
        mSeekBar = (SeekBar) layoutSettings.findViewById(R.id.seek_bar);
        mButtonDefaultValues = (Button) layoutSettings.findViewById(R.id.default_button);

        mSwitchBolt.setChecked(boolBoltText);
        mSwitchDayNight.setChecked(!dayTheme);
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
                settingsSizeText = 25;
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.play, menu);
//        menu.findItem(R.id.action_play).setVisible(true);

        return true;
    }

    private void showQuiz() {

        saveStateNotePadLayout();

        mQuizLogic.setCurrSentence(currentSentenceIndex);
        mQuizLogic.createArrayCurrentSentence(currentSentenceIndex);

        ab.setSubtitle("#" + (currentSentenceIndex + 1) + " from " + mQuizLogic.getNumberOfSentences());


        mNotepadText.setTextColor(Color.parseColor("#000000"));
        mTrainingText.setTextColor(Color.parseColor("#FFFFFF"));
        mSettingsText.setTextColor(Color.parseColor("#000000"));
        mEditorText.setTextColor(Color.parseColor("#000000"));

        mNotepadImage.setImageResource(R.drawable.ic_notepad);
        mTrainingImage.setImageResource(R.drawable.ic_japanese_white);
        mSettingsImage.setImageResource(R.drawable.ic_settings);
        mEditorImage.setImageResource(R.drawable.ic_edit);

        if (dayTheme) {
            mNotePadButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
            mTreaningButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.colorPrimary));
            mSettingsButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
            mEditorButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        } else {
            mNotePadButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey800));
            mTreaningButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey900));
            mSettingsButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey800));
            mEditorButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey800));
        }

        mLayoutLearnText = getLayoutInflater().inflate(R.layout.train_layout, null); //тренировочный лайоут с текстовым полем и кнопками внизу
        layoutForClick = (RelativeLayout) mLayoutLearnText.findViewById(R.id.layout_tap_quiz); //первый лейоут где располагается главный текст и по нему отслеживаются нажатия
        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) mLayoutLearnText.findViewById(R.id.navigation_bottom);
        textFieldForLearning = (TextView) mLayoutLearnText.findViewById(R.id.text_field_for_learning);

        if (!dayTheme) {
            textFieldForLearning.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorDark));
            layoutForClick.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey600));
            bottomNavigationItemView.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColorGrey500));
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
                        break;
                    case R.id.ok:
                        textFieldForLearning.setText(mQuizLogic.setCurrWordNull());
                        break;
                    case R.id.next:
                        textFieldForLearning.setText(mQuizLogic.nextSentence());
                        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
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


        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutLearnText);

    }

    private void showNotePad() {


        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can change, add and remove sentences");
        mNotepadText.setTextColor(Color.parseColor("#FFFFFF"));
        mTrainingText.setTextColor(Color.parseColor("#000000"));
        mSettingsText.setTextColor(Color.parseColor("#000000"));
        mEditorText.setTextColor(Color.parseColor("#000000"));

        mNotepadImage.setImageResource(R.drawable.ic_notepad_white);
        mTrainingImage.setImageResource(R.drawable.ic_japanese);
        mSettingsImage.setImageResource(R.drawable.ic_settings);
        mEditorImage.setImageResource(R.drawable.ic_edit);


        mNotePadButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.colorPrimary));
        mTreaningButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mSettingsButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mEditorButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));

        layoutNote = getLayoutInflater().inflate(R.layout.note_layout, null);
        mScroll = (ScrollView) layoutNote.findViewById(R.id.scroll);
        mScroll.setFillViewport(true);
        layoutNotepadForAddContent = (LinearLayout) layoutNote.findViewById(R.id.note_layout_for_add);
        mQuizLogic.setCurrSentenceNull();
        for (int i = 0; i < mQuizLogic.getNumberOfSentences(); i++) {
            final int indexSentence = i;
            layoutForSentence = getLayoutInflater().inflate(R.layout.layout_sentence, null);
            textNoteForSentence = (TextView) layoutForSentence.findViewById(R.id.text_for_sentence);
            mNumberSentence = (TextView) layoutForSentence.findViewById(R.id.number_sentence);
            mNumberSentence.setText("#" + (i + 1));
            textNoteForSentence.setText(mQuizLogic.getCurrentSentenceString());
            layoutNotepadForAddContent.addView(layoutForSentence);

            if (updateTextMarkColor == i) {
//                textNoteForSentence.setTextColor((Color.parseColor("#0097a7")));
                textNoteForSentence.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor2));
                textNoteForSentence.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
//                textNoteForSentence.setTextColor((Color.parseColor("#303f9f")));
                textNoteForSentence.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.colorPrimaryDark));
            }

            layoutForSentence.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Correcting");
                    final EditText editText = new EditText(QuizActivity.this);
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    editText.setText(mQuizLogic.getSentenceString(indexSentence));
                    builder.setView(editText);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            saveStateNotePadLayout();

                            if (editText.getText().length() == 0) {
                                Toast.makeText(QuizActivity.this, "Text can`t be empty!", Toast.LENGTH_SHORT).show();
                            } else {
                                focused = layoutForSentence;
                                updateTextMarkColor = indexSentence;
                                mJsonUtilsOld.updateSentence(jsonArrayText, nameLesson, mQuizLogic.changeSentence(editText.getText().toString(), indexSentence));

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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(QuizActivity.this);
                            builder1.setCancelable(false);
                            builder1.setTitle("Delete sentence?");
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveStateNotePadLayout();
                                    updateTextMarkColor = indexSentence;
                                    mJsonUtilsOld.updateSentence(jsonArrayText, nameLesson, mQuizLogic.deleteSentence(indexSentence));
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


//        mScroll.setActivated(true);
//        mScroll.setFillViewport(true);
        mScroll.post(new Runnable() {
            @Override
            public void run() {
//                mScroll.scrollBy(0, scrollYPosition);
                mScroll.smoothScrollBy(0, scrollYPosition);
            }
        });

//        mScroll.smoothScrollTo(0, 500);


//        mScroll.requestChildFocus(focused, layoutNote);

    }

    private void showEditor() {

        currentSentenceIndex = mQuizLogic.getCurrentSentenceIndex();

        ab.setSubtitle("You can change, add and remove sentences");
        mNotepadText.setTextColor(Color.parseColor("#000000"));
        mTrainingText.setTextColor(Color.parseColor("#000000"));
        mSettingsText.setTextColor(Color.parseColor("#000000"));
        mEditorText.setTextColor(Color.parseColor("#FFFFFF"));

        mNotepadImage.setImageResource(R.drawable.ic_notepad);
        mTrainingImage.setImageResource(R.drawable.ic_japanese);
        mSettingsImage.setImageResource(R.drawable.ic_settings);
        mEditorImage.setImageResource(R.drawable.ic_edit_white);

        mNotePadButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mTreaningButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mSettingsButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.myColor1));
        mEditorButton.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.colorPrimary));


        mLayoutBulder = getLayoutInflater().inflate(R.layout.builder_layout, null);


        LinearLayout layoutChangeLabel = (LinearLayout) mLayoutBulder.findViewById(R.id.btn_choose_label_builder);
        LinearLayout saveLesson = (LinearLayout) mLayoutBulder.findViewById(R.id.save_lesson_builder);
        nameLessonChange = (EditText) mLayoutBulder.findViewById(R.id.lesson_name_builder);
        mainTextLessonChange = (EditText) mLayoutBulder.findViewById(R.id.text_lesson_builder);
        labelForLesson = "label_1";
        mLabelLesson = (ImageView) mLayoutBulder.findViewById(R.id.image_for_lesson_builder);
        mJsonUtilsOld.updateLabel("label_1", mLabelLesson);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Save Lesson?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mJsonUtilsOld.deleteLesson(oldNameLesson);
                            mJsonUtilsOld.insertTextIntoJson(nameLessonChange.getText().toString(), mainTextLessonChange.getText().toString(), labelForLesson);
                            updateContent();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();


                } else {
                    Toast.makeText(QuizActivity.this, "Input text into text fields", Toast.LENGTH_SHORT).show();


                }
            }
        });
        layoutQuizForAddContent.removeAllViews();
        layoutQuizForAddContent.addView(mLayoutBulder);


    }

    private void updateContent() {
        String mainText = mJsonUtilsOld.readFile();
        allTextForLesson = mJsonUtilsOld.getAllTextForLesson(mainText, nameLesson);
        mQuizLogic.setNull(allTextForLesson);
        showNotePad();
    }


    private void initializeQuizLayout() {

        mLayoutLearnText = getLayoutInflater().inflate(R.layout.train_layout, null);
        RelativeLayout layoutForClick = (RelativeLayout) mLayoutLearnText.findViewById(R.id.layout_tap_quiz);
//        BottomNavigationView topNavigationItemView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) mLayoutLearnText.findViewById(R.id.navigation_bottom);

        final TextView textFieldForLearning = (TextView) mLayoutLearnText.findViewById(R.id.text_field_for_learning);
//
//        topNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.training:
//                        nameLessonChange.setEnabled(false);
//                        mainTextLessonChange.setEnabled(false);
//                        layoutQuizForAddContent.removeAllViewsInLayout();
//                        layoutQuizForAddContent.addView(mLayoutLearnText);
//                        break;
//                    case R.id.change:
//                        nameLessonChange.setEnabled(true);
//                        mainTextLessonChange.setEnabled(true);
//                        layoutQuizForAddContent.removeAllViewsInLayout();
//                        layoutQuizForAddContent.addView(mLayoutBulder);
//                        break;
//                    case R.id.settings:
//                        layoutQuizForAddContent.removeAllViewsInLayout();
//                        nameLessonChange.setEnabled(false);
//                        mainTextLessonChange.setEnabled(false);
//                        Toast.makeText(QuizActivity.this, "settings", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return true;
//            }
//
//        });


        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.previous:
                        textFieldForLearning.setText(mQuizLogic.previousSentence());
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        break;
                    case R.id.ok:

                        textFieldForLearning.setText(mQuizLogic.setCurrWordNull());
                        break;
                    case R.id.next:
                        textFieldForLearning.setText(mQuizLogic.nextSentence());
                        ab.setSubtitle("#" + mQuizLogic.getCurrentSentenceInt() + " from " + mQuizLogic.getNumberOfSentences());
                        break;
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


    }


    private void initializeBuilderLayout() {
        mLayoutBulder = getLayoutInflater().inflate(R.layout.builder_layout, null);
        LinearLayout layoutChangeLabel = (LinearLayout) mLayoutBulder.findViewById(R.id.btn_choose_label_builder);
        LinearLayout saveLesson = (LinearLayout) mLayoutBulder.findViewById(R.id.save_lesson_builder);
        nameLessonChange = (EditText) mLayoutBulder.findViewById(R.id.lesson_name_builder);
        mainTextLessonChange = (EditText) mLayoutBulder.findViewById(R.id.text_lesson_builder);
        labelForLesson = "label_1";
        mLabelLesson = (ImageView) mLayoutBulder.findViewById(R.id.image_for_lesson_builder);
        mJsonUtilsOld.updateLabel("label_1", mLabelLesson);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Save Lesson?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mJsonUtilsOld.deleteLesson(oldNameLesson);
                            mJsonUtilsOld.insertTextIntoJson(nameLessonChange.getText().toString(), mainTextLessonChange.getText().toString(), labelForLesson);
                            updateContent();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();


                } else {
                    Toast.makeText(QuizActivity.this, "Input text into text fields", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }


    private void createQuiz() {
        allTextForLesson = mJsonUtilsOld.getAllTextForLesson(jsonArrayText, nameLesson);
        mQuizLogic = new QuizLogic(allTextForLesson);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            labelForLesson = data.getStringExtra("Label");

            mJsonUtilsOld.updateLabel(labelForLesson, mLabelLesson);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_play) {
//            Toast.makeText(this, "Act", Toast.LENGTH_SHORT).show();


            t1.speak(mQuizLogic.getSentenceString(currentSentenceIndex), TextToSpeech.QUEUE_FLUSH, null);


        }


        return true;
    }




}
