package com.assignment2.ashish.multinotes;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private MainActivity mainActivity;

    public MyAsyncTask(MainActivity main) {
        mainActivity = main;
    }

    @Override
    protected String doInBackground(String... strings) {
        return readJsonFileFromDevice();
    }
    private String readJsonFileFromDevice() {
        try {
            InputStream ipStream = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            int size = ipStream.available();
            byte[] buffer = new byte[size];
            ipStream.read(buffer);
            ipStream.close();
            return new String(buffer, "UTF-8");

        } catch (FileNotFoundException e) { return null;
        } catch (Exception e) { return null; }
    }
    @Override
    protected void onPostExecute(String s) {
        ArrayList<Notes> notesList = parseJSON(s);
        mainActivity.updateData(notesList);
    }

    private ArrayList<Notes> parseJSON(String s) {
        ArrayList<Notes> notesList = new ArrayList<>();

        try {
            JSONArray list = new JSONArray(s);
            JSONArray sortedJsonArray = new JSONArray();
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < list.length(); i++) {
                jsonValues.add(list.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "datetime";
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String s1 = new String();
                    String s2 = new String();

                    try {
                        s1 = (String) a.get(KEY_NAME);
                        s2 = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                    }
                    return -s1.compareTo(s2);
                }
            });

            for (int i = 0; i < list.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }

            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject jNotes = (JSONObject) sortedJsonArray.get(i);
                String title = jNotes.getString("title");
                String datetime = jNotes.getString("datetime");
                String notes = jNotes.getString("desc");
                notesList.add(new Notes(title, datetime, notes));
            }

            return notesList;
        } catch (Exception e) { return null; }
    }
}
