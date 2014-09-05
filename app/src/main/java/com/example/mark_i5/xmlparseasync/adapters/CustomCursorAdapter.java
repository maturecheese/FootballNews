package com.example.mark_i5.xmlparseasync.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark_i5.xmlparseasync.R;
import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.data.Article;
import com.example.mark_i5.xmlparseasync.data.ArticleDatabase;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconCacheTask;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconTask;
import com.example.mark_i5.xmlparseasync.tasks.SaveImageTask;
import com.example.mark_i5.xmlparseasync.tasks.SaveImageTask2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.util.zip.Inflater;

import javax.xml.transform.Result;

/**
 * Created by mark-i5 on 28/08/2014.
 */
public class CustomCursorAdapter extends CursorAdapter{
    private static final String LOGTAG ="CustomCursorAdapter";
    private LayoutInflater inflater;

    //private SaveImageTask2 saveImageTask;

    private ArticleIconCacheTask cacheImageTask;

    private final int titleIndex;
    private final int descIndex;
    private final int pubDateIndex;
    private final int imageUrlIndex;
    //private final int imageLocalPathIndex;
    //private final int rowIdIndex ;
    //private final int createdAtIndex;


    ArticleDatabase database;
   // private ResultsCallback callback;


    public CustomCursorAdapter(ArticleDatabase database, ResultsCallback callback,
                               Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.database = database;

        this.cacheImageTask = new ArticleIconCacheTask();
        //this.saveImageTask = new SaveImageTask2(context, database,callback);

        this.titleIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.TITLE);
        this.descIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.DESCRIPTION);
        this.pubDateIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.PUBLISHED_AT);
        this.imageUrlIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.IMAGE_URL);
       // this.imageLocalPathIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.IMAGE_LOCAL_PATH);
       // this.rowIdIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.TID);
       // this.createdAtIndex = cursor.getColumnIndex(ArticleDatabase.ArticleDatabaseHelper.CREATED_AT_LONG);

;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.custom_row, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        String url = cursor.getString(imageUrlIndex);

        if(holder == null){
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) view.findViewById(R.id.item_title);
            holder.textViewDesc = (TextView) view.findViewById(R.id.item_description);
            holder.textViewPubDate = (TextView) view.findViewById(R.id.item_pubdate);
            holder.imageViewCaption = (ImageView) view.findViewById(R.id.item_image);
            view.setTag(holder);
        }
        holder.textViewTitle.setText(cursor.getString(titleIndex));
        holder.textViewDesc.setText(cursor.getString(descIndex));
        holder.textViewPubDate.setText(cursor.getString(pubDateIndex));
        holder.imageViewCaption.setTag(url);

        Drawable icon = cacheImageTask.loadImage(this, holder.imageViewCaption);
        holder.imageViewCaption.setImageDrawable(icon);



       /* if (cursor.isNull(imageLocalPathIndex)){
            String url = cursor.getString(imageUrlIndex);
            String splits[] = url.split("/");
            String fileName = splits[splits.length -1];
            String row_id = cursor.getString(rowIdIndex);
            Log.d(LOGTAG, "filename: " + fileName + " \turl: " + url);
           // saveImageTask.saveImage(fileName, url, row_id);
        }
        else{

            Log.d(LOGTAG, "trying to load image");

            String path = cursor.getString(imageLocalPathIndex);


            Log.d(LOGTAG, "the local path of image is: " + path);

            Uri uri = Uri.parse(path);
            Drawable caption = Drawable.createFromPath(uri.getPath());
            if(caption == null){
                Log.d(LOGTAG, "ARRRGGGGG");
            }


*//*

            try {
                FileInputStream fileIS = new FileInputStream(path);
                if (fileIS == null ){
                    Log.d(LOGTAG, "fileIS is null aswell!!! WTF");
                }
                Drawable caption = Drawable.createFromStream(fileIS, "src");
                if(caption == null){
                    Log.d(LOGTAG, "caption is null seriously WTTTFF");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
*//*

           // Drawable caption = Drawable.createFromPath(path);

           // holder.imageViewCaption.setImageDrawable(caption);

            //Bitmap bMap = BitmapFactory.decodeFile(cursor.getString(imageLocalPathIndex));
            //Log.d(LOGTAG, bMap.toString());
           // holder.imageViewCaption.setImageBitmap(bMap);

        }*/



    }

    static class ViewHolder{

        TextView textViewTitle;
        TextView textViewDesc;
        TextView textViewPubDate;
        ImageView imageViewCaption;
    }

    //BitmapFactory.decodeStream(new FlushedInputStream(is), null, opts);

    public class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int myByte = read();
                    if (myByte < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
