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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddAttendance extends JFrame {

    private JComboBox<String> subjectDropdown;
    private JDateChooser dateChooser;
    private List<String> studentList;
    private List<JCheckBox> presentCheckboxes;
    private List<JCheckBox> absentCheckboxes;

    public AddAttendance(String username) {
        setTitle("Add Attendance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        subjectDropdown = new JComboBox<>(Getters.getSubjects());
        dateChooser = new JDateChooser();
        studentList = Getters.getStudent();
        presentCheckboxes = new ArrayList<>();
        absentCheckboxes = new ArrayList<>();

        // Create top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Subject:"));
        topPanel.add(subjectDropdown);
        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);
        add(topPanel, BorderLayout.NORTH);

        // Create checkboxes panel
        JPanel checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new GridBagLayout());
        JScrollPane checkboxesScrollPane = new JScrollPane(checkboxesPanel);
        add(checkboxesScrollPane, BorderLayout.CENTER);

        // Initialize GridBagConstraint for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0 for student names
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

        // Define a larger font size
        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        // Add student names and checkboxes
        for (String student : studentList) {
            gbc.gridy++;
            checkboxesPanel.add(new JLabel(student), gbc);

            // Present checkbox
            gbc.gridx = 1; // Column 1 for checkboxes
            JCheckBox presentCheckbox = new JCheckBox("Present");
            presentCheckbox.setFont(largerFont);
            presentCheckboxes.add(presentCheckbox);

            // Absent checkbox
            JCheckBox absentCheckbox = new JCheckBox("Absent");
            absentCheckbox.setFont(largerFont);
            absentCheckboxes.add(absentCheckbox);

            // Group checkboxes for mutual exclusion
            ButtonGroup checkboxGroup = new ButtonGroup();
            checkboxGroup.add(presentCheckbox);
            checkboxGroup.add(absentCheckbox);

            // Add present checkbox to panel
            checkboxesPanel.add(presentCheckbox, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

            // Add absent checkbox to panel
            gbc.gridx = 2;
            checkboxesPanel.add(absentCheckbox, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

            // Reset to column 0 for the next student name
            gbc.gridx = 0;
        }

        // Create bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Attendance");
        JButton cancelButton = new JButton("Cancel");
        bottomPanel.add(cancelButton);
        bottomPanel.add(addButton);

        // Action listener for Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FacultyMenu(username);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to add attendance for selected students
    private void addStudent() {
        String selectedSubject = (String) subjectDropdown.getSelectedItem();
        Date selectedDate = dateChooser.getDate();

        // Check if subject and date are selected
        if (selectedSubject == null || selectedDate == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Subject: " + selectedSubject);
        System.out.println("Date: " + dateFormat.format(selectedDate));

        // Flags to track checkbox selection and duplicate attendance records
        boolean isAnyCheckboxSelected = false;
        boolean duplicateFound = false;

        // Iterate through students to check checkboxes and add attendance
        for (int i = 0; i < studentList.size(); i++) {
            String student = studentList.get(i);
            JCheckBox presentCheckbox = presentCheckboxes.get(i);
            JCheckBox absentCheckbox = absentCheckboxes.get(i);
            String status = "Absent";

            // Check if present checkbox is selected
            if (presentCheckbox.isSelected()) {
                System.out.println(student + ": Present");
                status = "Present";
                isAnyCheckboxSelected = true;
            }
            // Check if absent checkbox is selected
            else if (absentCheckbox.isSelected()) {
                System.out.println(student + ": Absent");
                status = "Absent";
                isAnyCheckboxSelected = true;
            }

            // Check for duplicate attendance record
            if (duplicateFound) {
                break; // Break out of the loop if duplicate found
            }

            try (Connection c = SQL.makeConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT * FROM attendance WHERE username = ? AND course_name = ? AND date = ?")) {

                // Set parameters for the SELECT query
                ps.setString(1, student);
                ps.setString(2, selectedSubject);
                ps.setDate(3, new java.sql.Date(selectedDate.getTime())); // Convert java.util.Date to java.sql.Date

                // Execute the SELECT query
                ResultSet resultSet = ps.executeQuery();

                // If the attendance record already exists, set the flag and break the loop
                if (resultSet.next()) {
                    duplicateFound = true;
                    JOptionPane.showMessageDialog(this, "Attendance record already exists for " + student + " on " + selectedDate + " for course " + selectedSubject, "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Attendance record already exists for " + student + " on " + selectedDate + " for course " + selectedSubject);
                    break;
                }

            } catch (Exception e) {
                System.out.println("Error checking for existing attendance record: " + e.getMessage());
                return;
            }

            // If no duplicate attendance record found, add the attendance to the database
            if (!duplicateFound) {
                addAttendanceToDatabase(student, selectedDate, selectedSubject, status);
                JOptionPane.showMessageDialog(null, "Attendance Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Show error message if no checkbox selected and no duplicate found
        if (!isAnyCheckboxSelected && !duplicateFound) {
            JOptionPane.showMessageDialog(null, "Please select Present or Absent for at least one student.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }

    // Method to add attendance record to the database
    private void addAttendanceToDatabase(String username, Date date, String course, String status) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO attendance(username, course_name, date, status) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, username);
            ps.setString(2, course);
            ps.setDate(3, new java.sql.Date(date.getTime())); // Convert java.util.Date to java.sql.Date
            ps.setString(4, status);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected < 0) {
                System.out.println("Attendance not added");
            } else {
                System.out.println("Attendance added");

            }

        } catch (Exception e) {
            System.out.println("Error adding attendance: " + e.getMessage());
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddAttendance("faculty1");
            }
        });
    }
}
