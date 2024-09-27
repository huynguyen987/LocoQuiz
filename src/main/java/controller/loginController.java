package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.UsersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import Module.*;

import model.Users;
import dao.UsersDAO;
import model.Users;

@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class loginController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service == null){
            service = "login";
        }
        if (service.equals("login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String hashedPassword = HashPassword.hashPassword(password);
            ExampleDAO ex = new ExampleDAO();

            // Get password hash from DB
            String user_password_hash = ex.getSingleResult("SELECT password FROM users WHERE username = ?", username);

            // Get user ID by username and hashed password
            String user_id_str = ex.getSingleResult("SELECT id FROM users WHERE username = ? AND password = ?", username, hashedPassword);

            if (user_password_hash != null && user_password_hash.equals(hashedPassword)) {
                // User authentication successful
                int user_id = Integer.parseInt(user_id_str);
                UsersDAO userDAO = new UsersDAO();
                Users user = null;
                try {
                    user = userDAO.getUsersById(user_id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                session.setAttribute("user", user);
                response.sendRedirect("index.jsp");
            } else {
                // nếu sai thì hiện một ô thông báo lỗi ở trang đăng nhập
                session.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
//                tạo một biến error trong session để lưu thông báo lỗi, nếu có lỗi thì hiển thị thông báo lỗi ở trang đăng nhập
//                để in ra thông báo lỗi ở trang đăng nhập, sử dụng cú pháp ${error} trong file jsp

                response.sendRedirect("dangnhap.jsp");
            }
        } else if (service.equals("forgotPassword")) {
            session.invalidate();
            response.sendRedirect("forgotPassword.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
