package com.example.shalamov.brainwave.utils;

import android.util.Log;

import com.example.shalamov.brainwave.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    private JSONArray jsonArray;
    public JsonUtils(String resource) {


        if(resource.equalsIgnoreCase("FileNotFound")){
            createFirstJsonArray();
            Log.d("brain", "JsonUtils-constructor-resource.equalsIgnoreCase(FileNotFound)");
        }else {
            Log.d("brain", "JsonUtils-constructor-resource.equalsIgnoreCase(FileNotFound)-else resource = " + resource);
            try {
//                createFirstJsonArray();
                jsonArray = new JSONArray(resource);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFirstJsonArray() {
        try {
        jsonArray = new JSONArray();

            for (int i = 0; i < 2; i++) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("number", i);
                jsonObject.put("lesson_name", "Introduce " + i);
                jsonObject.put("Text", "This application is very useful " + i);
                jsonObject.put("TextFavorite", "");
                jsonObject.put("description1", "description1");
                jsonObject.put("words", "word=>слово");
                jsonObject.put("label", "label_0");
                jsonObject.put("progress", "");
                jsonArray.put(jsonObject);
            }
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
                Log.d("brain", "fori - i = " + i);

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
                if(lesson.getTextFavorite() == null){
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
        }catch (JSONException e){

        }


    }

}
