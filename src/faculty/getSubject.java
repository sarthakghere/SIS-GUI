package faculty;

import java.sql.*;
import java.util.*;

import javax.security.auth.Subject;

import common.SQL;;

public class getSubject {
    Subject[] subjects;
    public static void main(String[] args) {
        String[] subs = getStudent();
        for(String sub : subs){
            System.out.println(sub);
        }
    }

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
            // TODO: handle exception
        }
        return subjectStrings;
    }

    public static String[] getStudent() {
        String[] usernameArray = null;
        try (Connection c = SQL.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from student")){
            ResultSet r = ps.executeQuery();
            ArrayList<String> subs = new ArrayList<>();
            while(r.next()){
                String subName = r.getString("first_name");
                subs.add(subName);
            }
            usernameArray = new String[subs.size()];
            subs.toArray(usernameArray);


            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return usernameArray;
    }
    
    
}
