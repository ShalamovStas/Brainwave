package com.example.shalamov.brainwave;

import android.util.Log;

import com.example.shalamov.brainwave.utils.Lesson;

/**
 * Created by shala on 19.06.2018.
 */

public class QuizLogic {
    private String mainText;
    private String textFavorite;
    private String[] mainTextArray; // предолжения разделены (предл1), (предл2), ...
    private String[] textFavoriteArray;
    private String[] temp; //сдесь разделяется текущее предложение. после
    // изменения предложения архив пересоздается

    private String currentTextInLayout;
    private int currentSentence; //Текущее предложение
    private int currentWord; //Текущее Слово

    public QuizLogic(String mainText, String textFavorite) {
        this.mainText = mainText;
        this.textFavorite = textFavorite;
        currentSentence = 0;
        currentWord = 0;
        createArray();
        createArrayCurrentSentence(currentSentence);
    }

    public String[] getCurrentSentenceArray(){
        return temp;
    }

    public void setNull(String mainText) {
        this.mainText = mainText;
        currentSentence = 0;
        currentWord = 0;
        createArray();
        createArrayCurrentSentence(currentSentence);
    }

    private void createArray() {
        mainTextArray = mainText.split("[.\\?\\!]");



        for (int i = 0; i < mainTextArray.length; i++) {
            boolean flag = true;
            while (flag) {
                if (mainTextArray[i].length() != 0) { // если в конце текста стоит пробелы, это условие не позволит появится ошибке на строчке temp[i].substring(0, 1
                    // пробелы будут удалятся и строчка будет в таком виде temp[i] = "";
                    String substring = mainTextArray[i].substring(0, 1);
                    if (substring.equalsIgnoreCase(" ")) {
                        mainTextArray[i] = mainTextArray[i].substring(1, mainTextArray[i].length());
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            }
        }

    }

    public String nextWord() {

        String out = "";
        if (currentWord == temp.length) {
            currentTextInLayout = "";

        } else {

            if (currentTextInLayout == "") {
                currentTextInLayout = temp[currentWord].replace(" ", "") + " ";
                currentWord++;
                out = currentTextInLayout;

            } else {
                currentTextInLayout = currentTextInLayout + temp[currentWord] + " ";
                currentWord++;
                out = currentTextInLayout;
            }

        }

        return out;
    }

    public String setCurrWordNull() {
        currentWord = 0;
        currentTextInLayout = "";
        return "";
    }

    public String nextSentence() {

        if (currentSentence == mainTextArray.length - 1) {
            currentSentence = 0;
            createArrayCurrentSentence(currentSentence);
            return allWord();
        } else {
            currentSentence++;
            createArrayCurrentSentence(currentSentence);
            return allWord();
        }

    }

    public String previousSentence() {

        currentSentence--;

        if (currentSentence == -1) {
            currentSentence = mainTextArray.length - 1;
            createArrayCurrentSentence(currentSentence);
            return allWord();
        } else {
            createArrayCurrentSentence(currentSentence);
            return allWord();
        }

    }

    public void createArrayCurrentSentence(int currentSentence) {
        currentTextInLayout = "";
        currentWord = 0;

        String senttenceForSplit = mainTextArray[currentSentence]; //получим предложение для разделения
        temp = senttenceForSplit.split("[ ]");           // арай со словами готов

    }

    public int getNumberOfSentences() {
        return mainTextArray.length;
    }


    public int getCurrentSentenceInt() {
        return currentSentence + 1;
    }

    public int getCurrentSentenceIndex() {
        if (currentSentence == mainTextArray.length) {
            currentSentence = 0;
        }
        return currentSentence;
    }

    public String getCurrentSentenceString() {

        return mainTextArray[currentSentence++];
    }

    public String getSentenceString(int index) {

        return mainTextArray[index];
    }

    public String allWord() {
        String allText = "";

        for (int i = 0; i < temp.length; i++) {
            allText = allText + temp[i] + " ";

        }

        currentWord = temp.length;
        return allText;
    }

    public void setCurrSentenceNull() {
        currentSentence = 0;
        currentWord = 0;
    }

    public void setCurrSentence(int index) {
        currentSentence = index;
        currentWord = 0;
    }

    public String previousPlusSentence() {


        return "";
    }

    public void nextSentencePlus() {

        int jump;
        int maxIndex = mainTextArray.length - 1;
        jump = maxIndex - currentSentence;
        jump = jump / 2;
        currentSentence = currentSentence + jump;
        createArrayCurrentSentence(currentSentence);

    }

    public String changeSentence(String newSentence, int numberInArray) {


        mainTextArray[numberInArray] = newSentence;
        String allSentences = "";
        for (int i = 0; i < mainTextArray.length; i++) {
            if(i != mainTextArray.length-1) {
                allSentences = allSentences + mainTextArray[i] + ". ";
            }else{
                allSentences = allSentences + mainTextArray[i];
            }
        }


        return allSentences;
    }

    public String deleteSentence(int indexSentence) {

        String allSentences = "";
        for (int i = 0; i < mainTextArray.length; i++) {
            if (i == indexSentence) {
                continue;
            }
            allSentences = allSentences + mainTextArray[i] + ". ";
        }
        return allSentences;
    }

    public boolean checkFavoriteSentence(Lesson lesson, String text){

        boolean flag = false;



           textFavoriteArray =  lesson.getTextFavorite().split("[.\\?\\!]");

            for (int i = 0; i < textFavoriteArray.length; i++) {
                if(textFavoriteArray[i].equalsIgnoreCase(text)){
                    flag = true;
                }
            }
            String textTag = "";

        for (int i = 0; i < textFavoriteArray.length; i++) {
            textTag = textTag + "[" + textFavoriteArray[i] + "]";
        }

        Log.d("brain", textTag);
        return flag;
    }

    public boolean checkIfFavoriteTextEmpty(Lesson lesson){

        if((lesson.getTextFavorite() == null)){
           return true;
        }
        return false;
    }
}
