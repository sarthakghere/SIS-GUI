package faculty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.*;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import common.SQL;

public class ViewStudents extends JFrame{
    String username;
        public ViewStudents(String username) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("select * from student")) {

            ResultSet resultSet = ps.executeQuery();

            // Create a table model to hold the data
            DefaultTableModel tableModel = new DefaultTableModel();
            JTable table = new JTable(tableModel);

            // Get the number of columns in the result set
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Add columns to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                tableModel.addRow(rowData);
            }

            // Create a scroll pane for the table
            JScrollPane scrollPane = new JScrollPane(table);

            // Create a custom JDialog
            JDialog infoDialog = new JDialog(this, "All Students Information", true);
            JButton backButton = new JButton("Back");
            JButton exitButton = new JButton("Exit");

            // Add action listener for Back button
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    infoDialog.dispose(); // Close the dialog
                    // Additional actions for Back button if needed
                }
            });

            // Add action listener for Exit button
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });

            // Set layout for the dialog
            infoDialog.setLayout(new BorderLayout());
            infoDialog.add(scrollPane, BorderLayout.CENTER);

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);
            buttonPanel.add(exitButton);

            // Add button panel to the dialog
            infoDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Increase the size of the window
            infoDialog.setSize(600, 400);

            infoDialog.pack();
            infoDialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
            infoDialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while viewing all students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while viewing all students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
