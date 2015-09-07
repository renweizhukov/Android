package com.example.notetaker;

import java.io.Serializable;

public class Note implements Serializable
{
    private String title;
    private String note;
    private String date;
    
    public Note(String title, String note, String date)
    {
        super();
        this.title = title;
        this.note = note;
        this.date = date;
    }
    
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getNote()
    {
        return note;
    }
    public void setNote(String note)
    {
        this.note = note;
    }
    
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    
}
