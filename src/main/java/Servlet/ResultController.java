package Servlet;

import Module.AnswersReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ResultController", value = "/ResultController")
public class ResultController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read JSON data from the request
        ObjectMapper mapper = new ObjectMapper();
        UserSubmission submission = mapper.readValue(request.getInputStream(), UserSubmission.class);

        // Retrieve correct answers from the session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Session expired. Please retake the quiz.");
            return;
        }

        List<AnswersReader> correctAnswers = (List<AnswersReader>) session.getAttribute("correctAnswers");
        if (correctAnswers == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No quiz data found in session.");
            return;
        }

        // Calculate the score
        int score = 0;
        List<String> userAnswers = submission.getAnswers();
        for (int i = 0; i < correctAnswers.size(); i++) {
            String correctAnswer = correctAnswers.get(i).getCorrect();
            String userAnswer = userAnswers.size() > i ? userAnswers.get(i) : null;

            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
            }
        }

        // Store user answers in session if needed
        session.setAttribute("userAnswers", userAnswers);

        // Prepare the result data
        ResultData resultData = new ResultData();
        resultData.setScore(score);
        resultData.setTotal(correctAnswers.size());

        // Send the result back to the client
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(resultData));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Inner classes for data binding
    public static class UserSubmission {
        private List<String> answers;
        private String quizId;

        public List<String> getAnswers() {
            return answers;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }
    }

    public static class ResultData {
        private int score;
        private int total;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
