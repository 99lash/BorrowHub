package com.example.borrowhub.view;

/**
 * ISSUE: Conversion of React LoginScreen to Android XML and Java.
 * - Converted from: LoginScreen.tsx
 * - Credentials: username: "user", password: "123"
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide action bar for full screen feel
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        // Explicitly set the logo (already set in XML but ensuring programmatic control if needed)
        if (binding.loginBhLogo != null) {
            binding.loginBhLogo.setImageResource(R.drawable.bh_logo);
        }

        binding.signInButton.setOnClickListener(v -> handleSignIn());
    }

    private void handleSignIn() {
        String username = binding.usernameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_username), Toast.LENGTH_SHORT).show();
            binding.usernameInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            binding.passwordInput.requestFocus();
            return;
        }

        // Simple hardcoded login for now as per the placeholder requirements
        if (username.equals("user") && password.equals("123")) {
            Toast.makeText(this, getString(R.string.login_successful) + ": " + getString(R.string.welcome_message), Toast.LENGTH_SHORT).show();
            
            // Navigate to MainActivity after a short delay
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 500);
        } else {
            Toast.makeText(this, getString(R.string.invalid_credentials) + ": " + getString(R.string.error_message), Toast.LENGTH_LONG).show();
        }
    }
}