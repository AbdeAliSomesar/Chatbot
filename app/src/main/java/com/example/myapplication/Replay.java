package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Replay extends Thread {
    private static List<ListofLine> listofLines = Collections.synchronizedList(new ArrayList<ListofLine>());
    //private Cleaner c=new Cleaner();
    private int start,end;
    private  String s;
    private SQLiteDatabase db;

    public  Replay(int start,int end, String s, SQLiteDatabase db)
    {
        this.start=start;
        this.end=end+1;
        this.s=s;
        this.db=db;
    }
    public static List<ListofLine> getList()
    {
        return listofLines;
    }
    public void run()
    {
        Cursor cursor= db.rawQuery("select lineid,Line from cleanline where id between "+(start)+" AND "+(end),null);
        if(cursor==null||cursor.getCount()<1)
            return;
        String id,s2,arr1[],arr2[];
        cursor.moveToFirst();
        float f;
        int count;
        do
        {
            count=0;

            s2=cursor.getString(1).trim();
            id=cursor.getString(0);
            arr1=s.split(" ");
            arr2=s2.split(" ");
            for(String s:arr1)
            {
                for(String a:arr2) {
                    if (a.equals(s)) {
                        count++;
                        break;
                    }
                }
            }
            f=((count*100)/arr1.length);
            if((count!=0)&&(f>50f))
                listofLines.add(new ListofLine(id,f,null));

        }while(cursor.moveToNext());
        //this.notify();

    }
}
