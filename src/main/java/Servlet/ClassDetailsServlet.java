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
        String classIdStr = request.getParameter("classsId");
        System.out.println("User who is accessing class details: " + currentUser.getUsername() +" and have role: "+ currentUser.getRole_id());
        System.out.println("Your class id: "+ classIdStr);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (currentUser.getRole_id() == Users.ROLE_STUDENT) {
            System.out.println("You are student");
            response.sendRedirect(request.getContextPath() + "/jsp/my-classes.jsp?message=" + URLEncoder.encode("Bạn không có quyền truy cập lớp học này.", StandardCharsets.UTF_8));
            return;
        }

        ClassDAO classDAO = new ClassDAO();
        classs classEntity = null;
        int classId = Integer.parseInt(classIdStr);

        try {
            // Lấy thông tin lớp học
            classEntity = classDAO.getClassById(classId);
            System.out.println("Class entity: "+ classEntity.getName());

            if (classEntity == null) {
                response.sendRedirect(request.getContextPath() + "/myClass.jsp?message=" + URLEncoder.encode("Lớp học không tồn tại.", StandardCharsets.UTF_8));
                return;
            }

            // Kiểm tra xem ban có phải là giáo viên của lớp học không, hoặc là sinh viên đã tham gia lớp học không
            if (currentUser.getRole_id() == Users.ROLE_TEACHER) {
                if (classEntity.getTeacher_id() != currentUser.getId()) {
                    response.sendRedirect(request.getContextPath() + "/myClass.jsp?message =" + URLEncoder.encode("Bạn không có quyền truy cập lớp học này.", StandardCharsets.UTF_8));
                    return;
                }
            } else {
                ClassUserDAO classUserDAO = new ClassUserDAO();
                boolean isStudentEnrolled = classUserDAO.isStudentEnrolled(classId, currentUser.getId());
                if (!isStudentEnrolled) {
                    response.sendRedirect(request.getContextPath() + "/myClass.jsp?message =" + URLEncoder.encode("Bạn không có quyền truy cập lớp học này.", StandardCharsets.UTF_8));
                    return;
            }
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
