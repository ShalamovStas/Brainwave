package com.example.shalamov.brainwave;

/**
 * Created by shala on 22.06.2018.
 */

public class MainActivityLogic {

    public String getNumberOfSentences(String text){
       String[] arrayText = text.split("[.\\?\\!]");
        String number = arrayText.length + " sentences";
        return number;
    }
}
