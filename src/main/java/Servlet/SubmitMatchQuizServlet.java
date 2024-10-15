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

@WebServlet(name = "SubmitMatchQuizServlet", value = "/SubmitMatchQuizServlet")
public class SubmitMatchQuizServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only allow POST method
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().write("{\"error\":\"Only POST method is allowed.\"}");
            return;
        }

        // Read JSON data from request body
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

        // Parse JSON into QuizSubmission object
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

        // Retrieve correctAnswers from session
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

        // Build a map from sequence to correct answer
        Map<String, String> correctAnswerMap = new HashMap<>();
        for (AnswersReader ar : correctAnswers) {
            correctAnswerMap.put(String.valueOf(ar.getSequence()), ar.getCorrect());
        }

        // Calculate score
        int score = 0;
        int total = correctAnswerMap.size();

        for (String sequence : correctAnswerMap.keySet()) {
            String correctAnswer = correctAnswerMap.get(sequence);
            String userAnswer = userAnswers.get(sequence);

            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
                System.out.println("Correct: " + correctAnswer + " User: " + userAnswer);
            } else {
                System.out.println("Correct: " + correctAnswer + " User: " + userAnswer);
            }
        }

        // Create result object
        Map<String, Object> result = Map.of(
                "score", score,
                "total", total
        );

        // Convert object to JSON and send response
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

    // Inner class representing submission data
    public static class QuizSubmission {
        private int quizId;
        private Map<String, String> userAnswers;

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
    }
}
