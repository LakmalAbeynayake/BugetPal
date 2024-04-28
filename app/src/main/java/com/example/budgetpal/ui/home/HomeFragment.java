package com.example.budgetpal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.ChatAdapter;
import com.example.budgetpal.R;
import com.example.budgetpal.databinding.FragmentHomeBinding;
import com.example.budgetpal.ui.AccountAdapter;
import com.example.budgetpal.ui.BankAccount;
import com.example.budgetpal.ui.login.DatabaseHelper;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView accountsRecyclerView;
    private AccountAdapter accountAdapter;

    public void setAccountData(List<BankAccount> accountList) {
        accountAdapter = new AccountAdapter(accountList);
        accountsRecyclerView.setAdapter(accountAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //accountsRecyclerView = view.findViewById(R.id.accounts_list);
        //accountsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //return view;


        // Initialize RecyclerView
        accountsRecyclerView = view.findViewById(R.id.accounts_list);
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
        List<BankAccount> accountList = databaseHelper.getAllAccounts();

        // Initialize and set adapter
        AccountAdapter accAdapter = new AccountAdapter(accountList);
        //AccountAdapter.setOnChatItemClickListener(this); // Set the click listener
        //recyclerView.setAdapter(chatAdapter);

        Toast.makeText(this.getContext(),"HEY HEY", Toast.LENGTH_SHORT);

        return view;
    }
}