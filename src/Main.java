package src;

import java.util.*;
import java.io.IOException;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GradeManager gradeManager = new GradeManager();

    public static void main(String[] args) {
        loadInitialData();
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        handleAddStudent();
                        break;
                    case "2":
                        handleAddGrade();
                        break;
                    case "3":
                        handleViewAllStudents();
                        break;
                    case "4":
                        handleViewStudentDetails();
                        break;
                    case "5":
                        handleRemoveStudent();
                        break;
                    case "6":
                        handleGenerateSummary();
                        break;
                    case "7":
                        handleSaveData();
                        break;
                    case "8":
                        handleLoadData();
                        break;
                    case "9":
                        running = false;
                        System.out.println("\n==================================================");
                        System.out.println("Exiting Student Grade Tracker. Goodbye!");
                        System.out.println("==================================================");
                        break;
                    default:
                        System.out.println("\n[ERROR] Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR] An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==================================================");
        System.out.println("             STUDENT GRADE TRACKER");
        System.out.println("==================================================");
        System.out.println("1. Add Student");
        System.out.println("2. Add Grade");
        System.out.println("3. View All Students");
        System.out.println("4. View Student Details");
        System.out.println("5. Remove Student");
        System.out.println("6. Generate Summary Report");
        System.out.println("7. Save Data");
        System.out.println("8. Load Data");
        System.out.println("9. Exit");
        System.out.println("==================================================");
        System.out.print("Enter your choice: ");
    }

    private static void handleAddStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("[ERROR] Student ID cannot be empty.");
            return;
        }

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[ERROR] Student Name cannot be empty.");
            return;
        }

        try {
            gradeManager.addStudent(id, name);
            System.out.println("[SUCCESS] Student added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleAddGrade() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        try {
            System.out.print("Enter Grade (0-100): ");
            String gradeInput = scanner.nextLine().trim();
            double grade = Double.parseDouble(gradeInput);
            gradeManager.addGrade(id, grade);
            System.out.println("[SUCCESS] Grade added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric input. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleViewAllStudents() {
        ArrayList<Student> students = gradeManager.getStudents();
        if (students.isEmpty()) {
            System.out.println("\n[INFO] No students found in the system.");
            return;
        }

        System.out.println("\n------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-10s\n", "ID", "NAME", "GRADES COUNT", "AVERAGE");
        System.out.println("------------------------------------------------------------------");
        for (Student s : students) {
            double avg = GradeStatistics.calculateAverage(s.getGrades());
            String avgStr = s.getGrades().isEmpty() ? "N/A" : String.format("%.2f", avg);
            System.out.printf("%-10s %-20s %-15d %-10s\n",
                s.getStudentId(), s.getName(), s.getGrades().size(), avgStr);
        }
        System.out.println("------------------------------------------------------------------");
    }

    private static void handleViewStudentDetails() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        Optional<Student> studentOpt = gradeManager.findStudent(id);

        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            System.out.println("\n==================================================");
            System.out.println("STUDENT DETAILS");
            System.out.println("==================================================");
            System.out.println("ID:       " + s.getStudentId());
            System.out.println("Name:     " + s.getName());

            ArrayList<Double> grades = s.getGrades();
            if (grades.isEmpty()) {
                System.out.println("Grades:   No grades recorded.");
                System.out.println("Average:   N/A");
                System.out.println("Highest:   N/A");
                System.out.println("Lowest:    N/A");
            } else {
                System.out.println("Grades:   " + grades.toString());
                System.out.printf("Average:   %.2f\n", GradeStatistics.calculateAverage(grades));
                System.out.printf("Highest:   %.2f\n", GradeStatistics.findHighest(grades));
                System.out.printf("Lowest:    %.2f\n", GradeStatistics.findLowest(grades));
            }
            System.out.println("==================================================");
        } else {
            System.out.println("[ERROR] Student not found.");
        }
    }

    private static void handleRemoveStudent() {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine().trim();
        if (gradeManager.removeStudent(id)) {
            System.out.println("[SUCCESS] Student removed successfully!");
        } else {
            System.out.println("[ERROR] Student not found.");
        }
    }

    private static void handleGenerateSummary() {
        ArrayList<Student> students = gradeManager.getStudents();
        if (students.isEmpty()) {
            System.out.println("\n[INFO] No student data available to generate report.");
            return;
        }

        System.out.println("\n====================================================================");
        System.out.println("                      STUDENT SUMMARY REPORT");
        System.out.println("====================================================================");
        System.out.printf("%-10s %-20s %-12s %-12s %-12s\n", "ID", "NAME", "AVERAGE", "HIGHEST", "LOWEST");
        System.out.println("--------------------------------------------------------------------");
        for (Student s : students) {
            ArrayList<Double> grades = s.getGrades();
            String avg = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.calculateAverage(grades));
            String high = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findHighest(grades));
            String low = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findLowest(grades));
            System.out.printf("%-10s %-20s %-12s %-12s %-12s\n", s.getStudentId(), s.getName(), avg, high, low);
        }
        System.out.println("====================================================================");
    }

    private static void handleSaveData() {
        try {
            FileManager.saveData(gradeManager.getStudents());
            System.out.println("[SUCCESS] Data saved successfully to data/students.dat!");
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save data: " + e.getMessage());
        }
    }

    private static void handleLoadData() {
        try {
            ArrayList<Student> students = FileManager.loadData();
            gradeManager.setStudents(students);
            System.out.println("[SUCCESS] Data loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Failed to load data: " + e.getMessage());
        }
    }

    private static void loadInitialData() {
        try {
            ArrayList<Student> students = FileManager.loadData();
            gradeManager.setStudents(students);
        } catch (IOException | ClassNotFoundException e) {
            // Silently handle if no data exists on startup
        }
    }
}
