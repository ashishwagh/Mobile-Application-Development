package com.assignment4.ashish.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private Official official;
    private TextView official_office;
    private TextView official_name;
    private ImageView official_photo;
    private TextView location;
    private String loc;
    private boolean netCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        official_office = (TextView) findViewById(R.id.offcial_office);
        official_name = (TextView) findViewById(R.id.official_name);
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

        setBackground();
        setTexts();
        loadPhoto();
    }

    private void setBackground(){
        if (official.getParty().matches("Democrat.*")){
            findViewById(R.id.constraintIn).setBackgroundColor(getResources().getColor(R.color.colorDemocratic));
        } else if (official.getParty().matches("Republic.*")){
            findViewById(R.id.constraintIn).setBackgroundColor(getResources().getColor(R.color.colorRepublican));
        } else {
            findViewById(R.id.constraintIn).setBackgroundColor(getResources().getColor(R.color.colorNoParty));
        }
    }

    private void setTexts(){
        official_office.setText((official.getOffice()!=null ? official.getOffice() : "No Data Provided"));
        official_name.setText((official.getName()!=null ? official.getName() : "No Data Provided"));
        location.setText(loc);
    }

    private void loadPhoto(){
        if(netCheck) {
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

                Picasso.with(this).load((String) null)
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
}
