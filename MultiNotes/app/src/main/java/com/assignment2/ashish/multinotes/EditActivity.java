package com.assignment2.ashish.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    EditText edit_title, edit_desc;

    private int loc;
    private String datetime;
    private Notes notes;
    private MainActivity mainActivity;
    private Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_title = (EditText) findViewById(R.id.editTitleId);
        edit_desc = (EditText) findViewById(R.id.editDesc);

        Intent intent = getIntent();
        if (intent.hasExtra(Notes.class.getName())) {
            note = (Notes) intent.getSerializableExtra(Notes.class.getName());
            edit_title.setText(note.getNoteTitle());
            edit_desc.setText(note.getDescription());
            datetime = (String) intent.getSerializableExtra(note.getDateTime());
        }
        loc = (Integer) intent.getSerializableExtra("");

    }

    public String getDateTimeFormat() {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("EEE MMM dd, h:mm:ss a", Locale.US);
        return sd.format(date);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNotes:
                Intent data = new Intent();

                if (edit_title.getText().toString().trim().equals("")){
                    data.putExtra("USER_MESSAGE", "un-titled activity was not saved");
                }else{

                    if (note != null && note.getNoteTitle().equals(edit_title.getText().toString()) && note.getDescription().equals(edit_desc.getText().toString())){
                        data.putExtra("USER_DATETIME", note.getDateTime());
                    }else{
                        data.putExtra("USER_DATETIME", getDateTimeFormat());
                    }
                    data.putExtra("USER_TITLE", edit_title.getText().toString());
                    data.putExtra("USER_NOTES", edit_desc.getText().toString());
                }
                data.putExtra("NOTE_INDEX", loc);
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if(note != null && (!(note.getNoteTitle().equals(edit_title.getText().toString())) || !(note.getDescription().equals(edit_desc.getText().toString())))){
            showDialogBox();
        }else if (note == null && (edit_title.getText().toString().length() > 0 || edit_desc.getText().toString().length() > 0)){
            showDialogBox();
        }else{
            super.onBackPressed();
        }
    }

    public void showDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                if (edit_title.getText().toString().length() == 0) {
                    finish();
                    Toast.makeText(EditActivity.this,"Your note is not saved as Title is missing!.",Toast.LENGTH_SHORT).show();
                } else {
                    if (note != null && note.getNoteTitle().equals(edit_title.getText().toString()) && note.getDescription().equals(edit_desc.getText().toString())) {
                        data.putExtra("USER_DATETIME", note.getDateTime());
                    } else {
                        data.putExtra("USER_DATETIME", getDateTimeFormat());
                    }
                    data.putExtra("USER_TITLE", edit_title.getText().toString());
                    data.putExtra("USER_NOTES", edit_desc.getText().toString());
                    data.putExtra("NOTE_INDEX", loc);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                finish();

            }
        });
        builder.setMessage("Your note is not saved! Save note '"+edit_title.getText().toString()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
