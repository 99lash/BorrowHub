package com.example.borrowhub.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.borrowhub.R;
import com.example.borrowhub.data.local.entity.ItemEntity;
import com.example.borrowhub.databinding.FragmentInventoryBinding;
import com.example.borrowhub.view.adapter.ItemAdapter;
import com.example.borrowhub.viewmodel.InventoryViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class InventoryFragment extends Fragment implements ItemAdapter.ItemActionListener {

    private static final String[] ITEM_TYPES = new String[]{"All Types", "Equipment", "Laptop"};

    private FragmentInventoryBinding binding;
    private InventoryViewModel viewModel;
    private ItemAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        boolean useCompactLayout = isCompactLayout();
        itemAdapter = new ItemAdapter(useCompactLayout, this);

        binding.rvInventory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInventory.setAdapter(itemAdapter);

        setupSearchFilter();
        setupTypeFilter();
        setupAddItemButton();
        observeInventory();
    }

    private void setupSearchFilter() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s == null ? "" : s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupTypeFilter() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                ITEM_TYPES
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeFilter.setAdapter(spinnerAdapter);

        binding.spinnerTypeFilter.setSelection(0);
        binding.spinnerTypeFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                viewModel.setTypeFilter(selectedType);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                viewModel.setTypeFilter("All Types");
            }
        });
    }

    private void setupAddItemButton() {
        binding.btnAddItem.setOnClickListener(v -> showAddEditDialog(null));
    }

    private void observeInventory() {
        viewModel.getFilteredItems().observe(getViewLifecycleOwner(), this::renderInventory);
    }

    private void renderInventory(List<ItemEntity> items) {
        itemAdapter.setItems(items);
        boolean isEmpty = items == null || items.isEmpty();
        binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEditItem(ItemEntity item) {
        showAddEditDialog(item);
    }

    @Override
    public void onDeleteItem(ItemEntity item) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Item")
                .setMessage("Delete " + item.name + " from inventory?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteItem(item.id);
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showAddEditDialog(@Nullable ItemEntity itemToEdit) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_inventory_item, null);

        TextInputEditText etItemName = dialogView.findViewById(R.id.etItemName);
        AutoCompleteTextView acType = dialogView.findViewById(R.id.acType);
        TextInputEditText etTotalQuantity = dialogView.findViewById(R.id.etTotalQuantity);
        TextInputEditText etAvailableQuantity = dialogView.findViewById(R.id.etAvailableQuantity);

        String[] dialogTypes = new String[]{"Equipment", "Laptop"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                dialogTypes
        );
        acType.setAdapter(typeAdapter);

        if (itemToEdit != null) {
            etItemName.setText(itemToEdit.name);
            acType.setText(itemToEdit.type, false);
            etTotalQuantity.setText(String.valueOf(itemToEdit.totalQuantity));
            etAvailableQuantity.setText(String.valueOf(itemToEdit.availableQuantity));
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(itemToEdit == null ? "Add New Item" : "Edit Item")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(itemToEdit == null ? "Add Item" : "Save Changes", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = asString(etItemName.getText());
            String type = asString(acType.getText());
            Integer totalQuantity = parsePositiveInt(etTotalQuantity.getText());
            Integer availableQuantity = parsePositiveInt(etAvailableQuantity.getText());

            if (name.isEmpty() || type.isEmpty() || totalQuantity == null || availableQuantity == null) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (availableQuantity > totalQuantity) {
                Toast.makeText(requireContext(), "Available quantity cannot exceed total", Toast.LENGTH_SHORT).show();
                return;
            }

            if (itemToEdit == null) {
                viewModel.addItem(name, type, totalQuantity, availableQuantity);
                Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.updateItem(itemToEdit.id, name, type, totalQuantity, availableQuantity);
                Toast.makeText(requireContext(), "Item updated", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        }));

        dialog.show();
    }

    private boolean isCompactLayout() {
        int screenWidthDp = getResources().getConfiguration().screenWidthDp;
        boolean isLandscape = getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        return !isLandscape && screenWidthDp < 600;
    }

    private String asString(CharSequence value) {
        return value == null ? "" : value.toString().trim();
    }

    private Integer parsePositiveInt(CharSequence value) {
        try {
            int parsed = Integer.parseInt(asString(value));
            return parsed < 0 ? null : parsed;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
