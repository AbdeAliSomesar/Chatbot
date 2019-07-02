package com.example.myapplication;

public class ListofLine
{
    String id,line;
    float rank;
    int sentiments;

    public  ListofLine(String id,float f,String line)
    {
        this.id=id;
        this.rank=f;
        this.line=line;
    }
    public  ListofLine(String id,float f,String line,int sentiments)
    {
        this.id=id;
        this.rank=f;
        this.line=line;
        this.sentiments=sentiments;
    }
    public ListofLine(String line,int sentiments)
    {
        this.line=line;
        this.sentiments=sentiments;
    }
}
