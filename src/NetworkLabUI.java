//** NOTE: this is about a bit less than 50% of the lab grade covered, there will be a a bit of a stretch to get a 100.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.plaf.ColorUIResource;    //color import
import javax.imageio.ImageIO; // Needed for reading images
import java.io.File;         // Needed for loading the file
import java.io.IOException;    // Needed for error handling



public class NetworkLabUI extends JFrame {
    private JComboBox<String> testCaseSelector; //combo box for selecting the testcase
    private JButton runTestsButton;
    private JTextArea testOutputArea;
    private GraphPanel graphPanel;
    private JTextArea roommateArea;
    private JComboBox<String> startStudentSelector;
    private JTextField targetCompanyField;
    private JTextArea referralArea;

    private UniversityStudent selectedRoommateStudent; // Student selected for roommate visualization
    private List<UniversityStudent> currentReferralPath; // Path list for visualization

    private List<List<UniversityStudent>> testCases;
    private static final Color UT_ORANGE = new Color(204, 85, 0);

    // Custom panel to draw the background image, gets drawn first and other panels layover it
    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private float opacity = 0.15f; // Set the fading level (15% opacity)

        public BackgroundPanel(String imagePath) {
            // Set the panel to be non-opaque so it doesn't draw its own background,
            // relying only on the image painting in paintComponent.
            setOpaque(false);
            try {
                // IMPORTANT: Replace "longhorn_logo.png" with the actual path to your PNG file.
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("Error loading background image: " + imagePath);
                e.printStackTrace();
            }
            setLayout(new BorderLayout()); // Use BorderLayout to hold the JTabbedPane
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g.create();
// Set the desired transparency
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
// Draw the image, scaling it to fit the panel size
// You can change the scaling strategy (e.g., center, tile) here.
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        }
    }

    public NetworkLabUI() {
        super("Longhorn Network Lab UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.WHITE);

        //LF
        try {
            UIManager.put("TabbedPane.selected", new ColorUIResource(UT_ORANGE));
            UIManager.put("TabbedPane.selectedForeground", new ColorUIResource(Color.WHITE));
            UIManager.put("TabbedPane.unselectedForeground", new ColorUIResource(Color.BLACK));
            UIManager.put("TabbedPane.focus", new ColorUIResource(UT_ORANGE.darker()));
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Prepare test cases
        testCases = Arrays.asList(
                Main.generateTestCase1(),
                Main.generateTestCase2(),
                Main.generateTestCase3()
        );

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Home", createHomePanel());
        tabs.addTab("Test Runner", createTestRunnerPanel());
        tabs.addTab("Graph Viewer", createGraphViewerPanel());
        tabs.addTab("Roommate Pairs", createRoommatePanel());
        tabs.addTab("Referral Path", createReferralPanel());

        // Create the background panel, loading LNN logo.
        BackgroundPanel background = new BackgroundPanel("src/images/longhorn_logo.png");

        // Make the JTabbedPane itself transparent so the background shows through the empty areas
        tabs.setOpaque(false);

        // Add the JTabbedPane to the background panel
        background.add(tabs, BorderLayout.CENTER);

        // Add the background panel to the JFrame
        add(background);
    }

    private JPanel createHomePanel(){
        BackgroundPanel panel = new BackgroundPanel("src/images/longhorn_logo.png");


        //JPanel panel = new JPanel(new BorderLayout());
        //panel.setOpaque(false); // Crucial for background visibility

        String htmlContent = String.format(
                "<html><body style='width: 550px; padding: 20px; font-family: sans-serif;'>" +
                        "<h1 style='color: #CC5500;'>Longhorn Network Lab</h1>" +
                        "<p>Welcome to the Longhorn Network Roommate tool. Use the tabs above to explore different aspects of the student network.</p>" +
                        "<h2>Tab Functions:</h2>" +
                        "<ul>" +
                        "<li><b>Test Runner:</b> Executes main tests on algorithms (Graph, Roommate, Referral) and displays scores and data output.</li>" +
                        "<li><b>Graph Viewer:</b> Loads a network visualization. Click on a node to view detailed student information and also shows highlighted edges.</li>" +
                        "<li><b>Roommate Pairs:</b> Computes optimal stable roommate pairings using the Gale-Shapley algorithm for the selected dataset.</li>" +
                        "<li><b>Referral Path:</b> Finds the shortest path (based on connection strength) for a student to get a referral to a target company.</li>" +
                        "</ul>" +
                        "<p style='text-align: right; font-size: small; color: #888;'>Version 1.0 | UT Austin</p>" +
                        "</body></html>"
        );
        JLabel contentLabel = new JLabel(htmlContent);
        contentLabel.setForeground(Color.BLACK); // Set text color for readability
        contentLabel.setOpaque(false); // Ensure the label itself is transparent

        panel.add(contentLabel, BorderLayout.WEST);

        return panel;

    }

    private JPanel createTestRunnerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        testCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3", "All Test Cases"});
        runTestsButton = new JButton("Run Tests");
        runTestsButton.addActionListener(e -> onRunTests());
        top.add(new JLabel("Select Test Case:"));
        top.add(testCaseSelector);
        top.add(runTestsButton);
        panel.add(top, BorderLayout.NORTH);
        testOutputArea = new JTextArea();
        testOutputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(testOutputArea);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGraphViewerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        JComboBox<String> graphCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton loadGraphButton = new JButton("Load Graph");

        JButton visualizeRoommatesButton = new JButton("Show Roommates");

        loadGraphButton.addActionListener(e -> {
            int idx = graphCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            StudentGraph graph = new StudentGraph(data);
            graphPanel.setGraph(graph, data);

            // Re-run roommate assignment so the graph can access the roommate field
            data.forEach(s -> s.setRoommate(null));
            GaleShapley.assignRoommates(data);

            graphPanel.setReferralPath(null);
            graphPanel.setShowRoommates(false);
        });

        visualizeRoommatesButton.addActionListener(e -> {
            // Set the mode in GraphPanel and repaint
            graphPanel.setReferralPath(null); // Clear other modes
            graphPanel.setShowRoommates(true);
            graphPanel.repaint();
        });

        controls.add(new JLabel("Select Data:"));
        controls.add(graphCaseSelector);
        controls.add(loadGraphButton);
        controls.add(visualizeRoommatesButton);

        panel.add(controls, BorderLayout.NORTH);
        graphPanel = new GraphPanel();
        panel.add(graphPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoommatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        JComboBox<String> rmCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton computeButton = new JButton("Compute Roommates");
        computeButton.addActionListener(e -> {
            int idx = rmCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            // clear previous roommates
            data.forEach(s -> s.setRoommate(null));
            GaleShapley.assignRoommates(data);
            StringBuilder sb = new StringBuilder();
            for (UniversityStudent s : data) {
                if (s.getRoommate() != null && s.getName().compareTo(s.getRoommate().getName()) < 0) {
                    sb.append(s.getName()).append(" → ").append(s.getRoommate().getName()).append("\n");
                }
            }
            roommateArea.setText(sb.toString());
        });
        controls.add(new JLabel("Select Data:"));
        controls.add(rmCaseSelector);
        controls.add(computeButton);
        panel.add(controls, BorderLayout.NORTH);
        roommateArea = new JTextArea();
        roommateArea.setEditable(false);
        panel.add(new JScrollPane(roommateArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReferralPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        JComboBox<String> refCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        startStudentSelector = new JComboBox<>();
        targetCompanyField = new JTextField(10);
        JButton findButton = new JButton("Find Path");
        findButton.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            String selectedName = (String) startStudentSelector.getSelectedItem();
            UniversityStudent start = data.stream().filter(s -> s.getName().equals(selectedName)).findFirst().orElse(null);
            String target = targetCompanyField.getText().trim();
            if (start != null && !target.isEmpty()) {
                StudentGraph graph = new StudentGraph(data);
                ReferralPathFinder finder = new ReferralPathFinder(graph);
                List<UniversityStudent> path = finder.findReferralPath(start, target);

                if (graphPanel != null) {
                    graphPanel.setGraph(graph, data); // Make sure graph is loaded first
                    graphPanel.setReferralPath(path);
                }

                StringBuilder sb = new StringBuilder();
                path.forEach(s -> sb.append(s.getName()).append(" -> "));
                if (!path.isEmpty()) sb.setLength(sb.length() - 4);
                referralArea.setText(sb.toString());
            }
        });
        refCaseSelector.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            startStudentSelector.removeAllItems();
            data.forEach(s -> startStudentSelector.addItem(s.getName()));
        });
        refCaseSelector.setSelectedIndex(0); // trigger population
        controls.add(new JLabel("Data:"));
        controls.add(refCaseSelector);
        controls.add(new JLabel("Start:"));
        controls.add(startStudentSelector);
        controls.add(new JLabel("Target Company:"));
        controls.add(targetCompanyField);
        controls.add(findButton);
        panel.add(controls, BorderLayout.NORTH);
        referralArea = new JTextArea();
        referralArea.setEditable(false);
        panel.add(new JScrollPane(referralArea), BorderLayout.CENTER);
        return panel;
    }

    private void onRunTests() {
        testOutputArea.setText("");
        String sel = (String) testCaseSelector.getSelectedItem();
        if (sel.equals("All Test Cases")) {
            for (int i = 1; i <= testCases.size(); i++) runTests(i);
        } else {
            int num = Integer.parseInt(sel.split(" ")[2]);
            runTests(num);
        }
    }

    private void runTests(int caseNum) {
        testOutputArea.append("=== Test Case " + caseNum + " ===\n");
        List<UniversityStudent> data = testCases.get(caseNum - 1);
        // Print data
        data.forEach(s -> testOutputArea.append(s + "\n"));
        testOutputArea.append("\n");
        int score = Main.gradeLab(data, caseNum);
        testOutputArea.append("Test Case " + caseNum + " Score: " + score + "\n\n");
    }

    // Custom panel to draw the graph
    private static class GraphPanel extends JPanel {
        private StudentGraph graph;
        private List<UniversityStudent> nodes;
        private Map<UniversityStudent, Point> coords; // Store coordinates for hit-testing

        private UniversityStudent roommateTarget;
        private List<UniversityStudent> referralPath;
        private boolean showRoommates = false;

        // ** Node Drawing Constants ** (Carried over from previous changes)
        final int NODE_DIAMETER = 40;
        final int NODE_RADIUS = NODE_DIAMETER / 2;

        public GraphPanel() {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onNodeClick(e.getX(), e.getY());
                }
            });
        }

        void setGraph(StudentGraph g, List<UniversityStudent> data) {
            this.graph = g;
            this.nodes = data;
            this.coords = new HashMap<>(); // Re-initialize map
            // Clear visualization states when a new graph is loaded
            this.roommateTarget = null;
            this.referralPath = null;

            repaint();
        }

        void setShowRoommates(boolean show) {
            this.showRoommates = show;
            this.referralPath = null; // Clear referral path when showing roommates
            repaint();
        }

        void setReferralPath(List<UniversityStudent> path) {
            this.referralPath = path;
            this.roommateTarget = null; // Clear other modes
            repaint();
        }

        private void onNodeClick(int clickX, int clickY) {
            if (coords == null || nodes == null) return;

            // ** STEP 2: Calculate Click Proximity (Hit-Testing) **
            for (UniversityStudent student : nodes) {
                Point p = coords.get(student);
                if (p == null) continue;

                // Use the distance formula (or check if click is within a square bounding box)
                // Distance squared to avoid expensive Math.sqrt()
                double distSq = Math.pow(clickX - p.x, 2) + Math.pow(clickY - p.y, 2);

                // Check if the click is within the node's radius squared
                if (distSq <= NODE_RADIUS * NODE_RADIUS) {
                    // Node clicked!
                    displayStudentInfo(student);
                    return;
                }
            }
        }

        // ** STEP 3: Display the Information **
        private void displayStudentInfo(UniversityStudent student) {

            // --- Data Preparation ---

            // 1. Roommate Name (Handle null case)
            String roommateName = (student.getRoommate() != null)
                    ? student.getRoommate().getName()
                    : "None Assigned";

            // 2. Previous Internships (Convert list to a clean, comma-separated string)
            // List<String> will be converted to a string, and brackets [] removed for clean display.
            String internships = (student.getPreviousInternships() != null && !student.getPreviousInternships().isEmpty()) ? student.getPreviousInternships().toString().replaceAll("[\\[\\]]", "") : "None";

            // Prepare chat history for display
            List<String> historyList = student.getChatHistory();
            String historyDisplay;

            if (historyList.size() == 0 || (historyList.size() == 1 && historyList.get(0).equals("None"))) {
                // Case 1: Empty or only contains the default "None"
                historyDisplay = "None";
            } else {
                // Case 2: Contains actual chat entries

                // Get the sublist starting from index 1 (skipping "None" at index 0), array by default has none :0
                List<String> actualHistory = historyList.subList(1, historyList.size());

                // Join the actual entries, separated by the HTML line break tag
                historyDisplay = String.join("<br>", actualHistory);
            }

            // --- HTML Formatting ---

            String info = String.format(
                    "<html><h2>%s</h2>" +
                            "<b>Age:</b> %d<br>" +          // Correct: %d for int getAge()
                            "<b>Gender:</b> %s<br>" +       // Correct: %s for String getGender()
                            "<b>Year:</b> %d<br>" +           // Correct: %d for int getYear()
                            "<b>Major:</b> %s<br>" +        // Correct: %s for String getMajor()
                            "<b>GPA:</b> %.2f<br>" +        // Correct: %.2f for double getGpa()
                            "<b>Roommate:</b> %s<br>" +
                            "<b>Previous Internships:</b> %s<br>" +
                            "<b>Friend Requests:</b> %d<br>" +
                            "<b>Chat History:</b> %s</html>",
                    student.getName(),
                    student.getAge(),
                    student.getGender(),
                    student.getYear(),
                    student.getMajor(),
                    student.getGpa(),                       // Uses getGpa()
                    roommateName,
                    internships,
                    student.getFriendRequestCount(),
                    historyDisplay
            );

            // Display the information using JOptionPane (assuming this is called from GraphPanel)
            JOptionPane.showMessageDialog(this, info, "Student Information", JOptionPane.PLAIN_MESSAGE);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (graph == null || nodes == null) return;

            int width = getWidth(), height = getHeight();
            int r = Math.min(width, height) / 3;
            int cx = width / 2, cy = height / 2;

            // 1. Recalculate and store coordinates (Layout)
            coords.clear();
            int n = nodes.size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = cx + (int) (r * Math.cos(angle));
                int y = cy + (int) (r * Math.sin(angle));
                coords.put(nodes.get(i), new Point(x, y));
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Default stroke
            g2.setStroke(new BasicStroke(1));

            // Draw Edges and Highlights (Roommate & Referral Path)

            // Store path edges for special drawing
            Set<String> pathEdges = new HashSet<>();
            if (referralPath != null && referralPath.size() > 1) {
                for (int i = 0; i < referralPath.size() - 1; i++) {
                    UniversityStudent s1 = referralPath.get(i);
                    UniversityStudent s2 = referralPath.get(i + 1);
                    // Use a unique, order-independent key for the set
                    pathEdges.add(s1.getName().compareTo(s2.getName()) < 0 ?
                            s1.getName() + s2.getName() : s2.getName() + s1.getName());
                }
            }

            for (UniversityStudent s : nodes) {
                Point p1 = coords.get(s);

                // Ensure p1 is not null before proceeding
                if (p1 == null) continue;

                for (StudentGraph.Edge e : graph.getNeighbors(s)) {
                    UniversityStudent t = e.neighbor;
                    // Draw each edge only once
                    if (nodes.indexOf(t) <= nodes.indexOf(s)) continue;

                    Point p2 = coords.get(t);
                    if (p2 == null) continue; // Ensure p2 is not null

                    String edgeKey = s.getName().compareTo(t.getName()) < 0 ?
                            s.getName() + t.getName() : t.getName() + s.getName();

                    // Determine styling
                    if (pathEdges.contains(edgeKey)) {
                        g2.setColor(Color.RED);          // Referral Path (Requirement Met)
                        g2.setStroke(new BasicStroke(4));
                    } else if (showRoommates && s.getRoommate() != null && s.getRoommate().equals(t)) {
                        g2.setColor(Color.BLUE);        // Roommates (Requirement Met)
                        g2.setStroke(new BasicStroke(3));
                    } else {
                        g2.setColor(Color.BLACK);       // Default Edge
                        g2.setStroke(new BasicStroke(1));
                    }

                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // Draw Edge Weight Text (Always black, normal stroke)
                    g2.setStroke(new BasicStroke(1));
                    g2.setColor(Color.BLACK);
                    int mx = (p1.x + p2.x) / 2, my = (p1.y + p2.y) / 2;
                    g2.drawString(String.valueOf(e.weight), mx, my);
                }
            }

            // Draw Nodes and Highlight Path Nodes
            for (UniversityStudent s : nodes) {
                Point p = coords.get(s);
                if (p == null) continue;

                Color nodeColor = UT_ORANGE;
                if (referralPath != null && referralPath.contains(s)) { //checks if ref path is enabled and highlights green
                    // Highlight nodes in the path
                    nodeColor = Color.GREEN.darker();
                }

                g2.setColor(nodeColor);
                g2.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_DIAMETER, NODE_DIAMETER);

                g2.setColor(Color.BLACK);
                String name = s.getName();
                FontMetrics fm = g2.getFontMetrics();
                int textX = p.x - fm.stringWidth(name) / 2;
                int textY = p.y + fm.getAscent() / 2;

                g2.drawString(name, textX, textY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }
}

