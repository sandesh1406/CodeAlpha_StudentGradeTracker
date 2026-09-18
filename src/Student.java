package src;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Represents a student in the Grade Tracker system.
 * Implements Serializable to allow the object to be saved to a file.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String studentId;
    private String name;
    private ArrayList<Double> grades;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public void addGrade(double grade) {
        this.grades.add(grade);
    }

    @Override
    public String toString() {
        return "Student[ID=" + studentId + ", Name=" + name + ", GradesCount=" + grades.size() + "]";
    }
}
