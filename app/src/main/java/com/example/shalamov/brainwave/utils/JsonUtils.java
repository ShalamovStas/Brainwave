package com.example.shalamov.brainwave.utils;

import com.example.shalamov.brainwave.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    private JSONArray jsonArray;
    public JsonUtils(String resource) {
        try {
            jsonArray = new JSONArray(resource);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // метод читает Json и использует LessonsUtils чтобы создать лист уроков
    public void readJsonToLesson () {
        try {
            JSONObject jsonObject = null;

            int numberOfKeysArray = jsonArray.length();
            for (int i = 0; i < numberOfKeysArray; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Global.getLessonsUtils().createLesson(
                        i,                                          //номер урока
                        jsonObject.get("lesson_name").toString(),
                        jsonObject.get("Text").toString(),
                        jsonObject.get("description1").toString(),
                        jsonObject.get("description2").toString(),
                        jsonObject.get("description3").toString(),
                        jsonObject.get("description4").toString(),
                        jsonObject.get("label").toString(),
                        jsonObject.get("progress").toString()
                );

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    public void changeJson (int number, String newText) {
        try {
            JSONObject jsonObject = null;
            jsonObject = jsonArray.getJSONObject(number);
            jsonObject.put("Text", newText);



        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    public void saveFromModelToFile(){

        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            ArrayList lessonArrayList = Global.getLessonsList();

            for (int i = 0; i < lessonArrayList.size(); i++) {
                Lesson lesson = (Lesson) lessonArrayList.get(i);
                jsonObject = new JSONObject();
                        jsonObject.put("number", lesson.getNumber());
                        jsonObject.put("lesson_name", lesson.getName());
                        jsonObject.put("Text", lesson.getText());
                        jsonObject.put("description1", lesson.getDescription1());
                        jsonObject.put("description2", lesson.getDescription2());
                        jsonObject.put("description3", lesson.getDescription3());
                        jsonObject.put("description4", lesson.getDescription4());
                        jsonObject.put("label", lesson.getLabel());
                        jsonObject.put("progress", lesson.getProgress());

                jsonArray.put(jsonObject);
            }

            String result = jsonArray.toString();
            Global.getFilesUtils().createFile(result);
        }catch (JSONException e){

        }


    }

}
