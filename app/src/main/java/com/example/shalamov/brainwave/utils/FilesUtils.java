package com.example.shalamov.brainwave.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.shalamov.brainwave.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilesUtils {

    String text = "[{\"number\":42,\"lesson_name\":\"Temp\",\"Text\":\"add1.add 2. add 3\",\"TextFavorite\":\"add 2\",\"description1\":\"important\",\"description2\":\"description2\",\"description3\":\"description3\",\"description4\":\"description1\",\"label\":\"label_0\",\"progress\":\"progress 1\"}]";

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

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
            return readFileFromAssets();
        }
        return stringBuilder.toString();
    }

    public String readFileFromAssets() { InputStream inputStream = null;
        InputStreamReader isr = null;
        StringBuilder stringBuilder = null;
        try {

            inputStream = context.getResources().getAssets()
                    .open("Lessons.txt");
            isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
//            fin.close();
            isr.close();

        } catch (java.io.IOException e) {

        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
