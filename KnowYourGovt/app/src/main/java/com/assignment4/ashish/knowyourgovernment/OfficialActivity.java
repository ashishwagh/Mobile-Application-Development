package com.assignment4.ashish.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.assignment4.ashish.knowyourgovernment.R.id.googleplus;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private Official official;
    private TextView official_office;
    private TextView official_name;
    private TextView official_party;
    private TextView official_address;
    private TextView official_phone;
    private TextView official_email;
    private TextView official_website;
    private ImageView official_youtube;
    private ImageView official_googleplus;
    private ImageView official_facebook;
    private ImageView official_twitter;
    private ImageView official_photo;
    private TextView location;
    private String loc;
    private boolean netCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        official_office = (TextView) findViewById(R.id.offcial_office);
        official_name = (TextView) findViewById(R.id.official_name);
        official_party = (TextView) findViewById(R.id.official_party);
        official_address = (TextView) findViewById(R.id.official_address);
        official_phone = (TextView) findViewById(R.id.official_phone);
        official_email = (TextView) findViewById(R.id.official_email);
        official_website = (TextView) findViewById(R.id.official_website);
        official_youtube = (ImageView) findViewById(R.id.youtube) ;
        official_googleplus = (ImageView) findViewById(googleplus) ;
        official_facebook = (ImageView) findViewById(R.id.facebook) ;
        official_twitter = (ImageView) findViewById(R.id.twitter) ;
        official_photo = (ImageView) findViewById(R.id.official_photo);
        location = (TextView) findViewById(R.id.location);

        Intent intent = getIntent();
        if (intent.hasExtra("Official")) {
            official = (Official) intent.getSerializableExtra("Official");
        } else {
            Log.e(TAG, "No official passed");
            finish();
        }
        if(intent.hasExtra("Location")){
            loc = (String) intent.getSerializableExtra("Location");
        } else {
            Log.e(TAG, "No location passed");

        }
        if(intent.hasExtra("internetCheck")){
            netCheck = (boolean)intent.getSerializableExtra("internetCheck");
        }

        setTexts();
        setBackground();
        setVisibilities();
        addLinks();
        loadPhoto();

    }

    private void setBackground(){
        if (official.getParty().matches("Democrat.*")){
            findViewById(R.id.constraintScrollView).setBackgroundColor(getResources().getColor(R.color.colorDemocratic));
        } else if (official.getParty().matches("Republic.*")){
            findViewById(R.id.constraintScrollView).setBackgroundColor(getResources().getColor(R.color.colorRepublican));
        } else {
            findViewById(R.id.constraintScrollView).setBackgroundColor(getResources().getColor(R.color.colorNoParty));
        }
    }

    private void setTexts(){
        location.setText(loc);
        official_office.setText((official.getOffice()!=null ? official.getOffice() : "No Data Provided"));
        official_name.setText((official.getName()!=null ? official.getName() : "No Data Provided"));
        official_address.setText((official.getAddress()!=null ? official.getAddress() : "No Data Provided"));
        official_phone.setText((official.getPhone()!=null ? official.getPhone() : "No Data Provided"));
        official_email.setText((official.getEmail()!=null ? official.getEmail() : "No Data Provided"));
        official_website.setText((official.getWebsite()!=null ? official.getWebsite() : "No Data Provided"));

        if(!official.getParty().isEmpty() && !official.getParty().equalsIgnoreCase("unknown"))
            official_party.setText("("+official.getParty()+")" );
        else
            official_party.setText("(Unknown)");

    }

    private void setVisibilities(){
        official_googleplus.setVisibility((official.getGoogleplusId()!=null ? View.VISIBLE : View.INVISIBLE));
        official_twitter.setVisibility((official.getTwitterId()!=null ? View.VISIBLE : View.INVISIBLE));
        official_facebook.setVisibility((official.getFacebookId()!=null ? View.VISIBLE : View.INVISIBLE));
        official_youtube.setVisibility((official.getYoutubeId()!=null ? View.VISIBLE : View.INVISIBLE));
    }

    private void addLinks(){
        Linkify.addLinks(official_website, Linkify.ALL);
        Linkify.addLinks(official_phone, Linkify.ALL);
        Linkify.addLinks(official_address, Linkify.ALL);
        Linkify.addLinks(official_email, Linkify.ALL);

    }

    private void loadPhoto(){
        if(netCheck){
            if (official.getPhotoURL() != null && !official.getPhotoURL().isEmpty()) {

                Picasso picasso = new Picasso.Builder(this)
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                final String changedUrl = official.getPhotoURL().replace("http:", "https:");

                                picasso.load(changedUrl)
                                        .error(R.drawable.brokenimage)
                                        .placeholder(R.drawable.placeholder)
                                        .into(official_photo);
                            }
                        })
                        .build();

                picasso.load(official.getPhotoURL())
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(official_photo);
            } else {

                Picasso. with(this).load((String) null)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.missingimage)
                        .into(official_photo);

            }
        }else{
            Picasso. with(this).load(R.drawable.placeholder)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(official_photo);
        }
    }


    public void youtubeClick(View v) {

        String name = official.getYoutubeId();
        Intent intent = null;
        try {
            intent = new Intent(Intent. ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri. parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent. ACTION_VIEW, Uri. parse("https://www.youtube.com/" + name)));
        }

    }

    public void googleplusClick(View arg0) {

        String name = official.getGoogleplusId();
        Intent intent = null;
        try {
            intent = new Intent(Intent. ACTION_VIEW);
            intent.setClassName("com.google.android. apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent. ACTION_VIEW, Uri. parse("https://plus.google.com/" + name)));
        }
    }

    public void twitterClick(View arg0) {

        String name = official.getTwitterId();
        String twitterAppUrl = "twitter://user?screen_name=" + name;
        String twitterWebUrl = "https://twitter.com/" + name;

        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }

    public void facebookClick(View arg0) {

        String facebookUrl = "https://www.facebook.com/" + official.getFacebookId();
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + facebookUrl;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + official.getFacebookId();
            }

        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = facebookUrl;
        }

        Intent fbIntent = new Intent(Intent.ACTION_VIEW);
        fbIntent.setData(Uri.parse(urlToUse));
        startActivity(fbIntent);
    }


    public void photoClick(View v){
        if (official.getPhotoURL() != null && !official.getPhotoURL().isEmpty()) {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("Official", official);
            intent.putExtra("Location", loc);
            intent.putExtra("internetCheck",netCheck);
            startActivity(intent);
        }
    }

}
