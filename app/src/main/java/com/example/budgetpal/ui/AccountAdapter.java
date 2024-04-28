package com.example.budgetpal.ui;

import android.accounts.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.budgetpal.Chat;
import com.example.budgetpal.ChatAdapter;
import com.example.budgetpal.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private static List<BankAccount> accountList;
    private OnClickListener onClickListener;
    private AccountAdapter.OnAccItemClickListener onAccItemClickListener;

    public AccountAdapter(List<BankAccount> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BankAccount account = accountList.get(position);
        holder.accountNameTextView.setText(account.getAccountName());
        holder.addedOnTextView.setText("Added on: " + account.getAddedOn());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, account);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }



    public interface OnClickListener {
        void onClick(int position, BankAccount model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView accountNameTextView;
        public TextView addedOnTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accountNameTextView = itemView.findViewById(R.id.account_name);
            addedOnTextView = itemView.findViewById(R.id.added_on);
        }
    }
    public interface OnAccItemClickListener {
        void onAccItemClick(String senderAddress);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnAccItemClickListener(AccountAdapter.OnAccItemClickListener listener) {
        this.onAccItemClickListener = listener;
    }
}