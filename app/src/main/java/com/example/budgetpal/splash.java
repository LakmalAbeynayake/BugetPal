package com.example.budgetpal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetpal.ui.login.DatabaseHelper;
import com.example.budgetpal.ui.login.LoginActivity;

import android.widget.Toast;
public class splash extends AppCompatActivity {

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        try {
            Thread.sleep(2000); // Sleep for 2000 milliseconds (2 seconds)
        } catch (InterruptedException e) {
            // Handle interrupted exception if necessary
        }

        if (!dbHelper.checkUserExists()) {
            Toast.makeText(this, "No Users", Toast.LENGTH_SHORT).show();
            // Navigate to setup activity
            try {
                startActivity(new Intent(splash.this, SetupActivity.class));
                finish();
            }catch (Exception ex){
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            try {
                startActivity(new Intent(splash.this, LoginActivity.class));
                finish();
            }catch (Exception ignored){

            }
        }
    }
}