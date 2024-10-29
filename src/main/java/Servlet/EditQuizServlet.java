package Servlet;

import dao.QuizDAO;
import entity.Users;
import entity.quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "EditQuizServlet", urlPatterns = {"/EditQuizServlet"})
public class EditQuizServlet extends HttpServlet {

    private QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy user từ session
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (!currentUser.hasRole("teacher") && !currentUser.hasRole("admin"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy quizId từ request
        String stringQuizId = request.getParameter("quizId");
        if (stringQuizId == null || stringQuizId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(stringQuizId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        try {
            quiz quizObj = quizDAO.getQuizById(quizId);

            if (quizObj == null) {
                // Quiz không tồn tại
                request.setAttribute("errorMessage", "Quiz không tồn tại.");
                // Lấy tất cả các tag
                List<Map<String, Object>> allTags = quizDAO.getAllTags();
                request.setAttribute("allTags", allTags != null ? allTags : new ArrayList<>());
                request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
                return;
            }

            // Kiểm tra quyền sửa quiz
            if (quizObj.getUser_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                request.setAttribute("errorMessage", "Bạn không có quyền chỉnh sửa quiz này.");
                // Lấy tất cả các tag
                List<Map<String, Object>> allTags = quizDAO.getAllTags();
                request.setAttribute("allTags", allTags != null ? allTags : new ArrayList<>());
                request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
                return;
            }

            // Lấy tất cả các tag
            List<Map<String, Object>> allTags = quizDAO.getAllTags();
            request.setAttribute("allTags", allTags != null ? allTags : new ArrayList<>());

            // Đặt đối tượng quiz vào request
            request.setAttribute("quiz", quizObj);

            // Đặt danh sách câu hỏi vào request
            List<Map<String, Object>> questions = quizObj.getQuestions();
            request.setAttribute("questions", questions != null ? questions : new ArrayList<>());

            // Đặt typeId vào request
            int typeId = quizObj.getType_id();
            request.setAttribute("typeId", typeId);

            // Forward tới edit-quiz.jsp
            request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi lấy dữ liệu quiz.");
            // Lấy tất cả các tag
            try {
                List<Map<String, Object>> allTags = quizDAO.getAllTags();
                request.setAttribute("allTags", allTags != null ? allTags : new ArrayList<>());
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                request.setAttribute("allTags", new ArrayList<>());
            }
            request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP POST method to update the quiz.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy user từ session
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Kiểm tra quyền
        if (currentUser == null || (!currentUser.hasRole("teacher") && !currentUser.hasRole("admin"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy tham số từ form
        String stringQuizId = request.getParameter("quizId");
        String quizName = request.getParameter("quizName");
        String quizDescription = request.getParameter("quizDescription");
        String[] selectedTags = request.getParameterValues("quizTags");
        String quizTypeRadio = request.getParameter("quizTypeRadio"); // e.g., 1, 2, 3

        // Kiểm tra dữ liệu đầu vào
        if (stringQuizId == null || quizName == null || quizDescription == null ||
                stringQuizId.trim().isEmpty() || quizName.trim().isEmpty() || quizDescription.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc.");
            doGet(request, response);
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(stringQuizId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Quiz ID không hợp lệ.");
            doGet(request, response);
            return;
        }

        try {
            quiz quizObj = quizDAO.getQuizById(quizId);

            if (quizObj == null) {
                request.setAttribute("errorMessage", "Quiz không tồn tại.");
                doGet(request, response);
                return;
            }

            // Kiểm tra quyền sửa quiz
            if (quizObj.getUser_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                request.setAttribute("errorMessage", "Bạn không có quyền chỉnh sửa quiz này.");
                doGet(request, response);
                return;
            }

            // Lấy số lượng câu hỏi đã được cập nhật
            String questionCountStr = request.getParameter("questionCount");
            int questionCount = 0;
            if (questionCountStr != null && !questionCountStr.trim().isEmpty()) {
                try {
                    questionCount = Integer.parseInt(questionCountStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    questionCount = 0;
                }
            }

            List<Map<String, Object>> updatedQuestions = new ArrayList<>();

            for (int q = 1; q <= questionCount; q++) {
                String questionContent = request.getParameter("questionContent" + q);
                String questionType = request.getParameter("questionType" + q); // Trường ẩn

                if (questionContent == null || questionContent.trim().isEmpty()) {
                    continue; // Bỏ qua câu hỏi không đầy đủ
                }

                Map<String, Object> questionMap = new java.util.HashMap<>();
                questionMap.put("question", questionContent);

                if ("1".equals(questionType)) { // Multiple Choice
                    String correctAnswerIndexStr = request.getParameter("correctAnswer" + q);
                    int correctAnswerIndex = 0; // Mặc định

                    if (correctAnswerIndexStr != null && !correctAnswerIndexStr.trim().isEmpty()) {
                        try {
                            correctAnswerIndex = Integer.parseInt(correctAnswerIndexStr) - 1;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    // Lấy số lượng câu trả lời
                    String answerCountStr = request.getParameter("answerCount" + q);
                    int answerCount = 4; // Mặc định 4 câu trả lời
                    if (answerCountStr != null && !answerCountStr.trim().isEmpty()) {
                        try {
                            answerCount = Integer.parseInt(answerCountStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    List<String> options = new ArrayList<>();
                    for (int a = 1; a <= answerCount; a++) {
                        String option = request.getParameter("answer" + q + "_" + a);
                        if (option != null && !option.trim().isEmpty()) {
                            options.add(option);
                        }
                    }

                    // Đảm bảo correctAnswerIndex nằm trong phạm vi
                    if (correctAnswerIndex < 0 || correctAnswerIndex >= options.size()) {
                        correctAnswerIndex = 0; // Mặc định chọn câu trả lời đầu tiên
                    }

                    String correctAnswer = options.get(correctAnswerIndex);

                    questionMap.put("options", options);
                    questionMap.put("correct", correctAnswer);
                } else if ("2".equals(questionType)) { // Fill in the Blank
                    String correctAnswer = request.getParameter("correctAnswer" + q);
                    questionMap.put("correct", correctAnswer);
                } else if ("3".equals(questionType)) { // Matching
                    // Xử lý câu hỏi Matching nếu cần
                    // Ví dụ: questionMap.put("matchingPairs", matchingPairs);
                    // Hiện tại chưa triển khai
                }

                updatedQuestions.add(questionMap);
            }

            // Cập nhật thông tin quiz
            quizObj.setName(quizName);
            quizObj.setDescription(quizDescription);

            // Lấy typeId từ quizTypeRadio
            int typeId = 1; // Mặc định
            if (quizTypeRadio != null && !quizTypeRadio.trim().isEmpty()) {
                try {
                    typeId = Integer.parseInt(quizTypeRadio);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            quizObj.setType_id(typeId);

            // Thiết lập danh sách câu hỏi đã cập nhật
            quizObj.setQuestions(updatedQuestions);

            // Cập nhật quiz trong cơ sở dữ liệu
            boolean isUpdated = quizDAO.updateQuizForEditQuiz(quizObj, selectedTags);

            if (isUpdated) {
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editSuccess");
            } else {
                request.setAttribute("errorMessage", "Cập nhật quiz thất bại.");
                doGet(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật quiz.");
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for editing quizzes";
    }
}
