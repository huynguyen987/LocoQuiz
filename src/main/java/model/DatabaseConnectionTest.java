package model;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void testConnection() {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    public static void main(String[] args) {
        testConnection();
    }
}
