package faculty;
import common.SQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddGrade extends JFrame {
    private JComboBox<String> courseComboBox;
    private JComboBox<String> testComboBox;
    private List<String> studentList;
    private List<JTextField> gradeFields;
    String username;

    public AddGrade(String username) {
        this.username = username;
        setTitle("Add Grade");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        courseComboBox = new JComboBox<>(Getters.getSubjects());
        testComboBox = new JComboBox<>(new String[]{"CES 1", "CES 2", "Internal 1", "Internal 2"});
        studentList = Getters.getStudent();
        gradeFields = new ArrayList<>();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Select Test:"));
        topPanel.add(testComboBox);

        add(topPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0 for labels
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        Font studentFont = new Font("Arial", Font.PLAIN, 14);

        for (String student : studentList) {
            gbc.gridy++;

            // Add username label
            JLabel nameLabel = new JLabel(student);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(studentFont);
            middlePanel.add(nameLabel, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Add text field
            gbc.gridx = 2;
            JTextField gradeField = new JTextField();
            gradeField.setPreferredSize(new Dimension(100, 20)); // Adjust the width and height as needed
            gradeFields.add(gradeField);
            middlePanel.add(gradeField, gbc);

            // Reset grid constraints
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
        }

        JScrollPane scrollPane = new JScrollPane(middlePanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cancelButton = new JButton("Cancel");
        JButton submitButton = new JButton("Submit");

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addGradesToDatabase();
                    JOptionPane.showMessageDialog(null, "Grades added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Fill All the fields", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FacultyMenu(username);
            }
        });

        bottomPanel.add(cancelButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addGradesToDatabase() throws Exception {
        try (Connection c = SQL.makeConnection()) {
            String course = (String) courseComboBox.getSelectedItem();
            String test = (String) testComboBox.getSelectedItem();
    
            for (int i = 0; i < studentList.size(); i++) {
                String student = studentList.get(i);
                int grade = Integer.parseInt(gradeFields.get(i).getText());
    
                // Validate CES and Internal marks
                if ((test.equals("CES 1") || test.equals("CES 2")) && (grade < 0 || grade > 10)) {
                    throw new Exception("CES marks must be between 0 and 10.");
                } else if ((test.equals("Internal 1") || test.equals("Internal 2")) && (grade < 0 || grade > 40)) {
                    throw new Exception("Internal marks must be between 0 and 40.");
                }
    
                // Fetch course_id from the course table
                String courseIdQuery = "SELECT course_id FROM course WHERE course_name = ?";
                try (PreparedStatement courseIdPs = c.prepareStatement(courseIdQuery)) {
                    courseIdPs.setString(1, course);
                    ResultSet courseIdRs = courseIdPs.executeQuery();
                    if (courseIdRs.next()) {
                        String courseId = courseIdRs.getString("course_id");
    
                        // Fetch student_id from the student table
                        String studentIdQuery = "SELECT student_id FROM student WHERE username = ?";
                        try (PreparedStatement studentIdPs = c.prepareStatement(studentIdQuery)) {
                            studentIdPs.setString(1, student);
                            ResultSet studentIdRs = studentIdPs.executeQuery();
                            if (studentIdRs.next()) {
                                String studentId = studentIdRs.getString("student_id");
    
                                // Check if a record with the same course, student, and test type already exists
                                String existingGradeQuery = "SELECT * FROM grades WHERE course_id = ? AND student_id = ? AND Type = ?";
                                try (PreparedStatement existingGradePs = c.prepareStatement(existingGradeQuery)) {
                                    existingGradePs.setString(1, courseId);
                                    existingGradePs.setString(2, studentId);
                                    existingGradePs.setString(3, test);
                                    ResultSet existingGradeRs = existingGradePs.executeQuery();
    
                                    if (existingGradeRs.next()) {
                                        // If a record exists, update the grade
                                        String updateQuery = "UPDATE grades SET grade = ? WHERE course_id = ? AND student_id = ? AND Type = ?";
                                        try (PreparedStatement updatePs = c.prepareStatement(updateQuery)) {
                                            updatePs.setInt(1, grade);
                                            updatePs.setString(2, courseId);
                                            updatePs.setString(3, studentId);
                                            updatePs.setString(4, test);
                                            int rowsAffected = updatePs.executeUpdate();
                                            if (rowsAffected > 0) {
                                                System.out.println("Grade updated");
                                            } else {
                                                System.out.println("Failed to update grade");
                                            }
                                        }
                                    } else {
                                        // If no record exists, insert the grade
                                        String insertQuery = "INSERT INTO grades(course_id, student_id, grade, username, Type) VALUES (?, ?, ?, ?, ?)";
                                        try (PreparedStatement ps = c.prepareStatement(insertQuery)) {
                                            ps.setString(1, courseId);
                                            ps.setString(2, studentId);
                                            ps.setInt(3, grade);
                                            ps.setString(4, student);
                                            ps.setString(5, test);
    
                                            int rowsAffected = ps.executeUpdate();
                                            if (rowsAffected > 0) {
                                                System.out.println("Grade added");
                                            } else {
                                                System.out.println("Failed to add grade");
                                            }
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Student not found");
                            }
                        }
                    } else {
                        System.out.println("Course not found");
                    }
                }
            }
        }
    }
    
    

    public static void main(String[] args) {
        new AddGrade("faculty1");
    }
}
