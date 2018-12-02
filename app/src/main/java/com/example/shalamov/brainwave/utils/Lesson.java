package com.example.shalamov.brainwave.utils;

import java.util.ArrayList;

public class Lesson {

    private int number;
    private String name;
    private String text;
    private ArrayList<String> arrayListText;
    private String textFavorite;
    private ArrayList<String> arrayListTextFavorite;
    private String label;
    private String progress;
    private String description1;
    private String description2;
    private String description3;
    private String description4;



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

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
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

    public String getDescription2() {
        return description2;
    }

    public String getDescription3() {
        return description3;
    }

    public String getDescription4() {
        return description4;
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
