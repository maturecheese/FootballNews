package com.example.mark_i5.xmlparseasync.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.data.Article;
import com.example.mark_i5.xmlparseasync.data.ArticleDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by mark-i5 on 26/08/2014.
 */
public class SaveImageTask {
    Context context;
    private static final String LOGTAG = "SaveImageTask";
    private ArticleDatabase database;
    private ResultsCallback callback;


    public SaveImageTask(Context context, ArticleDatabase articleDatabase, ResultsCallback callback) {
        this.context = context;
        this.database = articleDatabase;
        this.callback = callback;
    }


    public void saveImage(String filename, String url, String row_id){
        String[] params = new String[3];
        params[0]= filename;
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
            String filePath ="";
            try {
                // this is the file you want to download from the remote server

                String fileName = strings[0];
                String url = strings[1];
                String row_id = strings[2];

                URL u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();


                File file = new File(context.getFilesDir(), fileName);
               // File outputFile = new File(file, fileName);
                //filePath = outputFile.getPath()+fileName;

                FileOutputStream f = new FileOutputStream(file);
                InputStream in = c.getInputStream();
                Log.v("log_tag", " InputStream consist of : " + in.read());
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    //System.out.println("Reading byte : "+in.read(buffer));
                    f.write(buffer, 0, len1);
                }
                //Toast.makeText(this, "Download Completed Successfully @ " + PATH, Toast.LENGTH_LONG).show();
                f.close();
                Log.d(LOGTAG, filePath);
               // file.getAbsolutePath();
                database.updateRowById(Integer.parseInt(row_id), new String[] {ArticleDatabase.ArticleDatabaseHelper.IMAGE_LOCAL_PATH},
                        new String[] {file.getAbsolutePath()});

                return file.getAbsolutePath();


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Toast.makeText(this, "MalformedURLException", Toast.LENGTH_LONG).show();
            } catch (ProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // Toast.makeText(this, "ProtocolException", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG).show();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // Toast.makeText(this, "UnknownHostException", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
            }
            return filePath;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }



}

