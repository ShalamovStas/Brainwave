package com.example.shalamov.brainwave;

import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shala on 22.07.2018.
 */

public class LogicTraining2 {
    private View mViewMain;
    private View mView2;
    private String[] mixSentence;
    private String[] trueSentence;

    private int widthOfScreen;

    //переменные для логиик работы
    private int limitNumberSymbolsInLine;
    int numberOfElementsInCurrentLine;
    private int currentLine;
    private int numberOfLines;
    private int currentWord;
    private int currentFlagInSentence;
    private int rightFlagInSentence;
    private int leftFlagInSentence;
    private ArrayList<String> line;

    public String getCorrectSentence(){
        String request = "";

        for (int i = 0; i < trueSentence.length; i++) {
            request = request + trueSentence[i] + " ";
        }

        return request;
    }

    public void setParametersLogicTraining2 (String[] sentence, int widthMain) {
        this.trueSentence = sentence;
        this.widthOfScreen = widthMain;

        calculateAllParametersForLogic();
        createMixSentence();
    }

    private void createMixSentence(){
        mixSentence = new String[trueSentence.length];

        for (int i = 0; i < mixSentence.length; i++) {
            mixSentence[i] = trueSentence[i];
        }

        Random random = new Random();
        for (int i = 0; i < mixSentence.length; i++) {
            int k = random.nextInt(mixSentence.length);
            String temp = mixSentence[i];
            mixSentence[i] = mixSentence[k];
            mixSentence[k] = temp;
        }

    }

    public boolean checkSentence(String textForCheck){
        boolean flag = false;
        String rightText = "";


        for (int i = 0; i < trueSentence.length; i++) {
            rightText = rightText + trueSentence[i] + " ";
        }

        if(textForCheck.equalsIgnoreCase(rightText)){
            flag = true;
        }


        return flag;
    }

    private void calculateAllParametersForLogic(){
        line = new ArrayList<>();
        //расчитываем количество строк (линий)
        double temp = widthOfScreen/1080.0;
        limitNumberSymbolsInLine = (int) (temp * 30);

        currentFlagInSentence = 0;
        leftFlagInSentence = 0;
        currentLine = 1;
        rightFlagInSentence = 0;
        leftFlagInSentence = 0;
    }


    public int getNumberOfLines() {
        return numberOfLines;
    }

//    public Integer[] calculate() {
//        // одна буква = 30 widthElement (A=35 AA=70 a=29 f=18 d=30)
//
//        limitNumberSymbolsInLine = (widthOfScreen / 30) - 10;
//        int numberSymbolsTemp = 0;
//        numberOfLines = 0;
//        int counter = 0;
//        Integer[] numberElementsInLines;
//        //расчет количества линий
//        for (int i = 0; i < mixSentence.length; i++) {
//            numberSymbolsTemp = numberSymbolsTemp + mixSentence[i].length();
//            if (numberSymbolsTemp > limitNumberSymbolsInLine) {
//                numberOfLines++;
//                numberSymbolsTemp=0;
//            }
//        }
//        //заполнение массива - указание колва эл-тов в каждой линии
//        numberOfLines++;
//        numberElementsInLines= new Integer[numberOfLines];
//        numberSymbolsTemp = 0;
//
////заполним массив размером равным количеством линий и заполним его
//
//
//        for (int i = 0; i < mixSentence.length; i++) {
//            numberSymbolsTemp = numberSymbolsTemp + mixSentence[i].length();
//            if (numberSymbolsTemp > limitNumberSymbolsInLine) {
//
//                numberSymbolsTemp=0;
//            }
//            counter++;
//        }
//
//        for (int i = 0; i <= mixSentence.length; i++) {
//            if(i == mixSentence.length){
//                arrayList.add(counter);
//                continue;
//            }
//            numberSymbolsTemp = numberSymbolsTemp + mixSentence[i].length();
//            if (numberSymbolsTemp >= limitNumberSymbolsInLine) {
//                arrayList.add(counter);
//                numberSymbolsTemp=0;
//                counter = 0;
//                continue;
//            }
//            counter++;
//        }
//
//
//        return arrayList;
//    }


    public String nextWord(){
        String answer = "";

        if(currentFlagInSentence < mixSentence.length){
            answer =  mixSentence[currentFlagInSentence];
            currentFlagInSentence++;
        }

        return answer;
    }


    public int getNumberOfElementsInCurrentLine(){

//        rightFlagInSentence = currentFlagInSentence;
        numberOfElementsInCurrentLine=0;
        int numberOfSymbolsInLine = 0;
        boolean flag = true;
        while(flag) {
            if(rightFlagInSentence >= mixSentence.length){
                flag = false;
                continue;
            }

            numberOfSymbolsInLine = numberOfSymbolsInLine + mixSentence[rightFlagInSentence].length();
            numberOfElementsInCurrentLine++;
            rightFlagInSentence++;
            if(!ifNeedNextWord(numberOfSymbolsInLine)){
                numberOfElementsInCurrentLine--;
                rightFlagInSentence--;
                flag = false;

            }
        }

        return numberOfElementsInCurrentLine;
    }



    private boolean ifNeedNextWord(int lettersInLine){

        int lettersPx = lettersInLine * 25;
        int buttonsInLinePx = numberOfElementsInCurrentLine * 40;
        int sum = lettersPx + buttonsInLinePx;
        if(sum < widthOfScreen){
            return true;
        }else {
            return false;
        }
    }

    public ArrayList<String> nextLine(){
        line.clear();
        int numberOfElementsInCurrentLine = getNumberOfElementsInCurrentLine();

        if(numberOfElementsInCurrentLine != 0){
            for (int i = leftFlagInSentence; i < rightFlagInSentence; i++) {
                if(mixSentence[i].equalsIgnoreCase("I")){
                    line.add(" I ");
                }else {
                    line.add(mixSentence[i]);

                }
            }
        }
        leftFlagInSentence = rightFlagInSentence;




        return line;
    }

    public int getNumberOfWordsInSentence(){
       return trueSentence.length;
    }


}
