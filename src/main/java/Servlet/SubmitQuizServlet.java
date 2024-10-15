// File: src/main/java/Servlet/SubmitQuizServlet.java
package Servlet;

import Module.AnswersReader;
import dao.QuizDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

        // Logging
        System.out.println("Quiz ID: " + quizId);
        System.out.println("User Answers: " + userAnswers);

        if (userAnswers == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing userAnswers.\"}");
            return;
        }

        // Sử dụng QuizDAO để lấy correctAnswers từ cơ sở dữ liệu
        QuizDAO quizDAO = new QuizDAO();
        List<AnswersReader> correctAnswers;
        try {
//            correctAnswers = quizDAO.getCorrectbyId(quizId);
//            System.out.println("Fetched correctAnswers from database: " + correctAnswers);
//            get correct answers from session
            correctAnswers = (List<AnswersReader>) request.getSession().getAttribute("correctAnswers");
//            System.out.println("Fetched correctAnswers from session: " + correctAnswers);
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

        // Tính điểm
        int score = 0;
        int total = correctAnswers.size();
//        sort by sequence
        correctAnswers.sort(Comparator.comparing(AnswersReader::getSequence));

        for (int i = 0; i < total; i++) {
            String userAnswer = userAnswers.get(String.valueOf(i));
            String correctAnswer = correctAnswers.get(i).getCorrect();
            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
                System.out.println("Correct: " + correctAnswer + " User: " + userAnswer);
            } else {
                System.out.println("Correct: " + correctAnswer + " User: " + userAnswer);
            }
        }

        // Tạo đối tượng kết quả
        Map<String, Object> result = Map.of(
                "score", score,
                "total", total
        );

        // Chuyển đổi đối tượng thành JSON và gửi phản hồi
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonResult = mapper.writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResult);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Nếu cần xử lý GET thì có thể thêm doGet, nhưng ở đây chỉ xử lý POST
}
