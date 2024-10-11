package Servlet;

import dao.QuizDAO;
import dao.TagDAO;
import dao.userQuizDAO;
import entity.quiz;
import entity.Tag;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

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
            System.out.println("Number of tags retrieved: " + tagList.size());
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

        // Get the creator username and user ID from session
        HttpSession session = request.getSession();
        String creatorUsername = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");

        if (creatorUsername == null || userId == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect(request.getContextPath()+ "/jsp/login.jsp");
            return;
        }

        try {
            // Retrieve all selected tag IDs
            String[] quizTagIds = request.getParameterValues("quizTag");
            if (quizTagIds == null || quizTagIds.length == 0) {
                // No tags selected, set an error message
                request.setAttribute("errorMessage", "Please select at least one tag.");
                doGet(request, response);
                return;
            }

            // Collect all questions and build the JSON array
            JSONArray questionsArray = new JSONArray();

            // Retrieve the total number of questions
            int questionCount = Integer.parseInt(request.getParameter("questionCount"));

            for (int i = 1; i <= questionCount; i++) {
                String questionContent = request.getParameter("questionContent" + i);
                String answer1 = request.getParameter("answer" + i + "_1");
                String answer2 = request.getParameter("answer" + i + "_2");
                String answer3 = request.getParameter("answer" + i + "_3");
                String answer4 = request.getParameter("answer" + i + "_4");
                String correctAnswerStr = request.getParameter("correctAnswer" + i);

                if (questionContent == null || correctAnswerStr == null) {
                    continue; // Skip if question or correct answer is missing
                }

                int correctAnswer = Integer.parseInt(correctAnswerStr);

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

                // Build the question JSON object
                JSONObject questionObj = new JSONObject();
                questionObj.put("question", questionContent);
                questionObj.put("options", Arrays.asList(answer1, answer2, answer3, answer4));
                questionObj.put("correct", correctAnswerText);

                // Add to the questions array
                questionsArray.put(questionObj);
            }

            // Convert questions array to string
            String answersJson = questionsArray.toString();

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
                // Insert into user_quiz table for each selected tag
                userQuizDAO userQuizDAO = new userQuizDAO();

                for (String tagIdStr : quizTagIds) {
                    int tagId = Integer.parseInt(tagIdStr);
                    userQuizDAO.insertUserQuiz(userId, quizId, tagId);
                }

                // Redirect to a success page or quiz list
                response.sendRedirect(request.getContextPath()+"/jsp/quizList.jsp");
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
