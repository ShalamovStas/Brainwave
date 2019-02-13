package com.example.shalamov.brainwave.utils;

import android.util.Log;

import com.example.shalamov.brainwave.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
String TAG = "JsonUtils";



    // метод читает Json и использует LessonsUtils чтобы создать лист уроков
    public void readJsonToLessonMode1(String text) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = null;

            int numberOfKeysArray = jsonArray.length();

            for (int i = 0; i < numberOfKeysArray; i++) {


                jsonObject = jsonArray.getJSONObject(i);

                Global.getLessonsUtils().createLesson(
                        i,                                          //номер урока
                        jsonObject.get("lesson_name").toString(),
                        jsonObject.get("Text").toString(),
                        jsonObject.get("TextFavorite").toString(),
                        jsonObject.get("description1").toString(),
                        jsonObject.get("words").toString(),
                        jsonObject.get("label").toString(),
                        jsonObject.get("progress").toString()
                );

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public void readJsonToLessonMode2(String text) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = null;

            int numberOfKeysArray = jsonArray.length();

            for (int i = 0; i < numberOfKeysArray; i++) {


                jsonObject = jsonArray.getJSONObject(i);

                Global.getLessonsUtils().createLessonMode2(
                        i,                                          //номер урока
                        jsonObject.get("lesson_name").toString(),
                        jsonObject.get("words").toString(),
                        jsonObject.get("label").toString());

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    public void saveFromModelToFile() {

        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            ArrayList lessonArrayList = Global.getLessonsList();

            for (int i = 0; i < lessonArrayList.size(); i++) {
                LessonModel lesson = (LessonModel) lessonArrayList.get(i);
                if (lesson.getTextFavorite() == null) {
                    lesson.setTextFavorite("");
                }
                jsonObject = new JSONObject();
                jsonObject.put("number", lesson.getNumber());
                jsonObject.put("lesson_name", lesson.getName());
                jsonObject.put("Text", lesson.getText());
                jsonObject.put("TextFavorite", lesson.getTextFavorite());
                jsonObject.put("description1", lesson.getDescription1());
                jsonObject.put("words", lesson.getWords());

                jsonObject.put("label", lesson.getLabel());
                jsonObject.put("progress", lesson.getProgress());

                jsonArray.put(jsonObject);
            }

            String result = jsonArray.toString();
            Global.getFilesUtils().createFile(result);
        } catch (JSONException e) {

        }


    }

    public void saveFromModelToFileMode2() {

        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            ArrayList lessonArrayList = Global.getLessonsListForMode2();

            for (int i = 0; i < lessonArrayList.size(); i++) {
                WordModel lesson = (WordModel) lessonArrayList.get(i);

                jsonObject = new JSONObject();
                jsonObject.put("number", lesson.getNumber());
                jsonObject.put("lesson_name", lesson.getName());
                jsonObject.put("words", lesson.getWords());
                jsonObject.put("label", lesson.getLabel());


                jsonArray.put(jsonObject);
            }

            String result = jsonArray.toString();
            Global.getFilesUtils().createFileMode2(result);
        } catch (JSONException e) {

        }
    }

    public void exportToFile(int index) {

        try {
            JSONObject jsonObject;
            WordModel lesson = Global.getLessonsListForMode2().get(index);

                jsonObject = new JSONObject();
                jsonObject.put("number", lesson.getNumber());
                jsonObject.put("lesson_name", lesson.getName());
                jsonObject.put("words", lesson.getWords());
                jsonObject.put("label", lesson.getLabel());

            String result = jsonObject.toString();
            Global.getFilesUtils().exportFile(result);


        } catch (JSONException e) {

        }
    }

    public void importMode2(String text) {

        try {
            JSONObject jsonObject = new JSONObject(text);

                Global.getLessonsUtils().createLessonMode2(
                        Global.getLessonsListForMode2().size(),//номер урока
                        jsonObject.get("lesson_name").toString(),
                        jsonObject.get("words").toString(),
                        jsonObject.get("label").toString());


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


}
