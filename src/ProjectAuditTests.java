package src;

import java.util.*;
import java.io.*;

public class ProjectAuditTests {
    private GradeManager gradeManager;

    public ProjectAuditTests() {
        gradeManager = new GradeManager();
    }

    public static void main(String[] args) {
        ProjectAuditTests tester = new ProjectAuditTests();

        System.out.println("Running Functional Tests...\n");

        tester.testAddStudent();
        tester.testAddGrade();
        tester.testRemoveStudent();
        tester.testStatistics();
        tester.testPersistence();

        System.out.println("\nAll Logic Tests Completed.");
    }

    private void testAddStudent() {
        System.out.print("Test Add Student: ");
        try {
            gradeManager.addStudent("S1", "Alice");
            gradeManager.addStudent("S2", "Bob");
            // Duplicate ID
            try {
                gradeManager.addStudent("S1", "Charlie");
                System.out.println("FAIL (Duplicate ID allowed)");
                return;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL (" + e.getMessage() + ")");
        }
    }

    private void testAddGrade() {
        System.out.print("Test Add Grade: ");
        try {
            gradeManager.addGrade("S1", 85.0);
            gradeManager.addGrade("S1", 95.0);
            // Out of range
            try {
                gradeManager.addGrade("S1", 110.0);
                System.out.println("FAIL (Grade > 100 allowed)");
                return;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                gradeManager.addGrade("S1", -10.0);
                System.out.println("FAIL (Grade < 0 allowed)");
                return;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            // Non-existent student
            try {
                gradeManager.addGrade("S99", 50.0);
                System.out.println("FAIL (Grade added to non-existent student)");
                return;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL (" + e.getMessage() + ")");
        }
    }

    private void testRemoveStudent() {
        System.out.print("Test Remove Student: ");
        try {
            boolean removed = gradeManager.removeStudent("S1");
            if (!removed) {
                System.out.println("FAIL (Could not remove student)");
                return;
            }
            boolean removedAgain = gradeManager.removeStudent("S1");
            if (removedAgain) {
                System.out.println("FAIL (Removed non-existent student)");
                return;
            }
            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL (" + e.getMessage() + ")");
        }
    }

    private void testStatistics() {
        System.out.print("Test Statistics: ");
        try {
            gradeManager = new GradeManager();
            gradeManager.addStudent("S1", "Alice");
            gradeManager.addGrade("S1", 80.0);
            gradeManager.addGrade("S1", 90.0);
            gradeManager.addGrade("S1", 70.0);

            Student s = gradeManager.findStudent("S1").get();
            ArrayList<Double> grades = s.getGrades();

            double avg = GradeStatistics.calculateAverage(grades);
            double high = GradeStatistics.findHighest(grades);
            double low = GradeStatistics.findLowest(grades);

            if (avg != 80.0 || high != 90.0 || low != 70.0) {
                System.out.println("FAIL (Wrong calculations: avg=" + avg + ", high=" + high + ", low=" + low + ")");
                return;
            }

            // Test empty grades
            gradeManager.addStudent("S2", "Bob");
            Student s2 = gradeManager.findStudent("S2").get();
            if (GradeStatistics.calculateAverage(s2.getGrades()) != 0.0 ||
                GradeStatistics.findHighest(s2.getGrades()) != 0.0 ||
                GradeStatistics.findLowest(s2.getGrades()) != 0.0) {
                System.out.println("FAIL (Empty grades statistics wrong)");
                return;
            }

            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL (" + e.getMessage() + ")");
        }
    }

    private void testPersistence() {
        System.out.print("Test Persistence: ");
        try {
            // Clean start
            gradeManager = new GradeManager();
            gradeManager.addStudent("P1", "PersistAlice");
            gradeManager.addGrade("P1", 100.0);

            FileManager.saveData(gradeManager.getStudents());

            ArrayList<Student> loaded = FileManager.loadData();
            if (loaded.size() != 1 || !loaded.get(0).getStudentId().equals("P1")) {
                System.out.println("FAIL (Data not persisted correctly)");
                return;
            }
            System.out.println("PASS");
        } catch (Exception e) {
            System.out.println("FAIL (" + e.getMessage() + ")");
        }
    }
}
