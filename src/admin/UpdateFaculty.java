package admin;

import common.SQL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UpdateFaculty extends JFrame {
    private JComboBox<String> facultyComboBox;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    String username;

    public UpdateFaculty(String username) {
        this.username = username;
        setTitle("Update Faculty");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));
        getFacultyList();
        String[] namesArr = new String[names.size()];
        names.toArray(namesArr);
        facultyComboBox = new JComboBox<>(namesArr);
        facultyComboBox.setSelectedItem(null);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Faculty:"));
        topPanel.add(facultyComboBox);
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
                new AdminMenu(username);
            }
        });

        facultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = usernames.get(facultyComboBox.getSelectedIndex());
                populateFacultyInfo(selectedUser);
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

    private void getFacultyList() {
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select username, CONCAT(first_name , ' ' , last_name) as full_name from faculty")){
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
                names.add(resultSet.getString("full_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", ABORT);
        }
    }

    private void populateFacultyInfo(String username) {
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from faculty where username = ?")){
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                firstNameField.setText(resultSet.getString("first_name"));
                lastNameField.setText(resultSet.getString("last_name"));
                emailField.setText(resultSet.getString("email"));
                phoneField.setText(resultSet.getString("phone_number"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void saveChanges() {
        // Save the changes made to the faculty's information in the database
        // Example code
        String selectedFaculty = usernames.get(facultyComboBox.getSelectedIndex());
        String newFirstName = firstNameField.getText();
        String newLastName = lastNameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE faculty SET first_name=?, last_name=?, email=?, phone_number=? WHERE username=?")) {
            ps.setString(1, newFirstName);
            ps.setString(2, newLastName);
            ps.setString(3, newEmail);
            ps.setString(4, newPhone);
            ps.setString(5, selectedFaculty);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Faculty information updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update Faculty information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while updating Faculty information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new UpdateFaculty("admin");
    }
}
