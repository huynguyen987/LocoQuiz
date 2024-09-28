package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExampleUsage {

    public void fetchData() {
        Connection connection = null;
        try {
            // Get the connection
            connection = DatabaseConnection.getConnection();

            // Write your SQL query
            String sql = "SELECT * FROM learningmanagementsystem.subjects";
            PreparedStatement stmt = connection.prepareStatement(sql);

            // Execute the query and retrieve results
            ResultSet rs = stmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                String columnData = rs.getString("title");

                System.out.println("Data: " + columnData);
            }

            // Close the ResultSet and PreparedStatement
            rs.close();
            stmt.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            DatabaseConnection.closeConnection(connection);
        }
    }

    public static void main(String[] args) {
        ExampleUsage example = new ExampleUsage();
        example.fetchData();
    }
}

