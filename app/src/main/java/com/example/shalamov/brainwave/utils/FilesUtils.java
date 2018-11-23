package com.example.shalamov.brainwave.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FilesUtils {

    String text = "[\n" +
            "\n" +
            "  {\n" +
            "    \"lesson_name\":\"Lesson1\",\n" +
            "    \"Text\":\"The photo shows a street at night.\",\n" +
            "    \"progress\":\"1\",\n" +
            "    \"label\":\"label_1\",\n" +
            "    \"description2\":\"description2\",\n" +
            "    \"description3\":\"description3\",\n" +
            "    \"description4\":\"description4\",\n" +
            "    \"description1\":\"description1\"},\n" +
            "\n" +
            "  {\n" +
            "    \"lesson_name\":\"Lesson2\",\n" +
            "    \"progress\":\"1\",\n" +
            "    \"Text\":\"The first sentence.\",\n" +
            "    \"label\":\"label_1\",\n" +
            "    \"description2\":\"description2\",\n" +
            "    \"description3\":\"description3\",\n" +
            "    \"description1\":\"description1\",\n" +
            "    \"description4\":\"description4\"}\n" +
            "\n" +
            "]";


    // метод возвращает текст файла в формате String
// в случае неудачи возвращает FileNotFound
    public String readFile() {
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Lessons.txt");
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

            return "FileNotFound";

        }

        return stringBuilder.toString();
    }



    private void createFile() {

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void createFile(String text) {

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Lessons.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void checkFile(){

            File file = new File(Environment.getExternalStorageDirectory(), "Lessons.txt");

            if(!file.exists()){

                createFile();
            }else{

            }


    }



}
