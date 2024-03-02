package student;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentMenu extends JFrame{
    
    private final String username;

    public StudentMenu(String username) {
        this.username = username;
        setTitle("Student Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));
        setPreferredSize(new Dimension(500, 400)); // Set preferred size to make the window look bigger

        JButton viewInfoButton = createStyledButton("View Information");
        JButton viewGradesButton = createStyledButton("View Grades");
        JButton viewAttendanceButton = createStyledButton("View Attendance");
        JButton changePasswordButton = createStyledButton("Change Password");
        JButton logoutButton = createStyledButton("Logout");

        add(viewInfoButton);
        add(viewGradesButton);
        add(viewAttendanceButton);
        add(changePasswordButton);
        add(logoutButton);

        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private JButton createStyledButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set a bigger font
        button.setPreferredSize(new Dimension(200, 40)); // Set a larger button size
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(buttonText);
            }
        });
        return button;
    }

    private void handleButtonClick(String buttonName) {
        switch (buttonName) {
            case "View Information":
                new ViewInformation(username);
                break;
            case "View Grades":
                new ViewGrades();
                break;
            case "View Attendance":
                new ViewAttendance(username);
                break;
            case "Change Password":
                new common.ChangePassword(username);
                break;
            case "Logout":
                new common.Logout(this);
                break;
            default:
                // Handle unknown button click
                break;
        }
    }
}
