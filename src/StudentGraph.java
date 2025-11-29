import java.util.*;

/**
 * Represents a weighted undirected graph of {@link UniversityStudent} nodes.
 * Each node is a student, and edges represent connection strengths between
 * students. This graph is used for pod formation and referral path finding.
 */
public class StudentGraph {

    /**
     * Represents an edge between two students with a weight.
     */
    public static class Edge {
        /** The neighboring student connected by this edge. */
        public UniversityStudent neighbor;

        /** The weight of the connection (e.g., connection strength). */
        public int weight;

        /**
         * Constructs an Edge to the specified neighbor with a given weight.
         *
         * @param neighbor the neighboring student
         * @param weight   the connection strength
         */
        public Edge(UniversityStudent neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight;
        }

        //formats correctly
        @Override
        public String toString() {
            return "(" + neighbor.name + ", " + weight + ")";
        }
    }

    /** The adjacency list mapping each student to their list of edges. */
    private Map<UniversityStudent, List<Edge>> adjacencyList;

    /**
     * Constructs a StudentGraph from a list of students. Initially, no edges
     * exist. Edges can be added using {@link #addEdge}.
     *
     * @param students the list of students to add as nodes
     */
    public StudentGraph(List<UniversityStudent> students) {
        adjacencyList = new HashMap<>();
        //initializing nodes
        for (UniversityStudent s : students) {
            adjacencyList.put(s, new ArrayList<>());
        }
        //creates edges between every pair of students.
        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                UniversityStudent s1 = students.get(i);
                UniversityStudent s2 = students.get(j);
                int weight = s1.calculateConnectionStrength(s2);
                if (weight > 0) {
                    addEdge(s1, s2, weight);
                }
            }
        }
    }

    /**
     * Adds a weighted edge between two students.
     *
     * @param a      the first student
     * @param b      the second student
     * @param weight the weight of the connection
     */
    public void addEdge(UniversityStudent a, UniversityStudent b, int weight) {
        adjacencyList.get(a).add(new Edge(b, weight));
        adjacencyList.get(b).add(new Edge(a, weight));
    }

    /**
     * Returns the list of edges (neighbors) for a given student.
     *
     * @param student the student whose neighbors are requested
     * @return a list of {@link Edge} objects representing neighbors
     */
    public List<Edge> getNeighbors(UniversityStudent student) {
        return adjacencyList.get(student);
    }

    /**
     * Returns all students (nodes) in the graph.
     *
     * @return a set of all {@link UniversityStudent} nodes
     */
    public Set<UniversityStudent> getAllNodes() {
        return adjacencyList.keySet();
    }

    public void displayGraph(){
        System.out.println("\nStudent Graph:");
        for (UniversityStudent s : adjacencyList.keySet()) {
            System.out.println(s.name + " -> " + adjacencyList.get(s));
        }
    }
}
