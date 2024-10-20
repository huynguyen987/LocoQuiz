package Servlet;

import Module.HashPassword;
import dao.UsersDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/loginn")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the username and password from the login form
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Raw password from login form

        UsersDAO usersDAO = new UsersDAO();

        try {
            // Authenticate the user
            Users user = usersDAO.checkLogin(username, password);

            if (user != null) {
                // User is authenticated, set session and redirect based on role
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user); // Store the Users object in session
                    session.setAttribute("userId", user.getId()); // Store the user ID in session

                int roleId = user.getRole_id(); // Get the role of the user
                if (roleId == Users.ROLE_ADMIN) {
                    response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp");
                } else if (roleId == Users.ROLE_TEACHER) {
                    response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp");
                } else if (roleId == Users.ROLE_STUDENT) {
                    response.sendRedirect(request.getContextPath() + "/jsp/student.jsp");
                } else {
                    // Redirect to a default page if role is unknown
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
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
        }
    }


}
