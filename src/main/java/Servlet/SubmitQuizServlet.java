// File: src/main/java/Servlet/SubmitQuizServlet.java
package Servlet;

import Module.AnswersReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
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
            while ( (line = reader.readLine()) != null ) {
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

        // Parse JSON thành Map
        Map<String, Object> requestData;
        try {
            requestData = mapper.readValue(requestBody, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Malformed JSON data.\"}");
            return;
        }

        // Lấy quizId và userAnswers từ requestData
        String quizIdStr = (String) requestData.get("quizId");
        Map<String, String> userAnswers = (Map<String, String>) requestData.get("userAnswers");

        if (quizIdStr == null || quizIdStr.isEmpty() || userAnswers == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing quizId or userAnswers.\"}");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(quizIdStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid quizId.\"}");
            return;
        }

        // Lấy correctAnswers từ session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Session not found.\"}");
            return;
        }

        List<AnswersReader> correctAnswers = (List<AnswersReader>) session.getAttribute("correctAnswers");
        if (correctAnswers == null || correctAnswers.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"No correct answers found in session.\"}");
            return;
        }

        // Tính điểm
        int score = 0;
        int total = correctAnswers.size();
        for (int i = 0; i < total; i++) {
            String userAnswer = userAnswers.get(String.valueOf(i));
            String correctAnswer = correctAnswers.get(i).getCorrect();
            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
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
