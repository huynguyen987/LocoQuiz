package Servlet;

import dao.ClassDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import entity.classs;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteClassServlet", value = "/DeleteClassServlet")
public class DeleteClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check user permissions
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int classId = Integer.parseInt(request.getParameter("classId"));

        ClassDAO classDAO = new ClassDAO();
        try {
            classs classEntity = classDAO.getClassById(classId);
            if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
                boolean isDeleted = classDAO.deleteClass(classId);
                if (isDeleted) {
                    // Redirect to teacher dashboard with a success message
                    response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=Class deleted successfully.");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa lớp.");
                    request.getRequestDispatcher("/jsp/teacher.jsp").forward(request, response);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}

