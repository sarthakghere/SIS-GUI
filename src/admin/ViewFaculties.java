package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import common.SQL;

public class ViewFaculties extends JFrame {
    private JComboBox<String> removeDropdown;
    private DefaultTableModel tableModel;

    public ViewFaculties(String username) {
        
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM faculty")) {

            ResultSet resultSet = ps.executeQuery();

            // Create a table model to hold the data
            tableModel = new DefaultTableModel();
            JTable table = new JTable(tableModel);

            // Get the number of columns in the result set
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Add columns to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getString(i));
                }
                tableModel.addRow(rowData);
            }

            // Create a scroll pane for the table
            JScrollPane scrollPane = new JScrollPane(table);

            // Create a custom JDialog
            JDialog infoDialog = new JDialog(this, "All Faculty Information", true);
            JButton backButton = new JButton("Back");
            JButton exitButton = new JButton("Exit");

            // Add action listener for Back button
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    infoDialog.dispose(); // Close the dialog
                    new AdminMenu(username);
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

            // Create a panel for removing students
            JPanel removePanel = new JPanel();
            JLabel removeLabel = new JLabel("Remove Faculty:");
            removeDropdown = new JComboBox<>(getFacultyUsernames());
            JButton removeButton = new JButton("Remove");

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeFaculty((String) removeDropdown.getSelectedItem());
                    refreshTable(tableModel);
                }
            });

            removePanel.add(removeLabel);
            removePanel.add(removeDropdown);
            removePanel.add(removeButton);

            // Add components to the main frame
            infoDialog.add(removePanel, BorderLayout.SOUTH);

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);
            buttonPanel.add(exitButton);

            /// Set the size of the window
            infoDialog.setSize(1290, 530);
            infoDialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
            infoDialog.setVisible(true);


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while viewing all students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while viewing all students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }

    private void removeFaculty(String facultyUsername) {
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE username = ?")) {
            ps.setString(1, facultyUsername);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Faculty Removed Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Faculty Not Removed", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(DefaultTableModel tableModel) {
        getFacultyUsernames();
        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM faculty")) {

            ResultSet resultSet = ps.executeQuery();

            // Remove all rows from the table model
            tableModel.setRowCount(0);

            // Get the number of columns in the result set
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                tableModel.addRow(rowData);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error while refreshing table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while refreshing table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector<String> getFacultyUsernames() {
        Vector<String> usernames = new Vector<>();

        try (Connection c = SQL.makeConnection();
             PreparedStatement ps = c.prepareStatement("SELECT username FROM faculty");
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "SQL Error while fetching faculty usernames: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error while fetching faculty usernames: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return usernames;
    }

    public static void main(String[] args) {
        new ViewFaculties("admin");
    }
}