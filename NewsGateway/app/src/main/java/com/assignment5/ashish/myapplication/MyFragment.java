package com.assignment5.ashish.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyFragment extends Fragment implements Serializable {

    private ImageView imageView;
    private static MainActivity main;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);

    }


    public static final MyFragment newInstance(MainActivity ma, String title, String author, String description, String imageUrl, String publishedAt, int pageNo, int total, String url) {

        main = ma;
        MyFragment f = new MyFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString("title", title);
        bdl.putString("author", author);
        bdl.putString("description", description);
        bdl.putString("imageUrl", imageUrl);
        bdl.putString("publishedAt", publishedAt);
        bdl.putInt("total", total);
        bdl.putInt("pageNumber", pageNo);
        bdl.putString("url", url);
        f.setArguments(bdl);
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_fragment, container, false);

        String titleMsg = getArguments().getString("title");
        TextView title = (TextView)v.findViewById(R.id.title);
        title.setText(titleMsg);

        String authorMsg = getArguments().getString("author");

        if(authorMsg.equals("null")){
            authorMsg = "";
        }


        TextView author = (TextView)v.findViewById(R.id.author);
        author.setText(authorMsg);

        String descriptionMsg = getArguments().getString("description");

        if(descriptionMsg.equals("null")){
            descriptionMsg = "No Information Available";
        }

        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(descriptionMsg);

        String date = "";
        if(!(getArguments().getString("publishedAt").equals("null"))) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            SimpleDateFormat pd = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            try{
                Date parsed = sdf.parse(getArguments().getString("publishedAt"));
                date = pd.format(parsed);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }


        TextView datetime = (TextView)v.findViewById(R.id.datetime);
        datetime.setText(date);

        int total = getArguments().getInt("total");
        int pageNumber = getArguments().getInt("pageNumber");

        TextView pg = (TextView)v.findViewById(R.id.pagenumber);
        String number = pageNumber + " of "+total;
        pg.setText(number);

        String imageUrlMsg = getArguments().getString("imageUrl");
        imageView = (ImageView)v.findViewById(R.id.image);
        loadImage(imageUrlMsg);


        final String url = getArguments().getString("url");

        if(url != null) {
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        else {
            description.setText("No Information Available!");
        }


        return v;
    }



    private void loadImage(final String imageURL) {

        if(imageURL!=null && !imageURL.equals("")){
            Picasso picasso = new Picasso.Builder(this.main)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String changedUrl = imageURL.replace("http:", "https:");
                            picasso.load(changedUrl) .error(R.drawable.brokenimage)
                                    .placeholder(R.drawable.placeholder) .into(imageView);
                        }
                    })
                    .build();

            picasso.load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .resize(1000, 500).centerCrop().into(imageView);
        }else{
            Picasso.with(this.main).load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage).resize(1000,500)
                    .into(imageView);
        }

    }

}
