package com.example.mark_i5.xmlparseasync.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mark_i5.xmlparseasync.adapters.CustomCursorAdapter;
import com.example.mark_i5.xmlparseasync.data.Article;
import com.example.mark_i5.xmlparseasync.data.ArticleDatabase;
import com.example.mark_i5.xmlparseasync.fragments.PlaceholderFragment;
import com.example.mark_i5.xmlparseasync.R;
import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.adapters.MyAdapter;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class MyActivity extends Activity implements ResultsCallback {
    public static String LOGTAG = "MyActivity";
    public PlaceholderFragment taskFragment;
    public ListView articleListView;
    public ArticleIconTask articleIconTask;
    public ArticleDatabase articleDatabase;
    private CustomCursorAdapter customAdapter;
    private EditText searchBox;
    //public static ToastMessage L;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        articleDatabase.close();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onImageSaved(String filePath) {
        Log.d(LOGTAG, "image saved at: " + filePath);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(ArrayList<Article> items) {
        Log.d(LOGTAG, "onPostExecute firing");


        for (Article article: items){
             articleDatabase.insertArticle(article);
        }

        //MyAdapter myAdapter = new MyAdapter(this.getApplicationContext(), items, articleIconTask);

        //Cursor cursor = articleDatabase.fetchAllArticles();
        Cursor cursor = articleDatabase.fetchFilteredArticlesAllColumns("falcao");
        Log.d(LOGTAG, "cursor count: " + cursor.getCount());


        CustomCursorAdapter adapter = new CustomCursorAdapter(articleDatabase,this, this.getApplicationContext(),
                cursor, 0);
        this.customAdapter = adapter;

        articleListView.setAdapter(adapter);
    }

    public void search(View v, int keyCode, KeyEvent event){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Log.d(LOGTAG, "GOt here");
        this.searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        if(articleDatabase == null) {
            try {
                this.articleDatabase = new ArticleDatabase(getApplicationContext());
                articleDatabase.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.articleIconTask = new ArticleIconTask();
        //this.L = new ToastMessage(this.getApplicationContext());
        //L.displayMessage("WTFFF");
        //Log.d(LOGTAG, "WTFF!");

        if (savedInstanceState == null) {
            taskFragment = new PlaceholderFragment();

            getFragmentManager().beginTransaction()
                    .add(taskFragment, "myFragment").commit();
        } else {
            taskFragment = (PlaceholderFragment) getFragmentManager().findFragmentByTag("myFragment");
        }
        //taskFragment.startTask();
        taskFragment.startTaskForCustomCursorAdapter();
        articleListView = (ListView) findViewById(R.id.article_listview);
    }

}
