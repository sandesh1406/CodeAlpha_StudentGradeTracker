package src;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles saving and loading student data using Java Serialization.
 */
public class FileManager {
    private static final String FILE_PATH = "data/students.dat";

    /**
     * Saves the list of students to a file.
     * @param students List of students to save.
     * @throws IOException if an I/O error occurs.
     */
    public static void saveData(ArrayList<Student> students) throws IOException {
        // Ensure data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(students);
        }
    }

    /**
     * Loads the list of students from a file.
     * @return The list of students, or an empty list if no file exists.
     * @throws IOException if an I/O error occurs.
     * @throws ClassNotFoundException if the class of a serialized object cannot be found.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Student> loadData() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<Student>) ois.readObject();
        }
    }
}
