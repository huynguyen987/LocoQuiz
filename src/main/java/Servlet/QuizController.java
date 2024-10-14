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
        request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
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
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "User is not logged in. Please log in to create a quiz.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }



        try {
            // Retrieve all selected tag IDs
            String[] quizTagIds = request.getParameterValues("quizTags");
            if (quizTagIds == null || quizTagIds.length == 0) {
                // No tags selected, set an error message
                request.setAttribute("errorMessage", "Please select at least one tag.");
                request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
                return;
            }

            // Retrieve quiz type and question count
            String quizType = request.getParameter("quizType");
            int questionCount = Integer.parseInt(request.getParameter("questionCount"));

            // Collect all questions and build the JSON array
            JSONArray questionsArray = new JSONArray();

            // Loop through the questions dynamically
            for (int i = 1; i <= questionCount; i++) {
                String questionContent = request.getParameter("questionContent" + i);
                JSONObject questionObj = new JSONObject();

                if (quizType.equals("multiple-choice")) {
                    // Handle multiple choice questions
                    String[] answers = new String[4];
                    for (int j = 1; j <= 4; j++) {
                        answers[j - 1] = request.getParameter("answer" + i + "_" + j);
                    }
                    String correctAnswer = request.getParameter("correctAnswer" + i);

                    // Build the question JSON object
                    questionObj.put("question", questionContent);
                    questionObj.put("options", Arrays.asList(answers));
                    questionObj.put("correct", correctAnswer);
                } else if (quizType.equals("fill-in-the-blank")) {
                    // Handle fill-in-the-blank questions
                    String correctAnswer = request.getParameter("correctAnswer" + i);
                    questionObj.put("question", questionContent);
                    questionObj.put("correct", correctAnswer);
                } else if (quizType.equals("matching")) {
                    // Handle matching questions
                    JSONArray pairsArray = new JSONArray();
                    int pairCount = Integer.parseInt(request.getParameter("pairCount" + i));
                    for (int j = 1; j <= pairCount; j++) {
                        String columnA = request.getParameter("matchA" + i + "_" + j);
                        String columnB = request.getParameter("matchB" + i + "_" + j);
                        JSONObject pairObj = new JSONObject();
                        pairObj.put("columnA", columnA);
                        pairObj.put("columnB", columnB);
                        pairsArray.put(pairObj);
                    }
                    questionObj.put("question", questionContent);
                    questionObj.put("pairs", pairsArray);
                }

                // Add the question object to the array
                questionsArray.put(questionObj);
            }

            // Convert questions array to string
            String questionsJson = questionsArray.toString();

            // Create quiz object
            quiz newQuiz = new quiz();
            newQuiz.setName(quizName);
            newQuiz.setDescription(quizDescription);
            newQuiz.setUser_id(userId);
            newQuiz.setType_id(determineQuizTypeId(quizType));
            newQuiz.setAnswer(questionsJson);

            // Insert the quiz and retrieve the generated quiz ID
            QuizDAO quizDAO = new QuizDAO();
            int quizId = quizDAO.insertQuiz(newQuiz);

            if (quizId != -1) {
                // Insert into user_quiz table for each selected tag
                userQuizDAO userQuizDAO = new userQuizDAO();
                for (String tagIdStr : quizTagIds) {
                    int tagId = Integer.parseInt(tagIdStr);
                    // Insert into quiz_tag table
                    quizDAO.insertQuizTag(quizId, tagId);
                    // Insert into user_quiz table
                    userQuizDAO.insertUserQuiz(userId, quizId, tagId);

                }

                // Redirect to a success page
                request.setAttribute("successMessage", "Quiz created successfully!");
                request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
            } else {
                // Display error message if the quiz could not be created
                request.setAttribute("errorMessage", "Failed to create quiz.");
                request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
            }
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
        }
    }

    // Helper method to map quizType string to the corresponding type_id in the database
    private int determineQuizTypeId(String quizType) {
        switch (quizType) {
            case "multiple-choice":
                return 1; // Assuming '1' represents Multiple Choice
            case "fill-in-the-blank":
                return 2; // Assuming '2' represents Fill in the Blank
            case "matching":
                return 3; // Assuming '3' represents Matching
            default:
                return 1; // Default to Multiple Choice
        }
    }
}
