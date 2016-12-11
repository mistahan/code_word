package com.example.alexhan.codeword;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * A RecyclerView Adapter for the savedList.
 */
public class MessageCardAdapter extends RecyclerView.Adapter<MessageCardAdapter.MessageViewHolder> implements View.OnTouchListener{

    Context context;
    ArrayList<Messages> savedListHolder;

    public MessageCardAdapter(Context con,ArrayList<Messages> holder) {
        this.context = con;
        this.savedListHolder = holder;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_messages_card, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {

        Messages message = savedListHolder.get(position);
        holder.userView.setText(message.getFromUser());
        holder.messageView.setText(message.getMessage());
        holder.targetView.setText(message.getTargetUser());

    }

    @Override
    public int getItemCount() {
        return savedListHolder.size();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userView;
        TextView messageView;
        TextView targetView;

        public MessageViewHolder(View itemView) {
            super(itemView);

            userView = (TextView) itemView.findViewById(R.id.userView);
            messageView = (TextView) itemView.findViewById(R.id.messageView);
            targetView = (TextView) itemView.findViewById(R.id.targetView);
        }

        @Override
        public void onClick(View v) {
            if(v == userView || v == messageView || v== targetView){
                Intent intent = new Intent();
                intent.putExtra("fromUser",userView.getText().toString());
                System.out.println("Clicked");
            }
        }
    }
}