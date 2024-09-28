package com.example.demo;

import Module.HashPassword;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDAO;

import java.io.IOException;


@WebServlet(name = "resetPassController", value = "/resetpass")
public class resetPassController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service == null) {
            service = "resetPass";
        }
        if (service.equals("resetPass")) {
            String password = request.getParameter("password");
            String email = (String) session.getAttribute("email");
            if (email == null || email.isEmpty()) {
                session.setAttribute("error", "Email không hợp lệ.");
                response.sendRedirect("resetPass.jsp");
                return;  // Exit the method to prevent further processing
            }
            System.out.println("Email: " + email);
            User user = new UserDAO().getUserByEmail(email);
            System.out.println("User: " + user.toString());
            if (user == null) {
                session.setAttribute("error", "Người dùng không tồn tại.");
                response.sendRedirect("resetPass.jsp");
                return;
            }
            String hashedPassword = HashPassword.hashPassword(password);
            user.setPasswordHash(hashedPassword);
            boolean updated = new UserDAO().updateUser(user);
            if (updated) {
                session.setAttribute("success", "Cập nhật mật khẩu thành công.");
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            } else {
                session.setAttribute("error", "Cập nhật mật khẩu thất bại.");
                response.sendRedirect(request.getContextPath() + "/jsp/resetPass.jsp");
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