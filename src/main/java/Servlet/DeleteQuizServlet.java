package Servlet;

import dao.QuizDAO;
import entity.Users;
import entity.quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteQuizServlet", urlPatterns = {"/DeleteQuizServlet"})
public class DeleteQuizServlet extends HttpServlet {

    private QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy user hiện tại từ session
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy quiz ID từ request
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteError");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteError");
            return;
        }

        try {
            // Lấy quiz từ cơ sở dữ liệu
            quiz quizObj = quizDAO.getQuizById(quizId);

            if (quizObj == null) {
                // Quiz không tồn tại
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteError");
                return;
            }

            // Kiểm tra xem người dùng có quyền xóa quiz không
            boolean isAuthorized = false;

            if (currentUser.getId() == quizObj.getUser_id() || currentUser.hasRole("admin")) {
                isAuthorized = true;
            }

            if (!isAuthorized) {
                // Người dùng không có quyền xóa quiz này
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteUnauthorized");
                return;
            }

            // Thực hiện xóa quiz
            boolean isDeleted = quizDAO.deleteQuiz(quizId);

            if (isDeleted) {
                // Xóa thành công
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteSuccess");
            } else {
                // Xóa thất bại
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteError");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=deleteError");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for deleting quizzes";
    }
}
