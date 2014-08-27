package com.example.mark_i5.xmlparseasync.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mark_i5.xmlparseasync.ResultsCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by mark-i5 on 25/08/2014.
 */

public  class GuardianFootballTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
    private static final String LOGTAG = "GuardianFootballTask";
    ResultsCallback callBack;
    public GuardianFootballTask(ResultsCallback callBack){
        this.callBack = callBack;
    }
    public void onAttach(ResultsCallback callBack){
        this.callBack = callBack;
    }
    public void onDetach(){
        this.callBack = null;
    }


    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
        String downloadUrl = "http://www.theguardian.com/football/rss";
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            items = processXML(inputStream);
        } catch (Exception e) {
            Log.d(LOGTAG, "some url error");
            Log.e(LOGTAG, "error", e);
            e.printStackTrace();
        }

        return items;
    }

    protected static synchronized ArrayList<HashMap<String, String>> processXML(InputStream inputStream) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document xmlDocument = documentBuilder.parse(inputStream);
        Element rootElement = xmlDocument.getDocumentElement();
        //Log.d(LOGTAG, "root element tag: " + rootElement.getTagName());

        NodeList nodeList = rootElement.getElementsByTagName("item");
        Node currentItem, currentChild;
        NodeList itemChildren;

        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();


        int count = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            currentItem = nodeList.item(i);
            HashMap<String, String> item = new HashMap<String, String>();
            //L.displayMessage(currentItem.getNodeName().toString());
            //Log.d(LOGTAG, currentItem.getNodeName().toString());
            itemChildren = currentItem.getChildNodes();
            for (int j = 0; j < itemChildren.getLength(); j++) {

                currentChild = itemChildren.item(j);
                //Log.d(LOGTAG, currentChild.getNodeName().toString());
                String nodeName = currentChild.getNodeName().toString();
                if (nodeName.equalsIgnoreCase("title")) {
                    // Log.d(LOGTAG, currentChild.getTextContent());
                    item.put("title", currentChild.getTextContent());
                    count ++;
                } else if (nodeName.equalsIgnoreCase("pubdate")) {
                    item.put("pubDate", currentChild.getTextContent());
                    count ++;
                } else if (currentChild.getNodeName().equalsIgnoreCase("description")) {
                    //Log.d(LOGTAG, currentChild.getTextContent());
                    item.put("description", currentChild.getTextContent());
                    count++;
                } else if (currentChild.getNodeName().equalsIgnoreCase("media:content")) {
                    if (currentChild.getAttributes().item(0).getTextContent().equalsIgnoreCase("84")) {
                        // Log.d(LOGTAG, currentChild.getAttributes().item(2).getTextContent());
                        // Log.d(LOGTAG, currentChild.getAttributes().getNamedItem("url").getTextContent().toString());
                        String url = currentChild.getAttributes().getNamedItem("url").getTextContent().toString();
                        Log.d(LOGTAG, "check image url: " + url);
                        item.put("imageUrl", currentChild.getAttributes().getNamedItem("url").getTextContent().toString());
                        count++;

                    }else{
                        continue;
                    }

                }else{
                    continue;
                }
                if (count > 3){
                    count = 0;
                    items.add(item);
                }

            }
        }
        //L.displayMessage(rootElement.getTagName());

        return items;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callBack != null){
            callBack.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(ArrayList< HashMap<String, String>> items) {
        super.onPostExecute(items);
        //Log.d(LOGTAG, "arraylist to string: "+ items.toString());
        if (callBack != null){
            callBack.onPostExecute(items);
        }
    }
}