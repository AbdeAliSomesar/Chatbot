package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Sentiments
{
    private TreeMap<String,Integer> words = new TreeMap<>();
   // private Tree<String,Integer> positive = new Tree<>();
   // private TreeMap<String,Integer> negative = new TreeMap<>();
    private HashSet<String> positive = new HashSet();
    private HashSet<String> negative = new HashSet();
    //String positive="",negative="";
    private int count;
    private static Context context;
    Sentiments(Context context)
    {
        //System.out.println("check point 1");
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(context.getAssets().open("positive")));
            String line;
            while((line=br.readLine())!=null)
            {
                positive.add(line.trim());
            }
            //positive.replaceFirst("|","");
            br.close();
            br=new BufferedReader(new InputStreamReader(context.getAssets().open("Negative")));
            while((line=br.readLine())!=null)
            {
                negative.add(line.trim());            }
           // negative.replaceFirst("|","");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.context=context;
        Scanner input=null;
        /*try
        {
            DataInputStream textFileStream = new DataInputStream((context.getAssets().open("Pos.txt")));
           // input=new Scanner(textFileStream);
            input=new Scanner("good:1,bad:-1,best:2,worst:-2,perfect:2");//textFileStream);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*input=new Scanner("good:1,bad:-1,best:2,worst:-2,perfect:2");
        String s;
        String []arr1;
        String []arr2 = new String[2];
        // System.out.println("check point 2");
        try{
            while(input.hasNextLine())
            {
                s=input.nextLine();
                System.out.print(s);
                //System.out.println(s);
                arr1=s.split(",");
                for(String s2:arr1)
                {
                    arr2=s2.toLowerCase().split(":");
                    //System.out.println(arr2[0]+":"+arr2[1]);
                    words.put(arr2[0],Integer.parseInt(arr2[1]));
                }
            }
            //EditText et=(EditText)findViewById(R.id.edittext_chatbox);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }*/
    }
    private String clean(String s)
    {
        //s=s.replaceAll("\\b//W|(! )\\b*"," ");
       // s=s.replaceAll("\\(|\\)|\\{|\\}|\\[|\\]","");
        Cleaner cleaner =new Cleaner(context);
        s=cleaner.syms(s);
        String s1="the,on,a,is,im,was,do,dose,Be,being,been,Will,would,shall,should,may," +
                "might,must,can,could,Do,does,did,have,has,had,you're,to,i,i'm,am,we,you,he,she,they";
        s1=s1.toLowerCase();
        String[] arr=s1.split(",");
        s=s.trim().toLowerCase();
        for (String arr1 : arr) {
            s = s.trim().replaceAll("\\b"+arr1+"\\b", "");
        }
       // s=s.replaceAll("  ", " ");
       // s=s.replaceAll("."," ");
        System.out.println(s);

        return s;
    }
    public int moods(String s)
    {

        return 0;
    }
    public int positive(String s)
    {
        Log.d("myTag", "\n\nString:- "+s+"\n\n");
        s=clean(s.toLowerCase());
        //Contractions of negated auxiliary verbs in Standard English
        String s1="not,dosent,dont,arnt,cannot,couldnt,isnt,arent,wasnt,werent,cant,neednt,"
                +"isn't,aren't,wasn't,weren't,haven't,hasn't,hadn't,"
                + "don't,doesn't,didn't,can't,cannot,couldn't,mayn't,"
                + "mightn't,mustn't,shan't,shouldn't,won't,wouldn't,"
                + "daren't,needn't,oughtn't";
        s1=s1.replaceAll(","," ");
        int pos=0;
        //To count the no of found words
        count=0;
        Integer n;
        String arr[]=s.split(" +");
        for(int i=0;i<arr.length;i++)
        {
            boolean j=false;
            //if(s1.matches(arr[i]))
            if(arr[i].equals(""))
                continue;
            System.out.println(arr[i]);
            if(arr[i].equals("not"))
            {
                //To reverce the value of match word becausde of "not" eg:-not perfect or not bad
                if(i+1!=arr.length) {
                    //j = words.get(arr[i + 1]);
                    j=positive.contains(arr[i+1]);
                    if (j) {
                        pos -= 1;
                        count++;

                        arr[i] = arr[i + 1] = "";
                    }
                    else {
                        j=negative.contains(arr[i+1]);

                        if (j) {
                            pos += 1;
                            count++;
                            arr[i] = arr[i + 1] = "";
                        }
                    }
                }
                arr[i] = arr[i] = "";
            }
        }
        for(String i:arr)
        {
            if(i.equals(" "))
                continue;
            if (!i.equals("")) {
                //n = words.get(i);
                boolean b=false;
                b=positive.contains(i);
                //if (n != null) {
                if(b){
                    pos += 1;
                }
                else
                {
                    b=negative.contains(i);
                    if(b){
                        pos -=1;
                    }
                }

            }
        }
        return pos;
    }

}
