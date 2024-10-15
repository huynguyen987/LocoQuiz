package Servlet;

import dao.QuizDAO;
import entity.quiz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AllQuizzesServlet")
public class AllQuizzesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuizDAO quizDAO = new QuizDAO();
        try {
            // Fetch Latest Quizzes
            List<quiz> latestQuizzes = quizDAO.getLatestQuizzes();
            request.setAttribute("latestQuizzes", latestQuizzes);

            // Fetch Popular Quizzes
            List<quiz> popularQuizzes = quizDAO.getPopularQuizzes();
            request.setAttribute("popularQuizzes", popularQuizzes);

            // Fetch All Quizzes with Pagination
            int page = 1;
            int recordsPerPage = 9; // Adjusted for better grid display
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            int offset = (page - 1) * recordsPerPage;
            List<quiz> allQuizzes = quizDAO.getAllQuizzes(offset, recordsPerPage);
            int totalQuizzes = quizDAO.getTotalQuizCount();
            int totalPages = (int) Math.ceil(totalQuizzes * 1.0 / recordsPerPage);
            request.setAttribute("allQuizzes", allQuizzes);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // Forward to JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/all-quizzes.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
