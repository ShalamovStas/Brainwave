package com.example.shalamov.brainwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ChooseLabel extends AppCompatActivity {

    LinearLayout mButton0, mButton1, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8,
            mButton9, mButton10, mButton11, mButton12, mButton13, mButton14, mButton15, mButton16, mButton17,mButton18,mButton19,mButton20,mButton21
            ,mButton22,mButton23,mButton24,mButton25,mButton26,mButton27,mButton28,mButton29,mButton30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_label);
        mButton0 = (LinearLayout) findViewById(R.id.layout0);
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
        mButton14 = (LinearLayout) findViewById(R.id.layout14);
        mButton15 = (LinearLayout) findViewById(R.id.layout15);
        mButton16 = (LinearLayout) findViewById(R.id.layout16);
        mButton17 = (LinearLayout) findViewById(R.id.layout17);
        mButton18 = (LinearLayout) findViewById(R.id.layout18);
        mButton19 = (LinearLayout) findViewById(R.id.layout19);
        mButton20 = (LinearLayout) findViewById(R.id.layout20);
        mButton21 = (LinearLayout) findViewById(R.id.layout21);
        mButton22 = (LinearLayout) findViewById(R.id.layout22);
        mButton23 = (LinearLayout) findViewById(R.id.layout23);
        mButton24 = (LinearLayout) findViewById(R.id.layout24);
        mButton25 = (LinearLayout) findViewById(R.id.layout25);
        mButton26 = (LinearLayout) findViewById(R.id.layout26);
        mButton27 = (LinearLayout) findViewById(R.id.layout27);
        mButton28 = (LinearLayout) findViewById(R.id.layout28);
        mButton29 = (LinearLayout) findViewById(R.id.layout29);
        mButton30 = (LinearLayout) findViewById(R.id.layout30);

        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.putExtra("Label", "label_0");
                setResult(2, i);
                finish();
            }
        });

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
        mButton14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_14");
                setResult(2, i);
                finish();
            }
        });
        mButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_15");
                setResult(2, i);
                finish();
            }
        });

        mButton16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_16");
                setResult(2, i);
                finish();
            }
        });
        mButton17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_17");
                setResult(2, i);
                finish();
            }
        });
        mButton18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_18");
                setResult(2, i);
                finish();
            }
        });
        mButton19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_19");
                setResult(2, i);
                finish();
            }
        });
        mButton20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_20");
                setResult(2, i);
                finish();
            }
        });
        mButton21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_21");
                setResult(2, i);
                finish();
            }
        });
        mButton22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_22");
                setResult(2, i);
                finish();
            }
        });
        mButton23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_23");
                setResult(2, i);
                finish();
            }
        });
        mButton24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_24");
                setResult(2, i);
                finish();
            }
        });
        mButton25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_25");
                setResult(2, i);
                finish();
            }
        });
        mButton26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_26");
                setResult(2, i);
                finish();
            }
        });
        mButton27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_27");
                setResult(2, i);
                finish();
            }
        });
        mButton28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_28");
                setResult(2, i);
                finish();
            }
        });
        mButton29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_29");
                setResult(2, i);
                finish();
            }
        });
        mButton30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Label", "label_30");
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
