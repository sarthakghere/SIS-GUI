package admin;

import common.ChangePassword;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import common.*;
import javax.swing.*;

public class AdminMenu extends JFrame {
    private final String username;
    public AdminMenu(String username) {
        this.username = username;
        setTitle("Faculty Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));
        setPreferredSize(new Dimension(500, 400));

        JButton viewAllFacultyButton = createStyledButton("View All Faculties");
        JButton addFacultyButton = createStyledButton("Add Faculty");
        JButton updateFacultyInfoButton = createStyledButton("Update Faculty Information");
        JButton addCourseButton = createStyledButton("Add Course");
        JButton viewAllCourseButton = createStyledButton("View All Courses");
        JButton viewAllStudentsButton = createStyledButton("View All Students");
        JButton updateStudentsInfoButton = createStyledButton("Update Student Information");
        JButton changePasswordButton = createStyledButton("Change Password");
        JButton logoutButton = createStyledButton("Logout");
        JButton updateCourseButton = createStyledButton("Update Course");

        add(viewAllFacultyButton);
        add(addFacultyButton);
        add(updateFacultyInfoButton);
        add(viewAllCourseButton);
        add(addCourseButton);
        add(updateCourseButton);
        add(viewAllStudentsButton);
        add(updateStudentsInfoButton);
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
            case "View All Faculties":
                new ViewFaculties(username);
                break;
            case "Add Faculty":
                new AddFaculty(username);
                break;
            case "Update Faculty Information":
                new UpdateFaculty(username);
                break;
            case "View All Students":
                new ViewStudents(username);
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
            case "View All Courses":
                new ViewCourse(username);
                break;
            case "Add Course":
                new AddCourse(username);
                break;
            default:
                break;
        }
    }
    public static void main(String[] args) {
        new AdminMenu("admin");
    }
}
