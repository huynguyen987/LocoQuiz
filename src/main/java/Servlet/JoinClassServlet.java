package Servlet;

import dao.ClassDAO;
import dao.ClassUserDAO;
import entity.Users;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "JoinClassServlet", value = "/JoinClassServlet")
public class JoinClassServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check user session
        HttpSession session = request.getSession(false);
        Users currentUser = null;
        if (session != null) {
            currentUser = (Users) session.getAttribute("user");
        }
        if (currentUser == null || currentUser.getRole_id() != Users.ROLE_STUDENT) {
            response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
            return;
        }

        String classKey = request.getParameter("classKey");

        if (classKey == null || classKey.trim().isEmpty()) {
            String error = URLEncoder.encode("Vui lòng nhập mã lớp học.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + error);
            return;
        }

        ClassDAO classDAO = new ClassDAO();
        ClassUserDAO classUserDAO = new ClassUserDAO();

        try {
            // Find class by classKey
            classs classEntity = classDAO.getClassByClassKey(classKey);

            if (classEntity == null) {
                String error = URLEncoder.encode("Không tìm thấy lớp học với mã đã nhập.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + error);
                return;
            }

            int classId = classEntity.getId();
            int studentId = currentUser.getId();

            // Check if student is already enrolled or has a pending request
            if (classUserDAO.isUserEnrolledInClass(classId, studentId)) {
                String message = URLEncoder.encode("Bạn đã tham gia lớp học này rồi hoặc đang chờ duyệt.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?message=" + message);
                return;
            }

            // Send join request
            boolean isRequested = classUserDAO.sendJoinRequest(classId, studentId);

            if (isRequested) {
                String message = URLEncoder.encode("Yêu cầu tham gia lớp học đã được gửi.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?message=" + message);
            } else {
                String error = URLEncoder.encode("Có lỗi xảy ra. Vui lòng thử lại.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + error);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            String error = URLEncoder.encode("Có lỗi hệ thống. Vui lòng thử lại sau.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + error);
        }
    }
}
