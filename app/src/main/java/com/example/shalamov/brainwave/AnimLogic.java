package com.example.shalamov.brainwave;

/**
 * Created by shala on 24.06.2018.
 */

public class AnimLogic {
    private String[] sentence = {"My", "name", "is", "Stas" };

    public int getArraySize(){

        return sentence.length;
    }

    public String getTextUsingIndex(int index){
        return sentence[index];
    }
}
