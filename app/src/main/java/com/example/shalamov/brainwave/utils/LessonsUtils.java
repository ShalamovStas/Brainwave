package com.example.shalamov.brainwave.utils;

import com.example.shalamov.brainwave.Global;

import java.util.ArrayList;

public class LessonsUtils {

    // класс для работы с объектами Lesson и списком уроков
    // изменение объектов, добавление новых, итд

    public void createLesson(int number, String name, String text, String textFavorite, String description1, String description2, String description3, String description4, String label, String progress){
       Lesson lesson = new Lesson(number, name, text, textFavorite,description1, description2, description3, description4, label, progress);


        //создаем масив из предложений
        String[] arrayText = text.split("[.\\?\\!]");
        lesson.setArrayText(arrayText);


        ArrayList lessons = Global.getLessonsList();
        lessons.add(lesson);
    }

    public void changeLesson(int number, String name, String text, String description1, String description2, String description3, String description4, String label, String progress){
        Lesson lesson = (Lesson) Global.getLessonsList().get(number);
        lesson.setName(name);
        lesson.setText(text);
        lesson.setTextFavorite("");
        lesson.setDescription1(description1);
        lesson.setDescription2(description2);
        lesson.setDescription3(description3);
        lesson.setDescription4(description4);
        lesson.setLabel(label);
        lesson.setProgress(progress);

    }

    public void createNewLesson( String name, String text, String textFavorite, String description1, String description2, String description3, String description4, String label, String progress){
        Lesson lesson = new Lesson(Global.getLessonsList().size() + 1, name, text, textFavorite ,description1, description2, description3, description4, label, progress);


        //создаем масив из предложений
        String[] arrayText = text.split("[.\\?\\!]");
        lesson.setArrayText(arrayText);


        ArrayList lessons = Global.getLessonsList();
        lessons.add(lesson);
    }

    // метод изменения предложения
    public void changeSentence(Lesson lesson, int index, String newSentence){
        // из модели урка берем текст и разделяем его по предложениям

        String[] arrayText = lesson.getText().split("[.\\?\\!]");
        String oldSentence = arrayText[index];
        //изменяем предложение по извесному индексу
        arrayText[index] = newSentence;

        if(lesson.getTextFavorite() != null){
            String[] arrayTextFavorite = lesson.getTextFavorite().split("[.\\?\\!]");
            for (int i = 0; i < arrayTextFavorite.length; i++) {
                if(arrayTextFavorite[i].equalsIgnoreCase(oldSentence)){
                    arrayTextFavorite[i] = newSentence;
                }
            }
            StringBuilder newTextFavorite = new StringBuilder();
            for (int i = 0; i < arrayText.length; i++) {
                //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
                // при разделении текста на предложения.
                newTextFavorite.append(arrayText[i]);
                if(!((arrayText.length-1)==i)) {
                    newTextFavorite.append(". ");
                }
            }
            lesson.setTextFavorite(newTextFavorite.toString());
        }

        StringBuilder newText = new StringBuilder();
        //в цикле проходим все элементы массива для создания одного текста
        for (int i = 0; i < arrayText.length; i++) {
            //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
            // при разделении текста на предложения.
            newText.append(arrayText[i]);
            if(!((arrayText.length-1)==i)) {
                newText.append(". ");
            }
        }

        lesson.setText(newText.toString());


    }

    public void deleteSentence(Lesson lesson, int index){
        // из модели урка берем текст и разделяем его по предложениям
        String[] arrayText = lesson.getText().split("[.\\?\\!]");
        //изменяем предложение по извесному индексу
        arrayText[index] = "";


        ArrayList<String> arrayTextNew = new ArrayList<>();
// copy old array to new without old sentence
        for (int i = 0; i < arrayText.length; i++) {
            if(!(i == index)) {

                arrayTextNew.add(arrayText[i]);
            }
        }


        StringBuilder newText = new StringBuilder();
        //в цикле проходим все элементы массива для создания одного текста
        for (int i = 0; i < arrayTextNew.size(); i++) {


                //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
                // при разделении текста на предложения.
                newText.append(arrayTextNew.get(i));
                if (!((arrayTextNew.size() - 1) == i)) {
                    newText.append(". ");
                }
        }

        lesson.setText(newText.toString());
    }

    public void deleteLesson(int index){
        Global.getLessonsList().remove(index);
        Global.getJsonUtils().saveFromModelToFile();
    }

    public void addFavoriteSentence(Lesson lesson, String text){
        String newText;
        if(lesson.getTextFavorite() == null){
            newText = text;
            lesson.setTextFavorite(newText);
        }else{
            newText = lesson.getTextFavorite() + "." + text;
            lesson.setTextFavorite(newText);
        }

        }

    public void deleteFavoriteSentence(Lesson lesson, String text){


        String[] arrayTextFavorite = lesson.getTextFavorite().split("[.\\?\\!]");
        ArrayList<String> arrayTextNewFavorite = new ArrayList<>();


            for (int j = 0; j < arrayTextFavorite.length; j++) {
                if(!arrayTextFavorite[j].equalsIgnoreCase(text)){
                    arrayTextNewFavorite.add(arrayTextFavorite[j]);
                }
            }


        StringBuilder newText = new StringBuilder();
        //в цикле проходим все элементы массива для создания одного текста
        for (int i = 0; i < arrayTextNewFavorite.size(); i++) {


            //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
            // при разделении текста на предложения.
            newText.append(arrayTextNewFavorite.get(i));
            if (!((arrayTextNewFavorite.size() - 1) == i)) {
                newText.append(". ");
            }
        }

        lesson.setTextFavorite(newText.toString());

    }

}
