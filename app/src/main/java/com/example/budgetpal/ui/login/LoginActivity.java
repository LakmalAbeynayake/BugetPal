package com.example.budgetpal.ui.login;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetpal.MainActivity;
import com.example.budgetpal.R;
import com.example.budgetpal.SetupActivity;
import com.example.budgetpal.splash;
import com.example.budgetpal.ui.login.LoginViewModel;
import com.example.budgetpal.ui.login.LoginViewModelFactory;
import com.example.budgetpal.databinding.ActivityLoginBinding;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // Dummy user data (replace this with actual user data retrieval logic)
    private String userName = "John Doe";
    private String loginMethod = "Biometric"; // Example default login method
    private String pin = "1234"; // Example PIN

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper db = new DatabaseHelper(this);

        User usr = db.getLastUser();
        userName = usr.getName();
        loginMethod = usr.getLoginMethod();
        pin = usr.getPin();

        Toast.makeText(this, loginMethod, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, pin.toString(), Toast.LENGTH_SHORT).show();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (loginMethod.equals("Biometric")) {
            executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(LoginActivity.this,
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    showToast("Authentication error: " + errString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

                    try {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }catch (Exception ex){
                        Toast.makeText(LoginActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    showToast("Authentication failed");
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build();

            // Trigger biometric authentication automatically
            biometricPrompt.authenticate(promptInfo);
        } else {
            binding.username.setText(userName);
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

