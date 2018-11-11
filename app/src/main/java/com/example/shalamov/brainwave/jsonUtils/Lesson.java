package com.example.shalamov.brainwave.jsonUtils;

/**
 * Created by shala on 16.06.2018.
 */

public class Lesson {
    private String name;
    private String text;
    private String label;
    private String progress;
    private String description1;
    private String description2;
    private String description3;
    private String description4;

    public Lesson(String name, String text, String description1, String description2, String description3, String description4, String label, String progress) {
        this.name = name;
        this.text = text;
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
