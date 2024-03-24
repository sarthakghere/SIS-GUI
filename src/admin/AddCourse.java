package admin;
import common.SQL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourse extends JFrame {
    String username;

    public AddCourse(String username) {
        this.username = username;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(8, 2));
        this.setPreferredSize(new Dimension(400, 300));

        JTextField courseNameField = new JTextField();
        JTextField courseCreditsField = new JTextField();
        JTextField semesterField = new JTextField();

        JButton addButton = new JButton("Add Course");
        JButton cancelButton = new JButton("Cancel");

        this.add(new JLabel("Course Name:"));
        this.add(courseNameField);
        this.add(new JLabel("Course Credits:"));
        this.add(courseCreditsField);
        this.add(new JLabel("Semester:"));
        this.add(semesterField);
        this.add(addButton);
        this.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseName = courseNameField.getText();
                String courseCredits = courseCreditsField.getText();
                String semester = semesterField.getText();

                if (courseName.isEmpty() || courseCredits.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addCourseToDatabase(courseName, courseCredits, semester);

                // Close the addStudentFrame
                dispose();
                new AdminMenu(username);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the addStudentFrame
                dispose();
                new AdminMenu(username);
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void addCourseToDatabase(String courseName, String courseCredits, String semester) {
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO course(course_name, course_credits, semester) VALUES (?, ?, ?)");) {
                
            ps.setString(1, courseName);
            ps.setString(2, courseCredits);
            ps.setString(3, "SEM " + semester);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ps.close();
                JOptionPane.showMessageDialog(this, "Course Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);    
            } else {
                System.out.println("Failed to add course.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while adding course: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while adding course: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new AddCourse("admin");
    }
}
