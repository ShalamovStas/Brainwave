package com.example.shalamov.brainwave.utils;

import android.util.Log;

import com.example.shalamov.brainwave.Global;

import java.util.ArrayList;

public class LessonsUtils {

    final String TAG = "LessonUtils";

    // класс для работы с объектами Lesson и списком уроков
    // изменение объектов, добавление новых, итд

    public void createLesson(int number, String name, String text, String textFavorite, String description1, String description2, String description3, String description4, String label, String progress) {

        Log.d(TAG, "\n======================createLesson start======================");


        //создаем коллекцию, элементы которой являются отдельные предложения
        ArrayList<String> textArrayList = new ArrayList<>();
        ArrayList<String> textArrayListFavorite = new ArrayList<>();
        //используем метод разделения общего текста text на отдельные предложения и записываем
        //отдельные предложения в коллекцию - метод splitSentence это позволяет делать
        splitSentence(text, textArrayList);
        splitSentence(textFavorite, textArrayListFavorite);
        //из разделенных предложений с удаленными абзацами  и отступами формируется строка
        //textLesson, которая бутед записана в модель Lesson
        String textLesson = arrayListToString(textArrayList);


        Lesson lesson = new Lesson();
        lesson.setNumber(number);
        lesson.setName(name);
        lesson.setText(textLesson);
        lesson.setArrayListText(textArrayList);
        lesson.setTextFavorite(textFavorite);
        lesson.setArrayListTextFavorite(textArrayListFavorite);
        lesson.setLabel(label);
        lesson.setProgress(progress);
        lesson.setDescription1(description1);
        lesson.setDescription2(description2);
        lesson.setDescription3(description3);
        lesson.setDescription4(description4);


        Global.getLessonsList().add(lesson);


    }

    //метод для разделения текста на отдельные предложения, которые записываются в коллекцию
    private void splitSentence(String text, ArrayList<String> textArrayList) {
        Log.d(TAG, "\n======================splitSentence start======================");
        Log.d(TAG, "\ntext:[" + "" + text + "]");


        //разделение текста начального [ . Aaa 1. Bbb 2. Ccc 3] на предложения по признаку точки
        // результат разделения: [ ] [Aaa 1] [ Bbb 2] [ Ccc 3].
        String[] allTextArray = text.split("[.\\?\\!\\\n]");
//
        //цикл для удаления пробелов перед началом предложения
        // проходим по всем элементам массива, созданого их общего текста
        // для первого элемента, который равен [ ], flag = true;
        // while ->
        //if (true) {
        //выделяем первый элемент substring
        // если первій элемент == " "
        // присваиваем элементу массива знаяение без первого элемента
        // flag = true; цикл повторяется до тех пор, пока не будут удалены все пробелы
        // (условие substring.equalsIgnoreCase(" ") это регулирует)

        boolean needToDeleteEmptyElement = false; // если в массиве присутствуют пустые элементы, например
        // [][Aaa 1][Bbb 2][Ccc 3] - первый элемент не содержит символов.
        // этот флаг служит для указания того, что необходимо удалить пустые элементы

        int count = 0; // счетчик для защиты от бесконечного цикла
        boolean flag;
        for (int i = 0; i < allTextArray.length; i++) {
            flag = true;
            count = 0;
            while (flag) {
                if (allTextArray[i].length() != 0) {
                    count++;
                    String substring = allTextArray[i].substring(0, 1);

                    if (substring.equalsIgnoreCase(" ")) {
                        allTextArray[i] = allTextArray[i].substring(1, allTextArray[i].length());
                        flag = true;
                    }
                    if (!substring.equalsIgnoreCase(" ")) {
                        flag = false;
                    }
                }

                if (allTextArray[i].length() == 0) {
                    needToDeleteEmptyElement = true;
                    flag = false;
                }
                //защита от бесконечного цикла
                if (count == 5) {
                    flag = false;
                }
            }

        }

        // проверяем условие если существуют пустые элементы в массиве,
        // то создаем новую коллекцию, в которую запишем все элементы массива без пустых элементов


        for (int i = 0; i < allTextArray.length; i++) {
            if (allTextArray[i].length() != 0) {
                textArrayList.add(allTextArray[i]);
            }
        }


        // на в даной точке имеем либо массив либо коллекцию
        Log.d(TAG, "\ntextArrayList:[" + "" + textArrayList.toString() + "]");
        Log.d(TAG, "\n======================splitSentence end======================");
    }

    public void changeLesson(int number, String name, String text, String description1, String description2, String description3, String description4, String label, String progress) {

        Lesson lesson = (Lesson) Global.getLessonsList().get(number);

        ArrayList<String> textArrayList = lesson.getArrayListText();
        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();
        textArrayList.removeAll(textArrayList);
        textArrayListFavorite.removeAll(textArrayListFavorite);
        splitSentence(text, textArrayList);

        String textLesson = arrayListToString(textArrayList);

        lesson.setNumber(number);
        lesson.setName(name);
        lesson.setText(textLesson);
        lesson.setArrayListText(textArrayList);
        lesson.setTextFavorite("");
        lesson.setArrayListTextFavorite(textArrayListFavorite);
        lesson.setLabel(label);
        lesson.setProgress(progress);
        lesson.setDescription1(description1);
        lesson.setDescription2(description2);
        lesson.setDescription3(description3);
        lesson.setDescription4(description4);

    }

    // метод изменения предложения
    public void changeSentence(Lesson lesson, String oldSentence, String newSentence) {

        // изменяем основной текст
        // работаем с коллекцией
        ArrayList<String> textArrayList = lesson.getArrayListText();

        //проходим по коллекции и ищем предложение которое нужно заменить
        for (int i = 0; i < textArrayList.size(); i++) {
            if (textArrayList.get(i).equalsIgnoreCase(oldSentence)) {
                textArrayList.set(i, newSentence);
            }
        }
        // создание поля text для модели с учтом скорректированого предложения
        String textLesson = arrayListToString(textArrayList);
        // обновление коллекции для для соответствия количества предложений и количества элементов коллекций
        textArrayList.removeAll(textArrayList);
        splitSentence(textLesson, textArrayList);

        lesson.setText(textLesson);


        // изменяем отмеченый текст если он есть
        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        if (textArrayListFavorite.size() != 0) {

            for (int i = 0; i < textArrayListFavorite.size(); i++) {

                if (textArrayListFavorite.get(i).equalsIgnoreCase(oldSentence)) {
                    textArrayListFavorite.set(i, newSentence);
                }
            }

            // создание поля textFavorite для модели с учтом скорректированого предложения
            String textLessonFavorite = arrayListToString(textArrayListFavorite);
            textArrayListFavorite.removeAll(textArrayListFavorite);
            splitSentence(textLessonFavorite, textArrayListFavorite);
            lesson.setTextFavorite(textLessonFavorite);

        }

    }

    public void deleteSentence(Lesson lesson, String sentence) {

        // работаем с коллекцией
        ArrayList<String> textArrayList = lesson.getArrayListText();

        //проходим по коллекции и ищем предложение которое нужно заменить
        for (int i = 0; i < textArrayList.size(); i++) {
            if (textArrayList.get(i).equalsIgnoreCase(sentence)) {
                textArrayList.remove(i);
            }
        }
        // создание поля text для модели с учтом скорректированого предложения
        String textLesson = arrayListToString(textArrayList);
        lesson.setText(textLesson);

        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        if (textArrayListFavorite.size() != 0) {

            for (int i = 0; i < textArrayListFavorite.size(); i++) {

                if (textArrayListFavorite.get(i).equalsIgnoreCase(sentence)) {
                    textArrayListFavorite.remove(i);
                }
            }
            // создание поля textFavorite для модели с учтом скорректированого предложения
            String textLessonFavorite = arrayListToString(textArrayListFavorite);
            lesson.setTextFavorite(textLessonFavorite);
        }
    }

    public void deleteLesson(int index) {
        Global.getLessonsList().remove(index);
        Global.getJsonUtils().saveFromModelToFile();
    }

    public void addFavoriteSentence(Lesson lesson, String text) {
        Log.d(TAG, "=========addFavoriteSentence()=========");
        // изменяем отмеченый текст если он есть
        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        textArrayListFavorite.add(text);

        // создание поля textFavorite для модели с учтом скорректированого предложения
        String textLessonFavorite = arrayListToString(textArrayListFavorite);
        lesson.setTextFavorite(textLessonFavorite);
        Log.d(TAG, "textLessonFavorite = " + textLessonFavorite);
    }

    public void deleteFavoriteSentence(Lesson lesson, String text) {

        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        if (textArrayListFavorite.size() != 0) {

            for (int i = 0; i < textArrayListFavorite.size(); i++) {

                if (textArrayListFavorite.get(i).equalsIgnoreCase(text)) {
                    textArrayListFavorite.remove(i);
                }
            }
            // создание поля textFavorite для модели с учтом скорректированого предложения
            if(textArrayListFavorite.size() != 0) {
                String textLessonFavorite = arrayListToString(textArrayListFavorite);
                lesson.setTextFavorite(textLessonFavorite);
            }
        }


    }

    public String deleteElementInString(String allText, String elementForDelete) {

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
                if (count == 5) {
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
            if (!allTextArray[j].equalsIgnoreCase(elementForDelete)) {
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


    private String arrayListToString(ArrayList<String> array) {

        StringBuilder textLesson = new StringBuilder();
        boolean needPoint;
        for (int i = 0; i < array.size(); i++) {
            textLesson.append(array.get(i));

            if ((array.size() - 1) != i) {
                textLesson.append(".");
            }
        }

        return textLesson.toString();
    }

    public String formTextForWeb(String textSentence) {
        String[] allTextArray = textSentence.split("[=>]");
        int size = allTextArray.length;
        String finalText = "";
        switch (size) {
            case 0:
                finalText = "<b><font color=#FF0000>ERROR</font></b>";
                break;
            case 1:
                    finalText = "<font color=#082779>" + allTextArray[0] + "</font>";
                break;
            case 3:
                finalText = "<font color=#082779>" + allTextArray[0] + "</font>" +
                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>";
                break;
            case 5:
                finalText = "<font color=#082779>" + allTextArray[0] + "</font>" +
                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>" +
                        "<br><font color=#9f3924>" + allTextArray[4] + "</font>";
                break;
            default:
                finalText = "<font color=#082779>" + textSentence + "</font>";
                break;

        }

        return finalText;
    }

    public String formTextForWebBoltFormat(String textSentence) {
        String[] allTextArray = textSentence.split("[=>]");
        int size = allTextArray.length;
        String finalText = "";
        switch (size) {
            case 0:
                finalText = "<b><font color=#FF0000>ERROR</font></b>";
                break;
            case 1:
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>";
                break;
            case 3:
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                        "<br><br><b><font color=#205128>" + allTextArray[2] + "</font></b>";
                break;
            case 5:
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>" +
                        "<br><font color=#9f3924>" + allTextArray[4] + "</font></b>";
                break;
            default:
                finalText = "<b><font color=#082779>" + textSentence + "</font></b>";
                break;

        }

        return finalText;
    }



}
