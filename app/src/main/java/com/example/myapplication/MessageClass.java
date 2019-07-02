package com.example.myapplication;

public class MessageClass {
    private String text;
    private int id;

    public MessageClass(int id,String text)
    {
        this.id=id;
        this.text=text;
    }
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
