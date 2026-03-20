package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionViewModel extends AndroidViewModel {

    // --- Simulated Databases (from master) ---
    private static final Map<String, String[]> STUDENT_DATABASE = new HashMap<>();
    public static final String[] ITEM_TYPES = {"Equipment", "Laptop"};
    public static final Map<String, String[]> ITEMS_BY_TYPE = new HashMap<>();

    static {
        STUDENT_DATABASE.put("2024-12345", new String[]{"Sarah Chen", "BS Computer Science"});
        STUDENT_DATABASE.put("2024-23456", new String[]{"Emily Rodriguez", "BS Information Technology"});
        STUDENT_DATABASE.put("2024-34567", new String[]{"Mark Santos", "BS Computer Engineering"});

        ITEMS_BY_TYPE.put("Equipment", new String[]{
                "Projector - Epson EB-X41",
                "Camera - Canon EOS R6",
                "Conference Microphone",
                "Extension Cable 10m",
                "Wireless Presenter",
                "Tripod Stand",
                "HDMI Cable 5m"
        });
        ITEMS_BY_TYPE.put("Laptop", new String[]{
                "Laptop - Dell XPS 15",
                "Laptop - MacBook Pro 16",
                "Laptop - Lenovo ThinkPad"
        });
    }

    // --- Models (merged) ---

    /** Lightweight in-memory model for an active borrow transaction (from feature branch). */
    public static class ActiveBorrow {
        public final long id;
        public final String studentNumber;
        public final String studentName;
        public final String course;
        public final String collateral;
        public final List<String> items;
        public final String borrowDate;
        public final String borrowTime;

        public ActiveBorrow(long id, String studentNumber, String studentName,
                            String course, String collateral, List<String> items,
                            String borrowDate, String borrowTime) {
            this.id = id;
            this.studentNumber = studentNumber;
            this.studentName = studentName;
            this.course = course;
            this.collateral = collateral;
            this.items = items;
            this.borrowDate = borrowDate;
            this.borrowTime = borrowTime;
        }
    }

    /** Model for dynamic item rows in the Borrow form (from master). */
    public static class ItemRow {
        public String type;
        public String name;

        public ItemRow() {
            this.type = "";
            this.name = "";
        }

        public ItemRow(String type, String name) {
            this.type = type == null ? "" : type;
            this.name = name == null ? "" : name;
        }
    }

    // --- State: Borrow Workflow (from master) ---
    private final MutableLiveData<String> studentNameInput = new MutableLiveData<>("");
    private final MutableLiveData<String> courseInput = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> studentFound = new MutableLiveData<>(false);
    private final MutableLiveData<List<ItemRow>> itemRows = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> submitted = new MutableLiveData<>(false);
    private final MutableLiveData<String> submitError = new MutableLiveData<>(null);

    // --- State: Return Workflow (from feature branch) ---
    private final MutableLiveData<List<ActiveBorrow>> activeBorrows =
            new MutableLiveData<>(seedActiveBorrows());
    private final MutableLiveData<List<ActiveBorrow>> filteredBorrows =
            new MutableLiveData<>(new ArrayList<>());
    private String normalizedSearch = "";

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        // Initialize Borrow form
        List<ItemRow> initial = new ArrayList<>();
        initial.add(new ItemRow());
        itemRows.setValue(initial);

        // Initialize Return list
        applyFilters();
    }

    // --- Getters: Borrow Workflow ---
    public LiveData<String> getStudentNameInput() { return studentNameInput; }
    public LiveData<String> getCourseInput() { return courseInput; }
    public LiveData<Boolean> isStudentFound() { return studentFound; }
    public LiveData<List<ItemRow>> getItemRows() { return itemRows; }
    public LiveData<Boolean> isSubmitted() { return submitted; }
    public LiveData<String> getSubmitError() { return submitError; }

    // --- Getters: Return Workflow ---
    public LiveData<List<ActiveBorrow>> getFilteredBorrows() { return filteredBorrows; }

    // --- Logic: Student Lookup ---
    public void lookupStudent(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            studentNameInput.setValue("");
            courseInput.setValue("");
            studentFound.setValue(false);
            return;
        }
        String[] info = STUDENT_DATABASE.get(studentNumber.trim());
        if (info != null) {
            studentNameInput.setValue(info[0]);
            courseInput.setValue(info[1]);
            studentFound.setValue(true);
        } else {
            studentNameInput.setValue("");
            courseInput.setValue("");
            studentFound.setValue(false);
        }
    }

    // --- Logic: Borrow Row Management ---
    public void addItemRow() {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        current.add(new ItemRow());
        itemRows.setValue(current);
    }

    public void removeItemRow(int index) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (current.size() > 1 && index >= 0 && index < current.size()) {
            current.remove(index);
            itemRows.setValue(current);
        }
    }

    public void updateItemRowType(int index, String type) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (index >= 0 && index < current.size()) {
            current.get(index).type = type == null ? "" : type;
            current.get(index).name = "";
            itemRows.setValue(current);
        }
    }

    public void updateItemRowName(int index, String name) {
        List<ItemRow> current = deepCopy(itemRows.getValue());
        if (index >= 0 && index < current.size()) {
            current.get(index).name = name == null ? "" : name;
            itemRows.setValue(current);
        }
    }

    // --- Logic: Form Submission ---
    public void submitBorrow(String studentNumber, String studentNameStr, String courseStr, String collateral) {
        if (isBlank(studentNumber) || isBlank(studentNameStr)
                || isBlank(courseStr) || isBlank(collateral)) {
            submitError.setValue("Please complete all borrower fields.");
            return;
        }
        List<ItemRow> rows = deepCopy(itemRows.getValue());
        for (ItemRow row : rows) {
            if (isBlank(row.name)) {
                submitError.setValue("Please complete all item selections.");
                return;
            }
        }
        submitError.setValue(null);
        submitted.setValue(true);
    }

    public void clearSubmitError() { submitError.setValue(null); }

    public void resetForm() {
        studentNameInput.setValue("");
        courseInput.setValue("");
        studentFound.setValue(false);
        List<ItemRow> initial = new ArrayList<>();
        initial.add(new ItemRow());
        itemRows.setValue(initial);
        submitted.setValue(false);
        submitError.setValue(null);
    }

    // --- Logic: Return Workflow ---
    public void setSearchQuery(String query) {
        String trimmed = query == null ? "" : query.trim();
        normalizedSearch = trimmed.toLowerCase(Locale.US);
        applyFilters();
    }

    public void processReturn(long borrowId) {
        List<ActiveBorrow> current = safeList(activeBorrows.getValue());
        List<ActiveBorrow> updated = new ArrayList<>();
        for (ActiveBorrow borrow : current) {
            if (borrow.id != borrowId) {
                updated.add(borrow);
            }
        }
        activeBorrows.setValue(updated);
        applyFilters();
    }

    private void applyFilters() {
        List<ActiveBorrow> source = safeList(activeBorrows.getValue());
        if (normalizedSearch.isEmpty()) {
            filteredBorrows.setValue(source);
            return;
        }

        List<ActiveBorrow> filtered = new ArrayList<>();
        for (ActiveBorrow borrow : source) {
            if (matchesBorrow(borrow)) {
                filtered.add(borrow);
            }
        }
        filteredBorrows.setValue(filtered);
    }

    private boolean matchesBorrow(ActiveBorrow borrow) {
        String search = normalizedSearch;
        if (borrow.studentName != null && borrow.studentName.toLowerCase(Locale.US).contains(search)) return true;
        if (borrow.studentNumber != null && borrow.studentNumber.toLowerCase(Locale.US).contains(search)) return true;
        if (borrow.items != null) {
            for (String item : borrow.items) {
                if (item != null && item.toLowerCase(Locale.US).contains(search)) return true;
            }
        }
        return false;
    }

    // --- Helpers ---
    private List<ItemRow> deepCopy(List<ItemRow> source) {
        List<ItemRow> copy = new ArrayList<>();
        if (source != null) {
            for (ItemRow row : source) {
                copy.add(new ItemRow(row.type, row.name));
            }
        }
        return copy;
    }

    private List<ActiveBorrow> safeList(List<ActiveBorrow> source) {
        return source == null ? new ArrayList<>() : new ArrayList<>(source);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US);
        return sdf.format(new Date());
    }

    private List<ActiveBorrow> seedActiveBorrows() {
        List<ActiveBorrow> seed = new ArrayList<>();

        List<String> items1 = new ArrayList<>();
        items1.add("Projector - Epson EB-X41");
        seed.add(new ActiveBorrow(1, "2024-12345", "Sarah Chen",
                "BS Computer Science", "Employee ID", items1,
                "Feb 18, 2026", "09:30 AM"));

        List<String> items2 = new ArrayList<>();
        items2.add("Camera - Canon EOS R6");
        seed.add(new ActiveBorrow(2, "STU4521", "Emily Rodriguez",
                "BSCS-3", "Student ID", items2,
                "Feb 17, 2026", "02:15 PM"));

        List<String> items3 = new ArrayList<>();
        items3.add("Laptop - Dell XPS 15");
        items3.add("HDMI Cable 5m");
        seed.add(new ActiveBorrow(3, "EMP3107", "David Kim",
                "CS Faculty", "Employee ID", items3,
                "Feb 16, 2026", "11:00 AM"));

        return seed;
    }
}
