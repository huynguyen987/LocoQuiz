package com.example.demo;
import Module.HashPassword;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DatabaseConnection;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Raw password from login form

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Hash the entered password for comparison
            String hashedPassword = HashPassword.hashPassword(password);

            connection = DatabaseConnection.getConnection();

            // Query the user from the database using the hashed password
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Compare hashed passwords
            rs = stmt.executeQuery();

            if (rs.next()) {
                // User is authenticated, set session and redirect based on role
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                int roleId = rs.getInt("role_id");
                if (roleId == 3) {
                    response.sendRedirect("../admin.jsp");
                } else {
                    response.sendRedirect("index.jsp");
                }
            } else {
                // Authentication failed, send error message
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Login failed due to server error.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}