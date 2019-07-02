package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {
    private List<MessageClass> message;
    public MessageListAdapter(List<MessageClass> message)
    {
        this.message=message;
    }
    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return  message.get(position).getId();
    }
    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //if(message.get(i).getId()==2)
        if(i==1){

            View view = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
            return new MessageListViewHolder(view);
        }
        else
        {
            View view = inflater.inflate(R.layout.item_message_received, viewGroup, false);
            return new MessageListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder messageListViewHolder, int i) {
    String title = message.get(i).getText();
    messageListViewHolder.textView1.setText(title);
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView1,textView2;
        public MessageListViewHolder(View itemView)
        {
            super(itemView);
            textView1=(TextView) itemView.findViewById(R.id.text_message_body);
            //textView2=(TextView) itemView.findViewById(R.id.text_message_time);
        }

    }
}
