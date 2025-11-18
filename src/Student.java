import java.util.*;

/**
 * The {@code Student} class serves as an abstract base class representing a
 * university student with various personal, academic, and experiential
 * attributes. Subclasses of this class are expected to provide their own
 * implementation of the method used to evaluate the connection strength
 * between two students.
 *
 * <p>Fields such as name, age, gender, major, GPA, roommate preferences, and
 * prior internships are included to support matching algorithms, roommate
 * assignment systems, or networking/referral features.</p>
 */
public abstract class Student {

    /** The student's full name. */
    protected String name;

    /** The student's age. */
    protected int age;

    /** The student's gender. */
    protected String gender;

    /** The student's academic year (e.g., 1–4 for undergrad). */
    protected int year;

    /** The student's academic major. */
    protected String major;

    /** The student's grade point average. */
    protected double gpa;

    /** A list of preferred roommate names or criteria. */
    protected List<String> roommatePreferences;

    /** A list of previous internship experiences. */
    protected List<String> previousInternships;

    /**
     * Calculates the “connection strength” between this student and another
     * student. The definition of connection strength varies depending on the
     * specific subclass implementation, and may use factors such as shared
     * interests, academic similarities, overlapping experiences, or mutual
     * preferences.
     *
     * @param other the other student to compare against
     * @return an integer value representing the connection strength
     */
    public abstract int calculateConnectionStrength(Student other);
}
