package Servlet;

import java.io.IOException;
import java.security.MessageDigest;
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
import Module.*;

@WebServlet("/verify-register")
public class verifyRegister extends HttpServlet { // Đổi tên lớp theo chuẩn CamelCase

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập mã hóa cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false); // Lấy session hiện tại, không tạo mới nếu không tồn tại

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/register.jsp");
            return;
        }

        // Lấy dữ liệu từ session
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        String email = (String) session.getAttribute("email");
        String gender = (String) session.getAttribute("gender");
        Integer roleId = (Integer) session.getAttribute("role_id"); // Sử dụng Integer để tránh NullPointerException
        String sessionCaptcha = (String) session.getAttribute("captcha");

        System.out.println("User data from session: ");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        System.out.println("hashedPassword: " + password);
        System.out.println("email: " + email);
        System.out.println("gender: " + gender);
        System.out.println("role_id: " + roleId);
        System.out.println("sessionCaptcha: " + sessionCaptcha);

        // Kiểm tra xem các dữ liệu cần thiết có tồn tại trong session không
        if (username == null || password == null || email == null || gender == null || roleId == null || sessionCaptcha == null) {
            // Nếu thiếu dữ liệu, chuyển hướng về trang đăng ký
            response.sendRedirect(request.getContextPath() + "/jsp/register.jsp");
            return;
        }

        // Lấy mã xác thực người dùng nhập
        String userCaptcha = request.getParameter("capcha");
        System.out.println("userCaptcha: " + userCaptcha);

        // Kiểm tra mã captcha nhập vào
        if (userCaptcha == null || !userCaptcha.equals(sessionCaptcha)) {
            // Nếu mã captcha không đúng, hiển thị lỗi và cho phép người dùng thử lại
            request.setAttribute("error", "Mã xác thực không đúng. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        // Nếu mã captcha đúng, chèn dữ liệu vào cơ sở dữ liệu
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            String insertUserSql = "INSERT INTO users (username, password, email, gender, role_id) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertUserSql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, gender);
            stmt.setInt(5, roleId);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Xóa các thuộc tính trong session sau khi đăng ký thành công
                session.removeAttribute("username");
                session.removeAttribute("password");
                session.removeAttribute("email");
                session.removeAttribute("gender");
                session.removeAttribute("role_id");
                session.removeAttribute("captcha");

                // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            } else {
                // Nếu không chèn được dữ liệu, hiển thị lỗi
                request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("/jsp/verify-register.jsp").forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại do lỗi server.");
            request.getRequestDispatcher("/jsp/verify-register.jsp").forward(request, response);
        } finally {
            // Đóng kết nối và các tài nguyên
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

