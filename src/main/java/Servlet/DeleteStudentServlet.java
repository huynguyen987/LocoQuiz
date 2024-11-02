package Servlet;

import dao.ClassUserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/DeleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy classId và userId từ yêu cầu
        int classId = Integer.parseInt(request.getParameter("classId"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        ClassUserDAO classUserDAO = new ClassUserDAO();

        String message;
        try {
            // Xóa sinh viên khỏi lớp học
            if (classUserDAO.removeStudentFromClass(classId, userId)) {
                message = "removeSuccess";
            } else {
                message = "removeError";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "removeError";
        }

        // Chuyển hướng về trang chi tiết lớp học với thông báo
        response.sendRedirect(request.getContextPath() + "/ClassDetailsServlet?classsId=" + classId + "&message=" + message);
    }
}
