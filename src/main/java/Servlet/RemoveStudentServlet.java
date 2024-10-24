package Servlet;

import dao.ClassUserDAO;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/RemoveStudentServlet")
public class RemoveStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int classId = Integer.parseInt(request.getParameter("classId"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        ClassUserDAO classUserDAO = new ClassUserDAO();

        String message = null;
        try {
            if (classUserDAO.removeStudentFromClass(classId, userId)) {
                message = "studentRemoved";
            } else {
                message = "removeError";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "removeError";
        }

        response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId + "&message=" + message);
    }
}
