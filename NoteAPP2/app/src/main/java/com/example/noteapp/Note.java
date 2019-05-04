package com.example.noteapp;


public class Note {
    private String Title;
    private String Text;
    private String Date;
    private String Color;

    public Note(String text, String title, String date, String color) {
        Text = text;
        Title = title;
        Date = date;
        Color = color;
    }

    public String getText() {
        return Text;
    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return Date;
    }

    public String getColor() {
        return Color;
    }
}
