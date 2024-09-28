package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database credentials and URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test"; // Replace with your DB URL
    private static final String USER = "root"; // Replace with your DB username
    private static final String PASSWORD = "12345678"; // Replace with your DB password
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"; // MySQL Driver

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the MySQL JDBC driver
        Class.forName(DRIVER_CLASS);

        // Establish the connection
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        return connection;
    }

    // Optional: Method to close the connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

