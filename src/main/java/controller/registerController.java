package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import Module.*;

@WebServlet(name = "registerController", value = "/registerController")
public class registerController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service.equals("register")) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int roleId = 0;
            if (role.equals("Teacher")) {
                roleId = 1;
            } else if (role.equals("Student")) {
                roleId = 2;
            }

            // Check if username is already taken
            DBConnect db = new DBConnect();
            String usernameQuery = db.getData("SELECT username FROM users WHERE username = '" + username + "'").toString();
            if (username.equals(usernameQuery)) {
                // print error message: "Username already taken"
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Username already taken');");
                out.println("</script>");
                response.sendRedirect("register.jsp");
            } else {
                // Insert new user into the database
                String hashedPassword = HashPassword.hashPassword(password);
                String insertQuery = "INSERT INTO users (username, email, passwordHash, roleId) VALUES ('" + username + "', '" + email + "', '" + hashedPassword + "', " + roleId + ")";
                db.getData(insertQuery);
                response.sendRedirect("login.jsp");
            }
        }
    }

    protected void doGet
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}