package common;
import javax.swing.*;

public class Logout {
    public Logout(JFrame j) {

                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    j.dispose(); // Close the current JFrame
                    new LoginWindow(); // Open the main menu
                }
                
            }
}
