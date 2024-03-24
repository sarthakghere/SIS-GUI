package faculty;
import com.toedter.calendar.JDateChooser;
import common.SQL;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddAttendance extends JFrame {

    private JComboBox<String> subjectDropdown;
    private JDateChooser dateChooser;
    private List<String> studentList;
    private List<JCheckBox> presentCheckboxes;
    private List<JCheckBox> absentCheckboxes;

    public AddAttendance(String username) {
        setTitle("Add Attendance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        subjectDropdown = new JComboBox<>(Getters.getSubjects());
        dateChooser = new JDateChooser();
        studentList = Getters.getStudent();
        presentCheckboxes = new ArrayList<>();
        absentCheckboxes = new ArrayList<>();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Subject:"));
        topPanel.add(subjectDropdown);
        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);

        add(topPanel, BorderLayout.NORTH);

        JPanel checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new GridBagLayout());
        JScrollPane checkboxesScrollPane = new JScrollPane(checkboxesPanel);
        add(checkboxesScrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0 for student names
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

        presentCheckboxes = new ArrayList<>();
        absentCheckboxes = new ArrayList<>();

        // Define a larger font size
        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        // Add student names and checkboxes
        for (String student : studentList) {
            gbc.gridy++;
            checkboxesPanel.add(new JLabel(student), gbc);

            gbc.gridx = 1; // Column 1 for checkboxes
            JCheckBox presentCheckbox = new JCheckBox("Present");
            presentCheckbox.setFont(largerFont);
            presentCheckboxes.add(presentCheckbox);
            
            JCheckBox absentCheckbox = new JCheckBox("Absent");
            absentCheckbox.setFont(largerFont);
            absentCheckboxes.add(absentCheckbox);

            // Group checkboxes for mutual exclusion
            ButtonGroup checkboxGroup = new ButtonGroup();
            checkboxGroup.add(presentCheckbox);
            checkboxGroup.add(absentCheckbox);

            checkboxesPanel.add(presentCheckbox, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

            gbc.gridx = 2; 
            checkboxesPanel.add(absentCheckbox, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space

            gbc.gridx = 0; // Reset to column 0 for the next student name
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Attendance");
        JButton cancelButton = new JButton("Cancel");

        bottomPanel.add(cancelButton);
        bottomPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Adding Student");
                addStudent();
            }});

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new FacultyMenu(username);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addStudent(){
        String selectedSubject = (String) subjectDropdown.getSelectedItem();
                Date selectedDate = dateChooser.getDate();

                if (selectedSubject == null || selectedDate == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("Subject: " + selectedSubject);
                System.out.println("Date: " + dateFormat.format(selectedDate));

                boolean isAnyCheckboxSelected = false;

                for (int i = 0; i < studentList.size(); i++) {
                    String student = studentList.get(i);
                    JCheckBox presentCheckbox = presentCheckboxes.get(i);
                    JCheckBox absentCheckbox = absentCheckboxes.get(i);
                    String status = "Absent";

                    if (presentCheckbox.isSelected()) {
                        System.out.println(student + ": Present");
                        status = "Present";
                        isAnyCheckboxSelected = true;
                    } else if (absentCheckbox.isSelected()) {
                        System.out.println(student + ": Absent");
                        status = "Absent";
                        isAnyCheckboxSelected = true;
                    }
                    addStudentToDatabase(student, selectedDate, selectedSubject, status);
                }

                if (!isAnyCheckboxSelected) {
                    JOptionPane.showMessageDialog(null, "Please select Present or Absent for at least one student.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();
    }

    private void addStudentToDatabase(String username, Date date, String course, String status) {
        try (Connection c = SQL.makeConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO attendance(username, course_name, date, status) VALUES (?, ?, ?, ?)")) {
    
            ps.setString(1, username);
            ps.setString(2, course);
            ps.setDate(3, new java.sql.Date(date.getTime())); // Convert java.util.Date to java.sql.Date
            ps.setString(4, status);
    
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected < 0) {
                System.out.println("Attendance not added");
            } else {
                System.out.println("Attendance added");
            }
    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
            
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddAttendance("faculty1");
            }
        });
    }
}
