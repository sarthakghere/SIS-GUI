package faculty;

import common.SQL;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class UpdateStudent extends JFrame {
    private JComboBox<String> studentComboBox;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JDateChooser enrollmentDateField;
    private JDateChooser birthDateChooser;
    String username;

    public UpdateStudent(String username) {
        this.username = username;
        setTitle("Update Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));
        getStudentList();
        String[] namesArr = new String[names.size()];
        names.toArray(namesArr);
        studentComboBox = new JComboBox<>(namesArr);
        studentComboBox.setSelectedItem(null);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        enrollmentDateField = new JDateChooser();
        birthDateChooser = new JDateChooser();
        birthDateChooser.getDate();

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Student:"));
        topPanel.add(studentComboBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(6, 2));
        centerPanel.add(new JLabel("First Name:"));
        centerPanel.add(firstNameField);
        centerPanel.add(new JLabel("Last Name:"));
        centerPanel.add(lastNameField);
        centerPanel.add(new JLabel("Email:"));
        centerPanel.add(emailField);
        centerPanel.add(new JLabel("Phone:"));
        centerPanel.add(phoneField);
        centerPanel.add(new JLabel("Enrollment Date:"));
        centerPanel.add(enrollmentDateField);
        centerPanel.add(new JLabel("Birth Date:"));
        centerPanel.add(birthDateChooser);
        add(centerPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FacultyMenu(username);
            }
        });

        studentComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = usernames.get(studentComboBox.getSelectedIndex());
                populateStudentInfo(selectedUser);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void getStudentList() {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("select username, CONCAT(first_name , ' ' , last_name) as full_name from student")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
                names.add(resultSet.getString("full_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", ABORT);
        }
    }

    private void populateStudentInfo(String username) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("select * from student where username = ?")) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                firstNameField.setText(resultSet.getString("first_name"));
                lastNameField.setText(resultSet.getString("last_name"));
                emailField.setText(resultSet.getString("email"));
                phoneField.setText(resultSet.getString("phone_number"));
                enrollmentDateField.setDate(resultSet.getDate("enrollment_date"));
                birthDateChooser.setDate(resultSet.getDate("birthdate"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while populating student information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                enrollmentDateField.getDate() == null || birthDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String email = emailField.getText();
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selectedStudent = usernames.get(studentComboBox.getSelectedIndex());
        String currentEmail = getEmailByUsername(selectedStudent);
        if (!currentEmail.equals(email) && isDuplicateEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email address already exists in the database.", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String phone = phoneField.getText();
        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number (10 digits).", "Invalid Phone Number", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newFirstName = firstNameField.getText();
        String newLastName = lastNameField.getText();
        Date newEnrollmentDate = enrollmentDateField.getDate();
        Date newBirthDate = birthDateChooser.getDate();

        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE student SET first_name=?, last_name=?, email=?, phone_number=?, enrollment_date=?, birthdate=? WHERE username=?")) {
            ps.setString(1, newFirstName);
            ps.setString(2, newLastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setDate(5, new java.sql.Date(newEnrollmentDate.getTime()));
            ps.setDate(6, new java.sql.Date(newBirthDate.getTime()));
            ps.setString(7, selectedStudent);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Student information updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while updating student information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isDuplicateEmail(String email) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) AS count FROM student WHERE email=?")) {
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while checking for duplicate email: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private String getEmailByUsername(String username) {
        String email = "";
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT email FROM student WHERE username=?")) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while retrieving email from the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return email;
    }

    private boolean isValidPhoneNumber(String phone) {
        // Check if the phone number consists of exactly 10 digits
        return phone.matches("\\d{10}");
    }

    public static void main(String[] args) {
        new UpdateStudent("faculty1");
    }
}
