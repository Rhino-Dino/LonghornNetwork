# LonghornNetwork AI Coding Guidelines

## Project Overview
LonghornNetwork is an ECE 422C lab project simulating a social networking system for university students. It implements three core algorithms and features: Gale-Shapley roommate matching, Dijkstra's referral path finding, and multithreaded friend requests/chat. The system processes student data files and builds weighted graphs representing connection strengths between students.

## Architecture & Key Components

### Core Data Model
- **`Student`** (abstract): Base class defining student attributes (name, age, gender, year, major, GPA, roommatePreferences, previousInternships). Declares abstract `calculateConnectionStrength()` method.
- **`UniversityStudent`**: Concrete implementation. Implements `calculateConnectionStrength(Student other)` scoring students by: roommate status (+4), shared internships (+3 each), same major (+2), same age (+1).

### Graph-Based System
- **`StudentGraph`**: Weighted undirected graph with adjacency list structure. Inner `Edge` class holds neighbor and weight. **Critical**: Only add edges for students with non-zero connection strength; zero-strength pairs remain disconnected. Use `getNeighbors()` for algorithm traversals.

### Algorithm Implementations
1. **`GaleShapley`**: Roommate stable matching via proposal queue. Students without preferences stay unmatched.
2. **`ReferralPathFinder`**: Dijkstra's algorithm using **inverted weights** (10 - strength) to prioritize stronger connections as shorter paths.
3. **`PodFormation`**: Groups students by graph connectivity (likely Prim's algorithm, though details are implementation-specific).

### Threading System
- **`FriendRequestThread`** and **`ChatThread`**: Concurrent operations on shared student resources. Use `synchronized` blocks or concurrent data structures when modifying `chatHistory` or friend lists to prevent race conditions.

### I/O Pipeline
- **`DataParser.parseStudents(filename)`**: Reads student file with format: name, age, gender, year, major, gpa, roommatePreferences (comma-separated), previousInternships (comma-separated). Returns `List<UniversityStudent>`.

## Critical Design Patterns

### Connection Strength Formula
Scoring is NOT additive per README—verify that a student who is roommates AND shares internships AND has same major gets 4+3+2=9, not inflated. See `UniversityStudent.calculateConnectionStrength()`.

### Graph Edge Filtering
Build graph only by **iterating all pairs** and conditionally adding edges with non-zero strength. Do NOT add zero-weight edges; this ensures disconnected components naturally represent isolated student groups.

### Dijkstra Implementation Detail
For referral path finding, invert weights locally (map strength to 10-strength) so stronger connections = lower cost. Pod formation uses raw weights directly.

### Thread-Safe Operations
Use `ExecutorService` (from `java.util.concurrent`) to manage thread pools. Guard shared resources:
- `chatHistory` updates require synchronization
- Friend list modifications must be atomic

## Testing & Validation

### Built-in Test Framework
`Main.gradeLab()` runs three test cases and computes average score. Test data is hardcoded; no file input for these. Review `Main.generateTestCase1/2/3()` to understand expected behavior.

### Sample Data
- Location: `testing/` directory contains `input_sample.txt` and `output_sample.txt` for checkpoint validation.
- Format: See DataParser javadoc; fields are pipe-separated or colon-delimited (verify in actual files).

### Common Pitfalls
1. **Double-edge addition**: Ensure `addEdge()` adds undirected edge (both directions) only once per pair.
2. **Preference validation**: Gale-Shapley must handle missing preferences (null/empty lists) without crashes.
3. **Disconnected graphs**: Some students may have zero connections—algorithms must handle isolated nodes or return empty results gracefully.

## File Organization
```
src/
  Student.java (abstract base)
  UniversityStudent.java (concrete student model)
  StudentGraph.java (adjacency list graph + inner Edge class)
  GaleShapley.java (stable matching)
  ReferralPathFinder.java (Dijkstra variant)
  PodFormation.java (graph grouping)
  FriendRequestThread.java (concurrent operations)
  ChatThread.java (concurrent messaging)
  DataParser.java (file parsing)
  Main.java (test harness + grading)
```

## Development Workflow
1. Implement classes in dependency order: Student hierarchy → StudentGraph → algorithms → threading.
2. Test each component with built-in cases in Main before integrating.
3. Use provided javadoc stubs; maintain existing method signatures.
4. Validate with sample files in `testing/` before final submission.

## Key External Dependencies
- **Java Collections**: `HashMap`, `ArrayList`, `HashSet`, `Queue` for graph/algorithm state.
- **Concurrency**: `ExecutorService`, `synchronized` for threading.
- **File I/O**: `BufferedReader`, `FileReader` for DataParser.
