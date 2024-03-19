package faculty;

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import common.SQL;

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

    public static List<String> getStudent() {
        List<String> usernames = new ArrayList<String>();
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from student")){
            ResultSet r = ps.executeQuery();
            while(r.next()){
                String username = r.getString("username");
                usernames.add(username);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usernames;
    }
    
    
}
