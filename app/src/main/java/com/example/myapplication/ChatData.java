package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatData{

    private Context context;
    private Cleaner cleaner;
    public List<String> getChat(String s)
    {
        s=cleaner.sortForm(s);
        List<String> list=new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("Basic Chat")));
            String line,arr1[],aar2[];
            outer:
            while ((line=br.readLine())!=null)
            {
                arr1=line.split("=");
                aar2=arr1[0].split("\\|");
                for(String m:aar2)
                    if(m.trim().equals(s))
                    {
                        aar2=arr1[1].split("\\|");
                        for(String m2:aar2)
                            list.add(m2);
                        break outer;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
    private List<ListofLine> match(String s )
    {
        List<ListofLine> list =new ArrayList();
        TSQLite ts =new TSQLite(context);
       // Cursor cursor=ts.getReadableDatabase().rawQuery("select lineid, line from lines",null);
        Cursor cursor= ts.getReadableDatabase().rawQuery("select lineid,Line from cleanline",null);

        if(cursor!=null&&cursor.getCount()>0)
        {
            float f=0;
            int size=s.split(" ").length;
            cursor.moveToFirst();
            do
            {
                String line=cursor.getString(1);

                if(line.matches(".*\\b"+s+"\\b.*"))
                {
                    f=(size*100)/line.split(" ").length;
                    list.add(new ListofLine(cursor.getString(0),f,cursor.getString(1)));
                    if(list.size()>400)
                        break;
                }
            }while(cursor.moveToNext());
            Comparator<ListofLine> com=new Comparator<ListofLine>() {
                @Override
                public int compare(ListofLine s1, ListofLine s2) {
                    //if(
                            return Float.compare(s2.rank,s1.rank);
                      /*  return 1;
                    else
                        return -1;*/
                }
            };
            try {
                Collections.sort(list, com);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
        if(list.size()>15)
            list.subList(0,15).clear();

        return list;
    }

    public ChatData(Context context)
    {
        this.context=context;
        cleaner=new Cleaner(context);
    }

    public  List<ListofLine> getReplay(String s) {
        String replay = null,ids,r=null;
        TSQLite ts = new TSQLite(context);
        s=cleaner.syms(s);
        List<ListofLine> listofLines = match(s);
        boolean mtch=false;
        if(listofLines.isEmpty())
            listofLines=ts.compareText(s);
        else {
            outer1:
            for (int pos = 0; pos < listofLines.size(); pos++) {
                try {
                    String listid=listofLines.get(pos).id;
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(context.getAssets().open("Conversations")));
                    String check, arr[];
                    outer2:
                    while ((ids = br.readLine()) != null) {
                        check = "\'" +listid+ "\'";
                        //       if(ids.matches("\'"+listofLines.get(0).id+"\'"))
                        arr = ids.split(" ");
                        for (int i = 0; i < arr.length; i++) {
                            if (check.equals(arr[i])) {
                                if ((i + 1) < arr.length){
                                    mtch=true;
                                    break outer1;
                                }
                                break outer2;
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
        if(!mtch)
            listofLines=ts.compareText(s);
        List<ListofLine> list= new ArrayList();
        if(listofLines.isEmpty())
            return list;

        int index,listindex=0,listid=0,listsize=0;
        if(listofLines.size()>1)
            index=1;
        else
            index= 0;
       do {


           try {
               BufferedReader br = new BufferedReader(
                       new InputStreamReader(context.getAssets().open("Conversations")));
               String check, arr[];
               outer:
               while ((ids = br.readLine()) != null) {
                   check = "\'" + listofLines.get(listid).id + "\'";
                   //       if(ids.matches("\'"+listofLines.get(0).id+"\'"))
                   arr = ids.split(" ");
                   for (int i = 0; i < arr.length; i++){
                       if (check.equals(arr[i])){
                           if((i + 1) < arr.length)
                                r = arr[++i];
                           break outer;
                       }
                   }

                /*    if(ids.matches(check))
                {  r=ids;
                break;}*/
               }
               listid++;
               int i = 0;
               //declare a list to store replays
               String sequence = null;
               //List<ListofLine>[] list = new ArrayList[3];
               if (r != null) {
                   // r.replaceAll("[","");
                   // r.replaceAll("]","");
                   // String arr[]=r.split(",");
                /*boolean b=false;
                int index=0;
                for(i=1;i<arr.length;i++)
                {
                    sequence=arr[i].replaceAll("\'","").replaceAll("l","");
                    if(Integer.parseInt(sequence)>Integer.parseInt(listofLines.get(0).id.replaceAll("\'","").replaceAll("l","")))
                    {
                        b=true;
                        index=i;
                        break;
                    }
                }
                if(b)
                {
                    sequence="";
                    while(i<arr.length)
                        sequence+=arr[i++];
                }*/
                   String sql = null;
              /* // if(b)
               // {
                   // sql="select lineid,line from Lines where lineid in ("+sequence+")";
                //}
                else
                    return null;*/
                   sql = "select line,Sentiments from lines where lineid = " + r;
                   Cursor cursor = ts.getReadableDatabase().rawQuery(sql, null);

                   if (cursor != null && cursor.getCount() > 0) {
                       int j = 0;
                       //list[0]=new ArrayList();
                       cursor.moveToFirst();
                       //do {
                       list.add(new ListofLine(cursor.getString(0),cursor.getInt(1)));
                       // }while(cursor.moveToNext());

                   }
               }
               else
               {
                   listindex++;
               }


           } catch (IOException e) {
               e.printStackTrace();
           }
           if(list.isEmpty())
               listsize=0;
           else
               listsize=list.size();

       }while(((listofLines.size()>listindex)&&(listofLines.size()>index+1)) &&((listsize<4)&&(listofLines.get(listindex).rank-listofLines.get(index++).rank)<=10f));
        //return listofLines;
        return list;
    }

        /*String s1="\'";//'//=""+list.size();
        int i;
        for(i=0;i<list.size()-1&&i<6;i++)
        {
            s1=s1+list.get(i).id+"\'"+","+"\'";
        }
        s1+=list.get(i).id;
        s1=s1+"\'";
        float rank;
        String sql="select lineid,line from Lines where lineid in ("+s1+")";
        Cursor cursor = ts.getReadableDatabase().rawQuery(sql,null);
        if(cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                {
                    s1=cursor.getString(1);
                    for(ListofLine slist:list)
                    {
                        if(slist.id==s1)
                            slist.line=cursor.getString(1);
                    }


                }
            }while(cursor.moveToNext());
            for(ListofLine sline:list)
                if(sline.line!=null)
                    m.add(new MessageClass(2,sline.line));
        }
        return ;
    }*/
}
