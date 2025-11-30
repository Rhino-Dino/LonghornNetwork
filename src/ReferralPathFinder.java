import java.util.*;

/**
 * The {@code ReferralPathFinder} class provides functionality for searching
 * through a {@link StudentGraph} to find a referral path between university
 * students. A referral path represents a sequence of connected students
 * starting from a given student and ending at a student who works at a
 * specified company.
 *
 * <p>This class is intended to be used with a graph structure where each
 * {@link UniversityStudent} may be connected to other students through referral
 * relationships.</p>
 */
public class ReferralPathFinder {
    private StudentGraph graph;

    /**
     * Constructs a {@code ReferralPathFinder} using the provided student graph.
     *
     * @param graph the {@link StudentGraph} containing students and their
     *              referral connections
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    /**
     * Finds a referral path starting from the specified student and ending at
     * any student who is employed at the target company.
     *
     * <p>If a valid path exists, the returned list contains the sequence of
     * {@link UniversityStudent} objects that form the referral chain,
     * beginning with {@code start}. If no such path can be found, an empty
     * list is returned.</p>
     *
     * @param start         the student from whom the search begins
     * @param targetCompany the name of the company to search for
     * @return a list of {@link UniversityStudent} objects representing the
     *         referral path, or an empty list if no path is found
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        // Maps to store the best known distance and previous node for path reconstruction.
        Map<UniversityStudent, Double> dist = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> prev = new HashMap<>();
        Set<UniversityStudent> visited = new HashSet<>();

        // Initialize distances to infinity
        for (UniversityStudent s : graph.getAllNodes()){
            dist.put(s, Double.MAX_VALUE);
            prev.put(s, null);
        }
        dist.put(start, 0.0);

        // Priority Queue orders nodes by their current distance
        PriorityQueue<UniversityStudent> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            UniversityStudent u = pq.poll();
            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);

            // Check if this student has interned at the target company.
            for (String internship : u.previousInternships){
                if (internship.equalsIgnoreCase(targetCompany)) {
                    // Reconstruct the path from start to u.
                    List<UniversityStudent> path = new ArrayList<>();
                    UniversityStudent cur = u;
                    while (cur!=null){
                        path.add(cur);  //adding current student to path
                        cur = prev.get(cur);    //getting the previous distance of student
                    }
                    Collections.reverse(path);
                    return path;
                }
            }

            // Relaxation for neighbors.
            //checks if this edge is most optimal/shortest dinner amonst neighbors
            for (StudentGraph.Edge edge : graph.getNeighbors(u)) {
                UniversityStudent v = edge.neighbor;
                if (visited.contains(v)) {continue;}

                // Calculate new "distance": using the reciprical of the edge weight
                double newDist = dist.get(u) + (1.0/edge.weight);
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }
        // no student found with the target internship
        return new ArrayList<>();
    }
}
