package com.assignment2.ashish.multinotes;

import java.io.Serializable;

public class Notes implements Serializable{

    private String noteTitle;
    private String description;
    private String dateTime;

    public Notes(String noteTitle,  String dateTime,String description) {
        this.noteTitle = noteTitle;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
