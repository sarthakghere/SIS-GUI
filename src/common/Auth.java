package common;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class Auth {
    public static boolean authenticate(String username, String password, String role) {
        
        String encryptedPass = Hashing.hashPassword(password);

        try (Connection c = SQL.makeConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT password FROM User WHERE username = ? and role = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role.toLowerCase());
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                if (res.getObject(1).equals(encryptedPass)) {
                    System.out.println("Login Successful");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password or Username", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Login Unsuccessful");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password or Username", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Username not found.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Login failed. Error: " + e.getMessage());
            return false;
        }
    }
}
