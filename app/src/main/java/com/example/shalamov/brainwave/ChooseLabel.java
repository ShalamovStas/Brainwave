package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ChooseLabel extends AppCompatActivity {

    LinearLayout mButton1, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8,
            mButton9, mButton10, mButton11, mButton12, mButton13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_label);
        mButton1 = (LinearLayout) findViewById(R.id.layout1);
        mButton2 = (LinearLayout) findViewById(R.id.layout2);
        mButton3 = (LinearLayout) findViewById(R.id.layout3);
        mButton4 = (LinearLayout) findViewById(R.id.layout4);
        mButton5 = (LinearLayout) findViewById(R.id.layout5);
        mButton6 = (LinearLayout) findViewById(R.id.layout6);
        mButton7 = (LinearLayout) findViewById(R.id.layout7);
        mButton8 = (LinearLayout) findViewById(R.id.layout8);
        mButton9 = (LinearLayout) findViewById(R.id.layout9);
        mButton10 = (LinearLayout) findViewById(R.id.layout10);
        mButton11 = (LinearLayout) findViewById(R.id.layout11);
        mButton12 = (LinearLayout) findViewById(R.id.layout12);
        mButton13 = (LinearLayout) findViewById(R.id.layout13);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.putExtra("Label", "label_1");
                setResult(2, i);
                finish();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_2");
                setResult(2, i);
                finish();
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_3");
                setResult(2, i);
                finish();
            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_4");
                setResult(2, i);
                finish();
            }
        });

        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_5");
                setResult(2, i);
                finish();
            }
        });

        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_6");
                setResult(2, i);
                finish();
            }
        });

        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_7");
                setResult(2, i);
                finish();
            }
        });

        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_8");
                setResult(2, i);
                finish();
            }
        });

        mButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_9");
                setResult(2, i);
                finish();
            }
        });

        mButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_10");
                setResult(2, i);
                finish();
            }
        });

        mButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_11");
                setResult(2, i);
                finish();
            }
        });

        mButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_12");
                setResult(2, i);
                finish();
            }
        });
        mButton13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_13");
                setResult(2, i);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
