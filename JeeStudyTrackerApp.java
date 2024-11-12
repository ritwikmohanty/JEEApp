import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JeeStudyTrackerApp extends JFrame {
    private JPanel mainPanel;
    private JButton healthButton, subjectTrackingButton, testScoreButton, dashboardButton;
    private Map<String, JCheckBox> physicsTopics, chemistryTopics, mathTopics;
    private JTextField physicsScore, chemistryScore, mathScore;
    private JLabel completedSubjectsLabel;
    private JLabel healthScoreLabel, topicsScoreLabel, averageScoreLabel, overallScoreLabel, rewardLabel;

    public JeeStudyTrackerApp() {
        setTitle("JEE Study & Health Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());

        mainPanel.add(createHealthPage(), "Health");
        mainPanel.add(createSubjectTrackingPage(), "SubjectTracking");
        mainPanel.add(createTestScorePage(), "TestScore");
        mainPanel.add(createDashboardPage(), "Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        
        healthButton = new JButton("Health Tracker");
        healthButton.addActionListener(e -> showPage("Health"));
        buttonPanel.add(healthButton);

        subjectTrackingButton = new JButton("Subject Tracking");
        subjectTrackingButton.addActionListener(e -> showPage("SubjectTracking"));
        buttonPanel.add(subjectTrackingButton);

        testScoreButton = new JButton("Test Scores");
        testScoreButton.addActionListener(e -> showPage("TestScore"));
        buttonPanel.add(testScoreButton);

        dashboardButton = new JButton("Dashboard");
        dashboardButton.addActionListener(e -> showPage("Dashboard"));
        buttonPanel.add(dashboardButton);

        add(buttonPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHealthPage() {
        // Health page code remains the same
        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Sleep (hours):"));
        JTextField sleepInput = new JTextField();
        panel.add(sleepInput);

        panel.add(new JLabel("Nutrition (calories):"));
        JTextField nutritionInput = new JTextField();
        panel.add(nutritionInput);

        panel.add(new JLabel("Exercise (minutes):"));
        JTextField exerciseInput = new JTextField();
        panel.add(exerciseInput);

        panel.add(new JLabel("Meditation/Yoga (minutes):"));
        JTextField meditationInput = new JTextField();
        panel.add(meditationInput);

        panel.add(new JLabel("Caffeine (milligrams):"));
        JTextField caffeineInput = new JTextField();
        panel.add(caffeineInput);

        JButton saveButton = new JButton("Save Health Data");
        saveButton.addActionListener(e -> saveHealthData(sleepInput, nutritionInput, exerciseInput, meditationInput, caffeineInput));
        panel.add(saveButton);

        return panel;
    }

    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dashboardLabel = new JLabel("Your Progress and Rewards", SwingConstants.CENTER);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(dashboardLabel);

        topicsScoreLabel = new JLabel("Topics Score: Not Calculated", SwingConstants.CENTER);
        panel.add(topicsScoreLabel);

        healthScoreLabel = new JLabel("Health Score: Not Calculated", SwingConstants.CENTER);
        panel.add(healthScoreLabel);

        averageScoreLabel = new JLabel("Average Test Score: Not Calculated", SwingConstants.CENTER);
        panel.add(averageScoreLabel);

        overallScoreLabel = new JLabel("Overall Score: Not Calculated", SwingConstants.CENTER);
        overallScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(overallScoreLabel);

        rewardLabel = new JLabel("Reward Status: Not Calculated", SwingConstants.CENTER);
        rewardLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(rewardLabel);

        return panel;
    }

    private void updateDashboard(int healthScore) {
        healthScoreLabel.setText("Health Score: " + healthScore + " / 100");
        
        // Calculate topics score
        int completedTopics = countSelectedTopics(physicsTopics) + 
                            countSelectedTopics(chemistryTopics) + 
                            countSelectedTopics(mathTopics);
        int topicsScore = Math.min(100, completedTopics * 20);
        topicsScoreLabel.setText("Topics Score: " + topicsScore + " / 100");

        // Get test scores
        int avgTestScore =0 ;
        try {
            int physics = Integer.parseInt(physicsScore.getText());
            int chemistry = Integer.parseInt(chemistryScore.getText());
            int math = Integer.parseInt(mathScore.getText());
            avgTestScore = (physics + chemistry + math) / 3;
        } catch (NumberFormatException | NullPointerException e) {
            // Handle empty or invalid test scores
            avgTestScore = 0;
        }
        averageScoreLabel.setText("Average Test Score: " + avgTestScore + " / 100");

        // Calculate overall score
        double overallScore = (topicsScore + (healthScore ) + avgTestScore) / 3.0;
        overallScoreLabel.setText(String.format("Overall Score: %.1f / 100", overallScore));

        // Update reward status
        String rewardStatus;
        if (overallScore >= 90) {
            rewardStatus = "Congratulations! You have earned a gold reward!";
        } else if (overallScore >= 75) {
            rewardStatus = "Great job! You have earned a silver reward!";
        } else if (overallScore >= 50) {
            rewardStatus = "Good effort! You have earned a bronze reward!";
        } else {
            rewardStatus = "Keep going! Every step counts!";
        }
        rewardLabel.setText(rewardStatus);
    }

    private JPanel createSubjectTrackingPage() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        physicsTopics = new HashMap<>();
        chemistryTopics = new HashMap<>();
        mathTopics = new HashMap<>();

        // Physics Topics
        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel physicsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        physicsPanel.setBorder(BorderFactory.createTitledBorder("Physics Topics"));
        String[] physicsTopicList = {"Physical World and Measurement", "Kinematics", "Laws of Motion",
                "Work, Energy and Power", "Motion of System of Particles and Rigid Body", "Gravitation",
                "Properties of Bulk Matter", "Thermodynamics", "Behavior of Perfect Gas and Kinetic Theory",
                "Oscillations and Waves", "Electrostatics", "Current Electricity",
                "Magnetic Effects of Current and Magnetism", "Electromagnetic Induction",
                "Electromagnetic Waves", "Optics", "Dual Nature of Matter and Radiation",
                "Atoms and Nuclei", "Electronic Devices"};
        addTopicsToPanel(physicsPanel, physicsTopicList, physicsTopics);
        mainPanel.add(physicsPanel, gbc);

        // Chemistry Topics
        gbc.gridx = 1;
        JPanel chemistryPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        chemistryPanel.setBorder(BorderFactory.createTitledBorder("Chemistry Topics"));
        String[] chemistryTopicList = {"Some Basic Concepts of Chemistry", "Structure of Atom",
                "Classification of Elements", "Chemical Bonding and Molecular Structure",
                "States of Matter", "Thermodynamics", "Equilibrium", "Redox Reactions",
                "Hydrogen", "s-Block Elements", "p-Block Elements", "Organic Chemistry",
                "Hydrocarbons", "Environmental Chemistry", "d- and f- Block Elements",
                "Coordination Compounds", "Haloalkanes and Haloarenes", "Alcohols, Phenols, and Ethers",
                "Aldehydes, Ketones, and Carboxylic Acids", "Organic Compounds Containing Nitrogen",
                "Biomolecules", "Polymers", "Chemistry in Everyday Life"};
        addTopicsToPanel(chemistryPanel, chemistryTopicList, chemistryTopics);
        mainPanel.add(chemistryPanel, gbc);

        // Mathematics Topics
        gbc.gridx = 2;
        JPanel mathPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        mathPanel.setBorder(BorderFactory.createTitledBorder("Mathematics Topics"));
        String[] mathTopicList = {"Sets, Relations, and Functions", "Complex Numbers",
                "Matrices and Determinants", "Permutations and Combinations", "Binomial Theorem",
                "Sequences and Series", "Straight Lines", "Conic Sections",
                "Three-Dimensional Geometry", "Limits and Derivatives", "Mathematical Reasoning",
                "Statistics and Probability", "Differential Equations", "Coordinate Geometry",
                "Vectors", "Linear Programming"};
        addTopicsToPanel(mathPanel, mathTopicList, mathTopics);
        mainPanel.add(mathPanel, gbc);
           
        
        // Completed subjects label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        completedSubjectsLabel = new JLabel("Completed Subjects: 0");
        mainPanel.add(completedSubjectsLabel, gbc);


        // Save button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton saveButton = new JButton("Save Subjects Progress");
        saveButton.addActionListener(e -> saveSubjectData());
        mainPanel.add(saveButton, gbc);

        return mainPanel;
    }

    private void addTopicsToPanel(JPanel panel, String[] topics, Map<String, JCheckBox> topicMap) {
        for (String topic : topics) {
            JCheckBox checkBox = new JCheckBox(topic);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    updateCompletedSubjectsCount();
                }
            });
            panel.add(checkBox);
            topicMap.put(topic, checkBox);
        }
    }

    private void updateCompletedSubjectsCount() {
        int completedCount = 0;
        completedCount += countSelectedTopics(physicsTopics);
        completedCount += countSelectedTopics(chemistryTopics);
        completedCount += countSelectedTopics(mathTopics);

        completedSubjectsLabel.setText("Completed Subjects: " + completedCount);
    }
    private int countSelectedTopics(Map<String, JCheckBox> topics) {
        int count = 0;
        for (JCheckBox checkBox : topics.values()) {
            if (checkBox.isSelected()) {
                count++;
            }
        }
        return count;
    }

    
    private JPanel createTestScorePage() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Physics Test Score (out of 100):"));
        physicsScore = new JTextField();
        panel.add(physicsScore);

        panel.add(new JLabel("Chemistry Test Score (out of 100):"));
        chemistryScore = new JTextField();
        panel.add(chemistryScore);

        panel.add(new JLabel("Mathematics Test Score (out of 100):"));
        mathScore = new JTextField();
        panel.add(mathScore);

        JButton saveButton = new JButton("Save Test Scores");
        saveButton.addActionListener(e -> saveTestScores());
        panel.add(saveButton);

        return panel;
    }

    private void saveHealthData(JTextField sleepInput, JTextField nutritionInput, JTextField exerciseInput, JTextField meditationInput, JTextField caffeineInput) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;
    
        try {
            double sleep = Double.parseDouble(sleepInput.getText());
            int nutrition = Integer.parseInt(nutritionInput.getText());
            int exercise = Integer.parseInt(exerciseInput.getText());
            int meditation = Integer.parseInt(meditationInput.getText());
            int caffeine = Integer.parseInt(caffeineInput.getText());
    
            String insertSQL = "INSERT INTO HealthData (sleep_hours, nutrition_calories, exercise_minutes, " +
                               "meditation_minutes, caffeine_mg) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setDouble(1, sleep);
                pstmt.setInt(2, nutrition);
                pstmt.setInt(3, exercise);
                pstmt.setInt(4, meditation);
                pstmt.setInt(5, caffeine);
                
                pstmt.executeUpdate();
    
                // Calculate and display health score
                int healthScore = calculateHealthScore(sleep, nutrition, exercise, meditation, caffeine);
                JOptionPane.showMessageDialog(this, "Health data saved successfully! Health Score: " + healthScore);
    
                // Update the dashboard with the new health score
                updateDashboard(healthScore);
    
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving health data: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers in all fields.",
                                          "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void saveSubjectData() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String insertSQL = "INSERT INTO SubjectsProgress (subject, topic, completed) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            // Save Physics topics
            saveTopics(pstmt, "Physics", physicsTopics);
            // Save Chemistry topics
            saveTopics(pstmt, "Chemistry", chemistryTopics);
            // Save Math topics
            saveTopics(pstmt, "Mathematics", mathTopics);

            pstmt.executeBatch();
            JOptionPane.showMessageDialog(this, "Subject progress saved successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving subject progress: " + e.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // private int calculateHealthScore(double sleep, int nutrition, int exercise, int meditation, int caffeine) {
    //     int sleepScore = 20 - (int)(Math.abs(8 - sleep) * 2.5);
    //     sleepScore = Math.max(0, sleepScore);
    
    //     int nutritionScore = 20 - (int)(Math.abs(3000 - nutrition) / 100.0);
    //     nutritionScore = Math.max(0, nutritionScore);
    
    //     int exerciseScore = Math.min(20, exercise * 2);
    //     int meditationScore = Math.min(20, meditation * 2);
    //     int caffeineScore = Math.max(0, 20 - (int)(caffeine / 5.0));
    
    //     int totalScore = sleepScore + nutritionScore + exerciseScore + meditationScore + caffeineScore;
    //     return totalScore ; 
    // }
    private static int calculateHealthScore(double sleep, int nutrition, int exercise, int meditation, int caffeine) {
        // Sleep: Out of 20
        int sleepScore = 20 - (int)(Math.abs(8 - sleep) * 2.5);
        sleepScore = Math.max(0, sleepScore);

        // Nutrition: Out of 20
        int nutritionScore = 20 - (int)(Math.abs(2800 - nutrition) / 50.0);
        nutritionScore = Math.max(0, nutritionScore);

        // Exercise: Out of 20 (1 point per 10 minutes, capped at 20)
        int exerciseScore = Math.min(20, 2*(exercise / 10));

        // Meditation/Yoga: Out of 20 (1 point per 10 minutes, capped at 20)
        int meditationScore = Math.min(20, 2*(meditation / 10));

        // Caffeine: Out of 20 (Less caffeine means higher score)
        int caffeineScore = Math.max(0, 20 - (int)(caffeine / 5.0));

        // Total Health Score
        int totalScore = sleepScore + nutritionScore + exerciseScore + meditationScore + caffeineScore;
        return totalScore;
    }

    
        

    private void saveTopics(PreparedStatement pstmt, String subject, Map<String, JCheckBox> topics) 
            throws SQLException {
        for (Map.Entry<String, JCheckBox> entry : topics.entrySet()) {
            pstmt.setString(1, subject);
            pstmt.setString(2, entry.getKey());
            pstmt.setBoolean(3, entry.getValue().isSelected());
            pstmt.addBatch();
        }
    }



    private void saveTestScores() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return;

        String insertSQL = "INSERT INTO TestScores (subject, score) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            // Save Physics score
            pstmt.setString(1, "Physics");
            int physics = Integer.parseInt(physicsScore.getText());
            pstmt.setInt(2, physics);
            pstmt.addBatch();

            // Save Chemistry score
            pstmt.setString(1, "Chemistry");
            int chemistry = Integer.parseInt(chemistryScore.getText());
            pstmt.setInt(2, chemistry);
            pstmt.addBatch();

            // Save Mathematics score
            pstmt.setString(1, "Mathematics");
            int math = Integer.parseInt(mathScore.getText());
            pstmt.setInt(2, math);
            pstmt.addBatch();

            pstmt.executeBatch();

            // Calculate and update dashboard
            int avgScore = (physics + chemistry + math) / 3;
            updateDashboard( avgScore);
            
            JOptionPane.showMessageDialog(this, "Test scores saved successfully!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error saving test scores: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPage(String pageName) {
        CardLayout layout = (CardLayout) mainPanel.getLayout();
        layout.show(mainPanel, pageName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JeeStudyTrackerApp app = new JeeStudyTrackerApp();
            app.setVisible(true);
        });
    }
}