package com.example.shalamov.brainwave.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.shalamov.brainwave.Global;
import com.example.shalamov.brainwave.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilesUtils {

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

    public String readFileForMode2() {
        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Words.txt");
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
            return "";
        }
        return stringBuilder.toString();
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

    public void createFileMode2(String text) {

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Words.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exportFile(String text) {

        try {
//            File path = new File(Environment.getExternalStorageDirectory() + "/EnglishExport");
//            path.mkdirs();
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EnglishExport", "/EnglishWordsList.txt");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "EnglishWordsList.txt");

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String importFile() {

        FileInputStream fin = null;
        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "EnglishWordsList.txt");
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

            file.delete();

        } catch (java.io.IOException e) {
            return "";
        }
        return stringBuilder.toString();
    }





}
