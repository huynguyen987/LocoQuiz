package Servlet;

import dao.QuizDAO;
import dao.UserLibraryDAO;
import entity.Users;
import entity.quiz;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "QuizDetailsServlet", urlPatterns = {"/QuizDetailsServlet"})
public class QuizDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id"); // Sử dụng "id" thay vì "quizId"
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Invalid Quiz ID");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Invalid Quiz ID Format");
            return;
        }

        try {
            QuizDAO quizDAO = new QuizDAO();
            quiz q = quizDAO.getQuizById(quizId);

            if (q == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp?message=Quiz Not Found");
                return;
            }

            // Lấy người dùng từ session
            HttpSession session = request.getSession();
            Users currentUser = (Users) session.getAttribute("user");

            // Kiểm tra xem quiz có trong thư viện của người dùng hay không
            boolean isInLibrary = false;
            if (currentUser != null) {
                UserLibraryDAO userLibraryDAO = new UserLibraryDAO();
                isInLibrary = userLibraryDAO.isQuizInLibrary(currentUser.getId(), quizId);
            }

            // Đặt đối tượng quiz và isInLibrary vào request
            request.setAttribute("quiz", q);
            request.setAttribute("isInLibrary", isInLibrary);

            // Chuyển tiếp đến trang JSP
            request.getRequestDispatcher("/jsp/quiz-details.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Error Fetching Quiz Details");
        }
    }
}
