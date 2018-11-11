package com.example.shalamov.brainwave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shala on 08.11.2018.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

        //comment after git installation
        //comment after git installation
    }

}
