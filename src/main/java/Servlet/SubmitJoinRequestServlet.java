package Servlet;

import dao.ClassDAO;
import dao.ClassUserDAO;
import entity.classs;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SubmitJoinRequestServlet", value = "/SubmitJoinRequestServlet")
public class SubmitJoinRequestServlet extends HttpServlet {
    private ClassDAO classDAO = new ClassDAO();
    private ClassUserDAO classUserDAO = new ClassUserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" + URLEncoder.encode("Vui lòng đăng nhập trước khi tham gia lớp học.", StandardCharsets.UTF_8));
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" + URLEncoder.encode("Vui lòng đăng nhập với tư cách học sinh.", StandardCharsets.UTF_8));
            return;
        }

        String classKey = request.getParameter("classKey");
        if (classKey == null || classKey.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?error=" +
                    URLEncoder.encode("Mã lớp học không được để trống.", StandardCharsets.UTF_8));
            return;
        }

        try {
            // Lấy thông tin lớp học theo classKey
            classs classEntity = classDAO.getClassByClassKey(classKey);
            if (classEntity == null) {
                response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?error=" +
                        URLEncoder.encode("Không tìm thấy lớp học với mã đã nhập.", StandardCharsets.UTF_8));
                return;
            }

            int classId = classEntity.getId();

            // Kiểm tra xem học sinh đã tham gia hoặc đã gửi yêu cầu chưa
            if (classUserDAO.hasPendingOrApprovedRequest(classId, currentUser.getId())) {
                response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?error=" +
                        URLEncoder.encode("Bạn đã tham gia hoặc đã gửi yêu cầu tham gia lớp học này.", StandardCharsets.UTF_8));
                return;
            }

            // Gửi yêu cầu tham gia lớp học
            boolean success = classUserDAO.submitJoinRequest(classId, currentUser.getId());
            if (success) {
                response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?message=" +
                        URLEncoder.encode("Yêu cầu tham gia lớp học đã được gửi thành công.", StandardCharsets.UTF_8));
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?error=" +
                        URLEncoder.encode("Gửi yêu cầu tham gia lớp học không thành công. Có thể bạn đã gửi yêu cầu trước đó.", StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?error=" +
                    URLEncoder.encode("Đã xảy ra lỗi khi xử lý yêu cầu.", StandardCharsets.UTF_8));
        }
    }
}
