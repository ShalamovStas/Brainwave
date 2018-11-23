package com.example.shalamov.brainwave;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by shala on 07.11.2018.
 */

public class RemoteControlReceiver extends BroadcastReceiver {



    public RemoteControlReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {


            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                // Реагируем на нажатие

                if (Global.getActivityNavigation().history[1].equalsIgnoreCase("Training")) {
                    Global.getActivityNavigation().t1.setSpeechRate(1f);
                    Global.getActivityNavigation().t1.speak(Global.getActivityNavigation().textFieldForLearning.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }



            }
        }


    }
