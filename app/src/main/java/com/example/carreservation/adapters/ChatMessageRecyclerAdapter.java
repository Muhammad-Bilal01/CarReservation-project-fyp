package com.example.carreservation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.helper.FirebaseUtils;
import com.example.carreservation.models.ChatMessageModel;
import com.example.carreservation.models.UserModel;

import java.util.ArrayList;

public class ChatMessageRecyclerAdapter extends RecyclerView.Adapter<ChatMessageRecyclerAdapter.ChatViewHolder> {


    Context context;
    private ArrayList<ChatMessageModel> chatsArrayList;

    public ChatMessageRecyclerAdapter(Context context, ArrayList<ChatMessageModel> chatsArrayList) {
        this.context = context;
        this.chatsArrayList = chatsArrayList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessageModel chats = chatsArrayList.get(position);

        if (chats.getSenderId().equals(FirebaseUtils.currentUserId())) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(chats.getMessage());
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(chats.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return chatsArrayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView, rightChatTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textView);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textView);

        }
    }
}
