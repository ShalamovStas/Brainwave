package com.example.shalamov.brainwave.utils;

import android.util.Log;

import com.example.shalamov.brainwave.Global;

import java.util.ArrayList;

public class LessonsUtils {

    final String TAG = "LessonUtils";
    //==============================================================================================
    //==============================================================================================
    // Работа с объектами модели - создание объекта LessonModel, его изменение,
    // удаление

    public void createLesson(int number, String name, String text, String textFavorite, String description1, String words, String label, String progress) {

        Log.d(TAG, "\n======================createLesson start======================");


        //создаем коллекцию, элементы которой являются отдельные предложения
        ArrayList<String> textArrayList = new ArrayList<>();
        ArrayList<String> textArrayListFavorite = new ArrayList<>();
        ArrayList<String> textArrayWords = new ArrayList<>();
        //используем метод разделения общего текста text на отдельные предложения и записываем
        //отдельные предложения в коллекцию - метод splitSentence это позволяет делать
        splitSentence(text, textArrayList);
        splitSentence(textFavorite, textArrayListFavorite);
        splitSentence(words, textArrayWords);
        //из разделенных предложений с удаленными абзацами  и отступами формируется строка
        //textLesson, которая бутед записана в модель LessonModel
        String textLesson = arrayListToString(textArrayList);


        LessonModel lesson = new LessonModel();
        lesson.setNumber(number);
        lesson.setName(name);
        lesson.setText(textLesson);
        lesson.setArrayListText(textArrayList);
        lesson.setTextFavorite(textFavorite);
        lesson.setArrayListTextFavorite(textArrayListFavorite);
        lesson.setLabel(label);
        lesson.setProgress(progress);
        lesson.setDescription1(description1);
        lesson.setWords(words);
        lesson.setArrayListWords(textArrayWords);


        Global.getLessonsList().add(lesson);


    }

    public void createLessonMode2(int number, String name, String words, String label) {

        Log.d(TAG, "\n======================createLessonMode2 start======================");

        ArrayList<String> textArrayWords = new ArrayList<>();
        //используем метод разделения общего текста text на отдельные предложения и записываем
        //отдельные предложения в коллекцию - метод splitSentence это позволяет делать
        splitSentence(words, textArrayWords);


        WordModel lesson = new WordModel();
        lesson.setNumber(number);
        lesson.setName(name);
        lesson.setLabel(label);
        lesson.setWords(words);
        lesson.setArrayListWords(textArrayWords);

        Log.d(TAG, "number = " + number);

        Global.getLessonsListForMode2().add(lesson);

    }


    public void changeLesson(int number, String name, String text, String description1, String words, String label, String progress) {

        LessonModel lesson = (LessonModel) Global.getLessonsList().get(number);

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
        lesson.setWords(lesson.getWords());

    }

    public void deleteLesson(int index) {
        if(Global.mode == 1) {
            Global.getLessonsList().remove(index);
            Global.getJsonUtils().saveFromModelToFile();
        }
        if(Global.mode == 2) {
            Global.getLessonsListForMode2().remove(index);
            Global.getJsonUtils().saveFromModelToFileMode2();
        }
    }

    //==============================================================================================
    //==============================================================================================
    // Работа с предложениями - изменение предложений, удаление предложений

    public void changeSentence(LessonModel lesson, String oldSentence, String newSentence) {

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

    public void deleteSentence(LessonModel lesson, String sentence) {

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


    //==============================================================================================
    //==============================================================================================
    //Методы для работы с Отмеченными предложениями
    public void deleteFavoriteSentence(LessonModel lesson, String text) {

        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        if (textArrayListFavorite.size() != 0) {

            for (int i = 0; i < textArrayListFavorite.size(); i++) {

                if (textArrayListFavorite.get(i).equalsIgnoreCase(text)) {
                    textArrayListFavorite.remove(i);
                }
            }
            // создание поля textFavorite для модели с учтом скорректированого предложения
            if (textArrayListFavorite.size() != 0) {
                String textLessonFavorite = arrayListToString(textArrayListFavorite);
                lesson.setTextFavorite(textLessonFavorite);
            }
        }


    }

    public void addFavoriteSentence(LessonModel lesson, String text) {
        Log.d(TAG, "=========addFavoriteSentence()=========");
        // изменяем отмеченый текст если он есть
        ArrayList<String> textArrayListFavorite = lesson.getArrayListTextFavorite();

        textArrayListFavorite.add(text);

        // создание поля textFavorite для модели с учтом скорректированого предложения
        String textLessonFavorite = arrayListToString(textArrayListFavorite);
        lesson.setTextFavorite(textLessonFavorite);
        Log.d(TAG, "textLessonFavorite = " + textLessonFavorite);
    }

    public boolean checkIfExistThisSentenceInFavorite(LessonModel lesson, String text){
        boolean flag = false;
        ArrayList favoriteSentencesArray = lesson.getArrayListTextFavorite();

        if(favoriteSentencesArray.size() != 0){

            for (int i = 0; i < favoriteSentencesArray.size(); i++) {
                if(favoriteSentencesArray.get(i).toString().equalsIgnoreCase(text)){
                    flag = true;
                }
            }
        }

        return flag;
    }
    //==============================================================================================
    //==============================================================================================
    //Методы для работы со словами для изучения

    public boolean addWord(LessonModel lesson, String text) {
        Log.d(TAG, "=========addWord()=========");
        // изменяем отмеченый текст если он есть
        ArrayList<String> words = lesson.getArrayListWords();

        // флаг поиска совпадения
        //если  такое слово уже существует, операция не завершается и возвращает false
        boolean flag = true;

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equalsIgnoreCase(text)) {
                flag = false;
            }
        }

        if (flag) {
            words.add(text);

            // создание поля words для модели с учтом скорректированого
            String wordsString = arrayListToString(words);
            lesson.setWords(wordsString);
            Log.d(TAG, "words = " + wordsString);
        }
        return flag;
    }

    public boolean addWord(WordModel lesson, String text) {
        Log.d(TAG, "=========addWord()=========");
        // изменяем отмеченый текст если он есть
        ArrayList<String> words = lesson.getArrayListWords();

        // флаг поиска совпадения
        //если  такое слово уже существует, операция не завершается и возвращает false
        boolean flag = true;

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equalsIgnoreCase(text)) {
                flag = false;
            }
        }

        if (flag) {
            words.add(text);

            // создание поля words для модели с учтом скорректированого
            String wordsString = arrayListToString(words);
            lesson.setWords(wordsString);
            Log.d(TAG, "words = " + wordsString);
        }
        return flag;
    }

    public void deleteWord(LessonModel lesson, String text) {

        ArrayList<String> words = lesson.getArrayListWords();

        if (words.size() != 0) {

            for (int i = 0; i < words.size(); i++) {

                if (words.get(i).equalsIgnoreCase(text)) {
                    words.remove(i);
                }
            }
            String wordsString = arrayListToString(words);
            lesson.setWords(wordsString);
        }
    }

    public void deleteWord(WordModel lesson, String text) {

        ArrayList<String> words = lesson.getArrayListWords();

        if (words.size() != 0) {

            for (int i = 0; i < words.size(); i++) {

                if (words.get(i).equalsIgnoreCase(text)) {
                    words.remove(i);
                }
            }
            String wordsString = arrayListToString(words);
            lesson.setWords(wordsString);
        }
    }

    public void changeWord(LessonModel lesson, String oldText, String newText) {


        ArrayList<String> words = lesson.getArrayListWords();


        if (words.size() != 0) {

            for (int i = 0; i < words.size(); i++) {

                if (words.get(i).equalsIgnoreCase(oldText)) {
                    words.set(i, newText);
                }
            }

            String wordsString = arrayListToString(words);

            lesson.setWords(wordsString);
            Log.d(TAG, wordsString);
        }
    }

    public void changeWord(WordModel lesson, String oldText, String newText) {


        ArrayList<String> words = lesson.getArrayListWords();


        if (words.size() != 0) {

            for (int i = 0; i < words.size(); i++) {

                if (words.get(i).equalsIgnoreCase(oldText)) {
                    words.set(i, newText);
                }
            }

            String wordsString = arrayListToString(words);

            lesson.setWords(wordsString);
            Log.d(TAG, wordsString);
        }
    }


    public void updateWordLearnNotLearn(LessonModel lesson, int index, String flag) {

        String[] splitArray = lesson.getArrayListWords().get(index).split("[=>]");

        if (splitArray.length >= 7) {
            splitArray[6] = flag;
        }

        StringBuilder result = new StringBuilder();


        for (int i = 0; i < splitArray.length; i++) {
            result.append(splitArray[i]);
            if (splitArray[i].length() == 0) {
                result.append("=>");
            }
        }

        if (splitArray.length <= 3) {
            result.append("=>-");
        }

        if (splitArray.length < 7) {
            result.append("=>");
            result.append(flag);
        }

        Log.d(TAG, "\n\nupdateWordLearnNotLearn - end\nresult.toString() = " + result.toString());


        changeWord(lesson, lesson.getArrayListWords().get(index), result.toString());


    }

    public void updateWordLearnNotLearn(WordModel lesson, int index, String flag) {

        String[] splitArray = lesson.getArrayListWords().get(index).split("[=>]");

        if (splitArray.length >= 7) {
            splitArray[6] = flag;
        }

        StringBuilder result = new StringBuilder();


        for (int i = 0; i < splitArray.length; i++) {
            result.append(splitArray[i]);
            if (splitArray[i].length() == 0) {
                result.append("=>");
            }
        }

        if (splitArray.length <= 3) {
            result.append("=>-");
        }

        if (splitArray.length < 7) {
            result.append("=>");
            result.append(flag);
        }

        Log.d(TAG, "\n\nupdateWordLearnNotLearn - end\nresult.toString() = " + result.toString());


        changeWord(lesson, lesson.getArrayListWords().get(index), result.toString());


    }

    public boolean checkFlagLearnNotLearn(LessonModel lesson, int index) {
        boolean flag = true;
        String[] splitArray = lesson.getArrayListWords().get(index).split("[=>]");

        if (splitArray.length == 7) {

            switch (splitArray[6]) {
                case "0":
                    flag = false;
                    break;
                case "1":
                    flag = true;
                    break;
                default:
                    flag = true;
                    break;
            }
        }

        return flag;

    }

    public boolean checkFlagLearnNotLearn(WordModel lesson, int index) {
        boolean flag = true;
        String[] splitArray = lesson.getArrayListWords().get(index).split("[=>]");

        if (splitArray.length == 7) {

            switch (splitArray[6]) {
                case "0":
                    flag = false;
                    break;
                case "1":
                    flag = true;
                    break;
                default:
                    flag = true;
                    break;
            }
        }

        return flag;

    }

    //==============================================================================================
    //==============================================================================================
    //Вспомогательные методы - разделения текста, заполнения коллекций,
    //формирования текста с html атрибутами и тд
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

    public String[] formTextForWordList(String textSentence) {
        String[] allTextArray = textSentence.split("[=>]");
        int size = allTextArray.length;
        String finalText = "";
        int flagLearnNotLearn = 1;
        switch (size) {
            case 0:
                finalText = "<b><font color=#FF0000>ERROR</font></b>";
                break;
            case 1:
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>";
                break;
            case 3:
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                        "<br><font color=#205128>" + allTextArray[2] + "</font>";
                break;
            case 5:
//                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
//                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>" +
//                        "<br><i><u><font color=#9f3924 size=100>" + allTextArray[4] + "</font></u></i>";

                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                        "<br><font color=#205128>" + allTextArray[2] + "</font>" +
                        "<br><small>Пример: </small><i><small><font color=#9f3924 size=100>" + allTextArray[4] + "</font></small></i>" +
                        "<br>";
                break;

            case 7:
//                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
//                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>" +
//                        "<br><i><u><font color=#9f3924 size=100>" + allTextArray[4] + "</font></u></i>";

                if (allTextArray[4].equalsIgnoreCase("-")) {
                    finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                            "<br><font color=#205128>" + allTextArray[2] + "</font>";
                } else {
                    finalText = "<b><font color=#082779>" + allTextArray[0] + "</font></b>" +
                            "<br><font color=#205128>" + allTextArray[2] + "</font>" +
                            "<br><small>Пример: </small><i><small><font color=#9f3924 size=100>" + allTextArray[4] + "</font></small></i>" +
                            "<br>";
                }
                break;
            default:
                finalText = "<b><font color=#082779>" + textSentence + "</font></b>";
                break;

        }

        if (size >= 7) {

            switch (allTextArray[6]) {
                case "0":
                    flagLearnNotLearn = 0;
                    break;
                case "1":
                    flagLearnNotLearn = 1;
                    break;
                default:
                    flagLearnNotLearn = 1;
                    break;
            }

        } else {
            flagLearnNotLearn = 1;
        }

        String[] answer = {finalText, Integer.toString(flagLearnNotLearn)};
        Log.d(TAG, "formTextForWordList ==== end===\nanswer[0] = " + answer[0] + "\nanswer[1] = " + answer[1]);
        Log.d(TAG, "=\n\n==============================================================================\n\n");

        return answer;
    }

    public String cleanText(String textSentence) {
        String[] allTextArray = textSentence.split("[=>]");

        return allTextArray[0];
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
                finalText = "<b><font color=#082779>" + allTextArray[0] + "</font>" +
                        "<br><br><font color=#205128>" + allTextArray[2] + "</font>" +
                        "<br><br><font color=#9f3924>" + allTextArray[4] + "</font></b>";
                break;
            default:
                finalText = "<b><font color=#082779>" + textSentence + "</font></b>";
                break;

        }

        return finalText;
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
    //==============================================================================================
    //==============================================================================================

}
