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
import java.util.List;

@WebServlet(name = "TakeMatchQuiz", value = "/TakeMatchQuiz")
public class TakeMatchQuiz extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int quizId;
        try {
            quizId = Integer.parseInt(request.getParameter("quizId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid quizId parameter.\"}");
            return;
        }

        QuizDAO quizDAO = new QuizDAO();
        try {
            // Fetch quiz data
            quiz quizData = quizDAO.getQuizById(quizId);

            if (quizData == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Quiz not found.\"}");
                return;
            }

            // Parse the 'answer' field into List<AnswersReader>
            List<AnswersReader> questions = quizDAO.getCorrectbyId(quizId);

            if (questions == null || questions.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"No questions found for the given quizId.\"}");
                return;
            }

            // Save correct answers into session for later use in grading
            request.getSession().setAttribute("correctAnswers", questions);

            // Create response object
            QuizResponse quizResponse = new QuizResponse();
            quizResponse.setQuizId(quizId);
            quizResponse.setTitle(quizData.getName());
//            quizResponse.setTimeLimit(quizData.getTimeLimit()); // Assuming timeLimit is stored in the quiz object
            quizResponse.setQuestions(questions);

            // Send JSON response
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonResponse = mapper.writeValueAsString(quizResponse);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to retrieve quiz data.\"}");
        }
    }

    protected void doGet
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "TakeMatchQuiz servlet";
    }

    // Inner class to represent the quiz data sent to the client
    public static class QuizResponse {
        private int quizId;
        private String title;
        private int timeLimit;
        private List<AnswersReader> questions;

        // Getters and Setters

        public int getQuizId() {
            return quizId;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public List<AnswersReader> getQuestions() {
            return questions;
        }

        public void setQuestions(List<AnswersReader> questions) {
            this.questions = questions;
        }
    }
}
