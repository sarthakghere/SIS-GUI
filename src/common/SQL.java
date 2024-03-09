package common;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SQL {
    public static Connection makeConnection() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = SQL.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        }
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        Connection c;

            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, username, password);

        return c;
    }
}
