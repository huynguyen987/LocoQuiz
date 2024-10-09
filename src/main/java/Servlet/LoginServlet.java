package Servlet;

import Module.HashPassword;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Compare hashed passwords
            rs = stmt.executeQuery();

            if (rs.next()) {
                // User is authenticated
                HttpSession session = request.getSession();

                int userId = rs.getInt("id"); // Get userId from query result
                int roleId = rs.getInt("role_id"); // Get the role of the user

                session.setAttribute("userId", userId); // Save userId to session
                session.setAttribute("username", username);

                // Map roleId to role name
                String role;
                String redirectPage;

                if (roleId == 1) {
                    role = "student";
                    redirectPage = "jsp/student.jsp"; // Redirect to student page
                } else if (roleId == 2) {
                    role = "teacher";
                    redirectPage = "jsp/teacher.jsp"; // Redirect to teacher page
                } else if (roleId == 3) {
                    role = "admin";
                    redirectPage = "jsp/admin.jsp"; // Redirect to admin page
                } else {
                    // Default role if role_id is unknown
                    role = "guest";
                    redirectPage = "index.jsp";
                }

                // Set role in session
                session.setAttribute("role", role);

                // Redirect to the appropriate page based on role using absolute path
                response.sendRedirect(request.getContextPath() + "/" + redirectPage);

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
