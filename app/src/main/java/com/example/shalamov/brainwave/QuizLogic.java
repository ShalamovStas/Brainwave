package com.example.shalamov.brainwave;

import android.util.Log;

import com.example.shalamov.brainwave.utils.Lesson;

import java.util.ArrayList;

/**
 * Created by shala on 19.06.2018.
 */

public class QuizLogic {
    final String TAG = "QuizLogic";
    private Lesson lesson;
    private String mainText;
//    private String textFavorite;
    private ArrayList arrayListText;
    private ArrayList<String> arrayListTextFavorite;
//    private String[] mainTextArray; // предолжения разделены (предл1), (предл2), ...
//    private String[] textFavoriteArray;
    private String[] temp; //сдесь разделяется текущее предложение. после
    // изменения предложения архив пересоздается

    private String currentTextInLayout;
    private int currentSentence; //Текущее предложение
    private int currentWord; //Текущее Слово

    public QuizLogic(Lesson lesson) {

        this.lesson = lesson;
        this.mainText = lesson.getText();
//        this.textFavorite = lesson.getTextFavorite();
        currentSentence = 0;
        currentWord = 0;
        createArrayCurrentSentence(currentSentence);

        Log.d("brain", "QuizLogic(constructor)-mainText = " + mainText);
    }

    public String[] getCurrentSentenceArray() {
        return temp;
    }

    public void setNull() {
        Log.d(TAG, "setNull()\ncurrentSentence = " + currentSentence);
        currentSentence = 0;
        currentWord = 0;
//        createArray();
        createArrayCurrentSentence(currentSentence);
    }

//    private void createArray() {
//        Log.d(TAG, "\n======================createArray start======================");
//
//        Log.d(TAG, "mainText" + "{" +  mainText + "}");
//
//        String[] temp = mainText.split("[.\\?\\!\\\n]");
//
//        int count;
//        for (int i = 0; i < temp.length; i++) {
//            count = 0;
//            boolean flag = true;
//            while (flag) {
//                if (temp[i].length() != 0) { // если в конце текста стоит пробелы, это условие не позволит появится ошибке на строчке temp[i].substring(0, 1
//                    // пробелы будут удалятся и строчка будет в таком виде temp[i] = "";
//                    String substring = temp[i].substring(0, 1);
//                    if (substring.equalsIgnoreCase(" ")) {
//                        temp[i] = temp[i].substring(1, temp[i].length());
//                        flag = true;
//                    } else {
//                        flag = false;
//                    }
//                } else {
//                    flag = false;
//                }
//                count++;
//                if(count == 5){
//                    flag = false;
//                }
//            }
//
//
//        }
//        // delete empty elements
//        ArrayList<String> strings = new ArrayList<>();
//
//
//        for (int i = 0; i < temp.length; i++) {
//
//            if (temp[i].length() != 0) {
//                strings.add(temp[i]);
//            }
//        }
//
//        String[] newArraySplit = new String[strings.size()];
//        StringBuilder sb = new StringBuilder();
//
//        for (int j = 0; j < strings.size(); j++) {
//            newArraySplit[j] = strings.get(j);
//
//            //условте && needPoint позволяет не добовлять точку после последнего предложения
//            if ((newArraySplit.length - 1) != j) {
//                sb.append(".");
//            }
//
//        }
//
//        mainTextArray = newArraySplit;
////        lesson.setText(sb.toString());
//
//        Log.d(TAG,"strings:" + "{" +  strings.toString() + "}");
//        Log.d(TAG, "\n======================createArray stop======================");
//    }

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

        if (currentSentence == lesson.getArrayListText().size() - 1) {
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
            currentSentence = lesson.getArrayListText().size() - 1;
        }
        createArrayCurrentSentence(currentSentence);
        return allWord();

    }

    public void createArrayCurrentSentence(int currentSentence) {
        currentTextInLayout = "";
        currentWord = 0;

        String senttenceForSplit = lesson.getArrayListText().get(currentSentence); //получим предложение для разделения
        temp = senttenceForSplit.split("[ ]");           // арай со словами готов

    }

    public int getNumberOfSentences() {
        return lesson.getArrayListText().size();
    }


    public int getCurrentSentenceInt() {
        return currentSentence + 1;
    }

    public int getCurrentSentenceIndex() {
        if (currentSentence == lesson.getArrayListText().size()) {
            currentSentence = 0;
        }
        return currentSentence;
    }

    public String getCurrentSentenceString() {

        Log.d(TAG, "getCurrentSentenceString()\ncurrentSentence = " + currentSentence);

        String text = lesson.getArrayListText().get(currentSentence);

//        currentSentence++;
        return text;
    }



    public String getSentenceString(int index) {

        return lesson.getArrayListText().get(index);
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


//    public void nextSentencePlus() {
//
//        int jump;
//        int maxIndex = lesson.getArrayListText().size(). - 1;
//        jump = maxIndex - currentSentence;
//        jump = jump / 2;
//        currentSentence = currentSentence + jump;
//        createArrayCurrentSentence(currentSentence);
//
//    }

//    public String changeSentence(String newSentence, int numberInArray) {
//
//
//        mainTextArray[numberInArray] = newSentence;
//        String allSentences = "";
//        for (int i = 0; i < mainTextArray.length; i++) {
//            if (i != mainTextArray.length - 1) {
//                allSentences = allSentences + mainTextArray[i] + ". ";
//            } else {
//                allSentences = allSentences + mainTextArray[i];
//            }
//        }
//
//
//        return allSentences;
//    }

//    public String deleteSentence(int indexSentence) {
//
//        String allSentences = "";
//        for (int i = 0; i < mainTextArray.length; i++) {
//            if (i == indexSentence) {
//                continue;
//            }
//            allSentences = allSentences + mainTextArray[i] + ". ";
//        }
//        return allSentences;
//    }

    public boolean checkFavoriteSentence(Lesson lesson, String text) {

        boolean flag = false;


        ArrayList<String> arrayListTextFavorite = lesson.getArrayListTextFavorite();

        for (int i = 0; i < arrayListTextFavorite.size(); i++) {
            if (arrayListTextFavorite.get(i).equalsIgnoreCase(text)) {
                flag = true;
            }
        }

        return flag;
    }

    public boolean checkIfFavoriteTextEmpty(Lesson lesson) {

        boolean flag = false;
        ArrayList<String> arrayListTextFavorite = lesson.getArrayListTextFavorite();

        if ((arrayListTextFavorite.size() == 0)) {
            flag =  true;
        }
        return flag;
    }
}
