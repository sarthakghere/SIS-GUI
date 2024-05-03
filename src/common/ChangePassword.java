package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class ChangePassword extends JFrame{
    String username;
    public ChangePassword(String username) {
        this.username = username;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));
        setPreferredSize(new Dimension(400, 250));
    
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
    
        JButton changeButton = new JButton("Change Password");
        JButton cancelButton = new JButton("Cancel");
    
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
    
        add(new JLabel("Current Password:"));
        add(currentPasswordField);
        add(new JLabel("New Password:"));
        add(newPasswordField);
        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);
        add(changeButton);
        add(cancelButton);
        add(new JLabel("")); // Placeholder for alignment
        add(errorLabel);
    
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] currentPassword = currentPasswordField.getPassword();
                char[] newPassword = newPasswordField.getPassword();
                char[] confirmPassword = confirmPasswordField.getPassword();
    
                String currPass = new String(currentPassword);
                String newPass = new String(newPassword);
                String confirmPass = new String(confirmPassword);
    
                // Check if any field is empty
                if (currPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    errorLabel.setText("All fields must be filled.");
                    return;
                }
    
                // Check if current password and new password match
                if (currPass.equals(newPass)) {
                    errorLabel.setText("New password must be different from the current password.");
                    return;
                }
    
                // Check if new password and confirm password match
                if (!newPass.equals(confirmPass)) {
                    errorLabel.setText("New password and confirm password do not match.");
                    return;
                }
    
                // Continue with changing the password in the database
                changePasswordInDatabase(currPass, newPass, confirmPass);
    
                // Close the changePassFrame
                dispose();
            }
        });
    
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    

    private void changePasswordInDatabase(String currPass, String newPass, String confirmPass){
        try(Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select password from user where username = ?");){
            ps.setString(1, username);
            ResultSet result = ps.executeQuery();
            result.next();
            String actualPass = result.getString("password");
            if(actualPass.equals(Hashing.hashPassword(currPass))){
                if(newPass.equals(confirmPass)){
                    PreparedStatement updateString = c.prepareStatement("update user set password = ? where username = ?");
                    updateString.setString(1, Hashing.hashPassword(newPass));
                    updateString.setString(2, username);
                    int rowsAffected = updateString.executeUpdate();
                    if(rowsAffected < 0){
                        JOptionPane.showMessageDialog(this, "Error updating password", "Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(this, "Password updated for "+ username, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "New Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Wrong current password ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(Exception e){

        }
    }
}
