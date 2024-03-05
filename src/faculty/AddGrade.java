package faculty;

import common.SQL; // Assuming SQL connection and query logic are in a separate class

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddGrade extends JFrame {

    private JComboBox<String> subjectDropdown;
    private JComboBox<String> testTypeDropdown;
    private List<String> studentList;
    private List<JTextField> marksFields;
    private JButton submitButton;
    private JButton cancelButton;

    public AddGrade() {
        setTitle("Add Grade");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400)); // Adjust size as needed

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Subject:"));
        topPanel.add(subjectDropdown = new JComboBox<>(getSubjectList()));
        topPanel.add(new JLabel("Test Type:"));
        topPanel.add(testTypeDropdown = new JComboBox<>(new String[]{"CES 1", "CES 2", "Internal 1", "Internal 2"}));
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        JScrollPane centerScrollPane = new JScrollPane(centerPanel);
        add(centerScrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0 for student names
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

        // Define a larger font size
        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        studentList = getAllStudents(); // Modify this method to fetch student data from the database securely (see below)
        marksFields = new ArrayList<>();

        for (String student : studentList) {
            gbc.gridy++;
            JLabel studentLabel = new JLabel(student);
            studentLabel.setFont(largerFont);
            centerPanel.add(studentLabel, gbc);


            gbc.gridx = 1; // Column 1 for marks fields
            JTextField marksField = new JTextField(5); // Adjust preferred width if needed
            marksFields.add(marksField);
            centerPanel.add(marksField, gbc);

            gbc.gridx = 0; // Reset to column 0 for the next student name
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.SOUTH);

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        bottomPanel.add(cancelButton);
        bottomPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Submit");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] getSubjectList() {
        // Modify this method to fetch subject data from the database securely (see below)
        return new String[]{"Java Programming", "Web Development", "Cyber Security", "Optimization Techniques", "Computer Networks", "Operating System"};
    }

    private List<String> getAllStudents() {
        List<String> usernames = new ArrayList<>();
        try(Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select username from student");){
            String username;

            ResultSet r = ps.executeQuery();

            while(r.next()){
                username = r.getString("username");
                usernames.add(username);
            }
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usernames;
    }

    public static void main(String[] args){
        new AddGrade();
    }
}