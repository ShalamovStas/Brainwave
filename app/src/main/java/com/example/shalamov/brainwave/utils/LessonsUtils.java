package com.example.shalamov.brainwave.utils;

import android.nfc.Tag;

import com.example.shalamov.brainwave.Global;

import java.util.ArrayList;

public class LessonsUtils {

    // класс для работы с объектами Lesson и списком уроков
    // изменение объектов, добавление новых, итд

    public void createLesson(int number, String name, String text, String textFavorite, String description1, String description2, String description3, String description4, String label, String progress){
       Lesson lesson = new Lesson(number, name, text, textFavorite,description1, description2, description3, description4, label, progress);


        //создаем масив из предложений
        String[] arrayText = text.split("[.\\?\\!\\\n]");
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
        String[] arrayText = text.split("[.\\?\\!\\\n]");

        lesson.setArrayText(arrayText);


        ArrayList lessons = Global.getLessonsList();
        lessons.add(lesson);
    }

    // метод изменения предложения
    public void changeSentence(Lesson lesson, int index, String newSentence){
        // из модели урка берем текст и разделяем его по предложениям


        String[] arrayText = lesson.getText().split("[.\\?\\!\\\n]");
        String oldSentence = arrayText[index];
        //изменяем предложение по извесному индексу
        arrayText[index] = newSentence;

        if(!(lesson.getTextFavorite().equalsIgnoreCase(""))){
            String[] arrayTextFavorite = lesson.getTextFavorite().split("[.\\?\\!\\\n]");

            boolean flag = true;
            int count = 0;
            while (flag) {
                if (oldSentence.length() != 0) { // если в конце текста стоит пробелы, это условие не позволит появится ошибке на строчке temp[i].substring(0, 1
                    // пробелы будут удалятся и строчка будет в таком виде temp[i] = "";
                    String substring = oldSentence.substring(0, 1);
                    if (substring.equalsIgnoreCase(" ")) {
                        oldSentence = oldSentence.substring(1, oldSentence.length());
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
                count++;
                if(count == 5){
                    flag = false;
                }
            }

            for (int i = 0; i < arrayTextFavorite.length; i++) {



                if(arrayTextFavorite[i].equalsIgnoreCase(oldSentence)){
                    arrayTextFavorite[i] = newSentence;
                }
            }
            StringBuilder newTextFavorite = new StringBuilder();
            for (int i = 0; i < arrayTextFavorite.length; i++) {
                //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
                // при разделении текста на предложения.
                newTextFavorite.append(arrayTextFavorite[i]);
                if(!((arrayTextFavorite.length-1)==i)) {
                    newTextFavorite.append(".");
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
                newText.append(".");
            }
        }

        lesson.setText(newText.toString());


    }

    public void deleteSentence(Lesson lesson, int index){
        // из модели урка берем текст и разделяем его по предложениям
        String[] arrayText = lesson.getText().split("[.\\?\\!]");
        //изменяем предложение по извесному индексу

        //проверяем не пустой ли текст со звездочкой
        //
        if(checkIfFavoriteSentenceExist(lesson)){
            // при условии того, что существуют предложения со звездочкой,
            //используем метод удаления элемента в виде String из общей строки String,
            // который возвращает строку
            // метод удаляет пробелы перед строкой массива если они существуют
            lesson.setTextFavorite(deleteElementInString(lesson.getTextFavorite(), arrayText[index]));
        }

        ArrayList<String> arrayTextNew = new ArrayList<>();
// copy old array to new without old sentence
        for (int i = 0; i < arrayText.length; i++) {
            if(!(i == index)) {

                arrayTextNew.add(arrayText[i]);
            }
        }

        lesson.setText(arrayToString(arrayTextNew));


    }

    public void deleteLesson(int index){
        Global.getLessonsList().remove(index);
        Global.getJsonUtils().saveFromModelToFile();
    }

    public void addFavoriteSentence(Lesson lesson, String text){
        String newText;
        if(lesson.getTextFavorite().equalsIgnoreCase("")){
            newText = text;
            lesson.setTextFavorite(newText);
        }else{
            newText = lesson.getTextFavorite() + "." + text;
            lesson.setTextFavorite(newText);
        }

        }

    public void deleteFavoriteSentence(Lesson lesson, String text){


        String[] arrayTextFavorite = lesson.getTextFavorite().split("[.\\?\\!\\\n]");
        ArrayList<String> arrayTextNewFavorite = new ArrayList<>();


            for (int j = 0; j < arrayTextFavorite.length; j++) {
                if(!arrayTextFavorite[j].equalsIgnoreCase(text)){
                    arrayTextNewFavorite.add(arrayTextFavorite[j]);
                }
            }


        lesson.setTextFavorite(arrayToString(arrayTextNewFavorite));

    }

    private String deleteElementInString (String allText, String elementForDelete){

        //разделение текста начального [Aaa 1. Bbb 2. Ccc 3] на предложения по признаку точки
        // результат разделения: [Aaa 1] [ Bbb 2] [ Ccc 3].
        String[] allTextArray = allText.split("[.\\?\\!\\\n]");
//
        //цикл для удаления пробелов перед началом предложения
        // проходим по всем элементам массива, созданого их общего текста allText
        // для второго элемента, который равен [ Bbb 2], flag = true;
        // while ->
        //if (true) {
        //выделяем первый элемент substring
        // если первій элемент == " " а он и равен пробелу " "
        // присваиваем элементу массива знаяение без первого элемента
        // flag = true; цикл повторяется до тех пор, пока не будут удалены все пробелы
        // (условие substring.equalsIgnoreCase(" ") это регулирует)

        boolean needToDeleteEmptyElement = false; // если в массиве присутствуют пустые элементы, например
        // [][Aaa 1][Bbb 2][Ccc 3] - первый элемент не содержит символов.
        ArrayList<String> allTextArrayWithoutEmptyElements;
        boolean flag;
        //защита от бесконечного цыкла
        int count = 0;
        for (int i = 0; i < allTextArray.length; i++) {
            count = 0;
            flag = true;
            while (flag) {
                if (allTextArray[i].length() != 0) {

                    String substring = allTextArray[i].substring(0, 1);
                    if (substring.equalsIgnoreCase(" ")) {
                        allTextArray[i] = allTextArray[i].substring(1, allTextArray[i].length());
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else {
                    needToDeleteEmptyElement = true;
                    flag = false;
                }

                count++;
                if (count == 5){
                    flag = false;
                }
            }

        }

        // на даном этапе имеем массив, созданый из текста allText
        // с без пробелов в начале предложения



        StringBuilder newText = new StringBuilder();
        boolean needPoint;
        for (int j = 0; j < allTextArray.length; j++) {
            needPoint = false;
            if(!allTextArray[j].equalsIgnoreCase(elementForDelete)){
                newText.append(allTextArray[j]);
                needPoint = true;
            }

            //условте && needPoint позволяет не добовлять точку после последнего предложения
            if ((allTextArray.length - 1) != j && needPoint) {
                newText.append(".");
            }

        }

       return newText.toString();
    }


    private String arrayToString (ArrayList<String> array){

        StringBuilder newText = new StringBuilder();
        //в цикле проходим все элементы массива для создания одного текста
        for (int i = 0; i < array.size(); i++) {


            //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
            // при разделении текста на предложения.
            newText.append(array.get(i));
            if (!((array.size() - 1) == i)) {
                newText.append(".");
            }
        }
        return newText.toString();
    }


    private String arrayToString (String[] array){

        StringBuilder newText = new StringBuilder();
        //в цикле проходим все элементы массива для создания одного текста
        for (int i = 0; i < array.length; i++) {


            //условие для того, чтобы в конце текста не появлялась точка, что приводит к появлению нового элемента массива
            // при разделении текста на предложения.
            newText.append(array[i]);
            if (!((array.length - 1) == i)) {
                newText.append(".");
            }
        }
        return newText.toString();
    }

    private boolean checkIfFavoriteSentenceExist(Lesson lesson){

        if(lesson.getTextFavorite().equalsIgnoreCase("")){
            return false;
        }else{
            return true;
        }
    }

}
