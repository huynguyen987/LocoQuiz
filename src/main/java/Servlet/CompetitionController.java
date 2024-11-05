package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.*;
import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "CompetitionController", urlPatterns = {"/CompetitionController"})
public class CompetitionController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CompetitionDAO competitionDAO;
    private ClassDAO classDAO;
    private QuizDAO quizDAO;
    private CompetitionResultDAO competitionResultDAO;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
        classDAO = new ClassDAO();
        quizDAO = new QuizDAO();
        competitionResultDAO = new CompetitionResultDAO();
        mapper = new ObjectMapper();
    }

    /**
     * Handles GET requests.
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

            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Default action
            }

            if ("take".equalsIgnoreCase(action)) {
                // Students and teachers can take the competition
                if (currentUser == null || !(currentUser.hasRole("student") || currentUser.hasRole("teacher"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    sendJsonError(response, "Unauthorized access.");
                    return;
                }
//                takeCompetition(request, response, currentUser);
//                go to /TakeCompetitionController to take the competition
                String competitionIdStr = request.getParameter("competitionId");
//                send to /TakeCompetitionController
                if (competitionIdStr != null && !competitionIdStr.isEmpty()) {
                    int competitionId = Integer.parseInt(competitionIdStr);
                    Competition competition = competitionDAO.getCompetitionById(competitionId);
                    if (competition == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        sendJsonError(response, "Competition not found.");
                        return;
                    }
                    request.setAttribute("competitionId", competitionId);
                }

                response.sendRedirect(request.getContextPath() + "/TakeCompetitionController");
            } else {
                // Only teachers and admins can manage competitions
                if (currentUser == null || !(currentUser.hasRole("teacher") || currentUser.hasRole("admin"))) {
                    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                    return;
                }

                switch (action) {
                    case "list":
                        listCompetitions(request, response, currentUser);
                        break;
                    case "create":
                        showCreateForm(request, response, currentUser);
                        break;
                    case "edit":
                        showEditForm(request, response, currentUser);
                        break;
                    case "view":
                        viewCompetition(request, response, currentUser);
                        break;
                    case "delete":
                        deleteCompetition(request, response, currentUser);
                        break;
                    default:
                        listCompetitions(request, response, currentUser);
                        break;
                }
            }

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Handles POST requests.
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

            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Default action
            }

            if ("take".equalsIgnoreCase(action)) {
                // Students and teachers can take the competition
                if (currentUser == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    sendJsonError(response, "Unauthorized access.");
                    return;
                }
//                takeCompetition(request, response, currentUser);
//                go to /TakeCompetitionController to take the competition


            } else {
                // Only teachers and admins can manage competitions
                if (currentUser == null || !(currentUser.hasRole("teacher") || currentUser.hasRole("admin"))) {
                    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                    return;
                }

                switch (action) {
                    case "create":
                        createCompetition(request, response, currentUser);
                        break;
                    case "edit":
                        updateCompetition(request, response, currentUser);
                        break;
                    case "delete":
                        deleteCompetition(request, response, currentUser);
                        break;
                    default:
                        listCompetitions(request, response, currentUser);
                        break;
                }
            }

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Sends a JSON error response.
     */
    private void sendJsonError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        mapper.writeValue(response.getWriter(), errorResponse);
    }

    // The takeCompetition method is handled in TakeCompetitionController, so we can remove it from here.

    // Other methods: listCompetitions, showCreateForm, showEditForm, viewCompetition, deleteCompetition, createCompetition, updateCompetition

    private void listCompetitions(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        List<Competition> competitions = competitionDAO.getCompetitionsByTeacher(currentUser.getId());
        request.setAttribute("competitions", competitions);
        request.getRequestDispatcher("/jsp/competition-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
        List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
        request.setAttribute("classes", classes);
        request.setAttribute("quizzes", quizzes);
        request.setAttribute("pageTitle", "Create New Competition");
        request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String competitionIdStr = request.getParameter("id");
        if (competitionIdStr == null || competitionIdStr.isEmpty()) {
            response.sendRedirect("CompetitionController?action=list");
            return;
        }

        int competitionId = Integer.parseInt(competitionIdStr);
        Competition competition = competitionDAO.getCompetitionById(competitionId);
        if (competition == null) {
            response.sendRedirect("CompetitionController?action=list");
            return;
        }

        List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
        List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
        request.setAttribute("competition", competition);
        request.setAttribute("classes", classes);
        request.setAttribute("quizzes", quizzes);
        request.setAttribute("pageTitle", "Edit Competition");
        request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
    }

    private void viewCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String competitionIdStr = request.getParameter("id");
        System.out.println("Competition ID: " + competitionIdStr);
        if (competitionIdStr == null || competitionIdStr.isEmpty()) {
            response.sendRedirect("CompetitionController?action=list");
            return;
        }

        int competitionId = Integer.parseInt(competitionIdStr);
        Competition competition = competitionDAO.getCompetitionById(competitionId);
        if (competition == null) {
            System.out.println("Competition not found.");
            response.sendRedirect("CompetitionController?action=list");
            return;
        }

        CompetitionResultDAO competitionResultDAO = new CompetitionResultDAO();
        ClassDAO classDAO = new ClassDAO();

        if (currentUser.hasRole("teacher")) {
            System.out.println("Viewing competition as teacher.");
            List<CompetitionResult> competitionResultAll = competitionResultDAO.getCompetitionResultsByCompetitionId(competitionId);

            if (competitionResultAll.isEmpty()) {
                System.out.println("No results found.");
                request.setAttribute("competition", competition);
                request.setAttribute("competitionResults", Collections.emptyList());
                request.getRequestDispatcher("/jsp/CompetitionResultTeacher.jsp").forward(request, response);
            } else {
                List<CompetitionResult> competitionResults = new ArrayList<>();
                Set<Integer> userIds = new HashSet<>();
                for (CompetitionResult result : competitionResultAll) {
                    if (result != null) { // Additional null check
                        classs classs = classDAO.getClassById(result.getClassId());
                        if (classs != null) {
                            result.setClassId(classs.getId());
                            competitionResults.add(result);
                            userIds.add(result.getUserId());
                        }
                    }
                }

                // Retrieve students based on competition ID and class ID
                List<Users> students = competitionResultDAO.getStudentsByCompetitionIdAndClassId(competitionId, competition.getClassId());

                // Create a map of userId to Users for easy lookup in JSP
                Map<Integer, Users> usersMap = new HashMap<>();
                for (Users user : students) {
                    usersMap.put(user.getId(), user);
                }

                request.setAttribute("competition", competition);
                request.setAttribute("competitionResults", competitionResults);
                request.setAttribute("usersMap", usersMap);
                request.getRequestDispatcher("/jsp/CompetitionResultTeacher.jsp").forward(request, response);
            }
        } else if (currentUser.hasRole("student")) {
            System.out.println("Viewing competition as student.");
            int teacherId = classDAO.getTeacherIdByClassId(competition.getClassId());
            int studentId = currentUser.getId();
            List<CompetitionResult> competitionResults = competitionResultDAO.getCompetitionResultsByCompetitionIdAndUserId(competition.getId(), studentId);

            // Retrieve student object
            UsersDAO usersDAO = new UsersDAO();
            Users student = usersDAO.getUserById(studentId); // Ensure this method exists in UsersDAO

            request.setAttribute("student", student);
            request.setAttribute("competition", competition);
            request.setAttribute("teacherId", teacherId);
            request.setAttribute("competitionResults", competitionResults);
            request.getRequestDispatcher("/jsp/CompetitionResult.jsp").forward(request, response);
        } else {
            // Unauthorized user, redirect to competition list
            System.out.println("Do not have permission to view competition.");
            response.sendRedirect("CompetitionController?action=list");
        }
    }


    private void deleteCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String competitionIdStr = request.getParameter("id");
        if (competitionIdStr == null || competitionIdStr.isEmpty()) {
            response.sendRedirect("CompetitionController?action=list");
            return;
        }

        int competitionId = Integer.parseInt(competitionIdStr);
        boolean success = competitionDAO.deleteCompetition(competitionId);
        if (success) {
            response.sendRedirect("CompetitionController?action=list&message=competitionDeleted");
        } else {
            request.setAttribute("errorMessage", "Failed to delete competition.");
            listCompetitions(request, response, currentUser);
        }
    }

    private void createCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Read data from form
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String classIdStr = request.getParameter("classId");
        String quizIdStr = request.getParameter("quizId");
        String timeLimitStr = request.getParameter("timeLimit"); // in minutes
        String questionCountStr = request.getParameter("questionCount");
        String shuffleStr = request.getParameter("shuffle");
        String accessStartTimeStr = request.getParameter("accessStartTime"); // ISO_LOCAL_DATE_TIME
        String accessEndTimeStr = request.getParameter("accessEndTime"); // ISO_LOCAL_DATE_TIME

        // Validate data
        String errorMessage = null;

        if (name == null || name.trim().isEmpty() ||
                classIdStr == null || classIdStr.trim().isEmpty() ||
                quizIdStr == null || quizIdStr.trim().isEmpty() ||
                timeLimitStr == null || timeLimitStr.trim().isEmpty() ||
                questionCountStr == null || questionCountStr.trim().isEmpty() ||
                accessStartTimeStr == null || accessStartTimeStr.trim().isEmpty() ||
                accessEndTimeStr == null || accessEndTimeStr.trim().isEmpty()) {
            errorMessage = "Please fill in all required fields.";
        }

        int classId = 0, quizId = 0, timeLimit = 0, questionCount = 0;
        boolean shuffleQuestions = false;

        if (errorMessage == null) {
            try {
                classId = Integer.parseInt(classIdStr);
                quizId = Integer.parseInt(quizIdStr);
                timeLimit = Integer.parseInt(timeLimitStr) * 60; // Convert minutes to seconds
                questionCount = Integer.parseInt(questionCountStr);
                shuffleQuestions = "on".equalsIgnoreCase(shuffleStr) || "true".equalsIgnoreCase(shuffleStr);
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number format in one of the fields.";
            }
        }

        // Handle access times
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                accessStartTime = Timestamp.valueOf(accessStartTimeStr);
                accessEndTime = Timestamp.valueOf(accessEndTimeStr);
                if (accessEndTime.before(accessStartTime)) {
                    errorMessage = "Access end time must be after start time.";
                }
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid date/time format.";
            }
        }

        if (errorMessage != null) {
            // If there is an error, re-display the form with error message
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("pageTitle", "Create New Competition");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
            return;
        }

        // Create Competition object
        Competition competition = new Competition();
        competition.setName(name);
        competition.setDescription(description);
        competition.setClassId(classId);
        competition.setQuizId(quizId);
        competition.setTimeLimit(timeLimit);
        competition.setQuestionCount(questionCount);
        competition.setShuffleQuestions(shuffleQuestions);
        competition.setAccessStartTime(accessStartTime);
        competition.setAccessEndTime(accessEndTime);
        competition.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Save to database
        boolean success = competitionDAO.createCompetition(competition);
        if (success) {
            response.sendRedirect("CompetitionController?action=list&message=competitionCreated");
        } else {
            // If creation failed, re-display the form with error message
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", "Failed to create competition.");
            request.setAttribute("pageTitle", "Create New Competition");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
        }
    }

    private void updateCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Read data from form
        String competitionIdStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String classIdStr = request.getParameter("classId");
        String quizIdStr = request.getParameter("quizId");
        String timeLimitStr = request.getParameter("timeLimit"); // in minutes
        String questionCountStr = request.getParameter("questionCount");
        String shuffleStr = request.getParameter("shuffle");
        String accessStartTimeStr = request.getParameter("accessStartTime"); // ISO_LOCAL_DATE_TIME
        String accessEndTimeStr = request.getParameter("accessEndTime"); // ISO_LOCAL_DATE_TIME

        // Validate data
        String errorMessage = null;
        if (competitionIdStr == null || competitionIdStr.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                classIdStr == null || classIdStr.trim().isEmpty() ||
                quizIdStr == null || quizIdStr.trim().isEmpty() ||
                timeLimitStr == null || timeLimitStr.trim().isEmpty() ||
                questionCountStr == null || questionCountStr.trim().isEmpty() ||
                accessStartTimeStr == null || accessStartTimeStr.trim().isEmpty() ||
                accessEndTimeStr == null || accessEndTimeStr.trim().isEmpty()) {
            errorMessage = "Please fill in all required fields.";
        }

        int competitionId = 0, classId = 0, quizId = 0, timeLimit = 0, questionCount = 0;
        boolean shuffleQuestions = false;

        if (errorMessage == null) {
            try {
                competitionId = Integer.parseInt(competitionIdStr);
                classId = Integer.parseInt(classIdStr);
                quizId = Integer.parseInt(quizIdStr);
                timeLimit = Integer.parseInt(timeLimitStr) * 60; // Convert minutes to seconds
                questionCount = Integer.parseInt(questionCountStr);
                shuffleQuestions = "on".equalsIgnoreCase(shuffleStr) || "true".equalsIgnoreCase(shuffleStr);
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number format in one of the fields.";
            }
        }

        // Handle access times
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                accessStartTime = Timestamp.valueOf(accessStartTimeStr);
                accessEndTime = Timestamp.valueOf(accessEndTimeStr);
                if (accessEndTime.before(accessStartTime)) {
                    errorMessage = "Access end time must be after start time.";
                }
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid date/time format.";
            }
        }

        if (errorMessage != null) {
            // If there is an error, re-display the form with error message
            Competition competition = competitionDAO.getCompetitionById(competitionId);
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("competition", competition);
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("pageTitle", "Edit Competition");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
            return;
        }

        // Create Competition object
        Competition competition = new Competition();
        competition.setId(competitionId);
        competition.setName(name);
        competition.setDescription(description);
        competition.setClassId(classId);
        competition.setQuizId(quizId);
        competition.setTimeLimit(timeLimit);
        competition.setQuestionCount(questionCount);
        competition.setShuffleQuestions(shuffleQuestions);
        competition.setAccessStartTime(accessStartTime);
        competition.setAccessEndTime(accessEndTime);

        // Update in database
        boolean success = competitionDAO.updateCompetition(competition);
        if (success) {
            response.sendRedirect("CompetitionController?action=list&message=competitionUpdated");
        } else {
            // If update failed, re-display the form with error message
            Competition existingCompetition = competitionDAO.getCompetitionById(competitionId);
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("competition", existingCompetition);
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", "Failed to update competition.");
            request.setAttribute("pageTitle", "Edit Competition");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
        }
    }

    /**
     * ErrorResponse class for JSON error responses.
     */
    static class ErrorResponse {
        private String error;

        public ErrorResponse() {}

        public ErrorResponse(String error) {
            this.error = error;
        }

        // Getter and Setter
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
