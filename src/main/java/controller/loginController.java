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

import entity.User;

@WebServlet(name = "loginController", value = "/login")
public class loginController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service.equals("login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String hashedPassword = HashPassword.hashPassword(password);
            DBConnect db = new DBConnect();
            String user_password_hash = db.getData("SELECT passwordHash FROM users WHERE username = '" + username + "'").toString();
            if (username.equals("admin") && hashedPassword.equals(user_password_hash)) {
                session.setAttribute("username", username);
                response.sendRedirect("admin.jsp");
            } else {
                // print error message: "Invalid username or password"
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Invalid username or password');");
                out.println("</script>");
                response.sendRedirect("login.jsp");
            }
        }else if (service.equals("forgotPassword")) {
                session.invalidate();
                response.sendRedirect("forgotPassword.jsp");
            }
        }

    @Override
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