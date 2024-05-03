package faculty;

import com.toedter.calendar.JDateChooser;
import common.SQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class AddStudent extends JFrame {
    String username;

    public AddStudent(String username) {
        this.username = username;
        JFrame addStudentFrame = new JFrame("Add Student");
        addStudentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addStudentFrame.setLayout(new GridLayout(8, 2));
        addStudentFrame.setPreferredSize(new Dimension(400, 300));

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField usernameField = new JTextField();
        JDateChooser birthdateChooser = new JDateChooser();
        JDateChooser enrollmentDateChooser = new JDateChooser();

        JButton addButton = new JButton("Add Student");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the frame
        addStudentFrame.add(new JLabel("First Name:"));
        addStudentFrame.add(firstNameField);
        addStudentFrame.add(new JLabel("Last Name:"));
        addStudentFrame.add(lastNameField);
        addStudentFrame.add(new JLabel("Email:"));
        addStudentFrame.add(emailField);
        addStudentFrame.add(new JLabel("Phone No.:"));
        addStudentFrame.add(phoneField);
        addStudentFrame.add(new JLabel("Username:"));
        addStudentFrame.add(usernameField);
        addStudentFrame.add(new JLabel("Birthdate:"));
        addStudentFrame.add(birthdateChooser);
        addStudentFrame.add(new JLabel("Enrollment Date:"));
        addStudentFrame.add(enrollmentDateChooser);
        addStudentFrame.add(addButton);
        addStudentFrame.add(cancelButton);

        // Add action listener to the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve input values
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String username = usernameField.getText();
                java.util.Date birthdate = birthdateChooser.getDate();
                java.util.Date enrollmentDate = enrollmentDateChooser.getDate();

                // Validate input values
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()
                        || username.isEmpty() || birthdate == null || enrollmentDate == null) {
                    JOptionPane.showMessageDialog(addStudentFrame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Phone No. must contain only digits", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isValidDateFormat(birthdate) || !isValidDateFormat(enrollmentDate)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Invalid date format. Please use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (birthdate.equals(enrollmentDate)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Birthdate and Enrollment Date cannot be the same", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Username already exists. Please choose a different username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isValidEmailFormat(email)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isEmailExists(email)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Email already exists. Please choose a different email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isPhoneExists(phone)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Phone number already exists. Please choose a different phone number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Add student to the database
                addStudentToDatabase(firstName, lastName, email, phone, username, birthdate, enrollmentDate);

                // Close the addStudentFrame
                addStudentFrame.dispose();
                new FacultyMenu(username);
            }
        });

        // Add action listener to the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the addStudentFrame
                addStudentFrame.dispose();
                new FacultyMenu(username);
            }
        });

        // Set frame properties
        addStudentFrame.pack();
        addStudentFrame.setLocationRelativeTo(null);
        addStudentFrame.setVisible(true);
    }

    // Method to check if the date format is valid
    private boolean isValidDateFormat(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.format(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to add student to the database
    private void addStudentToDatabase(String firstName, String lastName, String email, String phone,
                                      String username, java.util.Date birthdate, java.util.Date enrollmentDate) {
        java.sql.Date bday = new java.sql.Date(birthdate.getTime());
        java.sql.Date enroll = new java.sql.Date(enrollmentDate.getTime());

        try (Connection c = SQL.makeConnection();
             PreparedStatement userTable = c.prepareStatement("INSERT INTO user(username, role) VALUES (?, ?)")) {

            // Insert into the user table
            userTable.setString(1, username);
            userTable.setString(2, "student");
            int rowsAffected = userTable.executeUpdate();
            if (rowsAffected > 0) {
                userTable.close();
                System.out.println("User Generated!");

                // Insert into the student table
                PreparedStatement ps = c.prepareStatement("INSERT INTO student(first_name, last_name, email, phone_number, username, birthdate, enrollment_date) VALUES (?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, phone);
                ps.setString(5, username);
                ps.setDate(6, bday);
                ps.setDate(7, enroll);
                ps.executeUpdate();
                ps.close();
                System.out.println("Student Added!");
                JOptionPane.showMessageDialog(this, "Student Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to check if a username already exists
    private boolean isUsernameExists(String username) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "SQL Error " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    // Method to check if an email already exists
    private boolean isEmailExists(String email) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM student WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "SQL Error " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    // Method to check if a phone number already exists
    private boolean isPhoneExists(String phone) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM student WHERE phone_number = ?")) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "SQL Error " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    // Method to validate email format
    private boolean isValidEmailFormat(String email) {
        // Regular expression for validating email addresses
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Main method to start the application
    public static void main(String[] args) {
        new AddStudent("faculty1");
    }
}
