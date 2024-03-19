package student;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import common.Getters;
import common.SQL;

public class ViewGrades extends JFrame {
    String username;
    private JLabel ces1JLabel, ces2JLabel, int1JLabel, int2JLabel;
    private JLabel ces1Marks, ces2Marks, int1Marks, int2Marks;
    private JComboBox<String> subjectsBox = new JComboBox<>(Getters.getSubjects());

    public ViewGrades(String username) {
        this.username = username;
        setSize(400, 200);
        setLocationRelativeTo(null);
        subjectsBox.setSelectedItem(null);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Subject:"));
        topPanel.add(subjectsBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);

        ces1JLabel = new JLabel("CES 1: ");
        ces1Marks = new JLabel("NULL");
        addComponent(middlePanel, ces1JLabel, ces1Marks, gbc);

        gbc.gridy++;
        ces2JLabel = new JLabel("CES 2: ");
        ces2Marks = new JLabel("NULL");
        addComponent(middlePanel, ces2JLabel, ces2Marks, gbc);

        gbc.gridy++;
        int1JLabel = new JLabel("Internal 1: ");
        int1Marks = new JLabel("NULL");
        addComponent(middlePanel, int1JLabel, int1Marks, gbc);

        gbc.gridy++;
        int2JLabel = new JLabel("Internal 2: ");
        int2Marks = new JLabel("NULL");
        addComponent(middlePanel, int2JLabel, int2Marks, gbc);

        add(middlePanel, BorderLayout.CENTER);
        subjectsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMarks();
            }
        });

        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton exitButton= new JButton("Exit");
        bottomPanel.add(backButton);
        bottomPanel.add(exitButton);

        backButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e){
            dispose();
            new StudentMenu(username);
           }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
             System.exit(0);
            }
         });

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void addComponent(JPanel panel, JLabel label1,JLabel label2, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(label1, gbc);

        gbc.gridx = 1;
        panel.add(label2, gbc);
    }

    private void getMarks() {
        try (Connection c = SQL.makeConnection()) {
            String courseName = (String) subjectsBox.getSelectedItem();
            String username = this.username;
    
            // Get CES 1 Marks
            fetchAndSetGrade(c, username, courseName, "CES 1", ces1Marks);
    
            // Get CES 2 Marks
            fetchAndSetGrade(c, username, courseName, "CES 2", ces2Marks);
    
            // Get Internal 1 Marks
            fetchAndSetGrade(c, username, courseName, "Internal 1", int1Marks);
    
            // Get Internal 2 Marks
            fetchAndSetGrade(c, username, courseName, "Internal 2", int2Marks);
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fetchAndSetGrade(Connection c, String username, String courseName, String type, JLabel label) {
        try (PreparedStatement ps = c.prepareStatement("SELECT grade FROM grades WHERE username = ? AND course_id = (SELECT course_id FROM course WHERE course_name = ?) AND type = ?")) {
            ps.setString(1, username);
            ps.setString(2, courseName);
            ps.setString(3, type);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String grade = rs.getString("grade");
                    label.setText(grade);
                } else {
                    label.setText("N/A");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        new ViewGrades("sarthakg.here");
    }
}
