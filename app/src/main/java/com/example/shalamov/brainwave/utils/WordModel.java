package com.example.shalamov.brainwave.utils;

import java.util.ArrayList;

public class WordModel {

    private int number;
    private String name;
    private String label;
    private String words;
    private ArrayList<String> arrayListWords;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public ArrayList<String> getArrayListWords() {
        return arrayListWords;
    }

    public void setArrayListWords(ArrayList<String> arrayListWords) {
        this.arrayListWords = arrayListWords;
    }
}
