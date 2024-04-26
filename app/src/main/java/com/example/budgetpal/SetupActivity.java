package com.example.budgetpal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.budgetpal.ui.login.DatabaseHelper;
import com.example.budgetpal.ui.login.User;


public class SetupActivity extends AppCompatActivity {

    private EditText editTextName, editTextPIN;
    private RadioGroup radioGroup;
    private Button buttonSave;
    private DatabaseHelper dbHelper;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, proceed with user registration
                    saveUser();
                } else {
                    // Permission denied, show a message or take appropriate action
                    Toast.makeText(this, "SMS reading permission denied. User registration failed.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup);
        dbHelper = new DatabaseHelper(this);
        initializeViews();
        buttonSave.setOnClickListener(v -> {
            // Request SMS reading permission
            requestSMSPermission();
        });
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPIN = findViewById(R.id.editTextPIN);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            requestPermissionLauncher.launch(Manifest.permission.READ_SMS);
        } else {
            // Permission is already granted, proceed with user registration
            saveUser();
        }
    }

    private void saveUser() {
        String name = editTextName.getText().toString();
        String pin = editTextPIN.getText().toString();
        String loginMethod = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

        // Save user in the database

        User usr = new User(1, name, pin, loginMethod);

        boolean isSuccess = dbHelper.addUser(usr);

        if (isSuccess) {
            // User saved successfully
            Toast.makeText(this, "User registration successful", Toast.LENGTH_SHORT).show();
            // TODO: Proceed to next screen or perform other actions
        } else {
            // User registration failed
            Toast.makeText(this, "User registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with user registration
                saveUser();
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(this, "SMS reading permission denied. User registration failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int REQUEST_CODE_SMS_PERMISSION = 100;
}
