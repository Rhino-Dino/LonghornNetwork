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
    private UniversityStudent roommate;
    private int friendRequestCount;
    private List<String> chatHistory;

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
        this.roommate = null;   //no roommate by default

        this.friendRequestCount = 0; // Example: Random count (0-4), hardcoded for now
        this.chatHistory = new ArrayList<>();
        this.chatHistory.add("None");
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
        return roommatePreferences;
    }
    public UniversityStudent getRoommate() {
        return roommate;
    }

    public List<String> getPreviousInternships() {
        return new ArrayList<>(previousInternships);
    }

    public int getFriendRequestCount() {
        return friendRequestCount;
    }

    public List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    //--------------------SETTERS-------------------
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    public void setRoommatePreferences(List<String> roommatePreferences) {
        this.roommatePreferences = new ArrayList<>(roommatePreferences);
    }
    public void setPreviousInternships(List<String> previousInternships) {
        this.previousInternships = new ArrayList<>(previousInternships);
    }
    public void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }

    public void setFriendRequestCount(int friendRequestCount) {
        this.friendRequestCount = friendRequestCount;
    }

    public void addFriendRequestCount() {
        this.friendRequestCount++;
    }

    public void setChatHistory(List<String> chatHistory) {
        this.chatHistory = new ArrayList<>(chatHistory);
    }

    public void addChatHistory(String message){
        this.chatHistory.add(message);
    }

    // ---------------- Connection Strength ----------------

    /**
     * Calculates the connection strength between this university student
     * and another student. +4 if roomates, +3 for internship, +2 major, +1 age
     *
     * @param other the student to compare against
     * @return an integer representing the connection strength
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        int strength = 0;
        if (other instanceof UniversityStudent) {
            UniversityStudent o = (UniversityStudent) other;    //cast

            //if o is the assigned roommate, add +4 to strength
            if (this.roommate != null && this.roommate.equals(o))
                strength += 4;

            //if o shares past internship experience, add +3 to strength
            for (String internship : this.previousInternships) {
                if (o.previousInternships.contains(internship))
                    strength += 3;
            }

            //if o has same major, +2 strength
            if (this.major.equals(o.major))
                strength += 2;

            //if o is same age, +1 strength
            if (this.year == o.year)
                strength += 1;

        }
        return strength;
    }
}
