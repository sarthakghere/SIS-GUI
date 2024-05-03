package faculty;

import common.ChangePassword;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import common.*;
import javax.swing.*;

public class FacultyMenu extends JFrame {
    private final String username;

    public FacultyMenu(String username) {
        this.username = username;
        setTitle("Faculty Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));
        setPreferredSize(new Dimension(500, 400));

        JButton viewInfoButton = createStyledButton("View Information");
        JButton viewAllStudentsButton = createStyledButton("View All Students");
        JButton addStudentButton = createStyledButton("Add Student");
        JButton addGradeButton = createStyledButton("Add Student Grade");
        JButton addStudentAttendanceButton = createStyledButton("Add Student Attendance");
        JButton updateStudentInfoButton = createStyledButton("Update Student Information");
        JButton changePasswordButton = createStyledButton("Change Password");
        JButton logoutButton = createStyledButton("Logout");

        add(viewInfoButton);
        add(viewAllStudentsButton);
        add(addStudentButton);
        add(addGradeButton);
        add(addStudentAttendanceButton);
        add(updateStudentInfoButton);
        add(changePasswordButton);
        add(logoutButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 40));
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
            case "View All Students":
                new ViewStudents(username);
                break;
            case "Add Student":
                dispose();
                new AddStudent(username);
                break;
            case "Add Student Grade":
                new AddGrade(username);
                break;
            case "Add Student Attendance":
                new AddAttendance(username);
                break;
            case "Update Student Information":
                new UpdateStudent(username);
                break;
            case "Change Password":
                new ChangePassword(username);
                break;
            case "Logout":
                new Logout(this);
                break;
            default:
                break;
        }
    }
    public static void main(String[] args) {
        new FacultyMenu("faculty1");
    }
}
