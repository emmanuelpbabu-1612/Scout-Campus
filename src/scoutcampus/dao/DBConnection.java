package scoutcampus.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place that opens a JDBC connection to the MySQL database.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/scoutcampus?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static void checkDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found. Add MySQL Connector/J to the runtime classpath (for example, lib/mysql-connector-j-8.4.0.jar).",
                    e);
        }
    }

    public static Connection getConnection() throws SQLException {
        checkDriver();
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (user == null || user.isBlank()) {
            throw new SQLException("Database user is not configured. Set the DB_USER environment variable.");
        }
        if (password == null) {
            throw new SQLException("Database password is not configured. Set the DB_PASSWORD environment variable.");
        }
        return DriverManager.getConnection(URL, user, password);
    }
}
