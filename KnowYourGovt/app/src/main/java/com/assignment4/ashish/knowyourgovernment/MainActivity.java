package com.assignment4.ashish.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private Locator locator;
    private RecyclerView recyclerView;
    private OfficialAdapter mAdapter;
    private List<Official> officialsList = new ArrayList<>();
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = (TextView) findViewById(R.id.location);

        if (networkCheck()) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            mAdapter = new OfficialAdapter(officialsList, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            locator = new Locator(this);
        }else {
            location.setText("No data for location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection");
            builder.setMessage("Data cannot be accessed/loaded without a network connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        boolean netCheck = networkCheck();
        Official m = officialsList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("Location",location.getText());
        intent.putExtra("Official",m);
        intent.putExtra("internetCheck",netCheck);
        startActivity(intent);

    }
    @Override
    public boolean onLongClick(View v) {
        onClick(v);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_location:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            List<Address> addresses;
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
                            addresses = geocoder.getFromLocationName(et.getText().toString(), 1);
                            Log.e(TAG, "addresses:"+addresses);

                            if (addresses.get(0)!=null ) {
                                AsyncDownloader cidl = new AsyncDownloader(MainActivity.this);
                                if(addresses.get(0).getPostalCode() != null)
                                    cidl.execute(addresses.get(0).getPostalCode());
                                else
                                    cidl.execute(addresses.get(0).getAddressLine(0));
                            } else {
                                throw new Exception("No address in geocoder result") ;
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Address cannot be acquired from provided input", Toast.LENGTH_LONG).show();
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Address cannot be acquired from provided input", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage("Enter a City, State or ZipCode:");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.info:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void doLocationWork(double latitude, double longitude) {
        Log.e(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.US);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.get(0) !=null ) {
                AsyncDownloader cidl = new AsyncDownloader(this);
                if(addresses.get(0).getPostalCode() != null)
                    cidl.execute(addresses.get(0).getPostalCode());
                else
                    cidl.execute(addresses.get(0).getAddressLine(0));

            } else {
                throw new Exception("No address in geocoder result") ;
            }
        } catch (IOException e) {
            Log.e(TAG, "doAddress: " + e.getMessage());
            Toast.makeText(this, "Address cannot be acquired from provided latitude & longitude", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Log.e(TAG, "doAddress: " + e.getMessage());
            Toast.makeText(this, "Address cannot be acquired from provided latitude & longitude", Toast.LENGTH_LONG).show();
        }
        return;
    }

    public void noLocationAwailable(){
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    public void setOfficialList(Object[] result){

        if (result == null){
            location.setText("No Data For Location");
            officialsList.clear();

            mAdapter.notifyDataSetChanged();
        } else{
            location.setText((String) result[0]);
            officialsList.clear();
            officialsList.addAll((ArrayList<Official>) result[1]);

            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        if(locator!=null)
            locator.shutdown();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }


    public boolean networkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
