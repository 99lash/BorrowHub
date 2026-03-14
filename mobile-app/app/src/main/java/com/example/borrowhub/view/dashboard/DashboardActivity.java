package com.example.borrowhub.view.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.borrowhub.databinding.ActivityDashboardBinding;
import com.example.borrowhub.viewmodel.dashboard.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding initialization
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel initialization
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Observe ViewModel LiveData for stats
        viewModel.getTotalItems().observe(this, total ->
            binding.tvTotalItemsValue.setText(String.valueOf(total)));

        viewModel.getCurrentlyBorrowed().observe(this, borrowed ->
            binding.tvCurrentlyBorrowedValue.setText(String.valueOf(borrowed)));

        viewModel.getAvailableNow().observe(this, available ->
            binding.tvAvailableNowValue.setText(String.valueOf(available)));

        viewModel.getDueToday().observe(this, due ->
            binding.tvDueTodayValue.setText(String.valueOf(due)));

        // Setup Buttons Listeners
        binding.btnBorrowReturn.setOnClickListener(v -> {
            // TODO: Navigate to Borrow/Return screen
        });

        binding.btnManageInventory.setOnClickListener(v -> {
            // TODO: Navigate to Manage Inventory screen
        });
    }
}
