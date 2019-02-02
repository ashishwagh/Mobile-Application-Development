package com.assignment2.ashish.multinotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdaptor extends RecyclerView.Adapter<NotesHolder>{
    private List<Notes> notesList;
    private MainActivity mainActivity;

    public NotesAdaptor(List<Notes> notesList, MainActivity main) {
        this.notesList = notesList;
        this.mainActivity = main;
    }

    public void updateList(List<Notes> list) {
        this.notesList.clear();
        this.notesList.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteview, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NotesHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull NotesHolder notesHolder, int pos) {
        notesHolder.title.setText(this.notesList.get(pos).getNoteTitle());
        SimpleDateFormat sd2 = new SimpleDateFormat("EEE MMM dd, h:mm a", Locale.US);
        SimpleDateFormat sd1 = new SimpleDateFormat("EEE MMM dd, h:mm:ss a", Locale.US);
        String datetime = new String();
        try {
            Date date = sd1.parse(this.notesList.get(pos).getDateTime());
            datetime = sd2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notesHolder.date.setText(datetime);


        String NoteString = this.notesList.get(pos).getDescription();
        if(NoteString.length()>80){
            NoteString  =  NoteString.substring(0,79)+"...";
        }
        notesHolder.desc.setText(NoteString);
    }

    @Override
    public int getItemCount() {
        return this.notesList.size();
    }
}
