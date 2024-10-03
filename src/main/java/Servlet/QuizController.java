// File: src/java/controller/QuizController.java
package Servlet;

import dao.QuizDAO;
import dao.TagDAO;
import dao.userQuizDAO;
import entity.quiz;
import entity.Tag;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "QuizController", urlPatterns = {"/QuizController"})
public class QuizController extends HttpServlet {

    // Handle GET request to display the quiz creation form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve all tags to populate the dropdown
        TagDAO tagDAO = new TagDAO();
        try {
            List<Tag> tagList = tagDAO.getAllTags();
            request.setAttribute("tagList", tagList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Forward to quiz-creator.jsp
        request.getRequestDispatcher("quiz-creator.jsp").forward(request, response);
    }

    // Handle POST request to process the form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set character encoding to handle Unicode characters
        request.setCharacterEncoding("UTF-8");

        // Retrieve form parameters
        String quizName = request.getParameter("quizName");
        String quizDescription = request.getParameter("quizDescription");
        String quizTagIdStr = request.getParameter("quizTag");
        String questionContent = request.getParameter("questionContent");
        String answer1 = request.getParameter("answer1");
        String answer2 = request.getParameter("answer2");
        String answer3 = request.getParameter("answer3");
        String answer4 = request.getParameter("answer4");
        String correctAnswerStr = request.getParameter("correctAnswer");

        // Get the creator username and user ID from session
        HttpSession session = request.getSession();
        String creatorUsername = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");

        if (creatorUsername == null || userId == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int correctAnswer = Integer.parseInt(correctAnswerStr);
            int quizTagId = Integer.parseInt(quizTagIdStr);

            // Build the answer JSON
            String correctAnswerText = "";
            switch (correctAnswer) {
                case 1:
                    correctAnswerText = answer1;
                    break;
                case 2:
                    correctAnswerText = answer2;
                    break;
                case 3:
                    correctAnswerText = answer3;
                    break;
                case 4:
                    correctAnswerText = answer4;
                    break;
            }

            StringBuilder answerJsonBuilder = new StringBuilder();
            answerJsonBuilder.append("{");
            answerJsonBuilder.append("\"question\":\"").append(questionContent).append("\",");
            answerJsonBuilder.append("\"options\":[\"").append(answer1).append("\",\"").append(answer2).append("\",\"").append(answer3).append("\",\"").append(answer4).append("\"],");
            answerJsonBuilder.append("\"correct\":\"").append(correctAnswerText).append("\"");
            answerJsonBuilder.append("}");
            String answersJson = answerJsonBuilder.toString();

            // Create quiz object
            quiz newQuiz = new quiz();
            newQuiz.setName(quizName);
            newQuiz.setDescription(quizDescription);
            newQuiz.setUser_id(userId);
            newQuiz.setType_id(1); // Assuming '1' represents Multiple Choice
            newQuiz.setAnswer(answersJson);

            // Insert the quiz and retrieve the generated quiz ID
            QuizDAO quizDAO = new QuizDAO();
            int quizId = quizDAO.insertQuiz(newQuiz);

            if (quizId != -1) {
                // Insert into user_quiz table
                userQuizDAO userQuizDAO = new userQuizDAO();
                userQuizDAO.insertUserQuiz(userId, quizId, quizTagId);
                // Redirect to a success page or quiz list
                response.sendRedirect("quizList.jsp");
            } else {
                // Display error message
                request.setAttribute("errorMessage", "Failed to create quiz.");
                doGet(request, response);
            }
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
