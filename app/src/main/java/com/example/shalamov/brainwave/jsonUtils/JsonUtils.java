package com.example.shalamov.brainwave.jsonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shalamov.brainwave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shala on 16.06.2018.
 */

public class JsonUtils {
    private Context mContext;
    private ArrayList lessonsList;


    private Integer[] mThumbIds = {
            R.drawable.ic_school,
            R.drawable.ic_apple_books,
            R.drawable.ic_apple_books_color,
            R.drawable.ic_australia_flag,
            R.drawable.ic_face,
            R.drawable.ic_message,
            R.drawable.ic_music,
            R.drawable.ic_ok,
            R.drawable.ic_science,
            R.drawable.ic_school_2,
            R.drawable.ic_school_3,
            R.drawable.ic_think,
            R.drawable.ic_ted,

    };

    public JsonUtils(Context c, ArrayList lessonsList) {
        this.mContext = c;
        this.lessonsList = lessonsList;
    }

    public JsonUtils(Context c) {
        this.mContext = c;
    }


    public String getTextFromJsonFile() {
        AssetManager assetManager = mContext.getAssets();
        StringBuilder sb = null;
        try (InputStreamReader istream = new InputStreamReader(assetManager.open("lessons.json"));
             BufferedReader br = new BufferedReader(istream)) {
            sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String readJsonString(String resource) {

        try {
            JSONObject jsonObject = null;
            JSONArray jsonArray = new JSONArray(resource);
            int numberOfKeysArray = jsonArray.length();

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public void readJsonStringToArrayList(String resource) {
        try {

            lessonsList.clear();


            JSONObject jsonObject = null;
            JSONArray jsonArray = new JSONArray(resource);
            int numberOfKeysArray = jsonArray.length();
            for (int i = 0; i < numberOfKeysArray; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Lesson lesson = new Lesson(
                        jsonObject.get("lesson_name").toString(),
                        jsonObject.get("Text").toString(),
                        jsonObject.get("description1").toString(),
                        jsonObject.get("description2").toString(),
                        jsonObject.get("description3").toString(),
                        jsonObject.get("description4").toString(),
                        jsonObject.get("label").toString(),
                        jsonObject.get("progress").toString());

                lessonsList.add(lesson);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }


    public void updateLabel(String labelForLesson, ImageView mLabelLesson) {

        switch (labelForLesson) {
            case "label_1":
                mLabelLesson.setImageResource(mThumbIds[0]);
                break;
            case "label_2":
                mLabelLesson.setImageResource(mThumbIds[1]);
                break;
            case "label_3":
                mLabelLesson.setImageResource(mThumbIds[2]);
                break;
            case "label_4":
                mLabelLesson.setImageResource(mThumbIds[3]);
                break;
            case "label_5":
                mLabelLesson.setImageResource(mThumbIds[4]);
                break;
            case "label_6":
                mLabelLesson.setImageResource(mThumbIds[5]);
                break;
            case "label_7":
                mLabelLesson.setImageResource(mThumbIds[6]);
                break;
            case "label_8":
                mLabelLesson.setImageResource(mThumbIds[7]);
                break;
            case "label_9":
                mLabelLesson.setImageResource(mThumbIds[8]);
                break;
            case "label_10":
                mLabelLesson.setImageResource(mThumbIds[9]);
                break;
            case "label_11":
                mLabelLesson.setImageResource(mThumbIds[10]);
                break;
            case "label_12":
                mLabelLesson.setImageResource(mThumbIds[11]);
                break;
            case "label_13":
                mLabelLesson.setImageResource(mThumbIds[12]);
                break;
            case "nothing":
                mLabelLesson.setImageResource(mThumbIds[1]);
                break;


        }

    }

    public String readFile() {
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Lessons.txt");
            fin = new FileInputStream(file);
            inputStream = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fin.close();
            inputStream.close();

        } catch (java.io.IOException e) {
            Toast.makeText(mContext, "FileNotFound in external directory", Toast.LENGTH_SHORT).show();
            return this.getTextFromJsonFile();
        }

        return stringBuilder.toString();
    }


    public void saveFile() {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String text = this.getTextFromJsonFile();
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(mContext, "Data saved", Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            Toast.makeText(mContext, "Unsuccessfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveSettings(String theme) {
        try {
            JSONObject settingsJson = new JSONObject();

            settingsJson.put("theme", theme);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Settings.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String text = settingsJson.toString();
            fos.write(text.getBytes());
            fos.close();

        } catch (JSONException e1) {
            e1.printStackTrace();
            Toast.makeText(mContext, "Unsuccessfully JSON", Toast.LENGTH_SHORT).show();
        }catch (java.io.IOException e) {
            Toast.makeText(mContext, "Unsuccessfully IO", Toast.LENGTH_SHORT).show();
        }
    }

    public String readSettings(){
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        String theme = "no settings";
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Settings.txt");
            fin = new FileInputStream(file);
            inputStream = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fin.close();
            inputStream.close();

            JSONObject settingsJson = new JSONObject(stringBuilder.toString());
            theme = settingsJson.get("theme").toString();

        }catch (JSONException e1) {
            e1.printStackTrace();
            return theme;
        } catch (java.io.IOException e) {
            return theme;
        }


        return theme;
    }

    public void insertTextIntoJson(String lesson_name, String text, String label) {

        String textJson = this.readFile();

        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray(textJson);
            int numberOfKeysArray = jsonArray.length();
            jsonObject.put("lesson_name", lesson_name);
            jsonObject.put("label", label);
            jsonObject.put("progress", "1");
            jsonObject.put("description1", "description1");
            jsonObject.put("description2", "description2");
            jsonObject.put("description3", "description3");
            jsonObject.put("description4", "description4");
            jsonObject.put("Text", text);
            jsonArray.put(jsonObject);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String result = jsonArray.toString();
            fos.write(result.getBytes());
            fos.close();
            Toast.makeText(mContext, "Lesson saved", Toast.LENGTH_SHORT).show();
        } catch (JSONException e1) {
            e1.printStackTrace();
            Toast.makeText(mContext, "Unsuccessfully JSON", Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Unsuccessfully", Toast.LENGTH_SHORT).show();
        }


    }


    public void deleteLesson(String name) {
        String jsontext = readFile();
        String newText = "";
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(name);
        try {

            JSONArray jsonArray = new JSONArray(jsontext);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String lessonName = jsonObject.get("lesson_name").toString();

                if (lessonName.equalsIgnoreCase(name)) {
                    jsonArray.remove(i);

                }
                stringBuilder1.delete(0, stringBuilder1.length());
            }
            newText = jsonArray.toString();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(newText.getBytes());
            fos.close();

        } catch (java.io.IOException e) {
            Toast.makeText(mContext, "Unsuccessful update", Toast.LENGTH_SHORT).show();
        }

    }

    public String getAllTextForLesson(String jsonArrayText, String nameLesson) {
        String out = "";
        try {

            JSONArray jsonArray = new JSONArray(jsonArrayText);
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String lessonName = jsonObject.get("lesson_name").toString();

                if (lessonName.equalsIgnoreCase(nameLesson)) {
                    out = jsonObject.get("Text").toString();
                }

            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return out;
    }

    public void updateSentence(String jsonArrayText, String nameLesson, String Sentences) {

        try {

            JSONArray jsonArray = new JSONArray(jsonArrayText);
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String lessonName = jsonObject.get("lesson_name").toString();

                if (lessonName.equalsIgnoreCase(nameLesson)) {
                    jsonObject.put("Text", Sentences);

                }

            }

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String result = jsonArray.toString();
            fos.write(result.getBytes());
            fos.close();


        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (java.io.IOException e) {
            Toast.makeText(mContext, "Unsuccessful update", Toast.LENGTH_SHORT).show();
        }
    }


}
