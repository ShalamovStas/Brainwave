package com.example.shalamov.brainwave.utils;

import android.widget.ImageView;

import com.example.shalamov.brainwave.R;

public class ImageUtils {
    private Integer[] mThumbIds = {
            R.drawable.ic_layout0,
            R.drawable.ic_layout1,
            R.drawable.ic_layout2,
            R.drawable.ic_layout3,
            R.drawable.ic_layout4,
            R.drawable.ic_layout5,
            R.drawable.ic_layout1,
            R.drawable.ic_layout4,
            R.drawable.ic_science,
            R.drawable.ic_layout9,
            R.drawable.ic_layout10,
            R.drawable.ic_think,
            R.drawable.ic_ted,
            R.drawable.ic_layout13,
            R.drawable.ic_layout14,
            R.drawable.ic_layout15,
            R.drawable.ic_layout16,
            R.drawable.ic_layout17,
            R.drawable.ic_layout18,
            R.drawable.ic_layout19,
            R.drawable.ic_layout20,
            R.drawable.ic_layout21,
            R.drawable.ic_layout22,
            R.drawable.ic_layout23,
            R.drawable.ic_layout24,
            R.drawable.ic_layout25,
            R.drawable.ic_layout26,
            R.drawable.ic_layout27,
            R.drawable.ic_layout28,
            R.drawable.ic_layout29,
            R.drawable.ic_layout30,

    };

    public void updateLabel(String labelForLesson, ImageView mLabelLesson) {

        switch (labelForLesson) {
            case "label_0":
                mLabelLesson.setImageResource(mThumbIds[0]);
                break;
            case "label_1":
                mLabelLesson.setImageResource(mThumbIds[1]);
                break;
            case "label_2":
                mLabelLesson.setImageResource(mThumbIds[2]);
                break;
            case "label_3":
                mLabelLesson.setImageResource(mThumbIds[3]);
                break;
            case "label_4":
                mLabelLesson.setImageResource(mThumbIds[4]);
                break;
            case "label_5":
                mLabelLesson.setImageResource(mThumbIds[5]);
                break;
            case "label_6":
                mLabelLesson.setImageResource(mThumbIds[6]);
                break;
            case "label_7":
                mLabelLesson.setImageResource(mThumbIds[7]);
                break;
            case "label_8":
                mLabelLesson.setImageResource(mThumbIds[8]);
                break;
            case "label_9":
                mLabelLesson.setImageResource(mThumbIds[9]);
                break;
            case "label_10":
                mLabelLesson.setImageResource(mThumbIds[10]);
                break;
            case "label_11":
                mLabelLesson.setImageResource(mThumbIds[11]);
                break;
            case "label_12":
                mLabelLesson.setImageResource(mThumbIds[12]);
                break;
            case "label_13":
                mLabelLesson.setImageResource(mThumbIds[13]);
                break;
            case "label_14":
                mLabelLesson.setImageResource(mThumbIds[14]);
                break;
            case "label_15":
                mLabelLesson.setImageResource(mThumbIds[15]);
                break;
            case "label_16":
                mLabelLesson.setImageResource(mThumbIds[16]);
                break;
            case "label_17":
                mLabelLesson.setImageResource(mThumbIds[17]);
                break;
            case "label_18":
                mLabelLesson.setImageResource(mThumbIds[18]);
                break;
            case "label_19":
                mLabelLesson.setImageResource(mThumbIds[19]);
                break;
            case "label_20":
                mLabelLesson.setImageResource(mThumbIds[20]);
                break;
            case "label_21":
                mLabelLesson.setImageResource(mThumbIds[21]);
                break;
            case "label_22":
                mLabelLesson.setImageResource(mThumbIds[22]);
                break;
            case "label_23":
                mLabelLesson.setImageResource(mThumbIds[23]);
                break;
            case "label_24":
                mLabelLesson.setImageResource(mThumbIds[24]);
                break;
            case "label_25":
                mLabelLesson.setImageResource(mThumbIds[25]);
                break;
            case "label_26":
                mLabelLesson.setImageResource(mThumbIds[26]);
                break;
            case "label_27":
                mLabelLesson.setImageResource(mThumbIds[27]);
                break;
            case "label_28":
                mLabelLesson.setImageResource(mThumbIds[28]);
                break;
            case "label_29":
                mLabelLesson.setImageResource(mThumbIds[29]);
                break;
            case "label_30":
                mLabelLesson.setImageResource(mThumbIds[30]);
                break;
            case "nothing":
                mLabelLesson.setImageResource(mThumbIds[1]);
                break;


        }}
}
