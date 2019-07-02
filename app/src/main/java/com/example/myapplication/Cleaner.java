package com.example.myapplication;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cleaner {
    private Context context;

    public Cleaner(Context context)
    {
        this.context=context;
    }
    public String syms(String s)
    {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("Synonyms")));
            int count = 0;
            //pr.println("Start from here\n");
            Pattern pattern;//= Pattern.compile("(\\b\\w+\\b)(\\s*\\1\\b)+");
            Matcher matcher;
            String synonyms, target[], source;
            List<String> list =new ArrayList();
            //Get Regular expretion from Synonyms file
            while ((synonyms = br.readLine()) != null) {
                list.add(synonyms);
            }
            s = s.replace(".", " ");
            s = s.replace("  ", " ");
            s = s.replace("?", " ?");
            s = s.replace("!", " !");
            s = s.replace("\"", "");


                // System.out.println("Started :-");
                for (String l : list) {
                    // System.out.println(lv.regex);
                    target=l.split("/");
                    pattern = Pattern.compile(target[1]);
                    matcher = pattern.matcher(s);//s.toLowerCase()
                    while (matcher.find()) {
                        s = matcher.replaceAll(target[0]);
                    }
                }
                s=sortForm(s);

        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        finally
        {
            return s;
        }
       // return s;
    }
    public String sortForm(String s) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("ShortForm")));
            int count = 0;
            //pr.println("Start from here\n");
            Pattern pattern;//= Pattern.compile("(\\b\\w+\\b)(\\s*\\1\\b)+");
            Matcher matcher;
            String shortf, target[], source;
            List<String> list = new ArrayList();
            //Get Regular expretion from Synonyms file
            while ((shortf = br.readLine()) != null) {
                list.add(shortf);
            }

            // System.out.println("Started :-");
            for (String l : list) {
                // System.out.println(lv.regex);
                target = l.split("/");
                pattern = Pattern.compile(target[1]);
                matcher = pattern.matcher(s.toLowerCase());
                while (matcher.find()) {
                    s = matcher.replaceAll(target[0]);
                }
            }

        } catch (IOException e) {
            System.out.println(e);

        } finally {
            return s;
        }
    }
    public String clean(String s)
    {

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
