package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import common.*;
import java.sql.SQLException;

public class ViewAttendance extends JFrame {

    private final String username;

    public ViewAttendance(String username) {
        this.username = username;

        setTitle("View Attendance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 500));
        setResizable(false);

        JTextArea attendanceTextArea = new JTextArea();
        attendanceTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(attendanceTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(subjectPanel, BorderLayout.EAST);

        JLabel totalPercentageLabel = new JLabel("Total Percentage: ");
        subjectPanel.add(totalPercentageLabel); // Move the totalPercentageLabel to subjectPanel
        totalPercentageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add margin around the label

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StudentMenu(username);
            }
        });

        add(closeButton, BorderLayout.SOUTH);

        // Fetch and display attendance data when the frame is created
        fetchAndDisplayAttendance(attendanceTextArea);
        fetchAndDisplaySubjectPercentages(subjectPanel, totalPercentageLabel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchAndDisplayAttendance(JTextArea attendanceTextArea) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM attendance WHERE username = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder attendanceData = new StringBuilder();

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String course = resultSet.getString("course_name");
                String status = resultSet.getString("status");

                attendanceData.append("Date: ").append(date).append(", Course: ").append(course).append(", Status: ").append(status).append("\n");
            }

            if (attendanceData.length() == 0) {
                attendanceData.append("No attendance records found.");
            }

            attendanceTextArea.setText(attendanceData.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while fetching attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while fetching attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchAndDisplaySubjectPercentages(JPanel subjectPanel, JLabel totalPercentageLabel) {
        int totalPresentClasses = 0;
        int totalClasses = 0;

        try (Connection c = SQL.makeConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT course_name, COUNT(*) AS total_classes, " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present_classes " +
                     "FROM attendance WHERE username = ? GROUP BY course_name")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String course = resultSet.getString("course_name");
                int totalClass = resultSet.getInt("total_classes");
                int presentClasses = resultSet.getInt("present_classes");

                double attendancePercentage = (presentClasses * 100.0) / totalClass;

                JLabel subjectLabel = new JLabel(course);
                subjectLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add padding
                JLabel percentageLabel = new JLabel("Percentage: " + String.format("%.2f", attendancePercentage) + "%");
                percentageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add padding

                subjectPanel.add(subjectLabel);
                subjectPanel.add(percentageLabel);
                subjectPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between subjects

                totalPresentClasses += presentClasses;
                totalClasses += totalClass;
            }

            // Calculate total percentage
            double totalPercentage = (totalPresentClasses * 100.0) / totalClasses;
            totalPercentageLabel.setText("Total Percentage: " + String.format("%.2f", totalPercentage) + "%");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while fetching subject percentages: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while fetching subject percentages: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewAttendance("sarthakg.here");
            }
        });
    }
}

