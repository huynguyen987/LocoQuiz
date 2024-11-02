package Servlet;

import dao.ClassDAO;
import entity.classs;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet(name = "CreateClassServlet", urlPatterns = {"/CreateClassServlet"})
public class CreateClassServlet extends HttpServlet {

    // Tạo ngẫu nhiên một class key
    private String generateClassKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin lớp học từ request
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Lấy thông tin người dùng hiện tại từ session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");
        System.out.println("User who is creating class: " + currentUser.getUsername() + " and have role: " + currentUser.getRole_id());
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
//        neu la sinh vien thi chuyen ve trang chu
        if (currentUser.getRole_id() == Users.ROLE_STUDENT) {
            System.out.println("You are student");
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp");
            return;
        }

        // Kiểm tra xem tên lớp học có hợp lệ không
        if (name == null || name.trim().isEmpty()) {
            // Set thông báo lỗi và forward lại form
            request.setAttribute("errorMessage", "Tên lớp học không được để trống.");
            request.getRequestDispatcher("/jsp/createClass.jsp").forward(request, response);
            return;
        }

        // Tạo một đối tượng classs mới và gán thông tin từ request
        classs newClass = new classs();
        newClass.setName(name.trim());
        newClass.setDescription(description != null ? description.trim() : "");
        newClass.setTeacher_id(currentUser.getId());
        newClass.setClass_key(generateClassKey());
        newClass.setCreated_at(new Timestamp(System.currentTimeMillis())); // Thiết lập createdAt

        // Tạo một đối tượng ClassDAO để thao tác với database
        ClassDAO classDAO = new ClassDAO();

        try {
            // Thêm lớp học mới vào database
            classDAO.createClass(newClass);

            // Redirect đến TeacherDashboardServlet với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/teacherDashboard?action=update&message=classCreated");
        } catch (Exception e) {
            e.printStackTrace();
            // Set thông báo lỗi và forward lại form
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tạo lớp học.");
            request.getRequestDispatcher("/jsp/createClass.jsp").forward(request, response);
        }
    }
}
