package com.example.mark_i5.xmlparseasync.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.data.ArticleDatabase;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by mark-i5 on 26/08/2014.
 */
public class SaveImageTask2 {
    Context context;
    private static final String LOGTAG = "SaveImageTask";
    private ArticleDatabase database;
    private ResultsCallback callback;


    public SaveImageTask2(Context context, ArticleDatabase articleDatabase, ResultsCallback callback) {
        this.context = context;
        this.database = articleDatabase;
        this.callback = callback;
    }


    public void saveImage(String filename, String url, String row_id) {
        String[] params = new String[3];
        params[0] = filename;
        params[1] = url;
        params[2] = row_id;
        new DownloadImage().execute(params);
    }


    private class DownloadImage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String filePath) {
            super.onPostExecute(filePath);
            callback.onImageSaved(filePath);
            //Toast.makeText(context,"saved image to: "+filePath, Toast.LENGTH_LONG);
        }


        @Override
        protected String doInBackground(String... strings) {


            String fileName = strings[0];
            String urlString = strings[1];
            String row_id = strings[2];

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file= new File(path, "DemoPicture.jpg");
            try {
                // Make sure the Pictures directory exists.
                path.mkdirs();

                URL url = new URL(urlString);
            /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();


                InputStream inputStream = ucon.getInputStream();

                OutputStream os = new FileOutputStream(file);
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                os.write(data);
                inputStream.close();
                os.close();

            } catch (IOException e) {
                Log.d("ImageManager", "Error: " + e);
            }
            return file.getAbsolutePath();


        }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}


}

