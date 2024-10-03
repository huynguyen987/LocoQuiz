package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.userQuizDAO;
import entity.user_quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserQuizServlet", urlPatterns = {"/UserQuizServlet"})
public class UserQuizServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        userQuizDAO dao = new userQuizDAO();
        String action = request.getParameter("action");

        try {
            if (action == null) {
                // Default action: list all user_quiz
                List<user_quiz> userQuizzes = dao.getAllUserQuiz();
                request.setAttribute("userQuizzes", userQuizzes);
                request.getRequestDispatcher("/jsp/user_quiz.jsp").forward(request, response);
            } else {
                switch (action) {
                    case "insertUserQuiz":
                        handleInsertUserQuiz(request, response, dao);
                        break;
                    case "updateUserQuiz":
                        handleUpdateUserQuiz(request, response, dao);
                        break;
                    case "deleteUserQuiz":
                        handleDeleteUserQuiz(request, response, dao);
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/UserQuizServlet");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/error.jsp");
        }
    }

    // Handle Insert UserQuiz
    private void handleInsertUserQuiz(HttpServletRequest request, HttpServletResponse response, userQuizDAO dao)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        String submit = request.getParameter("submit");
        if (submit == null) {
            // Hiển thị trang thêm user_quiz
            request.getRequestDispatcher("/jsp/insert-user_quiz.jsp").forward(request, response);
        } else {
            // Xử lý thêm user_quiz
            try {
                int user_id = Integer.parseInt(request.getParameter("user_id"));
                int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
                int tag_id = Integer.parseInt(request.getParameter("tag_id"));

                // Kiểm tra xem bản ghi đã tồn tại chưa
                if (!dao.existsUserQuiz(user_id, quiz_id, tag_id)) {
                    boolean inserted = dao.insertUserQuiz(user_id, quiz_id, tag_id);
                    if (inserted) {
                        // Redirect to list after successful insert
                        response.sendRedirect(request.getContextPath() + "/UserQuizServlet");
                    } else {
                        request.setAttribute("error", "Thêm user_quiz thất bại.");
                        request.getRequestDispatcher("/jsp/insert-user_quiz.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("error", "Bản ghi đã tồn tại.");
                    request.getRequestDispatcher("/jsp/insert-user_quiz.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Dữ liệu nhập không hợp lệ.");
                request.getRequestDispatcher("/jsp/insert-user_quiz.jsp").forward(request, response);
            }
        }
    }

    // Handle Update UserQuiz
    private void handleUpdateUserQuiz(HttpServletRequest request, HttpServletResponse response, userQuizDAO dao)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        String submit = request.getParameter("submit");
        if (submit == null) {
            // Hiển thị trang cập nhật user_quiz
            try {
                int old_user_id = Integer.parseInt(request.getParameter("old_user_id"));
                int old_quiz_id = Integer.parseInt(request.getParameter("old_quiz_id"));
                int old_tag_id = Integer.parseInt(request.getParameter("old_tag_id"));

                user_quiz oldUserQuiz = dao.getUserQuizByUserIdAndQuizIdAndTagId(old_user_id, old_quiz_id, old_tag_id);
                if (oldUserQuiz != null) {
                    request.setAttribute("oldUserQuiz", oldUserQuiz);
                    request.getRequestDispatcher("/jsp/update-user_quiz.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy bản ghi để cập nhật.");
                    request.getRequestDispatcher("/UserQuizServlet").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Dữ liệu nhập không hợp lệ.");
                request.getRequestDispatcher("/UserQuizServlet").forward(request, response);
            }
        } else {
            // Xử lý cập nhật user_quiz
            try {
                int old_user_id = Integer.parseInt(request.getParameter("old_user_id"));
                int old_quiz_id = Integer.parseInt(request.getParameter("old_quiz_id"));
                int old_tag_id = Integer.parseInt(request.getParameter("old_tag_id"));

                int new_user_id = Integer.parseInt(request.getParameter("user_id"));
                int new_quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
                int new_tag_id = Integer.parseInt(request.getParameter("tag_id"));

                // Kiểm tra xem bản ghi mới đã tồn tại chưa để tránh trùng lặp
                if (!dao.existsUserQuiz(new_user_id, new_quiz_id, new_tag_id)) {
                    boolean updated = dao.updateUserQuiz(old_user_id, old_quiz_id, old_tag_id, new_user_id, new_quiz_id, new_tag_id);
                    if (updated) {
                        response.sendRedirect(request.getContextPath() + "/UserQuizServlet");
                    } else {
                        request.setAttribute("error", "Cập nhật user_quiz thất bại.");
                        request.getRequestDispatcher("/jsp/update-user_quiz.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("error", "Bản ghi mới đã tồn tại.");
                    request.getRequestDispatcher("/jsp/update-user_quiz.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Dữ liệu nhập không hợp lệ.");
                request.getRequestDispatcher("/jsp/update-user_quiz.jsp").forward(request, response);
            }
        }
    }

    // Handle Delete UserQuiz
    private void handleDeleteUserQuiz(HttpServletRequest request, HttpServletResponse response, userQuizDAO dao)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        try {
            int user_id = Integer.parseInt(request.getParameter("user_id"));
            int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
            int tag_id = Integer.parseInt(request.getParameter("tag_id"));

            boolean deleted = dao.deleteUserQuiz(user_id, quiz_id, tag_id);
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/UserQuizServlet");
            } else {
                request.setAttribute("error", "Xóa user_quiz thất bại.");
                request.getRequestDispatcher("/UserQuizServlet").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ.");
            request.getRequestDispatcher("/UserQuizServlet").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
