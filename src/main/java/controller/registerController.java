package controller;

import java.io.IOException;
import java.sql.SQLException;

import model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import Module.*;
import dao.UsersDAO;

@WebServlet(name = "registerController", value = "/register")
public class registerController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String confirmPassword = request.getParameter("confirm_password");
        if (service == null) {
            service = "register";
        }
        if (service.equals("register")) {
            int roleId = 0;
            if (role.equals("teacher")) {
                roleId = 1;
            } else if (role.equals("student")) {
                roleId = 2;
            }

            // Check if username is already taken
            ExampleDAO ex = new ExampleDAO();
            String user_id_str = ex.getSingleResult("SELECT id FROM users WHERE username = ?", username);
            System.out.println(user_id_str);
            // kiem tra neu 2 mat khau khong trung nhau
            if (!password.equals(confirmPassword)) {
                session.setAttribute("error", "Mật khẩu không trùng khớp");
                response.sendRedirect("dangki.jsp");
            } else
            if (user_id_str != null) {
                // Username is already taken
                session.setAttribute("error", "Tên đăng nhập đã tồn tại");
                response.sendRedirect("dangki.jsp");
            } else {
                // Username is available
                String hashedPassword = HashPassword.hashPassword(password);
                Users user = new Users();
                user.setUsername(username);
                user.setEmail(email);
                user.setPasswordHash(hashedPassword);
                user.setRoleId(roleId);
                UsersDAO userDAO = new UsersDAO();
                try {
                    userDAO.addUser(user);
                    session.setAttribute("success", "Đăng kí thành công");
                    response.sendRedirect("dangnhap.jsp");
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    session.setAttribute("error", "Đăng kí thất bại");
                    response.sendRedirect("dangki.jsp");
                }
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