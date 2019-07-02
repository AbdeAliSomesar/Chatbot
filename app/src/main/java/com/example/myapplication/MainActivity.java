package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private EditText et;
    private Sentiments sentiments;
    private MessageListAdapter mMessageAdapter;
    private List<MessageClass> m;
    private TSQLite ts;
    private  ChatData chatData;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sentiments = new Sentiments(this);
        et=findViewById(R.id.edittext_chatbox);

        ts = new TSQLite(this);
        //ts.Insert();
        chatData=new ChatData(this);
        SQLiteDatabase db=ts.getWritableDatabase();
       Cursor res=db.rawQuery("SELECT chat, chatid FROM user",null);
       //Integer value=sentiments.positive("The java string replaceAll() method returns a string replacing all the sequence of characters matching regular expression and replacement string");
       //et.setText(value.toString());
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        //mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        m=new ArrayList<>();//{"java","c"," fefew","jjujhihj","hnuhuh","huhh","huhuhu","hnuhuhu","huhuhu","hhuhu","hugu","hhuh","bgygygygy","gggygyg","fwefee"};

        /*m.add(new MessageClass(1,"fsfsf"));
        m.add(new MessageClass(2,"fsddfsf"));
        m.add(new MessageClass(1,"fsfrwwsf"));
        m.add(new MessageClass(2,"fsfsf"));
        m.add(new MessageClass(2,"fsfsf"));*/
       // if(res.getCount()!=0) {
        if(res!=null&&res.getCount()>0){
           // StringBuffer buffer=null;
            res.moveToFirst();
            do
            {
                /*buffer = new StringBuffer();
                buffer.append(res.getString(3));*/
               // buffer.append(res.getDouble(1));

                m.add(new MessageClass(res.getInt(1), res.getString(0)));
            }while(res.moveToNext());
            //m.add(new MessageClass(2, buffer.toString()));
        }
       // else
           // m.add(new MessageClass(2, "No Data"));// m.add(new MessageClass(2,Boolean.toString(ts.ReadExcelSheet())));


        mMessageRecycler.setAdapter(new MessageListAdapter(m));
        mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
        //send();


    }
    //SpeechTo Text
    public int sentimentsAnalysis(String s)
    {
       // String s=et.getText()+Integer.toString(sentiments.positive(et.getText().toString()));
       return sentiments.positive(et.getText().toString());
       /* m.add(new MessageClass(1,s));
        mMessageRecycler.setAdapter(new MessageListAdapter(m));
       // mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
        mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);*/

        // mMessageAdapter.notifyDataSetChanged();

        //et.setText(et.getText().toString()+" Score :- "+Integer.toString(sentiments.positive(et.getText().toString())));
    }
    public void getSpeech(View v)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,10);
        }
        else{
            Toast.makeText(this,"Feature not supported in your Device",Toast.LENGTH_SHORT).show();
        }



    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode)
        {
            case 10:
                if(resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et.setText(result.get(0));
                }
        }
    }
    public void send(View v) {

        String s = et.getText().toString();//+Integer.toString(sentiments.positive(et.getText().toString()));//"not hacking and gagging and spitting part please";
        s = s.trim();
        if (s.equals(""))
            return;
        et.setText("");
        //id integer primary key autoincrement , chat text , chatid integer , sentiments integer )
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("chat", s);
            contentValues.put("chatid", 1);
            contentValues.put("sentiments", sentiments.positive(s));
            ts.getWritableDatabase().insert("user", null, contentValues);
            m.add(new MessageClass(1, s));
            s = s.toLowerCase();
            //TSQLite ts=new TSQLite(this);
            //int senti;
            List<String> l1 = chatData.getChat(s);
            if (!l1.isEmpty()) {
                int size = l1.size();
                int random = new Random().nextInt(size);
                m.add(new MessageClass(2, l1.get(random)));
                mMessageRecycler.setAdapter(new MessageListAdapter(m));
                mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                contentValues = new ContentValues();
                contentValues.put("chat", l1.get(random));
                contentValues.put("chatid", 2);
                contentValues.put("sentiments", sentiments.positive(l1.get(random)));
                ts.getWritableDatabase().insert("user", null, contentValues);

                return;
            }


            //  List<ListofLine> list=chatData.getReplay(s);//ts.compareText(s);
            List<ListofLine> list = chatData.getReplay(s);
            if (list.isEmpty()) {

                m.add(new MessageClass(2, "Please try something else :)"));
                mMessageRecycler.setAdapter(new MessageListAdapter(m));
                mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                contentValues = new ContentValues();
                contentValues.put("chat", "Please try something else :");
                contentValues.put("chatid", 2);
                contentValues.put("sentiments", 0);
                ts.getWritableDatabase().insert("user", null, contentValues);
                return;
            }
            Cleaner cleaner=new Cleaner(this);
            for (int i=0;i<list.size();i++) {
                list.get(i).sentiments= sentiments.positive(cleaner.syms(list.get(i).line));
            }

            Collections.sort(list, new Comparator<ListofLine>() {
                @Override
                public int compare(ListofLine s1, ListofLine s2) {
                    if (s1.sentiments < s2.sentiments)
                        return 1;
                    else
                        return -1;
                }
            });
            contentValues = new ContentValues();
            contentValues.put("chat", list.get(0).line);
            contentValues.put("chatid", 2);

            contentValues.put("sentiments", list.get(0).sentiments);
            ts.getWritableDatabase().insert("user", null, contentValues);
            m.add(new MessageClass(2, list.get(0).line + list.get(0).sentiments));
        /*
        String s1="\'";//'//=""+list.size();
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
            /*for(ListofLine sline:list)
                if(sline.line!=null)
                    m.add(new MessageClass(2,sline.line));*/

            //}
            mMessageRecycler.setAdapter(new MessageListAdapter(m));
            // mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
            mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
        }
        catch(Exception e)
        {
            m.add(new MessageClass(2, "Try something else"));
            mMessageRecycler.setAdapter(new MessageListAdapter(m));
            // mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
            mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
        }
    }
}

