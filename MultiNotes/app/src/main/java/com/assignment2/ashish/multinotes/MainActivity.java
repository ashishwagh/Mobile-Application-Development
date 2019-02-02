package com.assignment2.ashish.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final int B_REQ = 1;
    private NotesAdaptor notesAdaptor;
    private RecyclerView recyclerView;
    private List<Notes> notesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycleList);
        notesAdaptor = new NotesAdaptor(notesList, this);
        recyclerView.addItemDecoration(new VerticalSpaceID());
        recyclerView.setAdapter(notesAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initFilesIfNeeded();
        new MyAsyncTask(this).execute();
    }

    private void initFilesIfNeeded() {
        File file = this.getFileStreamPath(getString(R.string.file_name));
        if(!file.exists()) {
            try {
                FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(file_out_stream);
                writer.write("[]");
                writer.close();
            }
            catch (Exception e) { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNotes:
                Intent intent = new Intent(this,EditActivity.class);
                intent.putExtra("", -1);
                startActivityForResult(intent, B_REQ);
                return true;
            case R.id.helpMenu:
                Intent in = new Intent(this,AboutActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJson();
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Notes c = notesList.get(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("", pos);
        intent.putExtra(Notes.class.getName(), c);
        startActivityForResult(intent, B_REQ);
    }

    @Override
    public boolean onLongClick(View view) {
        final int position = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(position);
                notesAdaptor.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //do nothing on cancel
            }
        });
        builder.setMessage("Delete Note '"+notesList.get(position).getNoteTitle()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("USER_MESSAGE")){
                    Toast.makeText(this,data.getStringExtra("USER_MESSAGE"),Toast.LENGTH_SHORT).show();
                }else{
                    String title = data.getStringExtra("USER_TITLE");
                    String datetime = data.getStringExtra("USER_DATETIME");
                    String note = data.getStringExtra("USER_NOTES");
                    int position = data.getIntExtra("NOTE_INDEX", -1);



                    if(position != -1 && notesList.size() > 0) {
                        Notes newNotes = notesList.get(position);
                        newNotes.setNoteTitle(title);
                        newNotes.setDateTime(datetime);
                        newNotes.setDescription(note);
                    }
                    else notesList.add(new Notes(title, datetime, note));
                    writeJson();
                }

            }
        }
    }

    private void writeJson() {
        try {
            FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(file_out_stream, getString(R.string.encoding)));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            for (Notes n: this.notesList) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(n.getNoteTitle());
                jsonWriter.name("datetime").value(n.getDateTime());
                jsonWriter.name("desc").value(n.getDescription());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.close();
        } catch (Exception e) {

        }
    }

    public void updateData(ArrayList<Notes> cList) {
        if(cList == null) return;
        notesAdaptor.updateList(cList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFilesIfNeeded();
        new MyAsyncTask(this).execute();

    }
}
