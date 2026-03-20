package com.example.borrowhub.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.FragmentTransactionBinding;
import com.example.borrowhub.viewmodel.TransactionViewModel;
import com.google.android.material.tabs.TabLayout;

public class TransactionFragment extends Fragment {

    private static final int TAB_BORROW = 0;
    private static final int TAB_RETURN = 1;

    private FragmentTransactionBinding binding;
    private TransactionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        setupTabs();

        // Default to Borrow tab
        if (savedInstanceState == null) {
            showTab(TAB_BORROW);
        }
    }

    private void setupTabs() {
        binding.tabLayoutTransaction.addTab(
                binding.tabLayoutTransaction.newTab().setText(R.string.transaction_tab_borrow));
        binding.tabLayoutTransaction.addTab(
                binding.tabLayoutTransaction.newTab().setText(R.string.transaction_tab_return));

        binding.tabLayoutTransaction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Replaces the content of the transaction container with the appropriate fragment.
     */
    private void showTab(int position) {
        Fragment fragment;
        if (position == TAB_RETURN) {
            fragment = new ReturnItemFragment();
        } else {
            // BorrowItemFragment is the main borrow workflow implementation
            fragment = new BorrowItemFragment();
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.transactionTabContainer, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
