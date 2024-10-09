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
        // Kiểm tra quyền hạn
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != 1 && currentUser.getRole_id() != 3)) {
            response.sendRedirect("login.jsp");
            return;
        }

        int classId = Integer.parseInt(request.getParameter("classId"));

        ClassDAO classDAO = new ClassDAO();
        try {
            classs classEntity = classDAO.getClassById(classId);
            if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
                boolean isDeleted = classDAO.deleteClass(classId);
                if (isDeleted) {
                    response.sendRedirect("ClassListServlet");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa lớp.");
                    request.getRequestDispatcher("classDetails.jsp").forward(request, response);
                }
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("classDetails.jsp").forward(request, response);
        }
    }
}
