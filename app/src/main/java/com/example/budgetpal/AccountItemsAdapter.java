package com.example.budgetpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.ui.BankAccount;

import java.util.List;

public class AccountItemsAdapter extends RecyclerView.Adapter<AccountItemsAdapter.ViewHolder> {

    private static List<BankAccountItems> accountList;
    private OnClickListener onClickListener;
    private AccountItemsAdapter.OnAccItemClickListener onAccItemClickListener;

    public AccountItemsAdapter(List<BankAccountItems> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BankAccountItems account = accountList.get(position);
        holder.smsContent.setText(String.valueOf(account.getSmsContent()));
        holder.smsType.setText(String.valueOf(account.getSmsType()));
        holder.smsAmount.setText(String.valueOf(account.getAmount()));
        //holder.addedOnTextView.setText("Added on: " + account.getAddedOn());
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, account);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }



    public interface OnClickListener {
        void onClick(int position, BankAccountItems model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView smsContent;
        public TextView smsType;
        public TextView smsAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            smsContent = itemView.findViewById(R.id.t);
            smsType = itemView.findViewById(R.id.t2);
            smsAmount = itemView.findViewById(R.id.ts);
        }
    }
    public interface OnAccItemClickListener {
        void onAccItemClick(String senderAddress);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnAccItemClickListener(AccountItemsAdapter.OnAccItemClickListener listener) {
        this.onAccItemClickListener = listener;
    }
}