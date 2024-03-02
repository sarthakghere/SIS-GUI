package faculty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.*;
import javax.swing.*;
import java.sql.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import common.SQL;

public class AddStudent extends JFrame{
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
        JTextField birthdateField = new JTextField();
        JTextField enrollmentDateField = new JTextField();

        JButton addButton = new JButton("Add Student");
        JButton cancelButton = new JButton("Cancel");

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
        addStudentFrame.add(new JLabel("Birthdate (YYYY-MM-DD):"));
        addStudentFrame.add(birthdateField);
        addStudentFrame.add(new JLabel("Enrollment Date (YYYY-MM-DD):"));
        addStudentFrame.add(enrollmentDateField);
        addStudentFrame.add(addButton);
        addStudentFrame.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String username = usernameField.getText();
                String birthdate = birthdateField.getText();
                String enrollmentDate = enrollmentDateField.getText();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()
                || username.isEmpty() || birthdate.isEmpty() || enrollmentDate.isEmpty()) {
                    JOptionPane.showMessageDialog(addStudentFrame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Stop further execution if any field is empty
                }
                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Phone No. must contain only digits", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Stop further execution if Phone No. contains non-digit characters
                }
                if (!isValidDateFormat(birthdate) || !isValidDateFormat(enrollmentDate)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Invalid date format. Please use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Stop further execution if date format is invalid
                }

                if (birthdate.equals(enrollmentDate)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Birthdate and Enrollment Date cannot be the same", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Stop further execution if birthdate and enrollmentDate are the same
                }

                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(addStudentFrame, "Username already exists. Please choose a different username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Stop further execution if username already exists
                }

                addStudentToDatabase(firstName, lastName, email, phone, username, birthdate, enrollmentDate);

                // Close the addStudentFrame
                addStudentFrame.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the addStudentFrame
                addStudentFrame.dispose();
            }
        });

        addStudentFrame.pack();
        addStudentFrame.setLocationRelativeTo(null);
        addStudentFrame.setVisible(true);
    }

    private boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);  // Disallow lenient parsing
    
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void addStudentToDatabase(String firstName, String lastName, String email, String phone,
                                      String username, String birthdate, String enrollmentDate) {
        Date bday = null, enroll = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            bday = new java.sql.Date(dateFormat.parse(birthdate).getTime());
            enroll = new java.sql.Date(dateFormat.parse(enrollmentDate).getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO student(first_name, last_name, email, phone_number, username, birthdate, enrollment_date) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, username);
            ps.setDate(6, (java.sql.Date) bday);

            if (enroll != null) {
                ps.setDate(7, (java.sql.Date) enroll);
            } else {
                // Handle the case when enroll is still null
                ps.setNull(7, java.sql.Types.DATE);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student Added!");
                PreparedStatement userTable = c.prepareStatement("INSERT INTO user(username, role) VALUES (?, ?)");
                userTable.setString(1, username);
                userTable.setString(2, "student");
                userTable.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("User Generated!");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isUsernameExists(String username) {
    try (Connection c = SQL.makeConnection();
         PreparedStatement ps = c.prepareStatement("SELECT * FROM user WHERE username = ?")) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();  // Return true if username exists in the result set
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "SQL Error " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return true;  // Assume username exists in case of an exception
    }
}

}
