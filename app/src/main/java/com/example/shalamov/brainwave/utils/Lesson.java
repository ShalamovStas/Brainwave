package com.example.shalamov.brainwave.utils;

public class Lesson {

        private int number;
    private String name;
    private String text;
    private String textFavorite;
    private String[] arrayText;
    private String label;
    private String progress;

    public String getTextFavorite() {
        return textFavorite;
    }

    public void setTextFavorite(String textFavorite) {
        this.textFavorite = textFavorite;
    }

    public String[] getArrayText() {
        return arrayText;
    }

    public void setArrayText(String[] arrayText) {
        this.arrayText = arrayText;
    }

    public int getNumber() {
        return number;
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

    private String description1;
        private String description2;
        private String description3;
        private String description4;

        public Lesson(int number, String name, String text, String textFavorite, String description1, String description2, String description3, String description4, String label, String progress) {
            this.number = number;
            this.name = name;
            this.text = text;
            this.textFavorite = textFavorite;
            this.description1 = description1;
            this.description2 = description2;
            this.description3 = description3;
            this.description4 = description4;
            this.label = label;
            this.progress = progress;
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
    }
