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

import static Module.HashPassword.hashPassword;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    Validation validation = new Validation();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        String username = request.getParameter("username");
        String password = request.getParameter("password_hash");
        String hashedPassword = hashPassword(password);
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        int roleId = Integer.parseInt(request.getParameter("role_id"));

        // Kiểm tra xem các dữ liệu match với validation không
        if (!validation.validateEmail(email) || !validation.validatePassword(password) || !validation.validateUsername(username)) {
            request.setAttribute("error", "Dữ liệu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/jsp/register.jsp");
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            // Kiểm tra xem username hoặc email đã tồn tại chưa
            String checkUserSql = "SELECT * FROM users WHERE username = ? OR email = ?";
            stmt = connection.prepareStatement(checkUserSql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Username hoặc email đã tồn tại
                request.setAttribute("error", "Username hoặc email đã tồn tại.");
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }

            // Gửi mã xác thực qua email
            ReturnMail returnMail = new ReturnMail();
            String captcha = returnMail.generateVerificationCode();
            returnMail.sendMail(email, captcha);

            // Lưu dữ liệu vào session để chuyển sang servlet verify-register
            session.setAttribute("username", username);
            session.setAttribute("password", hashedPassword);
            session.setAttribute("email", email);
            session.setAttribute("gender", gender);
            session.setAttribute("role_id", roleId);
            session.setAttribute("captcha", captcha);

            // Chuyển hướng đến trang verify-register.jsp
            response.sendRedirect(request.getContextPath() + "/jsp/verify-register.jsp");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại do lỗi server.");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
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
