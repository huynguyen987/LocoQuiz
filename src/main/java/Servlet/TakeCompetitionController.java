package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.CompetitionResultDAO;
import dao.QuizDAO;
import dao.UsersDAO;
import entity.*;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "TakeCompetitionController", urlPatterns = {"/TakeCompetitionController"})
public class TakeCompetitionController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CompetitionDAO competitionDAO;
    private CompetitionResultDAO competitionResultDAO;
    private QuizDAO quizDAO;
    private ClassDAO classDAO;
    private UsersDAO usersDAO;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
        competitionResultDAO = new CompetitionResultDAO();
        quizDAO = new QuizDAO();
        classDAO = new ClassDAO();
        usersDAO = new UsersDAO();
        mapper = new ObjectMapper();
    }

    /**
     * Handles GET requests.
     * - If action=take, return competition data as JSON.
     * - Else, forward to competition-participation.jsp with competitionId.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Authentication and authorization
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            if (currentUser == null || !currentUser.hasRole("student")) {
                System.out.println("Unauthorized access attempt.");
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                return;
            }

            // Get action parameter
            String action = request.getParameter("action");
            System.out.println("Received GET request with action: " + action);

            if ("take".equalsIgnoreCase(action)) {
                // Handle AJAX request to fetch competition data
                handleTakeCompetition(request, response, currentUser, session);
            } else {
                // Forward to competition-participation.jsp with competitionId
                handleViewCompetition(request, response, currentUser, session);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }

    /**
     * Handles AJAX GET request to fetch competition data as JSON.
     */
    private void handleTakeCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser, HttpSession session)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        // Get competitionId from request
        String competitionIdStr = request.getParameter("competitionId");
        System.out.println("Handling 'take' action for competition ID: " + competitionIdStr);
        int competitionId = 0;
        if (competitionIdStr != null && !competitionIdStr.isEmpty()) {
            try {
                competitionId = Integer.parseInt(competitionIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid competition ID format: " + competitionIdStr);
                competitionId = 0;
            }
        }

        if (competitionId == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Invalid Competition ID.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Get competition information
        Competition competition = competitionDAO.getCompetitionById(competitionId);
        if (competition == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Competition not found.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Check access time
        Date accessStartTime = competition.getAccessStartTime();
        Date accessEndTime = competition.getAccessEndTime();

        if (accessStartTime == null || accessEndTime == null) {
            System.out.println("Competition date fields are not properly initialized for ID: " + competitionId);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Competition date fields are not properly initialized.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        Date now = new Date();
        if (now.before(accessStartTime) || now.after(accessEndTime)) {
            System.out.println("Competition ID: " + competitionId + " is not currently accessible. Current time: " + now);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Competition is not currently accessible.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Check if the student is enrolled in the class
        if (!classDAO.isStudentInClass(currentUser.getId(), competition.getClassId())) {
            System.out.println("User ID: " + currentUser.getId() + " is not enrolled in class ID: " + competition.getClassId() + " for competition ID: " + competitionId);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "You are not enrolled in the class for this competition.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Get questions from the associated quiz
        quiz quiz = quizDAO.getQuizById(competition.getQuizId());
        if (quiz == null) {
            System.out.println("Associated Quiz not found for competition ID: " + competitionId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Associated Quiz not found.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        List<Question> questions = quiz.getQuestions();
        System.out.println("Number of questions retrieved: " + questions.size());

        if (competition.isShuffleQuestions()) {
            Collections.shuffle(questions);
            System.out.println("Questions shuffled for competition ID: " + competitionId);
        }

        if (questions.size() > competition.getQuestionCount()) {
            questions = questions.subList(0, competition.getQuestionCount());
            System.out.println("Limited to " + competition.getQuestionCount() + " questions for competition ID: " + competitionId);
        }

        // Shuffle options for each question
        for (Question q : questions) {
            Collections.shuffle(q.getOptions());
        }

        // Prepare JSON response
        ObjectNode competitionData = mapper.createObjectNode();
        competitionData.put("competitionId", competitionId);
        competitionData.put("totalQuestions", questions.size());
        competitionData.put("timeLimit", competition.getTimeLimit()); // assuming getTimeLimit() returns in seconds

        // Add questions array
        competitionData.set("questions", mapper.valueToTree(questions));

        // Store competitionQuestions and competitionId in session
        session.setAttribute("competitionQuestions", questions);
        session.setAttribute("competitionId", competitionId);

        // Send JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(competitionData));
        System.out.println("Competition data sent for competition ID: " + competitionId);
    }

    /**
     * Forwards to competition-participation.jsp with competitionId.
     */
    private void handleViewCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser, HttpSession session)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        // Get competitionId from request
        String competitionIdStr = request.getParameter("competitionId");
        System.out.println("Handling 'view competition' action for competition ID: " + competitionIdStr);
        int competitionId = 0;
        if (competitionIdStr != null && !competitionIdStr.isEmpty()) {
            try {
                competitionId = Integer.parseInt(competitionIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid competition ID format: " + competitionIdStr);
                competitionId = 0;
            }
        }

        if (competitionId == 0) {
            System.out.println("Invalid Competition ID: " + competitionIdStr);
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?action=Classrooms");
            return;
        }

        // Set competitionId as request attribute
        request.setAttribute("competitionId", competitionId);
        // Forward to competition-participation.jsp
        request.getRequestDispatcher("/jsp/competition-participation.jsp").forward(request, response);
    }

    /**
     * Handles POST requests.
     * - If action=submit, process competition submission.
     * - Else, return error.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Authentication and authorization
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            if (currentUser == null || !currentUser.hasRole("student")) {
                System.out.println("Unauthorized POST request attempt.");
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                return;
            }

            // Get action parameter
            String action = request.getParameter("action");
            System.out.println("Received POST request with action: " + action);

            if ("submit".equalsIgnoreCase(action)) {
                // Handle AJAX POST request to submit competition answers
                handleSubmitCompetition(request, response, currentUser, session);
            } else {
                // Handle other POST actions if any
                System.out.println("Invalid action for POST request: " + action);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ObjectNode errorNode = mapper.createObjectNode();
                errorNode.put("error", "Invalid action for POST request.");
                response.getWriter().write(mapper.writeValueAsString(errorNode));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }

    /**
     * Handles AJAX POST request to submit competition answers.
     */
    private void handleSubmitCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser, HttpSession session)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        // Parse JSON request body
        CompetitionSubmission submission;
        try {
            submission = mapper.readValue(request.getReader(), CompetitionSubmission.class);
            System.out.println("Received submission: Competition ID: " + submission.getCompetitionId() + ", Time Taken: " + submission.getTimeTaken());
        } catch (IOException e) {
            System.out.println("Invalid JSON format in submission.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Invalid JSON format.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        int competitionId = submission.getCompetitionId();
        Map<Integer, String> userAnswers = submission.getUserAnswers();
        int timeTaken = submission.getTimeTaken();

        // Validate competitionId
        if (competitionId == 0) {
            System.out.println("Invalid Competition ID in submission: " + competitionId);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Invalid Competition ID.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Get competition information
        Competition competition = competitionDAO.getCompetitionById(competitionId);
        if (competition == null) {
            System.out.println("Competition not found for ID: " + competitionId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Competition not found.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Get questions from session
        List<Question> questions = (List<Question>) session.getAttribute("competitionQuestions");
        if (questions == null || questions.isEmpty()) {
            System.out.println("No competition questions found in session for competition ID: " + competitionId);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "No competition questions found in session.");
            response.getWriter().write(mapper.writeValueAsString(errorNode));
            return;
        }

        // Grade the competition
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            String correctAnswer = questions.get(i).getCorrect();
            String userAnswer = userAnswers.get(i);
            if (correctAnswer != null && correctAnswer.equalsIgnoreCase(userAnswer)) {
                correctCount++;
            }
        }

        float scorePercentage = ((float) correctCount / questions.size()) * 100;
        System.out.println("Competition ID: " + competitionId + ", User ID: " + currentUser.getId() + ", Score: " + scorePercentage);

        // Save the result to the database
        CompetitionResult competitionResult = new CompetitionResult();
        competitionResult.setCompetitionId(competitionId);
        competitionResult.setUserId(currentUser.getId());
        competitionResult.setClassId(competition.getClassId());
        competitionResult.setScore(scorePercentage);
        competitionResult.setTimeTaken(timeTaken);
        competitionResult.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        competitionResultDAO.insertCompetitionResult(competitionResult);
        System.out.println("Competition result saved for user ID: " + currentUser.getId());

        // Remove session attributes to prevent resubmission
        session.removeAttribute("competitionQuestions");
        session.removeAttribute("competitionId");

        // Prepare JSON response
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("score", scorePercentage);
        resultNode.put("correctCount", correctCount);
        resultNode.put("totalQuestions", questions.size());

        // Send JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(resultNode));
        System.out.println("Competition submission response sent for competition ID: " + competitionId);
    }

    /**
     * Inner class to map competition submission JSON.
     */
    public static class CompetitionSubmission {
        private int competitionId;
        private Map<Integer, String> userAnswers;
        private int timeTaken;

        public CompetitionSubmission() {
        }

        // Getters and Setters
        public int getCompetitionId() {
            return competitionId;
        }

        public void setCompetitionId(int competitionId) {
            this.competitionId = competitionId;
        }

        public Map<Integer, String> getUserAnswers() {
            return userAnswers;
        }

        public void setUserAnswers(Map<Integer, String> userAnswers) {
            this.userAnswers = userAnswers;
        }

        public int getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(int timeTaken) {
            this.timeTaken = timeTaken;
        }
    }
}
