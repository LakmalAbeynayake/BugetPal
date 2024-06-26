package com.example.budgetpal;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.budgetpal.ui.AccountAdapter;
import com.example.budgetpal.ui.BankAccount;
import com.example.budgetpal.ui.home.HomeFragment;
import com.example.budgetpal.ui.login.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_SMS = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
    private RecyclerView accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Initialize RecyclerView
        accounts = findViewById(R.id.accounts_list);
        accounts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set adapter
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<BankAccount> accountList = databaseHelper.getAllAccounts();
        final AccountAdapter acc = new AccountAdapter(accountList);

        //acc.setOnAccItemClickListener((AccountAdapter.OnAccItemClickListener) this); // Set the click listener
        accounts.setAdapter(acc);
        acc.setOnClickListener(new AccountAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BankAccount model) {
                //Toast.makeText(MainActivity.this, String.valueOf(model.getAccountId()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AccountDetails.class);
                intent.putExtra("ACCOUNT_ID",model.getAccountId());
                startActivity(intent);
            }
        });

    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                PERMISSION_REQUEST_SMS);
    }

    private void startNewActivity() {
        // Replace SetupActivity.class with the activity you want to start
        Intent intent = new Intent(MainActivity.this, addAccount.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start new activity
                startNewActivity();
                Toast.makeText(this, "Loading SMS's", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle this case as per your app's requirement
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void addAccount(View view) {
        try {
            Toast.makeText(this, "Working HAHA", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(MainActivity.this, SetupActivity.class));
            //finish();


            // Check if SMS permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted, start new activity
                startNewActivity();
            } else {
                // Permission is not granted, request it from the user
                requestSmsPermission();
            }


        }catch (Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void check_accounts() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<BankAccount> accountList = databaseHelper.getAllAccounts();
// Process the accountList as needed
        for (BankAccount account : accountList) {
            // Do something with the account object
            // String accountName = account.getAccountName();
            // String addedOn = account.getAddedOn();
        }
        // Pass the account data to the HomeFragment
        if (homeFragment != null) {
            homeFragment.setAccountData(accountList);
        }
    }
}