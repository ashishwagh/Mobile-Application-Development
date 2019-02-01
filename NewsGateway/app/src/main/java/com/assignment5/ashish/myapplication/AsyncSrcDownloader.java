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

public class AsyncSrcDownloader extends android.os.AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncTask";
    private MainActivity mainActivity;
    private final String newsUrl = "https://newsapi.org/v2/sources";
    private final String yourAPIKey = "b6f475961c324325906017787ea4c3b8";
    private ArrayList<String> newsCategory;


    public AsyncSrcDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... params) {

        if(params[0].equals("specific")){
            StringBuilder sb = new StringBuilder();

            Uri.Builder buildURL = Uri.parse(newsUrl).buildUpon();
            buildURL.appendQueryParameter("apiKey", yourAPIKey);
            buildURL.appendQueryParameter("language", "en");
            buildURL.appendQueryParameter("country", "us");
            buildURL.appendQueryParameter("category", params[1]);
            String urlToUse = buildURL.build().toString();
            System.out.println("urlToUse sources"+urlToUse);
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
        }else{

            StringBuilder sb = new StringBuilder();

            Uri.Builder buildURL = Uri.parse(newsUrl).buildUpon();
            buildURL.appendQueryParameter("apiKey", yourAPIKey);
            buildURL.appendQueryParameter("language", "en");
            buildURL.appendQueryParameter("country", "us");
            String urlToUse = buildURL.build().toString();

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

    }

    private ArrayList<NewsList> parseJSON(String s) {
        ArrayList<NewsList> newsList = new ArrayList<>();
        newsCategory = new ArrayList<>();
        newsCategory.add("all");
        try{
            JSONObject jsonObj = new JSONObject(s);
            JSONArray offices = jsonObj.getJSONArray("sources");

            for(int i = 0 ; i < offices.length() ; i++){
                String id = offices.getJSONObject(i).optString("id");
                String name = offices.getJSONObject(i).optString("name");
                String category = offices.getJSONObject(i).optString("category");
                newsList.add(new NewsList(id,name,category));
                if(!newsCategory.contains(category)){
                    newsCategory.add(category);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return newsList;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<NewsList> newsList = parseJSON(s);
        mainActivity.updateData(newsList, newsCategory);
    }
}

