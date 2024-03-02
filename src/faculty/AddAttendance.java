package faculty;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class AddAttendance extends JFrame {

    private final String[] courses = {"Java Programming", "Web Development", "Cyber Security", "Optimization Techniques", "Computer Networks"};
    private final String[] attendanceStatus = {"Absent", "Present"};

    public AddAttendance() {
        setTitle("Add Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2));
        setPreferredSize(new Dimension(400, 300));

        JLabel usernameLabel = new JLabel("Username of the Student:");
        JTextField usernameField = new JTextField();

        JLabel courseLabel = new JLabel("Course:");
        JComboBox<String> courseDropdown = new JComboBox<>(courses);

        JLabel dateLabel = new JLabel("Date of Attendance:");
        JDateChooser dateChooser = new JDateChooser();

        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusDropdown = new JComboBox<>(attendanceStatus);

        JButton addButton = new JButton("Add Attendance");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String course = (String) courseDropdown.getSelectedItem();
                Date date = dateChooser.getDate();
                String status = (String) statusDropdown.getSelectedItem();

                if (username.isEmpty() || date == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // TODO: Add logic to insert attendance data into the database

                // For now, let's just print the values
                System.out.println("Username: " + username);
                System.out.println("Course: " + course);
                System.out.println("Date: " + date);
                System.out.println("Status: " + status);

                // Close the frame
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the frame
                dispose();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(courseLabel);
        add(courseDropdown);
        add(dateLabel);
        add(dateChooser);
        add(statusLabel);
        add(statusDropdown);
        add(addButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddAttendance();
            }
        });
    }
}
