package src;

import java.util.ArrayList;

/**
 * Utility class to calculate statistics for student grades.
 */
public class GradeStatistics {

    /**
     * Calculates the average of a list of grades.
     * @param grades List of grades
     * @return Average grade, or 0.0 if the list is empty.
     */
    public static double calculateAverage(ArrayList<Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    /**
     * Finds the highest grade in a list of grades.
     * @param grades List of grades
     * @return Highest grade, or 0.0 if the list is empty.
     */
    public static double findHighest(ArrayList<Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double highest = grades.get(0);
        for (double grade : grades) {
            if (grade > highest) {
                highest = grade;
            }
        }
        return highest;
    }

    /**
     * Finds the lowest grade in a list of grades.
     * @param grades List of grades
     * @return Lowest grade, or 0.0 if the list is empty.
     */
    public static double findLowest(ArrayList<Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double lowest = grades.get(0);
        for (double grade : grades) {
            if (grade < lowest) {
                lowest = grade;
            }
        }
        return lowest;
    }
}
