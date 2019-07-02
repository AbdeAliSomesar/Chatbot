/*package com.example.myapplication;

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

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
         mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        m=new ArrayList<>();
        if(res!=null&&res.getCount()>0){
            res.moveToFirst();
            do{
                m.add(new MessageClass(res.getInt(1), res.getString(0)));
            }while(res.moveToNext());
        }
        mMessageRecycler.setAdapter(new MessageListAdapter(m));
        mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
    }
    //SpeechTo Text
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

        String s = et.getText().toString();
        s = s.trim();
        if (s.equals(""))
            return;
        et.setText("");
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("chat", s);
            contentValues.put("chatid", 1);
            contentValues.put("sentiments", sentiments.positive(s));
            ts.getWritableDatabase().insert("user", null, contentValues);
            m.add(new MessageClass(1, s));
            s = s.toLowerCase();
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
            for(ListofLine l:list)
                m.add(new MessageClass(2,l.line+l.sentiments));
            mMessageRecycler.setAdapter(new MessageListAdapter(m));
            // mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
            mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
        }
        catch(Exception e)
        {
            m.add(new MessageClass(2, "Some Error Occurred"));
            mMessageRecycler.setAdapter(new MessageListAdapter(m));
            // mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount()-1);
            mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
        }
    }
}

*/