package Servlet;

import dao.QuizDAO;
import entity.Users;
import entity.quiz;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {

    private QuizDAO quizDAO;

    @Override
    public void init() {
        quizDAO = new QuizDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Kiểm tra quyền truy cập
        Users currentUser = (Users) request.getSession().getAttribute("user");
        if (currentUser == null || currentUser.getRole_id() != Users.ROLE_ADMIN) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?error=" + java.net.URLEncoder.encode("Bạn không có quyền truy cập trang này.", "UTF-8"));
            return; // Dừng việc thực thi tiếp
        }

        try {
            switch (action) {
                case "addQuiz":
                    addQuiz(request, response);
                    break;
                case "editQuiz":
                    editQuiz(request, response);
                    break;
                case "toggleVisibility":
                    toggleVisibility(request, response);
                    break;
                case "deleteQuiz":
                    deleteQuiz(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Hành động không hợp lệ.", "UTF-8"));
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi xảy ra trước khi response chưa bị committed, bạn có thể chuyển hướng với thông báo lỗi
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Đã xảy ra lỗi khi xử lý yêu cầu.", "UTF-8"));
            }
            return;
        }

        // Chuyển hướng trở lại trang quản lý quiz với thông báo thành công
        if (!response.isCommitted()) {
            response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?message=" + java.net.URLEncoder.encode("Thao tác thành công.", "UTF-8"));
        }
    }

    private void addQuiz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Lấy dữ liệu từ form
        String quizTitle = request.getParameter("quizTitle");
        String description = request.getParameter("description");
        String creatorIdParam = request.getParameter("creatorId");
        String typeIdParam = request.getParameter("typeId"); // Thêm trường type_id
        String answer = request.getParameter("answer"); // Thêm trường answer
        String statusParam = request.getParameter("status");

        // Kiểm tra dữ liệu bắt buộc
        if (quizTitle == null || description == null || creatorIdParam == null || typeIdParam == null || answer == null || statusParam == null ||
                quizTitle.isEmpty() || description.isEmpty() || creatorIdParam.isEmpty() || typeIdParam.isEmpty() || answer.isEmpty() || statusParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/add_quiz.jsp?error=" + java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin.", "UTF-8"));
            return;
        }

        int creatorId;
        int typeId;
        boolean status;

        try {
            creatorId = Integer.parseInt(creatorIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/add_quiz.jsp?error=" + java.net.URLEncoder.encode("ID creator không hợp lệ.", "UTF-8"));
            return;
        }

        try {
            typeId = Integer.parseInt(typeIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/add_quiz.jsp?error=" + java.net.URLEncoder.encode("ID loại quiz không hợp lệ.", "UTF-8"));
            return;
        }

        // Chuyển đổi trạng thái từ String sang boolean
        status = statusParam.equalsIgnoreCase("Visible") || statusParam.equalsIgnoreCase("true");

        // Tạo đối tượng Quiz
        quiz newQuiz = new quiz(); // Đảm bảo tên lớp là 'quiz' hoặc 'Quiz' tùy theo định nghĩa
        newQuiz.setName(quizTitle);
        newQuiz.setDescription(description);
        newQuiz.setUser_id(creatorId);
        newQuiz.setType_id(typeId);
        newQuiz.setAnswer(answer);
        newQuiz.setStatus(status);
        newQuiz.setViews(0); // Khởi tạo views bằng 0

        // Thêm quiz vào DB
        quizDAO.addQuiz(newQuiz);

        // Không cần gọi sendRedirect() tại đây vì sẽ được gọi trong doPost
    }

    private void editQuiz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String quizIdParam = request.getParameter("quizId");
        int quizId = -1;

        if (quizIdParam != null && !quizIdParam.isEmpty()) {
            try {
                quizId = Integer.parseInt(quizIdParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("ID quiz không hợp lệ.", "UTF-8"));
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Thiếu ID quiz.", "UTF-8"));
            return;
        }

        // Lấy dữ liệu từ form
        String quizTitle = request.getParameter("quizTitle");
        String description = request.getParameter("description");
        String creatorIdParam = request.getParameter("creatorId");
        String typeIdParam = request.getParameter("typeId"); // Thêm trường type_id
        String answer = request.getParameter("answer"); // Thêm trường answer
        String statusParam = request.getParameter("status");

        // Kiểm tra dữ liệu bắt buộc
        if (quizTitle == null || description == null || creatorIdParam == null || typeIdParam == null || answer == null || statusParam == null ||
                quizTitle.isEmpty() || description.isEmpty() || creatorIdParam.isEmpty() || typeIdParam.isEmpty() || answer.isEmpty() || statusParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/edit_quiz.jsp?quizId=" + quizId + "&error=" + java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin.", "UTF-8"));
            return;
        }

        int creatorId;
        int typeId;
        boolean status;

        try {
            creatorId = Integer.parseInt(creatorIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/edit_quiz.jsp?quizId=" + quizId + "&error=" + java.net.URLEncoder.encode("ID creator không hợp lệ.", "UTF-8"));
            return;
        }

        try {
            typeId = Integer.parseInt(typeIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/edit_quiz.jsp?quizId=" + quizId + "&error=" + java.net.URLEncoder.encode("ID loại quiz không hợp lệ.", "UTF-8"));
            return;
        }

        // Chuyển đổi trạng thái từ String sang boolean
        status = statusParam.equalsIgnoreCase("Visible") || statusParam.equalsIgnoreCase("true");

        // Lấy quiz hiện tại từ DB
        quiz existingQuiz = quizDAO.getQuizById(quizId);
        if (existingQuiz == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Quiz không tồn tại.", "UTF-8"));
            return;
        }

        // Cập nhật thông tin quiz
        existingQuiz.setName(quizTitle);
        existingQuiz.setDescription(description);
        existingQuiz.setUser_id(creatorId);
        existingQuiz.setType_id(typeId);
        existingQuiz.setAnswer(answer);
        existingQuiz.setStatus(status);
        // Không cần cập nhật views vì nó liên quan đến lượt xem

        // Cập nhật quiz trong DB
        quizDAO.updateQuiz(existingQuiz);

        // Không cần gọi sendRedirect() tại đây vì sẽ được gọi trong doPost
    }

    private void toggleVisibility(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String quizIdParam = request.getParameter("quizId");
        int quizId = -1;

        if (quizIdParam != null && !quizIdParam.isEmpty()) {
            try {
                quizId = Integer.parseInt(quizIdParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("ID quiz không hợp lệ.", "UTF-8"));
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Thiếu ID quiz.", "UTF-8"));
            return;
        }

        // Lấy quiz từ DB
        quiz existingQuiz = quizDAO.getQuizById(quizId);
        if (existingQuiz != null) {
            boolean currentStatus = existingQuiz.isStatus();
            // Đảo ngược trạng thái
            boolean newStatus = !currentStatus;
            quizDAO.setQuizStatus(quizId, newStatus);
        } else {
            throw new SQLException("Quiz không tồn tại.");
        }

        // Không cần gọi sendRedirect() tại đây vì sẽ được gọi trong doPost
    }

    private void deleteQuiz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String quizIdParam = request.getParameter("quizId");
        int quizId = -1;

        if (quizIdParam != null && !quizIdParam.isEmpty()) {
            try {
                quizId = Integer.parseInt(quizIdParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("ID quiz không hợp lệ.", "UTF-8"));
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/manage_quizzes.jsp?error=" + java.net.URLEncoder.encode("Thiếu ID quiz.", "UTF-8"));
            return;
        }

        // Xóa quiz từ DB
        quizDAO.deleteQuiz(quizId);

        // Không cần gọi sendRedirect() tại đây vì sẽ được gọi trong doPost
    }
}
