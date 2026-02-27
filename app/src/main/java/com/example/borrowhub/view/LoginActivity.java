package com.example.borrowhub.view;

/**
 * ISSUE: Conversion of React LoginScreen to Android XML and Java.
 * - Converted from: LoginScreen.tsx
 * - Credentials: username: "user", password: "123"
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.borrowhub.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar for full screen feel
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        signInButton = findViewById(R.id.sign_in_button);
        
        // Explicitly set the logo
        ImageView logoView = findViewById(R.id.login_bh_logo);
        if (logoView != null) {
            logoView.setImageResource(R.drawable.app_icon);
        }

        signInButton.setOnClickListener(v -> handleSignIn());
    }

    private void handleSignIn() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_username), Toast.LENGTH_SHORT).show();
            usernameInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
            return;
        }

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