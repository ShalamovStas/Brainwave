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

    String text = "[{\"number\":42,\"lesson_name\":\"Temp\",\"Text\":\"add1.add 2. add 3\",\"TextFavorite\":\"add 2\",\"description1\":\"important\",\"description2\":\"description2\",\"description3\":\"description3\",\"description4\":\"description1\",\"label\":\"label_0\",\"progress\":\"progress 1\"}]";


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

            return text;

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
