package com.example.mark_i5.xmlparseasync.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mark_i5.xmlparseasync.R;
import com.example.mark_i5.xmlparseasync.ResultsCallback;
import com.example.mark_i5.xmlparseasync.tasks.ArticleIconTask;
import com.example.mark_i5.xmlparseasync.tasks.GuardianFootballTask;

/**
 * Created by mark-i5 on 25/08/2014.
 */
public class PlaceholderFragment extends Fragment {


    private static final String LOGTAG = "PlaceHolderFragment";
    ResultsCallback callBack;
    GuardianFootballTask downloadTask;
    ArticleIconTask downloadIconTask;

    public PlaceholderFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack =null;
        if (downloadTask != null){
            downloadTask.onDetach();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (ResultsCallback) activity;
        if (downloadTask != null){
            downloadTask.onAttach(callBack);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        return rootView;
    }

    public void startTask() {

        Log.d(LOGTAG, "startTask was called");
        if (downloadTask != null) {
            downloadTask.cancel(true);
        } else {
            downloadTask = new GuardianFootballTask(callBack);
            downloadTask.execute();
        }


    }
    public void startTaskForCustomCursorAdapter(){
        Log.d(LOGTAG, "startTaskForCustomCursorAdapter();");
        downloadTask = new GuardianFootballTask(callBack);
        downloadTask.execute();
    }
}