// File: src/main/java/Servlet/TakeQuizServlet.java
package Servlet;

import Module.AnswersReader;
import dao.QuizDAO;
import entity.quiz;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TakeQuizServlet", value = "/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if (service == null || service.isEmpty()) {
            // Forward to quiz.jsp
            String id = request.getParameter("id");
            request.setAttribute("quizId", id);
            request.getRequestDispatcher("jsp/quiz.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(true);

        if ("loadQuiz".equals(service)) {
            String quizIdStr = request.getParameter("id");
            if (quizIdStr == null || quizIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing quizId parameter.\"}");
                return;
            }

            int quizId;
            try {
                quizId = Integer.parseInt(quizIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid quizId parameter.\"}");
                return;
            }

            // Đọc tham số 'time' từ yêu cầu để cấu hình thời gian làm bài (tính bằng giây)
            String timeStr = request.getParameter("time");
            int timeLimit;
            if (timeStr != null && !timeStr.isEmpty()) {
                try {
                    timeLimit = Integer.parseInt(timeStr);
                    // Giới hạn thời gian làm bài tối đa (ví dụ: 60 phút)
                    if (timeLimit > 3600) {
                        timeLimit = 3600; // Giới hạn tối đa là 60 phút
                    }
                } catch (NumberFormatException e) {
                    timeLimit = 15 * 60; // Mặc định 15 phút nếu tham số 'time' không hợp lệ
                }
            } else {
                timeLimit = 15 * 60; // Mặc định 15 phút nếu không có tham số 'time'
            }

            // Truy xuất dữ liệu quiz từ cơ sở dữ liệu sử dụng QuizDAO
            QuizDAO quizDAO = new QuizDAO();
            quiz quizData = null;
            try {
                quizData = quizDAO.getQuizById(quizId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error.\"}");
                return;
            }

            if (quizData == null) {
                // Không tìm thấy quiz
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Quiz not found.\"}");
                return;
            }

            // Giả sử quizData.getAnswer() chứa chuỗi JSON của các câu hỏi với các lựa chọn và đáp án đúng
            ObjectMapper mapper = new ObjectMapper();
            List<AnswersReader> allQuestions = null;
            try {
                String answerJson = quizData.getAnswer();
                allQuestions = mapper.readValue(answerJson, mapper.getTypeFactory().constructCollectionType(List.class, AnswersReader.class));
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Error parsing quiz data.\"}");
                return;
            }

            if (allQuestions == null || allQuestions.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("[]");
                return;
            }

            // Lưu trữ đáp án đúng vào session
            session.setAttribute("correctAnswers", allQuestions);

            // Chuẩn bị dữ liệu gửi tới client (không bao gồm đáp án đúng)
            List<AnswersReader> questionsForClient = new ArrayList<>();
            for (AnswersReader q : allQuestions) {
                // Tạo một bản sao của câu hỏi mà không bao gồm trường 'correct' (do @JsonIgnore)
                AnswersReader clientQuestion = new AnswersReader();
                clientQuestion.setSequence(q.getSequence());
                clientQuestion.setQuestion(q.getQuestion());
                clientQuestion.setOptions(q.getOptions());
                // 'correct' được loại bỏ nhờ @JsonIgnore
                questionsForClient.add(clientQuestion);
            }

            // Sắp xếp các câu hỏi theo thứ tự sequence nếu cần thiết
            questionsForClient.sort((q1, q2) -> Integer.compare(q1.getSequence(), q2.getSequence()));

            // Tạo đối tượng QuizResponse
            QuizResponse quizResponse = new QuizResponse();
            quizResponse.setTimeLimit(timeLimit); // Thiết lập thời gian làm bài từ tham số hoặc mặc định
            quizResponse.setTotalQuestions(questionsForClient.size());
            quizResponse.setQuestions(questionsForClient);

            // Convert to JSON and send response
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonQuiz = mapper.writeValueAsString(quizResponse);
            response.getWriter().write(jsonQuiz);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Nếu cần xử lý POST thì có thể thêm doPost
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Lớp DTO để gửi dữ liệu tới client
    public static class QuizResponse {
        private int timeLimit; // thời gian làm bài tính bằng giây
        private int totalQuestions;
        private List<AnswersReader> questions;

        // Getters and Setters
        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public List<AnswersReader> getQuestions() {
            return questions;
        }

        public void setQuestions(List<AnswersReader> questions) {
            this.questions = questions;
        }
    }
}
