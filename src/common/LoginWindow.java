package common;

import javax.swing.*;
import faculty.FacultyMenu;
import student.StudentMenu;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginWindow() {
        // Set up the frame
        setTitle("Login Window");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        // Set font to bold and center align for username and password labels
        Font boldFont = new Font(usernameLabel.getFont().getName(), Font.BOLD, 16);
        usernameLabel.setFont(boldFont);
        passwordLabel.setFont(boldFont);
        roleLabel.setFont(boldFont);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        String[] roles = {"Student", "Faculty", "Admin"};
        roleComboBox = new JComboBox<>(roles);

        JButton loginButton = new JButton("Login");

        // Set layout
        setLayout(new GridLayout(4, 2, 5, 5));

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);
        setVisible(true);

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String role = (String) roleComboBox.getSelectedItem();
                if (Auth.authenticate(username, new String(password), role)) {
                    switch (role) {
                        case "Student":
                            dispose();
                            new StudentMenu(username);
                            break;

                        case "Faculty":
                            dispose();
                            new FacultyMenu(username);
                            break;

                        case "Admin":
                            dispose();
                            new admin.AdminMenu(username);
                            break;

                        default:
                            // Handle other roles or leave it empty
                            break;
                    }
                }
            }
        });
    }
}
