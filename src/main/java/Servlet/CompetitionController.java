package controller;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.QuizDAO;
import entity.Competition;
import entity.Users;
import entity.classs;
import entity.quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * CompetitionController Servlet
 *
 * Chịu trách nhiệm xử lý các yêu cầu liên quan đến Cuộc thi trong hệ thống.
 */
@WebServlet(name = "CompetitionController", urlPatterns = {"/CompetitionController"})
public class CompetitionController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CompetitionDAO competitionDAO;
    private ClassDAO classDAO;
    private QuizDAO quizDAO;

    // Định dạng thời gian tương thích với input datetime-local trong HTML5
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
        classDAO = new ClassDAO();
        quizDAO = new QuizDAO();
    }

    /**
     * Xử lý các yêu cầu GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra xác thực và phân quyền người dùng
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            if (currentUser == null || !(currentUser.hasRole("teacher") || currentUser.hasRole("admin"))) {
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                return;
            }

            // Xác định hành động
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Hành động mặc định
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

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Xử lý các yêu cầu POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra xác thực và phân quyền người dùng
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            if (currentUser == null || !(currentUser.hasRole("teacher") || currentUser.hasRole("admin"))) {
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                return;
            }

            // Xác định hành động
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Hành động mặc định
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

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Hiển thị danh sách các cuộc thi của giáo viên
     */
    private void listCompetitions(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        List<Competition> competitions = competitionDAO.getCompetitionsByTeacher(currentUser.getId());
        request.setAttribute("competitions", competitions);
        request.getRequestDispatcher("/jsp/competition-list.jsp").forward(request, response);
    }

    /**
     * Hiển thị form tạo mới cuộc thi
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
        List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
        request.setAttribute("classes", classes);
        request.setAttribute("quizzes", quizzes);
        request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa cuộc thi
     */
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
        request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị chi tiết một cuộc thi cụ thể
     */
    private void viewCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
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

        request.setAttribute("competition", competition);
        request.getRequestDispatcher("/jsp/competition-details.jsp").forward(request, response);
    }

    /**
     * Xóa một cuộc thi
     */
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

    /**
     * Xử lý tạo mới một cuộc thi
     */
    private void createCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Đọc dữ liệu từ form
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String classIdStr = request.getParameter("classId");
        String quizIdStr = request.getParameter("quizId");
        String timeLimitStr = request.getParameter("timeLimit"); // Trong phút
        String questionCountStr = request.getParameter("questionCount");
        String shuffleStr = request.getParameter("shuffle");
        String accessStartTimeStr = request.getParameter("accessStartTime"); // ISO_LOCAL_DATE_TIME
        String accessEndTimeStr = request.getParameter("accessEndTime"); // ISO_LOCAL_DATE_TIME

        // Xác thực dữ liệu
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
                timeLimit = Integer.parseInt(timeLimitStr) * 60; // Chuyển đổi phút thành giây
                questionCount = Integer.parseInt(questionCountStr);
                shuffleQuestions = "on".equalsIgnoreCase(shuffleStr) || "true".equalsIgnoreCase(shuffleStr);
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number format in one of the fields.";
            }
        }

        // Xử lý thời gian truy cập
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                accessStartTime = Date.from(LocalDateTime.parse(accessStartTimeStr, formatter).atZone(java.time.ZoneId.systemDefault()).toInstant());
                accessEndTime = Date.from(LocalDateTime.parse(accessEndTimeStr, formatter).atZone(java.time.ZoneId.systemDefault()).toInstant());
                if (accessEndTime.before(accessStartTime)) {
                    errorMessage = "Access end time must be after start time.";
                }
            } catch (Exception e) {
                errorMessage = "Invalid date/time format.";
            }
        }

        if (errorMessage != null) {
            // Nếu có lỗi, tái hiện lại form với thông báo lỗi
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng Competition
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

        // Lưu vào cơ sở dữ liệu
        boolean success = competitionDAO.createCompetition(competition);
        if (success) {
            response.sendRedirect("CompetitionController?action=list&message=competitionCreated");
        } else {
            // Nếu tạo thất bại, tái hiện lại form với thông báo lỗi
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", "Failed to create competition.");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý cập nhật một cuộc thi
     */
    private void updateCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Đọc dữ liệu từ form
        String competitionIdStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String classIdStr = request.getParameter("classId");
        String quizIdStr = request.getParameter("quizId");
        String timeLimitStr = request.getParameter("timeLimit"); // Trong phút
        String questionCountStr = request.getParameter("questionCount");
        String shuffleStr = request.getParameter("shuffle");
        String accessStartTimeStr = request.getParameter("accessStartTime"); // ISO_LOCAL_DATE_TIME
        String accessEndTimeStr = request.getParameter("accessEndTime"); // ISO_LOCAL_DATE_TIME

        // Xác thực dữ liệu
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
                timeLimit = Integer.parseInt(timeLimitStr) * 60; // Chuyển đổi phút thành giây
                questionCount = Integer.parseInt(questionCountStr);
                shuffleQuestions = "on".equalsIgnoreCase(shuffleStr) || "true".equalsIgnoreCase(shuffleStr);
            } catch (NumberFormatException e) {
                errorMessage = "Invalid number format in one of the fields.";
            }
        }

        // Xử lý thời gian truy cập
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                accessStartTime = Date.from(LocalDateTime.parse(accessStartTimeStr, formatter).atZone(java.time.ZoneId.systemDefault()).toInstant());
                accessEndTime = Date.from(LocalDateTime.parse(accessEndTimeStr, formatter).atZone(java.time.ZoneId.systemDefault()).toInstant());
                if (accessEndTime.before(accessStartTime)) {
                    errorMessage = "Access end time must be after start time.";
                }
            } catch (Exception e) {
                errorMessage = "Invalid date/time format.";
            }
        }

        if (errorMessage != null) {
            // Nếu có lỗi, tái hiện lại form với thông báo lỗi
            Competition competition = competitionDAO.getCompetitionById(competitionId);
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("competition", competition);
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng Competition
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

        // Cập nhật vào cơ sở dữ liệu
        boolean success = competitionDAO.updateCompetition(competition);
        if (success) {
            response.sendRedirect("CompetitionController?action=list&message=competitionUpdated");
        } else {
            // Nếu cập nhật thất bại, tái hiện lại form với thông báo lỗi
            Competition existingCompetition = competitionDAO.getCompetitionById(competitionId);
            List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
            List<quiz> quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            request.setAttribute("competition", existingCompetition);
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", "Failed to update competition.");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
        }
    }
    public void takeCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
//        Nhận data ừ
}
