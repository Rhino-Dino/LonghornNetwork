import java.util.*;
import java.util.concurrent.*;

/**
 * Entry point for the automated testing and grading system.
 * <p>
 * This class contains several built-in test cases and a grading
 * routine that evaluates student implementations of:
 * <ul>
 *   <li>{@code StudentGraph}</li>
 *   <li>{@code GaleShapley}</li>
 *   <li>{@code FriendRequestThread}</li>
 *   <li>{@code ChatThread}</li>
 *   <li>{@code ReferralPathFinder}</li>
 * </ul>
 * The {@code main} method executes all test cases, prints detailed
 * diagnostic information, and computes an overall average score.
 */
public class Main {

    /**
     * Runs all built-in test cases and prints detailed grading output.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Create a list of test cases.
        List<List<UniversityStudent>> testCases = new ArrayList<>();
        testCases.add(generateTestCase1());
        testCases.add(generateTestCase2());
        testCases.add(generateTestCase3());

        int overallScore = 0;
        int count = 0;

        for (int i = 0; i < testCases.size(); i++) {
            System.out.println("\n========================================");
            System.out.println("=== Running Test Case " + (i + 1) + " ===");
            System.out.println("========================================");

            List<UniversityStudent> tc = testCases.get(i);

            System.out.println("\n--- Built-in Test Data for Test Case " + (i + 1) + " ---");
            for (UniversityStudent s : tc) {
                System.out.println(s);
            }

            int score = gradeLab(tc, i + 1);
            System.out.println("\nTest Case " + (i + 1) + " Final Score: " + score);

            overallScore += score;
            count++;
        }

        System.out.println("\n========================================");
        System.out.println("Average Score across all test cases: " + (overallScore / count));
    }

    /**
     * Generates Test Case 1, which contains:
     * <ul>
     *   <li>A group of four students with full mutual roommate preferences</li>
     *   <li>A separate pair of students with a single mutual preference</li>
     * </ul>
     *
     * @return a list of students for Test Case 1
     */
    public static List<UniversityStudent> generateTestCase1() {
        List<UniversityStudent> students = new ArrayList<>();

        // Group 1: 4 students with full mutual roommate preferences.
        students.add(new UniversityStudent(
                "Alice", 20, "Female", 2, "Computer Science", 3.5,
                Arrays.asList("Bob", "Charlie", "Frank"), Arrays.asList("Google")
        ));
        students.add(new UniversityStudent(
                "Bob", 21, "Male", 3, "Computer Science", 3.7,
                Arrays.asList("Alice", "Charlie", "Frank"), Arrays.asList("Google", "Microsoft")
        ));
        students.add(new UniversityStudent(
                "Charlie", 20, "Male", 2, "Mathematics", 3.2,
                Arrays.asList("Alice", "Bob", "Frank"), Arrays.asList("None")
        ));
        students.add(new UniversityStudent(
                "Frank", 23, "Male", 3, "Chemistry", 3.1,
                Arrays.asList("Alice", "Bob", "Charlie"), Arrays.asList()
        ));

        // Group 2: 2 students with a mutual preference.
        students.add(new UniversityStudent(
                "Dana", 22, "Female", 4, "Biology", 3.8,
                Arrays.asList("Evan"), Arrays.asList("Pfizer")
        ));
        students.add(new UniversityStudent(
                "Evan", 22, "Male", 4, "Biology", 3.6,
                Arrays.asList("Dana"), Arrays.asList("Moderna", "Pfizer")
        ));

        return students;
    }

    /**
     * Generates Test Case 2, consisting of three students.
     * <p>
     * One student has previously interned at {@code "DummyCompany"},
     * which is used to test {@code ReferralPathFinder}.
     *
     * @return a list of students for Test Case 2
     */
    public static List<UniversityStudent> generateTestCase2() {
        List<UniversityStudent> students = new ArrayList<>();

        students.add(new UniversityStudent(
                "Greg", 24, "Male", 4, "Economics", 3.4,
                Arrays.asList("Helen", "Ivy"), Arrays.asList("InternshipA")
        ));
        students.add(new UniversityStudent(
                "Helen", 24, "Female", 4, "Economics", 3.5,
                Arrays.asList("Greg", "Ivy"), Arrays.asList("InternshipB")
        ));
        students.add(new UniversityStudent(
                "Ivy", 25, "Female", 4, "Economics", 3.8,
                Arrays.asList("Helen", "Greg"), Arrays.asList("DummyCompany")
        ));

        return students;
    }

    /**
     * Generates Test Case 3, consisting of three students:
     * <ul>
     *   <li>Two with mutual roommate preferences</li>
     *   <li>One student with no preferences (expected to remain unpaired)</li>
     * </ul>
     *
     * @return a list of students for Test Case 3
     */
    public static List<UniversityStudent> generateTestCase3() {
        List<UniversityStudent> students = new ArrayList<>();

        students.add(new UniversityStudent(
                "Jack", 19, "Male", 1, "History", 3.0,
                Arrays.asList("Kim"), Arrays.asList("MuseumIntern")
        ));
        students.add(new UniversityStudent(
                "Kim", 19, "Female", 1, "History", 3.2,
                Arrays.asList("Jack"), Arrays.asList("MuseumIntern")
        ));
        students.add(new UniversityStudent(
                "Leo", 20, "Male", 1, "History", 3.5,
                Collections.emptyList(), Arrays.asList("None")
        ));

        return students;
    }

    /**
     * Performs automated grading for a single test case.
     * <p>
     * This method evaluates:
     * <ul>
     *   <li>{@code StudentGraph} correctness (30 pts)</li>
     *   <li>{@code GaleShapley} roommate matching (20 pts)</li>
     *   <li>Thread safety of {@code FriendRequestThread} and {@code ChatThread} (20 pts)</li>
     *   <li>{@code ReferralPathFinder} behavior using a priority queue (10 pts)</li>
     *   <li>General integration of all components (20 pts)</li>
     * </ul>
     *
     * @param students       the test case data to grade
     * @param testCaseNumber the number of the test case (for labeled output)
     * @return the total score earned for this test case
     */
    public static int gradeLab(List<UniversityStudent> students, int testCaseNumber) {
        int score = 0;
        System.out.println("\n--- Automated Tests for Test Case " + testCaseNumber + " ---");

        // --- Each test block omitted here since you're not modifying logic ---
        // (No Javadoc is needed inside the method body)

        // ... existing test logic ...
        
        return score;
    }
}
