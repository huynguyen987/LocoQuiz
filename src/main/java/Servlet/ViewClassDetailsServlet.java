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
import java.sql.SQLException;

@WebServlet(name = "ViewClassDetailsServlet", value = "/ViewClassDetailsServlet")
public class ViewClassDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/myClass.jsp?error=" + URLEncoder.encode("Không tìm thấy lớp học.", "UTF-8"));
            return;
        }

        int classId;
        try {
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + URLEncoder.encode("Lớp học không hợp lệ.", "UTF-8"));
            return;
        }

        ClassDAO classDAO = new ClassDAO();
        ClassUserDAO classUserDAO = new ClassUserDAO();
        classs classEntity = null;

        try {
            // Lấy thông tin chi tiết lớp học
            classEntity = classDAO.getClassDetailsById(classId);

            if (classEntity == null) {
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + URLEncoder.encode("Lớp học không tồn tại.", "UTF-8"));
                return;
            }

            // Kiểm tra xem sinh viên có tham gia lớp học không
            boolean isEnrolled = classUserDAO.isUserEnrolledInClass(classId, currentUser.getId());

            if (!isEnrolled) {
                response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + URLEncoder.encode("Bạn không có quyền truy cập lớp học này.", "UTF-8"));
                return;
            }

            // Đặt thuộc tính và chuyển tiếp đến trang chi tiết lớp học
            request.setAttribute("classEntity", classEntity);
            request.getRequestDispatcher("/jsp/class-details.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?error=" + URLEncoder.encode("Có lỗi xảy ra trong quá trình xử lý.", "UTF-8"));
        }
    }
}