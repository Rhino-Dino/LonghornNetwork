import java.util.*;

/**
 * Implements the Gale–Shapley stable matching algorithm for assigning
 * roommates among a group of {@link UniversityStudent} objects.
 * <p>
 * This algorithm ensures that the final set of roommate pairings is
 * stable—meaning that no two students would prefer each other over
 * their assigned partners.
 */
public class GaleShapley {

    /**
     * Assigns stable roommate pairings to the given list of students using
     * the Gale–Shapley algorithm. The specific preference lists and matching
     * rules depend on how {@link UniversityStudent} defines and stores preferences.
     *
     * @param students the list of students for whom roommate assignments
     *                 are to be generated
     */
    public static void assignRoommates(List<UniversityStudent> students) {
        // Map to hold final pairings: each student is paired with a roommate.
        Map<UniversityStudent, UniversityStudent> roommatePairs = new HashMap<>();
        // Tracks which proposal each student is up to.
        Map<UniversityStudent, Integer> nextProposalIndex = new HashMap<>();
        // Map to quickly lookup a student by name for their university student data since we store preferences
        // as a list of strings
        Map<String, UniversityStudent> nameToStudent = new HashMap<>();

        for (UniversityStudent student : students) {    //initializes
            nameToStudent.put(student.name, student);   //i think getname should also if we wanna make name priv
            nextProposalIndex.put(student, 0);
        }

        // Queue for students who are free (unmatched) and still have preferences to propose.
        Queue<UniversityStudent> freeStudents = new LinkedList<>();
        for (UniversityStudent student : students) {
            if (!student.roommatePreferences.isEmpty()) {
                freeStudents.offer(student);    //looked into this, offer is non blocking whereas add waits till queue has space lol
            }
        }

        while (!freeStudents.isEmpty()) {   //there are still students that exist
            UniversityStudent student = freeStudents.poll();    //take off queue
            //skip if s is already paired
            if (student.getRoommate()!=null){
                continue;
            }   //regular gale-shapley: check to see if there is one roommate that is preferred over the other, no such case will be tested

            int index = nextProposalIndex.get(student);
            if (index >= student.roommatePreferences.size()) {
                continue;   //s has no more preferences.
            }

            String preferredName = student.roommatePreferences.get(index);
            nextProposalIndex.put(student, index + 1);
            UniversityStudent t = nameToStudent.get(preferredName);
            if (t == null) {
                //preferred student not found; try next option.
                if (nextProposalIndex.get(student) < student.roommatePreferences.size()) {
                    freeStudents.offer(student);
                }
                continue;
            }

            // if t does not list s as a preference, reject s.
            if (!t.roommatePreferences.contains(student.name)) {
                if (nextProposalIndex.get(student) < student.roommatePreferences.size()) {
                    freeStudents.offer(student);
                }
                continue;
            }

            //if t is free, pair student and t.
            if (t.getRoommate() == null) {    //null means hes free
                roommatePairs.put(student, t);
                roommatePairs.put(t, student);
                student.setRoommate(t);
            } else {
                //t is already paired; check if t prefers student over current partner
                UniversityStudent currentPartner = t.getRoommate();
                int currentIndex = t.roommatePreferences.indexOf(currentPartner.name);
                int newIndex = t.roommatePreferences.indexOf(student.name);
                if (newIndex < currentIndex) {
                    //t prefers s over their current partner (the index, aka ranking in list is higher)
                    roommatePairs.put(t, student);
                    roommatePairs.put(student, t);  //15min mark
                    roommatePairs.remove(currentPartner);
                    freeStudents.offer(currentPartner);
                    currentPartner.setRoommate(null);
                    student.setRoommate(t);
                } else {
                    // t rejects student.
                    if (nextProposalIndex.get(student) < student.roommatePreferences.size()) {
                        freeStudents.offer(student);
                    }
                }
            }
        }

        //print the roommate pairs (avoids duplicate printing).
        System.out.println("\nRoommate Pairings (Gale-Shapley):");
        Set<UniversityStudent> printed = new HashSet<>();
        for (UniversityStudent s : roommatePairs.keySet()) {
            UniversityStudent partner = roommatePairs.get(s);
            if (!printed.contains(s) && !printed.contains(partner)) {   //avoids duplicate
                System.out.println(s.name + " paired with " + partner.name);
                printed.add(s);
                printed.add(partner);   //log them into printed
            }
        }

    }
}
