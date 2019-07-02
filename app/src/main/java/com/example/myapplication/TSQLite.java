package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class TSQLite extends SQLiteOpenHelper {

    //LineID	CharID	MovieID	Line
    private static final String dbname="Chatbot";
    private static final String tname="Lines";
    private static final String col1="LineID";
    private static final String col2="CharID";
    private static final String col3="MovieID";
    private static final String col4="Line";
    private static final String col5="Sentiments";
    private static final String col6="Mood1";//SadOrhappy
    private static final String col7="Anger";
    private static final String col8="score";

    private long count=0l;
    private Sentiments sentiments;
    private AssetManager assetManager;
    public TSQLite(Context context)
    {

        super(context,dbname,null,1);
        //SQLiteDatabase db=this.getWritableDatabase();
        sentiments=new Sentiments(context);
        this.assetManager=context.getAssets();
       // boolean status=true;
      //  onCreate(this.getWritableDatabase());

    }
    public void insertFileteredData(SQLiteDatabase db) {
        ContentValues contentValues;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assetManager.open("test2")));
            //new InputStreamReader(assetManager.open("Test")));

            // do reading, usually loop until end of file reading
            String mLine, s[];
            while (((mLine = reader.readLine()) != null) && mLine != "") {

                s = mLine.trim().split("\t");
                if (s.length == 2) {
                    contentValues = new ContentValues();
                    contentValues.put("lineid", s[0]);
                    contentValues.put("line", s[1]);
                    long i = db.insert("cleanLine", null, contentValues);
                    i=i;
                }
            }
        }
        catch (Exception e) {

        }finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }
    public void insertLine(SQLiteDatabase db)
    {

        //SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assetManager.open("Test")));
                    //new InputStreamReader(assetManager.open("Test")));

            // do reading, usually loop until end of file reading
            String mLine,s[];
            while (((mLine = reader.readLine()) != null)&&mLine!="") {

                s=mLine.trim().split("\t");
                if(s.length==4) {
                    contentValues = new ContentValues();
                    contentValues.put(col1, s[0].toLowerCase());
                    contentValues.put(col2, s[1]);
                    contentValues.put(col3, s[2]);
                    contentValues.put(col4, s[3]);
                    // int j=0;
                    contentValues.put(col5,0);//sentiments.positive(s[2]) );
                    contentValues.put(col6, 0);
                    contentValues.put(col7, 0);
                    contentValues.put(col8,0);
                    long i = db.insert(tname, null, contentValues);
                    //i = db.insert(tname, null, contentValues);
                    count = i;


                    //db.execSQL("INSERT INTO ftsline VALUES ('"+s[3]+"')");
                    i=i;
                }
            }
             } catch (IOException e) {
            //log the exception
           // Log log;
            int i=9;

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }
   /* private void insert(String name,String des,double price,SQLiteDatabase db)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("DESCRIPTION",des);
        contentValues.put("PRICE",price);
        db.insert("PRODUCTS",null,contentValues);
    }*/
   public List<ListofLine> compareText(String s1)
   {
       List <ListofLine> listofLines=new ArrayList<ListofLine>(); //Class is in messageClass.java file

       Cursor cursor= this.getReadableDatabase().rawQuery("select lineid,Line from cleanline",null);
       if(cursor==null||cursor.getCount()<1)
           return listofLines;
       String id,s2,arr1[],arr2[];
       s1=clean(s1);
       arr1=s1.split(" ");
       cursor.moveToFirst();
       float f;
       int count;
       do
       {
           count=0;

           s2=cursor.getString(1).trim();
           id=cursor.getString(0);
           arr2=s2.split(" ");
           for(String s:arr1)
           {
               for(int i=0;i<arr2.length;i++)//String a:arr2) {
                   if (arr2[i].equals(s)) {
                       count++;
                       arr2[i]="";
                        break;
                   }
              // }
           }
           if(count!=0)
            f=((count*100)/arr1.length);
           else
               f=0;
           if((f>60f)) {
               //if((arr2.length-arr1.length)<6)
                //   f+=100;
               //Provide more score if f is > then 80 for better match
               if(arr1.length>2) {
                   if (f > 80f)
                       f += 80;
                   else if (f > 70)
                       f += 65;
               }
               //Add size of lines in percents
               if(arr1.length>arr2.length)
                   f+=((arr2.length*100)/arr1.length);
               else
                   f+=((arr1.length*100)/arr2.length);
               String sql = "select line from lines where lineid = \'"+id+"\'";
               Cursor cursor2= this.getReadableDatabase().rawQuery(sql,null);
                if(cursor2!=null&&cursor2.getCount()>0)
                {
                    cursor2.moveToFirst();
                    listofLines.add(new ListofLine(id, f,cursor2.getString(0)));
                    if(listofLines.size()>400)
                        break;
                }
                else
                    listofLines.add(new ListofLine(id, f,null));
           }
       }while(cursor.moveToNext());
        //anonymous inner class
       Comparator<ListofLine> com= new Comparator<ListofLine>() {
           @Override
           public int compare(ListofLine l1, ListofLine l2) {
               return l1.rank < l2.rank ? 1 : -1;

           }
       };
       Collections.sort(listofLines,com);
        if(listofLines.size()>15)
            listofLines.subList(0,15).clear();
//       Collections.sort(listofLines,com);
       return listofLines;
       /* Cursor cursor= this.getReadableDatabase().rawQuery("select id from cleanline",null);

       if(cursor==null||cursor.getCount()<1)
            return listofLines;
       int totalrow =cursor.getCount();
       int totalcores=8-2;
       int start=0;
       s1=clean(s1);
       SQLiteDatabase db=this.getReadableDatabase();
       Replay []r=new Replay[totalcores];
       try {
           for (int i = 0; i < 8; i++) {
               r[0] = new Replay(start, start+(totalrow/totalcores), s1,this.getReadableDatabase());
               start+=totalrow/totalcores;
               r[0].start();
               //r[i].join();
           }
           while(r[0].isAlive());

       }
       catch(Exception e)
       {
            System.out.println(e);
       }
       finally {
           listofLines=Replay.getList();
       }*/
   }
   /* public Cursor getData()
    {
        /*SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",1);
        contentValues.put("text","my name");
        long i=db.insert("student",null,contentValues);
        contentValues=new ContentValues();
        contentValues.put("id",2);
        contentValues.put("text","my name2");
        i=db.insert("student",null,contentValues);
        //SQLiteDatabase db= this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from student",null);
        return res;*
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        //table which content real conversations
        // LineID	CharID	MovieID	Line
         db.execSQL("create table "+tname+" ("+col1+" text primary key ,"+col2+" text,"+col3+" text,"+col4+" text,"+col5+" integer,"+col6+" integer,"+col7+" integer,"+col8+" integer)");


        //table which content filtered line
        db.execSQL("create table cleanline(id integer primary key AUTOINCREMENT  ,lineid text ,line text)");

        //create table for user data
        db.execSQL("create table user (id integer primary key autoincrement , chat text , chatid integer , sentiments integer )");

        insertLine(db);
        insertFileteredData(db);
        //db.execSQL("create virtual table ftsline using fts4 (content='Lines',Line)");//'"+tname+"',"+col4+")");
       // db.execSQL("create virtual table ftsline using fts4 (content=\"tb_bank\",Line)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private String clean(String s)
    {
        //s=s.replaceAll("\\b//W|(! )\\b*"," ");
        // s=s.replaceAll("\\(|\\)|\\{|\\}|\\[|\\]","");
        //Remove the repeated words
      /*  String regex="(\\b\\w+\\b)(\\s*\\1\\b)+";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            s = s.replaceAll(matcher.group(), matcher.group(1));
        }*/

        String s1="the,on,a,is,was,be,being,been," +
                "does,did,have,has,had,are,to";
        s1=s1.toLowerCase();
        String[] arr=s1.split(",");
        s=s.trim().toLowerCase();
        for (String arr1 : arr) {
            s = s.replaceAll("\\b"+arr1+"\\b", "");
        }
        s=s.replaceAll("  ", " ");
        System.out.println(s);

        return s;
    }
}
