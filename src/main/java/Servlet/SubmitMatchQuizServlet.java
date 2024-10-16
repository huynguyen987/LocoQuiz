// File: src/main/java/Servlet/SubmitMatchQuizServlet.java
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
        int timeTaken = submission.getTimeTaken();

        // Logging
        System.out.println("Quiz ID: " + quizId);
        System.out.println("User Answers: " + userAnswers);
        System.out.println("Time Taken: " + timeTaken + " seconds");

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

        // Calculate score and prepare detailed results
        int score = 0;
        int total = correctAnswers.size();

        List<QuestionResult> questionResults = new ArrayList<>();
        List<QuestionResult> incorrectAnswers = new ArrayList<>();

        for (AnswersReader question : correctAnswers) {
            String sequence = String.valueOf(question.getSequence());
            String correctAnswer = question.getCorrect();
            String userAnswer = userAnswers.get(sequence);

            boolean isCorrect = correctAnswer != null && correctAnswer.equals(userAnswer);
            if (isCorrect) {
                score++;
            } else {
                // Only add incorrect answers to the list
                QuestionResult incorrect = new QuestionResult();
                incorrect.setQuestionText(question.getQuestion());
                incorrect.setCorrectAnswer(correctAnswer);
                incorrect.setUserAnswer(userAnswer);
                incorrect.setCorrect(isCorrect);
                incorrectAnswers.add(incorrect);
            }

            // Prepare question result
            QuestionResult questionResult = new QuestionResult();
            questionResult.setQuestionText(question.getQuestion());
            questionResult.setCorrectAnswer(correctAnswer);
            questionResult.setUserAnswer(userAnswer);
            questionResult.setCorrect(isCorrect);

            questionResults.add(questionResult);
        }

        // Store detailed results in session
        HttpSession session = request.getSession();
        session.setAttribute("quizResult", questionResults);
        session.setAttribute("timeTaken", timeTaken); // Store timeTaken in session

        // Prepare result data with incorrect answers
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("score", score);
        resultData.put("total", total);
        resultData.put("incorrectAnswers", incorrectAnswers); // Add incorrect answers
        resultData.put("redirectUrl", request.getContextPath() + "/match-quiz-result.jsp");

        // Convert object to JSON and send response
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

    // Inner class representing submission data
    public static class QuizSubmission {
        private int quizId;
        private Map<String, String> userAnswers;
        private int timeTaken; // Add timeTaken field

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

    // Inner class representing question result
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
