package Module;

import java.sql.*;
import model.Users;

public class DBConnect {

    private Connection conn;

    // Constructor to establish connection when the object is created
    public DBConnect() {
//        try {
//            // Load the MySQL JDBC Driver
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // Establish the connection
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learningmanagementsystem", "root", "12345678");
//            System.out.println("Connected to MySQL database");
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("MySQL Driver not found");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            System.out.println("Failed to connect to the MySQL database");
//            e.printStackTrace();
//        }
    }

    // Function to fetch data from the database
    public ResultSet getData(String sql) {
        ResultSet rs = null;
        try {
            // Create a statement object to send SQL commands to the database
            conn = getConnection();
            Statement  stmt = conn.createStatement();

            // Execute the query and get the result set
            rs = stmt.executeQuery(sql);

            if (rs != null) {
                System.out.println("Data fetched successfully");
            } else {
                System.out.println("Failed to fetch data");
            }
        } catch (SQLException e) {
            System.out.println("SQL query execution failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    // Get the database connection
    public Connection getConnection() throws SQLException, ClassNotFoundException {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/learningmanagementsystem", "root", "12345678");
    }

    // Function to fetch a User object based on the username
    public Users getUser(String username) {
        Users user = null;
        try {
            // Use PreparedStatement to prevent SQL injection
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create a new user object and set its attributes
                user = new Users();
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setRoleId(rs.getInt("roleId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Main method to test the connection and data retrieval
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        DBConnect db = new DBConnect();
//
//        // Example query to fetch data from the users table
//        String query = "SELECT * FROM users";
//        Connection conn = db.getConnection();
//        // Call getData and retrieve the Results
//        ResultSet rs = db.getData(query);
//        System.out.println(conn);
//        try {
//            // Process the result set
//            while (rs != null && rs.next()) {
//                // Assuming the table has columns 'id' and 'username', modify based on your table structure
//                int id = rs.getInt("id");
//                String name = rs.getString("username");
//                System.out.println("ID: " + id + ", Name: " + name);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // Close the ResultSet and Connection
//                if (rs != null) rs.close();
//                if (db.getConnection() != null) db.getConnection().close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println(new DBConnect().getConnection());
    }
}
