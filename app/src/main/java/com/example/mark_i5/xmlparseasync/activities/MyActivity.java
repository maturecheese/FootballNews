package com.example.mark_i5.xmlparseasync.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mark_i5.xmlparseasync.fragments.PlaceholderFragment;
import com.example.mark_i5.xmlparseasync.R;
import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.adapters.MyAdapter;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconTask;

import java.util.ArrayList;
import java.util.HashMap;


public class MyActivity extends Activity implements ResultsCallback {
    public static String LOGTAG = "MyActivity";
    public PlaceholderFragment taskFragment;
    public ListView articleListView;
    public ArticleIconTask articleIconTask;
    //public static ToastMessage L;

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(ArrayList<HashMap<String, String>> items) {
        Log.d(LOGTAG, "onPostExecute firing");
        MyAdapter myAdapter = new MyAdapter(this.getApplicationContext(), items, articleIconTask);
/*
for (HashMap<String, String> item : items){
Log.d(LOGTAG, item.toString());
}
*/
        articleListView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
        taskFragment.startTask();
        articleListView = (ListView) findViewById(R.id.article_listview);
    }

}