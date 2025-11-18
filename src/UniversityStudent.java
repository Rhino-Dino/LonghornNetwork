import java.util.*;

/**
 * The {@code UniversityStudent} class represents a concrete implementation of
 * the abstract {@link Student} class. It models a university-level student
 * with personal, academic, and experiential attributes inherited from
 * {@code Student}. Additional behaviors specific to university students are
 * included, such as calculating connection strength with other students.
 *
 * <p>This class is intended for use in systems involving student matching,
 * referral networks, roommate assignments, or graph-based social modeling.</p>
 */
public class UniversityStudent extends Student {

    /**
     * Constructs a {@code UniversityStudent} with the specified attributes.
     *
     * @param name                the student's full name
     * @param age                 the student's age
     * @param gender              the student's gender
     * @param year                the academic year (e.g., 1-4 for undergrad)
     * @param major               the student's major
     * @param gpa                 the student's GPA
     * @param roommatePreferences a list of preferred roommate names
     * @param previousInternships a list of previous internship experiences
     */
    public UniversityStudent(
            String name,
            int age,
            String gender,
            int year,
            String major,
            double gpa,
            List<String> roommatePreferences,
            List<String> previousInternships
    ) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.roommatePreferences = new ArrayList<>(roommatePreferences);
        this.previousInternships = new ArrayList<>(previousInternships);
    }

    // ---------------- Getters ----------------

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }

    public double getGpa() {
        return gpa;
    }

    public List<String> getRoommatePreferences() {
        return new ArrayList<>(roommatePreferences);
    }

    public List<String> getPreviousInternships() {
        return new ArrayList<>(previousInternships);
    }

    // ---------------- Connection Strength ----------------

    /**
     * Calculates the connection strength between this university student
     * and another student. 
     *
     * @param other the student to compare against
     * @return an integer representing the connection strength
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        return 0;
    }
}
