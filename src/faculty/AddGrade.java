package faculty;

import common.SQL; 

import javax.swing.*;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddGrade extends JFrame {
    String username;
    public AddGrade(String username) {
        this.username = username;
        JButton cancelButton = new javax.swing.JButton();
        JButton submitButton = new javax.swing.JButton();
        JPanel TopPanel = new javax.swing.JPanel();
        JScrollBar jScrollBar1 = new javax.swing.JScrollBar();
        JPanel jPanel2 = new javax.swing.JPanel();
        JLabel subjectLabel = new javax.swing.JLabel();
        JComboBox subjectComboBox = new javax.swing.JComboBox<>(getSubjectList());
        JLabel testLabel = new javax.swing.JLabel();
        JComboBox testComboBox = new javax.swing.JComboBox<>();
        JLabel dateLabel = new javax.swing.JLabel();
        JDateChooser jDateChooser1 = new com.toedter.calendar.JDateChooser();
        JPanel middlePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        submitButton.setText("Submit");

        subjectLabel.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        subjectLabel.setText("Subject: ");

        subjectComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        testLabel.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        testLabel.setText("Test:");

        testComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CES 1", "CES 2", "Internal 1", "Internal 2" }));
        testComboBox.setMinimumSize(new java.awt.Dimension(72, 20));
        testComboBox.setPreferredSize(new java.awt.Dimension(72, 20));

        dateLabel.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        dateLabel.setText("Date:");

        List<String> studentList = getAllStudents(); // Modify this method to fetch student data from the database securely (see below)
        ArrayList<JTextField> gradeFields = new ArrayList<>();
        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0 for student names
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (String student : studentList) {
            gbc.gridy++;
            middlePanel.add(new JLabel(student), gbc);
        
            gbc.gridx = 1;
            JTextField gradeField = new JTextField();
            gradeField.setFont(largerFont);
            gradeFields.add(gradeField);
            middlePanel.add(gradeField, gbc);
        
            gbc.gridx = 0; // Reset to column 0 for the next student name
        }

        javax.swing.GroupLayout TopPanelLayout = new javax.swing.GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subjectLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(testLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(testComboBox, 0, 186, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TopPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145))
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TopPanelLayout.createSequentialGroup()
                        .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(subjectComboBox)
                            .addComponent(subjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(testLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(testComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(TopPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout middlePanelLayout = new javax.swing.GroupLayout(middlePanel);
        middlePanel.setLayout(middlePanelLayout);
        
        middlePanelLayout.setVerticalGroup(
    middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(middlePanelLayout.createSequentialGroup()
    .addContainerGap(20 * studentList.size(), 20) // Adjust spacing as needed
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(middlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(TopPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(middlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(middlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(submitButton)))
        );

        pack();
        setResizable(false);
        setVisible(true);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
        new FacultyMenu(username);
    }

    private String[] getSubjectList() {
        
        // Modify this method to fetch subject data from the database securely (see below)
        return new String[]{"Java Programming", "Web Development", "Cyber Security", "Optimization Techniques", "Computer Networks", "Operating System"};
    }

    private List<String> getAllStudents() {
        List<String> usernames = new ArrayList<>();
        try(Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select username from student");){
            String username;

            ResultSet r = ps.executeQuery();

            while(r.next()){
                username = r.getString("username");
                usernames.add(username);
            }
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usernames;
    }

    public static void main(String[] args){
        new AddGrade("faculty1");
    }
}