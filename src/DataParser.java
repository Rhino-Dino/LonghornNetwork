import java.io.*;
import java.util.*;

/**
 * Utility class for loading and parsing student-related data
 * from external files. Provides helper methods to convert raw
 * file input into usable {@link UniversityStudent} objects.
 */
public class DataParser {

    /**
     * Parses a file containing student data and returns a list of
     * {@link UniversityStudent}] objects. The expected file format
     * and parsing behavior should be defined by the assignment or
     * implementation.
     * universityStudent content: name, age, gender, year, major, gpa, roomaatePreferences, previousInternships
     *
     * @param filename the name or path of the file to read
     * @return a list of parsed {@link UniversityStudent} objects
     * @throws IOException if the file cannot be opened or read
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {
        //I cant lie, my main grader worked fine and has no usage of this method so idk if it actually works with no flaws when integrated
        List<UniversityStudent> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentName = null;
            String name = null;
            Integer age = null;
            String gender = null;
            Integer year = null;
            String major = null;
            Double gpa = null;
            List<String> roommatePreferences = null;
            List<String> previousInternships = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Check if this is the start of a new student entry
                if (line.equals("Student:")) {
                    // If we have a previous student, create it before starting a new one
                    if (currentName != null) {
                        validateAndCreateStudent(students, currentName, name, age, gender, year, major, gpa,
                                roommatePreferences, previousInternships);
                    }

                    // Reset all fields for new student
                    currentName = null;
                    name = null;
                    age = null;
                    gender = null;
                    year = null;
                    major = null;
                    gpa = null;
                    roommatePreferences = null;
                    previousInternships = null;
                    continue;
                }

                // Parse field: value format
                if (!line.contains(":")) {
                    throw new IOException("Parsing error: Incorrect format in line: '" + line + "'. Expected format 'Field: <value>'.");
                }

                int colonIndex = line.indexOf(':');
                String field = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                try {
                    switch (field) {
                        case "Name":
                            name = value;
                            if (currentName == null) {
                                currentName = value;
                            }
                            break;
                        case "Age":
                            try {
                                age = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                throw new IOException("Number format error: Invalid number format for age: '" + value + "' in student entry for " + (currentName != null ? currentName : "unknown") + ".");
                            }
                            break;
                        case "Gender":
                            gender = value;
                            break;
                        case "Year":
                            try {
                                year = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                throw new IOException("Number format error: Invalid number format for year: '" + value + "' in student entry for " + (currentName != null ? currentName : "unknown") + ".");
                            }
                            break;
                        case "Major":
                            major = value;
                            break;
                        case "GPA":
                            try {
                                gpa = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                throw new IOException("Number format error: Invalid number format for GPA: '" + value + "' in student entry for " + (currentName != null ? currentName : "unknown") + ".");
                            }
                            break;
                        case "RoommatePreferences":
                            if (value.isEmpty() || value.equalsIgnoreCase("None")) {
                                roommatePreferences = new ArrayList<>();
                            } else {
                                roommatePreferences = parseCommaSeparatedList(value);
                            }
                            break;
                        case "PreviousInternships":
                            if (value.isEmpty() || value.equalsIgnoreCase("None")) {
                                previousInternships = new ArrayList<>();
                            } else {
                                previousInternships = parseCommaSeparatedList(value);
                            }
                            break;
                    }
                } catch (IOException e) {
                    throw e; // Re-throw IOException
                }
            }

            // Don't forget the last student
            if (currentName != null) {
                validateAndCreateStudent(students, currentName, name, age, gender, year, major, gpa,
                        roommatePreferences, previousInternships);
            }
        }

        return students;
    }

    /**
     * Parses a comma deliminated list and returns them as a trimmed/formatted list.
     *
     * @param value Comma seperated string to parse
     * @return A list of trimmed strings
     */
    private static List<String> parseCommaSeparatedList(String value) {
        List<String> list = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            return list;
        }
        String[] parts = value.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                list.add(trimmed);
            }
        }
        return list;
    }

    /**
     * Helper that validates that all required fields are present and creates a UniversityStudent object.
     *
     * @param students output list for students
     * @param currentName The name of the current student (for error messages)
     * @param name The student's name
     * @param age The student's age
     * @param gender The student's gender
     * @param year The student's year
     * @param major The student's major
     * @param gpa The student's GPA
     * @param roommatePreferences The student's roommate preferences
     * @param previousInternships The student's previous internships
     * @throws IOException If any required field is missing
     */
    private static void validateAndCreateStudent(List<UniversityStudent> students, String currentName,
                                                 String name, Integer age, String gender, Integer year,
                                                 String major, Double gpa, List<String> roommatePreferences,
                                                 List<String> previousInternships) throws IOException {
        // Validate required fields
        if (name == null) {
            throw new IOException("Parsing error: Missing required field 'Name' in student entry.");
        }
        if (age == null) {
            throw new IOException("Parsing error: Missing required field 'Age' in student entry for " + currentName + ".");
        }
        if (gender == null) {
            throw new IOException("Parsing error: Missing required field 'Gender' in student entry for " + currentName + ".");
        }
        if (year == null) {
            throw new IOException("Parsing error: Missing required field 'Year' in student entry for " + currentName + ".");
        }
        if (major == null) {
            throw new IOException("Parsing error: Missing required field 'Major' in student entry for " + currentName + ".");
        }
        if (gpa == null) {
            throw new IOException("Parsing error: Missing required field 'GPA' in student entry for " + currentName + ".");
        }
        if (roommatePreferences == null) {
            throw new IOException("Parsing error: Missing required field 'RoommatePreferences' in student entry for " + currentName + ".");
        }
        if (previousInternships == null) {
            throw new IOException("Parsing error: Missing required field 'PreviousInternships' in student entry for " + currentName + ".");
        }

        // Create the UniversityStudent object
        students.add(new UniversityStudent(name, age, gender, year, major, gpa, roommatePreferences, previousInternships));
    }
}
