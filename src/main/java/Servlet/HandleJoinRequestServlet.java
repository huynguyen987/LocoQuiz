package Servlet;

import dao.ClassUserDAO;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/HandleJoinRequestServlet")
public class HandleJoinRequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int classId = Integer.parseInt(request.getParameter("classId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String action = request.getParameter("action");

        ClassUserDAO classUserDAO = new ClassUserDAO();

        String message = null;
        try {
            if ("approve".equals(action)) {
                if (classUserDAO.acceptJoinRequest(classId, userId)) {
                    message = "joinRequestApproved";
                } else {
                    message = "approveError";
                }
            } else if ("reject".equals(action)) {
                if (classUserDAO.rejectJoinRequest(classId, userId)) {
                    message = "joinRequestRejected";
                } else {
                    message = "rejectError";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if ("approve".equals(action)) {
                message = "approveError";
            } else if ("reject".equals(action)) {
                message = "rejectError";
            }
        }

        response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId + "&message=" + message);
    }
}
