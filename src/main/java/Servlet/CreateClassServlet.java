package Servlet;

import dao.ClassDAO;
import entity.classs;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CreateClassServlet", urlPatterns = {"/CreateClassServlet"})
public class CreateClassServlet extends HttpServlet {

    // tạo ngẫu nhiên một class key
    private String generateClassKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
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

        // Kiểm tra nếu không phải là giáo viên thì chuyển hướng về trang unauthorized.jsp
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole_id() != Users.ROLE_TEACHER) {
            response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
            return;
        }

        // Kiểm tra xem tên lớp học có hợp lệ không
        if (name == null || name.trim().isEmpty()) {
            // Redirect back to Create Class form with an error message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=createClass&message=createError");
            return;
        }

        // Tạo một đối tượng classs mới và gán thông tin từ request
        classs newClass = new classs();
        newClass.setName(name.trim());
        newClass.setDescription(description != null ? description.trim() : "");
        newClass.setTeacher_id(currentUser.getId());
        newClass.setClass_key(generateClassKey());

        // Tạo một đối tượng ClassDAO để thao tác với database
        ClassDAO classDAO = new ClassDAO();

        try {
            // Thêm lớp học mới vào database
            classDAO.createClass(newClass);

            // Redirect to Teacher page with a success message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=classCreated");
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect back to Create Class form with an error message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=createClass&message=createError");
        }
    }
}
