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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import common.SQL;

public class ViewInformation extends JFrame {
    String username;

    public ViewInformation(String username) {
        System.out.println("Viewing Information");
        this.username = username;
        try (Connection c = SQL.makeConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM faculty WHERE username = ?")) {

            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                // Construct the information message with improved formatting
                StringBuilder infoMessage = new StringBuilder();
                infoMessage.append("<html>");
                infoMessage.append("<style>body { font-family: 'Arial', sans-serif; font-size: 14px; padding: 10px; }</style>");
                infoMessage.append("<h2>Details for user ").append(username).append("</h2>");
                infoMessage.append("<ul>");
                infoMessage.append("<li><strong>ID:</strong> ").append(res.getString("faculty_id")).append("</li>");
                infoMessage.append("<li><strong>First Name:</strong> ").append(res.getString("first_name")).append("</li>");
                infoMessage.append("<li><strong>Last Name:</strong> ").append(res.getString("last_name")).append("</li>");
                infoMessage.append("<li><strong>Email:</strong> ").append(res.getString("email")).append("</li>");
                infoMessage.append("<li><strong>Phone:</strong> ").append(res.getString("phone_number")).append("</li>");
                infoMessage.append("</ul>");
                infoMessage.append("</html>");

                // Create a custom JDialog
                JDialog infoDialog = new JDialog(this, "Faculty Information", true);
                JLabel infoLabel = new JLabel(infoMessage.toString());
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
                infoDialog.add(infoLabel, BorderLayout.CENTER);

                // Create a panel for buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(backButton);
                buttonPanel.add(exitButton);

                // Add button panel to the dialog
                infoDialog.add(buttonPanel, BorderLayout.SOUTH);

                // Increase the size of the window
                infoDialog.setSize(500, 400);

                infoDialog.pack();
                infoDialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
                infoDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while viewing faculty information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
