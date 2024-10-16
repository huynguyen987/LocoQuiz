package Servlet;

import Module.AnswersReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SubmitQuizServlet", value = "/SubmitQuizServlet")
public class SubmitQuizServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ cho phép phương thức POST
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().write("{\"error\":\"Only POST method is allowed.\"}");
            return;
        }

        // Đọc dữ liệu JSON từ request body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid request data.\"}");
            return;
        }

        String requestBody = sb.toString();
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON thành đối tượng QuizSubmission
        QuizSubmission submission;
        try {
            submission = mapper.readValue(requestBody, QuizSubmission.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Malformed JSON data.\"}");
            return;
        }

        int quizId = submission.getQuizId();
        Map<String, String> userAnswers = submission.getUserAnswers();
        int timeTaken = submission.getTimeTaken(); // Giả sử client gửi thời gian làm bài

        // Logging
        System.out.println("Quiz ID: " + quizId);
        System.out.println("User Answers: " + userAnswers);
        System.out.println("Time Taken: " + timeTaken + " seconds");

        if (userAnswers == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing userAnswers.\"}");
            return;
        }

        // Lấy correctAnswers từ session (được lưu trữ từ trước khi quiz bắt đầu)
        List<AnswersReader> correctAnswers;
        try {
            correctAnswers = (List<AnswersReader>) request.getSession().getAttribute("correctAnswers");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to retrieve correct answers.\"}");
            return;
        }

        if (correctAnswers == null || correctAnswers.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"No correct answers found for the given quizId.\"}");
            return;
        }

        // Tính điểm và chuẩn bị kết quả chi tiết
        int score = 0;
        int total = correctAnswers.size();

        List<QuestionResult> questionResults = new ArrayList<>();
        List<QuestionResult> incorrectAnswers = new ArrayList<>();

        for (int i = 0; i < correctAnswers.size(); i++) {
            AnswersReader question = correctAnswers.get(i);
            String userAnswer = userAnswers.get(String.valueOf(i));
            String correctAnswer = question.getCorrect();

            boolean isCorrect = correctAnswer != null && correctAnswer.equals(userAnswer);
            if (isCorrect) {
                score++;
            } else {
                // Thêm vào danh sách câu trả lời sai
                QuestionResult incorrect = new QuestionResult();
                incorrect.setQuestionText(question.getQuestion());
                incorrect.setCorrectAnswer(correctAnswer);
                incorrect.setUserAnswer(userAnswer);
                incorrect.setCorrect(isCorrect);
                incorrectAnswers.add(incorrect);
            }

            // Thêm vào danh sách kết quả tổng thể
            QuestionResult questionResult = new QuestionResult();
            questionResult.setQuestionText(question.getQuestion());
            questionResult.setCorrectAnswer(correctAnswer);
            questionResult.setUserAnswer(userAnswer);
            questionResult.setCorrect(isCorrect);

            questionResults.add(questionResult);
        }

        // Lưu kết quả vào session
        HttpSession session = request.getSession();
        session.setAttribute("quizResult", questionResults);
        session.setAttribute("timeTaken", timeTaken);
        session.setAttribute("incorrectAnswers", incorrectAnswers);

        // Chuẩn bị dữ liệu phản hồi
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("score", score);
        resultData.put("total", total);
        resultData.put("redirectUrl", request.getContextPath() + "/jsp/quiz-result.jsp");

        // Chuyển đổi đối tượng thành JSON và gửi phản hồi
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonResult = mapper.writeValueAsString(resultData);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResult);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Inner class để đại diện cho dữ liệu submission
    public static class QuizSubmission {
        private int quizId;
        private Map<String, String> userAnswers;
        private int timeTaken; // Thêm trường timeTaken

        // Getters and Setters
        public int getQuizId() {
            return quizId;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public Map<String, String> getUserAnswers() {
            return userAnswers;
        }

        public void setUserAnswers(Map<String, String> userAnswers) {
            this.userAnswers = userAnswers;
        }

        public int getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(int timeTaken) {
            this.timeTaken = timeTaken;
        }
    }

    // Inner class để đại diện cho kết quả từng câu hỏi
    public static class QuestionResult implements java.io.Serializable {
        private String questionText;
        private String correctAnswer;
        private String userAnswer;
        private boolean isCorrect;

        // Getters and Setters
        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean correct) {
            isCorrect = correct;
        }
    }
}
