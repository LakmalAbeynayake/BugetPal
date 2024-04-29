package com.example.budgetpal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.ui.AccountAdapter;
import com.example.budgetpal.ui.BankAccount;
import com.example.budgetpal.ui.login.DatabaseHelper;

import java.util.List;

public class AccountDetails extends AppCompatActivity {

    private RecyclerView account_items;

    private Integer accountId;

    private AccountItemsAdapter acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        if (intent != null) {
            accountId = intent.getIntExtra("ACCOUNT_ID", -1); // -1 is the default value if key not found
            // Now you have the accountId value, you can use it as needed
            Toast.makeText(this, String.valueOf(accountId), Toast.LENGTH_SHORT).show();
        }


        // Initialize RecyclerView
        account_items = findViewById(R.id.account_item_list);
        account_items.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set adapter
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        try {
            //Toast.makeText(AccountDetails.this, String.valueOf("REC:"+accountId), Toast.LENGTH_SHORT).show();
            List<BankAccountItems> bankAccountItemsList = databaseHelper.getAllAccountItems(accountId);
            acc = new AccountItemsAdapter(bankAccountItemsList);

            //acc.setOnAccItemClickListener((AccountAdapter.OnAccItemClickListener) this); // Set the click listener
            account_items.setAdapter(acc);
            acc.setOnClickListener(new AccountItemsAdapter.OnClickListener(){
                @Override
                public void onClick(int position, BankAccountItems model) {
                    Toast.makeText(AccountDetails.this, String.valueOf(model.getAccountId()), Toast.LENGTH_SHORT).show();
    //                Intent intent = new Intent(AccountDetails.this, AccountDetails.class);
    //                intent.putExtra("ACCOUNT_ID",model.getAccountId());
    //                startActivity(intent);
                }
            });

        }catch (Exception ex){
            Log.e("",String.valueOf(ex));
            Toast.makeText(AccountDetails.this, String.valueOf("Empty DB"), Toast.LENGTH_SHORT).show();
            Toast.makeText(AccountDetails.this, String.valueOf(ex), Toast.LENGTH_SHORT).show();
        }
    }
}