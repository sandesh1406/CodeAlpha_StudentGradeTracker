package src;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Manages the list of students and provides operations to add, remove, and find students.
 */
public class GradeManager {
    private ArrayList<Student> students;

    public GradeManager() {
        this.students = new ArrayList<>();
    }

    /**
     * Adds a new student to the system.
     * @param id Student ID
     * @param name Student Name
     * @throws IllegalArgumentException if ID is already taken.
     */
    public void addStudent(String id, String name) {
        if (findStudent(id).isPresent()) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists.");
        }
        students.add(new Student(id, name));
    }

    /**
     * Finds a student by their ID.
     * @param id Student ID
     * @return An Optional containing the Student if found, otherwise empty.
     */
    public Optional<Student> findStudent(String id) {
        return students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(id))
                .findFirst();
    }

    /**
     * Removes a student from the system.
     * @param id Student ID
     * @return true if the student was removed, false otherwise.
     */
    public boolean removeStudent(String id) {
        return students.removeIf(s -> s.getStudentId().equalsIgnoreCase(id));
    }

    /**
     * Adds a grade to a specific student.
     * @param id Student ID
     * @param grade Grade value (0-100)
     * @throws IllegalArgumentException if student not found or grade out of range.
     */
    public void addGrade(String id, double grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
        Student student = findStudent(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found."));
        student.addGrade(grade);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Sets the student list (used for loading data).
     * @param students List of students.
     */
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
