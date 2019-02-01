package com.assignment5.ashish.myapplication;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncArtDownloader extends android.os.AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncTask";
    private Exception exceptionToBeThrown = null;
    private MyService myService;


    private final String newsUrl = "https://newsapi.org/v2/top-headlines?";
    private final String yourAPIKey = "b6f475961c324325906017787ea4c3b8";
    private String id = "";

    public AsyncArtDownloader(MyService ms) {
        myService = ms;
    }

    @Override
    protected String doInBackground(String... params) {

        id = params[0];

        StringBuilder sb = new StringBuilder();

        Uri.Builder buildURL = Uri.parse(newsUrl).buildUpon();
        buildURL.appendQueryParameter("sources", params[0]);
        buildURL.appendQueryParameter("apiKey", yourAPIKey);

        String urlToUse = buildURL.build().toString();
        System.out.println("urlToUse articles"+urlToUse);
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();

    }

    private ArrayList<ArticleList> parseJSON(String s) {
        ArrayList<ArticleList> newsDescriptionList = new ArrayList<>();

        try{
            JSONObject jsonObj = new JSONObject(s);
            JSONArray offices = jsonObj.getJSONArray("articles");

            for(int i = 0 ; i < offices.length() ; i++){
                String author = offices.getJSONObject(i).optString("author");
                String title = offices.getJSONObject(i).optString("title");
                String description = offices.getJSONObject(i).optString("description");
                String imageUrl = offices.getJSONObject(i).optString("urlToImage");
                String url = offices.getJSONObject(i).optString("url");
                String publishedAt = offices.getJSONObject(i).optString("publishedAt");
                newsDescriptionList.add(new ArticleList(id,author,title,description,imageUrl,url,publishedAt));

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return newsDescriptionList;

    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<ArticleList> newsDescriptionList = parseJSON(s);
        myService.updateDescriptionData(newsDescriptionList);
    }
}

