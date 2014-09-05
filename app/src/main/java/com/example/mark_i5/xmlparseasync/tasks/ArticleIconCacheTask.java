package com.example.mark_i5.xmlparseasync.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by marklloyd on 03/09/2014.
 */
public class ArticleIconCacheTask {

    private static final String LOGTAG = "ArticleIconTask";
    private HashMap<String, Drawable> imageCache;
    private BaseAdapter adapter;
    private static Drawable DEFAULT_ICON = null;

    public ArticleIconCacheTask(){
        this.imageCache = new HashMap<String, Drawable>();
    }

    public Drawable loadImage(CursorAdapter adapter, ImageView view){

        this.adapter = adapter;
        String url = (String) view.getTag();
        if(imageCache.containsKey(url)){
            //Log.d(LOGTAG, "found image in imageCache");
            return imageCache.get(url);
        }else{
            //Log.d(LOGTAG, "did not find image in imageCache");
            new ImageDownloader().execute(url);
            return DEFAULT_ICON;
        }


    }

    private class ImageDownloader extends AsyncTask<String,Void, Drawable > {

        String image_url;

        @Override
        protected Drawable doInBackground(String... strings) {

            image_url = strings[0];
            //image_url = "http://userserve-ak.last.fm/serve/64s/99430551.png";
            Drawable icon = null;
            InputStream inputStream;
            try {
                //Log.d(LOGTAG, "fetching: " + image_url);
                URL url = new URL(image_url);

                inputStream = url.openStream();
                Log.d(LOGTAG,"inputStream: " + inputStream.toString());
                icon = Drawable.createFromStream(inputStream, "src");
                inputStream.close();
            }
            catch ( MalformedURLException e){

                Log.d(LOGTAG, "MalformedURLException: " +e.getMessage() );
                throw new RuntimeException();

            }catch (IOException e){

                Log.d(LOGTAG, "IOException: " +e.getMessage());
                throw new RuntimeException();
            }




            return icon;
        }


        @Override
        protected void onPostExecute(Drawable downloadedDrawable){
            super.onPostExecute(downloadedDrawable);
            if (downloadedDrawable ==null){
                Log.d(LOGTAG, "the result in onPostExecute is null");
            }
            //Log.d(LOGTAG, downloadedDrawable.toString());
            Log.d(LOGTAG, "image_url: "+ image_url);
            imageCache.put(image_url, downloadedDrawable);
            synchronized (this){
                imageCache.put(image_url, downloadedDrawable);
            }
            adapter.notifyDataSetChanged();
        }


    }
}
