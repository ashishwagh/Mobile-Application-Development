package com.assignment4.ashish.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class AsyncDownloader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private String key = "AIzaSyCSrtxJ_WCpnUgRxC_KzLzyCeaWfeRPrIQ";
    private String url = "https://www.googleapis.com/civicinfo/v2/representatives?";

    public AsyncDownloader(MainActivity ma){
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {
        parseJSON(s);
    }

    @Override
    protected String doInBackground(String... params) {


        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("key", key);
        buildURL.appendQueryParameter("address", params[0]);
        String urlToUse = buildURL.build().toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));


            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.e(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }


    private void parseJSON(String s) {

        Log.e(TAG, "parseJson: " + s);
        Object[] result = new Object[2];
        ArrayList<Official> offRes = new ArrayList<>();

        if(s == null ){
            Toast.makeText(mainActivity, "Civic info service is unavailable", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
        } else if (s.isEmpty()){
            Toast.makeText(mainActivity, "No data available for this localisation", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
        }


        try {
            JSONObject jObject = new JSONObject(s);

            JSONObject jObjNI = jObject.getJSONObject("normalizedInput");
            String city = jObjNI.getString("city");
            String state = jObjNI.getString("state");
            String zip = jObjNI.getString("zip");

            JSONArray jArrOffices = jObject.getJSONArray("offices");
            for (int i = 0; i < jArrOffices.length(); i++)
            {
                String nameOffice = jArrOffices.getJSONObject(i).getString("name");
                JSONArray jArrIndices = jArrOffices.getJSONObject(i).getJSONArray("officialIndices");
                for (int j = 0; j < jArrIndices.length(); j++){
                    offRes.add(new Official(nameOffice, jArrIndices.getInt(j) ));
                }
            }

            JSONArray jArrOfficials = jObject.getJSONArray("officials");
            for (int i = 0; i < jArrOfficials.length(); i++) {

                offRes.get(i).setName(jArrOfficials.getJSONObject(i).optString("name"));

                JSONArray jArrAdd = jArrOfficials.getJSONObject(i).optJSONArray("address");
                String address =null;
                if(jArrAdd != null) {
                    if(!jArrAdd.getJSONObject(0).optString("line1").isEmpty())
                        address = jArrAdd.getJSONObject(0).optString("line1");
                    if(!jArrAdd.getJSONObject(0).optString("line2").isEmpty())
                       address = address.concat("\n"+jArrAdd.getJSONObject(0).optString("line2"));
                    if(!jArrAdd.getJSONObject(0).optString("city").isEmpty())
                        address = address.concat("\n"+jArrAdd.getJSONObject(0).optString("city"));
                    if(!jArrAdd.getJSONObject(0).optString("state").isEmpty())
                        address = address.concat(", "+jArrAdd.getJSONObject(0).optString("state"));
                    if(!jArrAdd.getJSONObject(0).optString("zip").isEmpty())
                        address = address.concat(" "+jArrAdd.getJSONObject(0).optString("zip"));
                    offRes.get(i).setAddress(address);
                }

                offRes.get(i).setParty(jArrOfficials.getJSONObject(i).optString("party"));

                if(jArrOfficials.getJSONObject(i).optJSONArray("phones") != null)
                    offRes.get(i).setPhone( jArrOfficials.getJSONObject(i).getJSONArray("phones").getString(0));
                if(jArrOfficials.getJSONObject(i).optJSONArray("urls") != null)
                    offRes.get(i).setWebsite(jArrOfficials.getJSONObject(i).getJSONArray("urls").getString(0));
                if(jArrOfficials.getJSONObject(i).optJSONArray("emails") != null)
                    offRes.get(i).setEmail(jArrOfficials.getJSONObject(i).getJSONArray("emails").getString(0));

                offRes.get(i).setPhotoURL(jArrOfficials.getJSONObject(i).optString("photoUrl"));

                JSONArray jArrChan = jArrOfficials.getJSONObject(i).optJSONArray("channels");
                if (jArrChan != null) {
                    for (int j = 0; j < jArrChan.length(); j++) {
                        if (jArrChan.getJSONObject(j).getString("type").equals("GooglePlus")) {
                            offRes.get(i).setGoogleplusId(jArrChan.getJSONObject(j).getString("id"));
                        } else if (jArrChan.getJSONObject(j).getString("type").equals("Facebook")) {
                            offRes.get(i).setFacebookId(jArrChan.getJSONObject(j).getString("id"));
                        } else if (jArrChan.getJSONObject(j).getString("type").equals("Twitter")) {
                            offRes.get(i).setTwitterId(jArrChan.getJSONObject(j).getString("id"));
                        } else if (jArrChan.getJSONObject(j).getString("type").equals("YouTube")) {
                            offRes.get(i).setYoutubeId(jArrChan.getJSONObject(j).getString("id"));
                        }
                    }
                }
            }

            result[0] = city + ", " + state + " " +zip;
            result[1] = offRes ;
            mainActivity.setOfficialList(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
