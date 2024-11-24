package Servlet;

import dao.AnalyticsDAO;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/teacher/analytics")
public class AnalyticsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra đăng nhập và quyền truy cập
        HttpSession session = request.getSession();
        Users teacher = (Users) session.getAttribute("user");
        if (teacher == null || teacher.getRole_id() != Users.ROLE_TEACHER) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        AnalyticsDAO analyticsDAO = new AnalyticsDAO();

        int totalQuizzes = analyticsDAO.getTotalQuizzesByTeacher(teacher.getId());
        int totalAttempts = analyticsDAO.getTotalQuizAttemptsByTeacher(teacher.getId());
        double averageScore = analyticsDAO.getAverageScoreByTeacher(teacher.getId());
        double averageTimeSeconds = analyticsDAO.getAverageTimeByTeacher(teacher.getId());
        double averageTimeMinutes = averageTimeSeconds / 60.0;
        Map<String, Double> scoreTrends = analyticsDAO.getScoreTrendsByTeacher(teacher.getId());

        // Đặt các thuộc tính cho JSP
        request.setAttribute("totalQuizzes", totalQuizzes);
        request.setAttribute("totalAttempts", totalAttempts);
        request.setAttribute("averageScore", averageScore);
        request.setAttribute("averageTime", averageTimeMinutes);
        request.setAttribute("scoreTrends", scoreTrends);

        // Chuyển tiếp đến trang JSP
        request.getRequestDispatcher("/jsp/analytics.jsp").forward(request, response);

    }
}
