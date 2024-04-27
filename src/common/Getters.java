package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Getters {
    public static String[] getSubjects() {
        String[] subjectStrings = null;
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from course")){
            ResultSet r = ps.executeQuery();
            ArrayList<String> subs = new ArrayList<>();
            while(r.next()){
                String subName = r.getString("course_name");
                subs.add(subName);
            }
            subjectStrings = new String[subs.size()];
            subs.toArray(subjectStrings);
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return subjectStrings;
    }
}
