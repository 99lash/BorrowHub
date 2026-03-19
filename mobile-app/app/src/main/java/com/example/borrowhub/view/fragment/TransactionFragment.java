package com.example.borrowhub.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.FragmentTransactionBinding;
import com.google.android.material.tabs.TabLayout;

public class TransactionFragment extends Fragment {

    private static final int TAB_BORROW = 0;
    private static final int TAB_RETURN = 1;

    private FragmentTransactionBinding binding;

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

        setupTabs();

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

    private void showTab(int position) {
        Fragment fragment;
        if (position == TAB_RETURN) {
            fragment = new ReturnItemFragment();
        } else {
            fragment = new BorrowPlaceholderFragment();
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

    // -------------------------------------------------------------------------
    // Inline placeholder for the Borrow tab (not yet implemented)
    // -------------------------------------------------------------------------

    public static class BorrowPlaceholderFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            android.widget.TextView tv = new android.widget.TextView(requireContext());
            tv.setText(R.string.transaction_borrow_placeholder);
            tv.setGravity(android.view.Gravity.CENTER);
            tv.setPadding(32, 64, 32, 64);
            tv.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                            requireContext(),
                            com.example.borrowhub.R.color.gray_500));
            return tv;
        }
    }
}
