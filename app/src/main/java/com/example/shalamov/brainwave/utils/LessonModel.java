package com.example.shalamov.brainwave.utils;

import java.util.ArrayList;

public class LessonModel {

    private int number;
    private String name;
    private String text;
    private ArrayList<String> arrayListText;
    private String textFavorite;
    private ArrayList<String> arrayListTextFavorite;
    private String label;
    private String progress;
    private String description1;
    private String words;
    private ArrayList<String> arrayListWords;



    public void setArrayListWords(ArrayList<String> arrayListWords) {
        this.arrayListWords = arrayListWords;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setArrayListTextFavorite(ArrayList<String> arrayListTextFavorite) {
        this.arrayListTextFavorite = arrayListTextFavorite;
    }

    public void setArrayListText(ArrayList<String> arrayListText) {
        this.arrayListText = arrayListText;
    }

    public void setTextFavorite(String textFavorite) {
        this.textFavorite = textFavorite;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }



    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getDescription1() {
        return description1;
    }

    public String getWords() {
        return words;
    }

    public ArrayList<String> getArrayListWords() {
        return arrayListWords;
    }

    public String getLabel() {
        return label;
    }

    public String getProgress() {
        return progress;
    }

    public int getNumber() {
        return number;
    }

    public String getTextFavorite() {
        return textFavorite;
    }


    public ArrayList<String> getArrayListTextFavorite() {
        return arrayListTextFavorite;
    }
    public ArrayList<String> getArrayListText() {
        return arrayListText;
    }
}
