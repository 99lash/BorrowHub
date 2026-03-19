package com.example.borrowhub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionViewModel extends ViewModel {

    /** Lightweight in-memory model for an active borrow transaction. */
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

    private final MutableLiveData<List<ActiveBorrow>> activeBorrows =
            new MutableLiveData<>(seedActiveBorrows());
    private final MutableLiveData<List<ActiveBorrow>> filteredBorrows =
            new MutableLiveData<>(new ArrayList<>());

    private String normalizedSearch = "";

    public TransactionViewModel() {
        applyFilters();
    }

    public LiveData<List<ActiveBorrow>> getFilteredBorrows() {
        return filteredBorrows;
    }

    public void setSearchQuery(String query) {
        String trimmed = query == null ? "" : query.trim();
        normalizedSearch = trimmed.toLowerCase(Locale.US);
        applyFilters();
    }

    /** Removes the borrow with the given id, simulating a successful return. */
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

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

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
        if (borrow.studentName != null
                && borrow.studentName.toLowerCase(Locale.US).contains(normalizedSearch)) {
            return true;
        }
        if (borrow.studentNumber != null
                && borrow.studentNumber.toLowerCase(Locale.US).contains(normalizedSearch)) {
            return true;
        }
        if (borrow.items != null) {
            for (String item : borrow.items) {
                if (item != null && item.toLowerCase(Locale.US).contains(normalizedSearch)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<ActiveBorrow> safeList(List<ActiveBorrow> source) {
        return source == null ? new ArrayList<>() : new ArrayList<>(source);
    }

    private List<ActiveBorrow> seedActiveBorrows() {
        List<ActiveBorrow> seed = new ArrayList<>();

        List<String> items1 = new ArrayList<>();
        items1.add("Projector - Epson EB-X41");
        seed.add(new ActiveBorrow(1, "EMP2045", "Sarah Chen",
                "CS Department", "Employee ID", items1,
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

        List<String> items4 = new ArrayList<>();
        items4.add("Conference Microphone");
        seed.add(new ActiveBorrow(4, "STU3891", "Lisa Thompson",
                "BSIT-2", "Student ID", items4,
                "Feb 17, 2026", "03:45 PM"));

        List<String> items5 = new ArrayList<>();
        items5.add("Wireless Presenter");
        seed.add(new ActiveBorrow(5, "EMP1523", "Michael Brown",
                "CS Department", "Employee ID", items5,
                "Feb 15, 2026", "10:20 AM"));

        return seed;
    }
}
