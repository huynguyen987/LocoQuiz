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

@WebServlet(name = "ClassDetailsServlet", value = "/ClassDetailsServlet")
public class ClassDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Kiểm tra phiên đăng nhập
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole_id() != Users.ROLE_STUDENT) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/myClass.jsp?message=" + URLEncoder.encode("Không tìm thấy lớp học.", StandardCharsets.UTF_8));
            return;
        }

        int classId = Integer.parseInt(classIdStr);

        ClassDAO classDAO = new ClassDAO();
        classs classEntity = null;

        try {
            // Lấy thông tin lớp học
            classEntity = classDAO.getClassById(classId);

            if (classEntity == null) {
                response.sendRedirect(request.getContextPath() + "/myClass.jsp?message=" + URLEncoder.encode("Lớp học không tồn tại.", StandardCharsets.UTF_8));
                return;
            }

            // Kiểm tra xem sinh viên có tham gia lớp này không
            ClassUserDAO classUserDAO = new ClassUserDAO();
            if (!classUserDAO.isUserEnrolledInClass(classId, currentUser.getId())) {
                response.sendRedirect(request.getContextPath() + "/myClass.jsp?message=" + URLEncoder.encode("Bạn không có quyền truy cập lớp học này.", "UTF-8"));
                return;
            }

            // Đặt thuộc tính và chuyển tiếp đến trang chi tiết lớp học
            request.setAttribute("classEntity", classEntity);
            request.getRequestDispatcher("/jsp/class-details.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/myClass.jsp?message=" + URLEncoder.encode("Có lỗi xảy ra.", StandardCharsets.UTF_8));
        }
    }
}
