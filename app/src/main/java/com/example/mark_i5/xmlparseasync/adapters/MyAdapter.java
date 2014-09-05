package com.example.mark_i5.xmlparseasync.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark_i5.xmlparseasync.R;
import com.example.mark_i5.xmlparseasync.data.Article;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconTask;
import com.example.mark_i5.xmlparseasync.tasks.SaveImageTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mark-i5 on 25/08/2014.
 */
public class MyAdapter extends BaseAdapter {

    private static String LOGTAG ="MyAdapter";
    ArrayList<Article> dataSource;
    Context context;
    LayoutInflater layoutInflater;
    ArticleIconTask articleIconTask;
    SaveImageTask saveImageTask;


    public MyAdapter(Context context, ArrayList<Article> dataSource, ArticleIconTask articleIconTask){
        this.dataSource = dataSource;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.articleIconTask = articleIconTask;
       // this.saveImageTask = new SaveImageTask(context);
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position  ) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        Log.d(LOGTAG, "getting row at position "+ i);
        View row = convertView;
        MyHolder holder = null;
        if (row == null){
            row = layoutInflater.inflate(R.layout.custom_row, parent, false);
            holder = new MyHolder(row);
            row.setTag(holder);
        }else{
            holder = (MyHolder) row.getTag();
        }
        Article currentItem = dataSource.get(i);
        holder.articleTitleText.setText(currentItem.getTitle());
        holder.articleDescriptionText.setText(currentItem.getDescription());
        holder.articlePubDate.setText(currentItem.getPublished());


        String url = currentItem.getImageUrl();
        holder.articleImage.setTag(url);

        String[] splits = url.split("/");
        String fileName = splits[splits.length - 1];
        Log.d(LOGTAG, "fileName: " + fileName + "\turl: " + url);
       // saveImageTask.saveImage(fileName, url);


        Drawable icon = articleIconTask.loadImage(this, holder.articleImage);

        if (icon != null){
            holder.articleImage.setImageDrawable(icon);
            Log.d(LOGTAG, "icon is NOT null" );
        }else{
            holder.articleImage.setImageResource(R.drawable.ic_launcher);
            Log.d(LOGTAG, "icon is null" );
        }




        /*holder.articleTitleText.setText("title");
        holder.articleDescriptionText.setText("description");
        holder.articlePubDate.setText("pubDate");*/
        //holder.articleImage.setImageURI(Uri.parse( currentItem.get("imageUrl")));


        return row;
    }

    static class MyHolder{
        TextView articleTitleText, articleDescriptionText, articlePubDate;
        ImageView articleImage;

        public MyHolder (View view){
            this.articleTitleText = (TextView) view.findViewById(R.id.item_title);
            this.articleDescriptionText = (TextView) view.findViewById(R.id.item_description);
            this.articlePubDate = (TextView) view.findViewById(R.id.item_pubdate);
            this.articleImage = (ImageView) view.findViewById(R.id.item_image);
        }

    }
}
