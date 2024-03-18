package faculty;
import com.toedter.calendar.JDateChooser;
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
    private JDateChooser dateChooser;
    private List<String> studentList;
    private List<JTextField> gradeFields;

    public AddGrade() {
        setTitle("Add Grade");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        courseComboBox = new JComboBox<>(getCourseList());
        testComboBox = new JComboBox<>(new String[]{"CES 1", "CES 2", "Internal 1", "Internal 2"});
        dateChooser = new JDateChooser();
        studentList = getAllStudents();
        gradeFields = new ArrayList<>();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Select Test:"));
        topPanel.add(testComboBox);
        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);

        add(topPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel(new GridLayout(studentList.size(), 2));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font studentFont = new Font("Arial", Font.PLAIN, 14);

        for (String student : studentList) {
            JLabel nameLabel = new JLabel(student);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(studentFont);
            middlePanel.add(nameLabel);

            JTextField gradeField = new JTextField();
            gradeField.setPreferredSize(new Dimension(100, 20)); // Adjust the width and height as needed
            gradeFields.add(gradeField);
            middlePanel.add(gradeField);
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
                    JOptionPane.showMessageDialog(null, "Error while adding grades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bottomPanel.add(cancelButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] getCourseList() {
        ArrayList<String> subs = new ArrayList<>();
        String[] subArray = null;
        try(Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select course_name from course");){
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                subs.add(resultSet.getString("course_name"));
            }
            subArray = new String[subs.size()];
            subs.toArray(subArray);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return subArray;
    }

    private List<String> getAllStudents() {
        ArrayList<String> students = new ArrayList<>();
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select username from student")){
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                students.add(resultSet.getString("username"));
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(students);
        return students; 
    }

    private void addGradesToDatabase() throws Exception {
        try (Connection c = SQL.makeConnection()) {
            String course = (String) courseComboBox.getSelectedItem();
            String test = (String) testComboBox.getSelectedItem();
    
            for (int i = 0; i < studentList.size(); i++) {
                String student = studentList.get(i);
                int grade = Integer.parseInt(gradeFields.get(i).getText());
    
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
    
                                // Insert grade record into the database
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
        SwingUtilities.invokeLater(AddGrade::new);
    }
}
