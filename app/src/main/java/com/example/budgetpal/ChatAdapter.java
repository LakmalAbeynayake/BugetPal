package com.example.budgetpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static List<Chat> chatList;
    private OnChatItemClickListener onChatItemClickListener;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(itemView, onChatItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void setOnChatItemClickListener(OnChatItemClickListener listener) {
        this.onChatItemClickListener = listener;
    }

    public interface OnChatItemClickListener {
        void onChatItemClick(String senderAddress);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView senderTextView;
        private TextView messageTextView;
        private TextView timeTextView;
        private OnChatItemClickListener onChatItemClickListener;

        public ChatViewHolder(@NonNull View itemView, OnChatItemClickListener listener) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            this.onChatItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Chat chat) {
            senderTextView.setText(chat.getSender());
            messageTextView.setText(chat.getMessage());
            timeTextView.setText(chat.getTime());
        }

        @Override
        public void onClick(View v) {
            if (onChatItemClickListener != null) {
                Chat chat = chatList.get(getAdapterPosition());
                onChatItemClickListener.onChatItemClick(chat.getSender());
            }
        }
    }
}