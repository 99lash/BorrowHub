package com.example.borrowhub.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.ActivityStudentManagementBinding;

public class StudentManagementActivity extends AppCompatActivity {

    public static final String EXTRA_OPEN_DASHBOARD = "open_dashboard";

    private ActivityStudentManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener(v -> navigateBackToDashboard());
    }

    @Override
    public boolean onSupportNavigateUp() {
        navigateBackToDashboard();
        return true;
    }

    private void navigateBackToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_OPEN_DASHBOARD, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
