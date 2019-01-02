package com.example.shalamov.brainwave;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InfoActivity extends AppCompatActivity {
    private WebView basicWeb;
    String TAG = "InfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        basicWeb = findViewById(R.id.web_info_activity);


//        webView.loadData(readFileFromAssets(),"text/html", "UTF-8");
//        webView.loadUrl("file:///android_asset/doc.html");
//        new MyAsynkTaskDownloadData().execute();


        basicWeb.loadUrl("file:///android_asset/doc.html");



    }

    public String readFileFromAssets() {

        InputStreamReader inputStream = null;
        StringBuilder stringBuilder = null;
        try {

            inputStream = new InputStreamReader(getAssets().open("doc.html"));
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
//            fin.close();
            inputStream.close();

        } catch (java.io.IOException e) {
            return "file not found";

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.i(TAG, "stringBuilder.toString() = \n" + stringBuilder.toString());
        return stringBuilder.toString();
    }


    private class MyAsynkTaskDownloadData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            View mWebViewLayout = getLayoutInflater().inflate(R.layout.webview_layout, null);
            WebView web = mWebViewLayout.findViewById(R.id.web);
            web.loadUrl("file:///android_asset/doc.html");
//            basicLayout.removeAllViews();
        }


    }
}
