package controller;

import java.io.IOException;
import java.io.PrintWriter;

import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import model.UserDAO;
import Module.*;


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
            UserDAO userDAO = new UserDAO();
            int id = userDAO.getUserIdByEmail(email);
            if (id <= 0) {
                session.setAttribute("error", "1.Người dùng không tồn tại.");
                response.sendRedirect("resetPass.jsp");
                return;  // Exit the method to prevent further processing
            }
            User user = userDAO.getUserById(id);
            if (user == null) {
                session.setAttribute("error", "2.Người dùng không tồn tại.");
                response.sendRedirect("resetPass.jsp");
                return;  // Exit the method to prevent further processing
            }
            String hashedPassword = HashPassword.hashPassword(password);
            user.setPasswordHash(hashedPassword);
            boolean success = userDAO.updateUser(user);
            if (success) {
                session.setAttribute("success", "Đổi mật khẩu thành công");
                response.sendRedirect("dangnhap.jsp");
            } else {
                session.setAttribute("error", "Đổi mật khẩu thất bại");
                response.sendRedirect("resetPass.jsp");
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