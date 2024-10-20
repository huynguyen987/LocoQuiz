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
        // Kiểm tra phiên đăng nhập
        HttpSession session = request.getSession(false);
        Users currentUser = null;
        if (session != null) {
            currentUser = (Users) session.getAttribute("user");
        }
        if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
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
            // Tìm lớp học dựa trên classKey
            classs classEntity = classDAO.getClassByClassKey(classKey);

            if (classEntity == null) {
                String error = URLEncoder.encode("Không tìm thấy lớp học với mã đã nhập.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + error);
                return;
            }

            int classId = classEntity.getId();
            int studentId = currentUser.getId();

            // Kiểm tra xem sinh viên đã tham gia lớp này chưa
            if (classUserDAO.isUserEnrolledInClass(classId, studentId)) {
                String message = URLEncoder.encode("Bạn đã tham gia lớp học này rồi.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?message=" + message);
                return;
            }

            // Đăng ký sinh viên vào lớp học
            boolean isEnrolled = classUserDAO.enrollStudentToClass(classId, studentId);

            if (isEnrolled) {
                String message = URLEncoder.encode("Tham gia lớp học thành công.", StandardCharsets.UTF_8);
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