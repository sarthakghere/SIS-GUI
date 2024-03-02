// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// // public class Student extends JFrame {
//     // private final String username;

//     // public Student(String username) {
//     //     this.username = username;
//     //     setTitle("Student Menu");
//     //     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     //     setLayout(new GridLayout(5, 1));
//     //     setPreferredSize(new Dimension(500, 400)); // Set preferred size to make the window look bigger

//     //     JButton viewInfoButton = createStyledButton("View Information");
//     //     JButton viewGradesButton = createStyledButton("View Grades");
//     //     JButton viewAttendanceButton = createStyledButton("View Attendance");
//     //     JButton changePasswordButton = createStyledButton("Change Password");
//     //     JButton logoutButton = createStyledButton("Logout");

//     //     add(viewInfoButton);
//     //     add(viewGradesButton);
//     //     add(viewAttendanceButton);
//     //     add(changePasswordButton);
//     //     add(logoutButton);

//     //     pack();
//     //     setLocationRelativeTo(null); // Center the frame
//     //     setVisible(true);
//     // }

//     // private JButton createStyledButton(String buttonText) {
//     //     JButton button = new JButton(buttonText);
//     //     button.setFont(new Font("Arial", Font.BOLD, 16)); // Set a bigger font
//     //     button.setPreferredSize(new Dimension(200, 40)); // Set a larger button size
//     //     button.addActionListener(new ActionListener() {
//     //         @Override
//     //         public void actionPerformed(ActionEvent e) {
//     //             handleButtonClick(buttonText);
//     //         }
//     //     });
//     //     return button;
//     // }

//     // private void handleButtonClick(String buttonName) {
//     //     switch (buttonName) {
//     //         case "View Information":
//     //             viewStudentInformation();
//     //             break;
//     //         case "View Grades":
//     //             viewStudentGrades();
//     //             break;
//     //         case "View Attendance":
//     //             viewStudentAttendance();
//     //             break;
//     //         case "Change Password":
//     //             changePassword();
//     //             break;
//     //         case "Logout":
//     //             logout();
//     //             break;
//     //         default:
//     //             // Handle unknown button click
//     //             break;
//     //     }
//     // }

//     // private void viewStudentInformation() {
        
//     // }
    

//     // private void viewStudentGrades() {
//     //     // Implement the logic to retrieve and display student grades
//     //     JOptionPane.showMessageDialog(this, "Viewing student grades for username: " + username, "Grades", JOptionPane.INFORMATION_MESSAGE);
//     //     // You can display student grades in a JOptionPane or any other Swing component.
//     // }

//     // private void viewStudentAttendance() {
//     //     // Implement the logic to retrieve and display student attendance
//     //     JOptionPane.showMessageDialog(this, "Viewing student attendance for username: " + username, "Attendance", JOptionPane.INFORMATION_MESSAGE);
//     //     // You can display student attendance in a JOptionPane or any other Swing component.
//     // }

//     // private void changePassword() {
//     //     JFrame changePassFrame = new JFrame("Change Password");
//     //     changePassFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//     //     changePassFrame.setLayout(new GridLayout(5, 2));
//     //     changePassFrame.setPreferredSize(new Dimension(400, 250));
    
//     //     JPasswordField currentPasswordField = new JPasswordField();
//     //     JPasswordField newPasswordField = new JPasswordField();
//     //     JPasswordField confirmPasswordField = new JPasswordField();
    
//     //     JButton changeButton = new JButton("Change Password");
//     //     JButton cancelButton = new JButton("Cancel");
    
//     //     JLabel errorLabel = new JLabel("");
//     //     errorLabel.setForeground(Color.RED);
    
//     //     changePassFrame.add(new JLabel("Current Password:"));
//     //     changePassFrame.add(currentPasswordField);
//     //     changePassFrame.add(new JLabel("New Password:"));
//     //     changePassFrame.add(newPasswordField);
//     //     changePassFrame.add(new JLabel("Confirm Password:"));
//     //     changePassFrame.add(confirmPasswordField);
//     //     changePassFrame.add(changeButton);
//     //     changePassFrame.add(cancelButton);
//     //     changePassFrame.add(new JLabel("")); // Placeholder for alignment
//     //     changePassFrame.add(errorLabel);
    
//     //     changeButton.addActionListener(new ActionListener() {
//     //         @Override
//     //         public void actionPerformed(ActionEvent e) {
//     //             char[] currentPassword = currentPasswordField.getPassword();
//     //             char[] newPassword = newPasswordField.getPassword();
//     //             char[] confirmPassword = confirmPasswordField.getPassword();
    
//     //             String currPass = new String(currentPassword);
//     //             String newPass = new String(newPassword);
//     //             String confirmPass = new String(confirmPassword);
    
//     //             // Check if any field is empty
//     //             if (currPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
//     //                 errorLabel.setText("All fields must be filled.");
//     //                 return;
//     //             }
    
//     //             // Check if current password and new password match
//     //             if (currPass.equals(newPass)) {
//     //                 errorLabel.setText("New password must be different from the current password.");
//     //                 return;
//     //             }
    
//     //             // Check if new password and confirm password match
//     //             if (!newPass.equals(confirmPass)) {
//     //                 errorLabel.setText("New password and confirm password do not match.");
//     //                 return;
//     //             }
    
//     //             // Continue with changing the password in the database
//     //             changePasswordInDatabase(currPass, newPass, confirmPass);
    
//     //             // Close the changePassFrame
//     //             changePassFrame.dispose();
//     //         }
//     //     });
    
//     //     cancelButton.addActionListener(new ActionListener() {
//     //         @Override
//     //         public void actionPerformed(ActionEvent e) {
//     //             // Close the changePassFrame
//     //             changePassFrame.dispose();
//     //         }
//     //     });
    
//     //     changePassFrame.pack();
//     //     changePassFrame.setLocationRelativeTo(null);
//     //     changePassFrame.setVisible(true);
//     // }
    

//     // private void changePasswordInDatabase(String currPass, String newPass, String confirmPass){
//     //     try(Connection c = SQL.makeConnection();
//     //     PreparedStatement ps = c.prepareStatement("select password from user where username = ?");){
//     //         ps.setString(1, username);
//     //         ResultSet result = ps.executeQuery();
//     //         result.next();
//     //         String actualPass = result.getString("password");
//     //         if(actualPass.equals(Hashing.hashPassword(currPass))){
//     //             if(newPass.equals(confirmPass)){
//     //                 PreparedStatement updateString = c.prepareStatement("update user set password = ? where username = ?");
//     //                 updateString.setString(1, Hashing.hashPassword(newPass));
//     //                 updateString.setString(2, username);
//     //                 int rowsAffected = updateString.executeUpdate();
//     //                 if(rowsAffected < 0){
//     //                     JOptionPane.showMessageDialog(this, "Error updating password", "Error", JOptionPane.ERROR_MESSAGE);
//     //                 }else{
//     //                     JOptionPane.showMessageDialog(this, "Password updated for "+ username, "Success", JOptionPane.INFORMATION_MESSAGE);
//     //                 }
//     //             }
//     //             else{
//     //                 JOptionPane.showMessageDialog(this, "New Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
//     //             }
//     //         }
//     //         else{
//     //             JOptionPane.showMessageDialog(this, "Wrong current password ", "Error", JOptionPane.ERROR_MESSAGE);
//     //         }
//     //     }
//     //     catch(Exception e){

//     //     }
//     // }

//     // private void logout() {
//     //     int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
//     //     if (option == JOptionPane.YES_OPTION) {
//     //         dispose(); // Close the current JFrame
//     //         new LoginWindow(); // Open the main menu
//     //     }
//     // }

//     public static void main(String[] args) {
//         new Student("sarthakg.here").changePassword();
//     }
// }
