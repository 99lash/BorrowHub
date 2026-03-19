package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.data.local.entity.StudentEntity;
import com.example.borrowhub.data.remote.dto.CreateStudentRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class StudentManagementViewModel extends AndroidViewModel {

    public static final String[] COURSES = {
            "BS Computer Science",
            "BS Information Technology",
            "BS Information Systems",
            "BS Electronics Engineering",
            "BS Civil Engineering"
    };

    private final MutableLiveData<List<StudentEntity>> allStudents = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<StudentEntity>> filteredStudents = new MutableLiveData<>(new ArrayList<>());
    private final AtomicLong nextId = new AtomicLong(1000L);

    private String searchQuery = "";
    private String normalizedSearchQuery = "";

    public StudentManagementViewModel(@NonNull Application application) {
        super(application);
        applyFilters();
    }

    public LiveData<List<StudentEntity>> getFilteredStudents() {
        return filteredStudents;
    }

    public int getTotalStudentCount() {
        List<StudentEntity> current = allStudents.getValue();
        return current == null ? 0 : current.size();
    }

    public void setSearchQuery(String query) {
        searchQuery = query == null ? "" : query.trim();
        normalizedSearchQuery = searchQuery.toLowerCase(Locale.US);
        applyFilters();
    }

    public void addStudent(String studentNumber, String name, String course) {
        List<StudentEntity> current = safeList(allStudents.getValue());

        // Check for duplicate student number
        for (StudentEntity s : current) {
            if (s.studentNumber.equalsIgnoreCase(studentNumber.trim())) {
                return;
            }
        }

        StudentEntity newStudent = new StudentEntity(
                nextId.incrementAndGet(),
                studentNumber.trim(),
                name.trim(),
                course
        );
        current.add(newStudent);
        allStudents.setValue(current);
        applyFilters();
    }

    public boolean isStudentNumberDuplicate(String studentNumber, long excludeId) {
        List<StudentEntity> current = safeList(allStudents.getValue());
        for (StudentEntity s : current) {
            if (s.id != excludeId && s.studentNumber.equalsIgnoreCase(studentNumber.trim())) {
                return true;
            }
        }
        return false;
    }

    public void updateStudent(long id, String studentNumber, String name, String course) {
        List<StudentEntity> current = safeList(allStudents.getValue());
        for (int i = 0; i < current.size(); i++) {
            StudentEntity s = current.get(i);
            if (s.id == id) {
                current.set(i, new StudentEntity(id, studentNumber.trim(), name.trim(), course));
                break;
            }
        }
        allStudents.setValue(current);
        applyFilters();
    }

    public void deleteStudent(long id) {
        List<StudentEntity> current = safeList(allStudents.getValue());
        List<StudentEntity> updated = new ArrayList<>();
        for (StudentEntity s : current) {
            if (s.id != id) {
                updated.add(s);
            }
        }
        allStudents.setValue(updated);
        applyFilters();
    }

    /**
     * Parse CSV text and import students.
     * Expected format per line: StudentNumber, Full Name, Course Name
     *
     * @return int[] where [0] = added count, [1] = skipped count
     */
    public int[] importFromCsv(String csvText) {
        int added = 0;
        int skipped = 0;

        if (csvText == null || csvText.trim().isEmpty()) {
            return new int[]{added, skipped};
        }

        List<StudentEntity> current = safeList(allStudents.getValue());
        List<StudentEntity> newStudents = new ArrayList<>();

        String[] lines = csvText.trim().split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 3) {
                skipped++;
                continue;
            }

            String studentNumber = parts[0].trim();
            String name = parts[1].trim();
            // Course may contain commas (join remaining parts)
            StringBuilder courseSb = new StringBuilder();
            for (int i = 2; i < parts.length; i++) {
                if (i > 2) courseSb.append(",");
                courseSb.append(parts[i].trim());
            }
            String course = courseSb.toString().trim();

            if (studentNumber.isEmpty() || name.isEmpty() || course.isEmpty()) {
                skipped++;
                continue;
            }

            // Check for duplicates in current list and in batch being added
            boolean isDuplicate = false;
            for (StudentEntity s : current) {
                if (s.studentNumber.equalsIgnoreCase(studentNumber)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                for (StudentEntity s : newStudents) {
                    if (s.studentNumber.equalsIgnoreCase(studentNumber)) {
                        isDuplicate = true;
                        break;
                    }
                }
            }

            if (isDuplicate) {
                skipped++;
            } else {
                newStudents.add(new StudentEntity(nextId.incrementAndGet(), studentNumber, name, course));
                added++;
            }
        }

        if (added > 0) {
            current.addAll(newStudents);
            allStudents.setValue(current);
            applyFilters();
        }

        return new int[]{added, skipped};
    }

    /**
     * Build CreateStudentRequestDTO list from CSV for API import.
     */
    public List<CreateStudentRequestDTO> buildImportRequests(String csvText) {
        List<CreateStudentRequestDTO> requests = new ArrayList<>();
        if (csvText == null || csvText.trim().isEmpty()) return requests;

        String[] lines = csvText.trim().split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 3) continue;

            String studentNumber = parts[0].trim();
            String name = parts[1].trim();
            StringBuilder courseSb = new StringBuilder();
            for (int i = 2; i < parts.length; i++) {
                if (i > 2) courseSb.append(",");
                courseSb.append(parts[i].trim());
            }
            String course = courseSb.toString().trim();

            if (!studentNumber.isEmpty() && !name.isEmpty() && !course.isEmpty()) {
                requests.add(new CreateStudentRequestDTO(studentNumber, name, course));
            }
        }
        return requests;
    }

    private void applyFilters() {
        List<StudentEntity> source = safeList(allStudents.getValue());
        if (normalizedSearchQuery.isEmpty()) {
            filteredStudents.setValue(new ArrayList<>(source));
            return;
        }

        List<StudentEntity> filtered = new ArrayList<>();
        for (StudentEntity s : source) {
            boolean matchesStudentNumber = s.studentNumber != null
                    && s.studentNumber.toLowerCase(Locale.US).contains(normalizedSearchQuery);
            boolean matchesName = s.name != null
                    && s.name.toLowerCase(Locale.US).contains(normalizedSearchQuery);

            if (matchesStudentNumber || matchesName) {
                filtered.add(s);
            }
        }
        filteredStudents.setValue(filtered);
    }

    private List<StudentEntity> safeList(List<StudentEntity> source) {
        return source == null ? new ArrayList<>() : new ArrayList<>(source);
    }
}
