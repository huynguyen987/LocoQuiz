package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.CompetitionResultDAO;
import dao.QuizDAO;
import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "CompetitionController", urlPatterns = {"/CompetitionController"})
public class CompetitionController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CompetitionDAO competitionDAO;
    private ClassDAO classDAO;
    private QuizDAO quizDAO;
    private CompetitionResultDAO competitionResultDAO;

    // Định dạng thời gian tương thích với input datetime-local trong HTML5
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
        classDAO = new ClassDAO();
        quizDAO = new QuizDAO();
        competitionResultDAO = new CompetitionResultDAO();
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

            // Xác định hành động
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Hành động mặc định
            }

            if ("take".equalsIgnoreCase(action)) {
                // Phân quyền cho học sinh và giáo viên
                if (currentUser == null || !(currentUser.hasRole("student") || currentUser.hasRole("teacher"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"Unauthorized access.\"}");
                    return;
                }
                takeCompetition(request, response, currentUser);
            } else {
                // Phân quyền cho giáo viên và admin
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

            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Hành động mặc định
            }

            if ("take".equalsIgnoreCase(action)) {
                // Phân quyền cho học sinh và giáo viên
                if (currentUser == null || !(currentUser.hasRole("student") || currentUser.hasRole("teacher"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"Unauthorized access.\"}");
                    return;
                }
                takeCompetition(request, response, currentUser);
            } else {
                // Phân quyền cho giáo viên và admin
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
        request.setAttribute("pageTitle", "Tạo Mới Cuộc Thi");
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
        request.setAttribute("pageTitle", "Chỉnh Sửa Cuộc Thi");
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

        // Phân phối trang JSP dựa trên vai trò của người dùng
        if (currentUser.hasRole("teacher")) {
            request.getRequestDispatcher("/jsp/competition-details-teacher.jsp").forward(request, response);
        } else if (currentUser.hasRole("student")) {
            request.getRequestDispatcher("/jsp/competition-details-student.jsp").forward(request, response);
        } else {
            // Người dùng không có quyền, chuyển hướng về danh sách cuộc thi
            response.sendRedirect("CompetitionController?action=list");
        }
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

        System.out.println("===============Edit Competition============");
        System.out.println("Name: " + name + ".Data Type: "+name.getClass().getName());
        System.out.println("Description: " + description + ".Data Type: "+description.getClass().getName());
        System.out.println("Class ID: " + classIdStr + ".Data Type: "+classIdStr.getClass().getName());
        System.out.println("Quiz ID: " + quizIdStr + ".Data Type: "+quizIdStr.getClass().getName());
        System.out.println("Time Limit: " + timeLimitStr + ".Data Type: "+timeLimitStr.getClass().getName());
        System.out.println("Question Count: " + questionCountStr + ".Data Type: "+questionCountStr.getClass().getName());
        System.out.println("Shuffle: " + shuffleStr);
        System.out.println("Access Start Time: " + accessStartTimeStr+ ".Data Type: "+accessStartTimeStr.getClass().getName());
        System.out.println("Access End Time: " + accessEndTimeStr+ ".Data Type: "+accessEndTimeStr.getClass().getName());
        System.out.println("===========================================");

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
//                errorMessage = "Invalid number format in one of the fields.";
//                print error invalid number format from each feild
                if (classId == 0) {
                    errorMessage = "Invalid class ID.";
                } else if (quizId == 0) {
                    errorMessage = "Invalid quiz ID.";
                } else if (timeLimit == 0) {
                    errorMessage = "Invalid time limit.";
                } else if (questionCount == 0) {
                    errorMessage = "Invalid question count.";
                } else if (shuffleQuestions == false) {
                        errorMessage = "Invalid shuffle questions.";
                } else if (accessStartTimeStr == null || accessStartTimeStr.trim().isEmpty()) {
                    errorMessage = "Invalid access start time.";
                } else if (accessEndTimeStr == null || accessEndTimeStr.trim().isEmpty()) {
                    errorMessage = "Invalid access end time.";
                } else {
                    errorMessage = "Invalid number format in one of the fields.";
                }
        }
        }

        // Xử lý thời gian truy cập
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                LocalDateTime ldtStart = LocalDateTime.parse(accessStartTimeStr, formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(accessEndTimeStr, formatter);
                accessStartTime = Date.from(ldtStart.atZone(ZoneId.systemDefault()).toInstant());
                accessEndTime = Date.from(ldtEnd.atZone(ZoneId.systemDefault()).toInstant());
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
            request.setAttribute("pageTitle", "Tạo Mới Cuộc Thi");
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
        competition.setAccessStartTime((java.sql.Date) accessStartTime);
        competition.setAccessEndTime((java.sql.Date) accessEndTime);
        competition.setCreatedAt(new Timestamp(System.currentTimeMillis()));

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
            request.setAttribute("pageTitle", "Tạo Mới Cuộc Thi");
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

        System.out.println("===============Edit Competition============");
        System.out.println("Name: " + name + ".Data Type: "+name.getClass().getName());
        System.out.println("Description: " + description + ".Data Type: "+description.getClass().getName());
        System.out.println("Class ID: " + classIdStr + ".Data Type: "+classIdStr.getClass().getName());
        System.out.println("Quiz ID: " + quizIdStr + ".Data Type: "+quizIdStr.getClass().getName());
        System.out.println("Time Limit: " + timeLimitStr + ".Data Type: "+timeLimitStr.getClass().getName());
        System.out.println("Question Count: " + questionCountStr + ".Data Type: "+questionCountStr.getClass().getName());
        System.out.println("Shuffle: " + shuffleStr);
        System.out.println("Access Start Time: " + accessStartTimeStr+ ".Data Type: "+accessStartTimeStr.getClass().getName());
        System.out.println("Access End Time: " + accessEndTimeStr+ ".Data Type: "+accessEndTimeStr.getClass().getName());
        System.out.println("===========================================");

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
//                errorMessage = "Invalid number format in one of the fields.";
                if (classId == 0) {
                    errorMessage = "Invalid class ID.";
                } else if (quizId == 0) {
                    errorMessage = "Invalid quiz ID.";
                } else if (timeLimit == 0) {
                    errorMessage = "Invalid time limit.";
                } else if (questionCount == 0) {
                    errorMessage = "Invalid question count.";
                } else if (shuffleQuestions == false) {
                    errorMessage = "Invalid shuffle questions.";
                } else if (accessStartTimeStr == null || accessStartTimeStr.trim().isEmpty()) {
                    errorMessage = "Invalid access start time.";
                } else if (accessEndTimeStr == null || accessEndTimeStr.trim().isEmpty()) {
                    errorMessage = "Invalid access end time.";
                } else {
                    errorMessage = "Invalid number format in one of the fields.";
                }
            }
        }

        // Xử lý thời gian truy cập
        Date accessStartTime = null;
        Date accessEndTime = null;
        if (errorMessage == null) {
            try {
                LocalDateTime ldtStart = LocalDateTime.parse(accessStartTimeStr, formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(accessEndTimeStr, formatter);
                accessStartTime = Date.from(ldtStart.atZone(ZoneId.systemDefault()).toInstant());
                accessEndTime = Date.from(ldtEnd.atZone(ZoneId.systemDefault()).toInstant());
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
            request.setAttribute("pageTitle", "Chỉnh Sửa Cuộc Thi");
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
        competition.setAccessStartTime((java.sql.Date) accessStartTime);
        competition.setAccessEndTime((java.sql.Date) accessEndTime);

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
            request.setAttribute("pageTitle", "Chỉnh Sửa Cuộc Thi");
            request.getRequestDispatcher("/jsp/competition-form.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý tham gia cuộc thi
     */
    private void takeCompetition(HttpServletRequest request, HttpServletResponse response, Users currentUser)
            throws SQLException, ServletException, IOException, ClassNotFoundException {

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // Xử lý GET: Gửi dữ liệu cuộc thi
            String competitionIdStr = request.getParameter("competitionId");
            if (competitionIdStr == null || competitionIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Competition ID is missing."));
                return;
            }

            int competitionId;
            try {
                competitionId = Integer.parseInt(competitionIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Invalid Competition ID."));
                return;
            }

            Competition competition = competitionDAO.getCompetitionById(competitionId);
            if (competition == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Competition not found."));
                return;
            }

            // Kiểm tra thời gian truy cập
            Date now = new Date();
            if (now.before(competition.getAccessStartTime()) || now.after(competition.getAccessEndTime())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Competition is not currently accessible."));
                return;
            }

            // Kiểm tra học sinh có thuộc lớp của cuộc thi hoac co phai la giao vien cua lop do khong

            if (currentUser.hasRole("student")) {
                if (!classDAO.isStudentInClass(currentUser.getId(), competition.getClassId())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    mapper.writeValue(response.getWriter(), new ErrorResponse("You are not enrolled in the class for this competition."));
                    return;
                }
            }else if(currentUser.hasRole("teacher")){
                if (!classDAO.isTeacherInClass(currentUser.getId(), competition.getClassId())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    mapper.writeValue(response.getWriter(), new ErrorResponse("You are not the teacher of the class for this competition."));
                    return;
                }
            }

            // Lấy danh sách câu hỏi từ Quiz
            quiz quiz = quizDAO.getQuizById(competition.getQuizId());
            if (quiz == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Associated Quiz not found."));
                return;
            }

            List<Question> questions = quiz.getQuestions();

            // Xáo trộn câu hỏi nếu cần
            if (competition.isShuffleQuestions()) {
                Collections.shuffle(questions);
            }

            // Giới hạn số lượng câu hỏi
            if (questions.size() > competition.getQuestionCount()) {
                questions = questions.subList(0, competition.getQuestionCount());
            }

            // Xáo trộn đáp án cho từng câu hỏi nếu cần
            for (Question q : questions) {
                Collections.shuffle(q.getOptions());
            }

            // Tạo dữ liệu để gửi về client
            CompetitionData competitionData = new CompetitionData();
            competitionData.setTotalQuestions(questions.size());
            competitionData.setTimeLimit(competition.getTimeLimit());
            competitionData.setQuestions(questions);
            competitionData.setCompetitionId(competitionId);

            mapper.writeValue(response.getWriter(), competitionData);

        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            // Xử lý POST: Nhận câu trả lời và chấm điểm
            try {
                // Đọc dữ liệu từ body
                BufferedReader reader = request.getReader();
                CompetitionSubmission submission = mapper.readValue(reader, CompetitionSubmission.class);

                int competitionId = submission.getCompetitionId();
                Competition competition = competitionDAO.getCompetitionById(competitionId);
                if (competition == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    mapper.writeValue(response.getWriter(), new ErrorResponse("Competition not found."));
                    return;
                }

                // Kiểm tra thời gian
                Date now = new Date();
                if (now.before(competition.getAccessStartTime()) || now.after(competition.getAccessEndTime())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    mapper.writeValue(response.getWriter(), new ErrorResponse("Competition is not currently accessible."));
                    return;
                }

                // Kiểm tra học sinh có thuộc lớp của cuộc thi không
                if (currentUser.hasRole("student")) {
                    if (!classDAO.isStudentInClass(currentUser.getId(), competition.getClassId())) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        mapper.writeValue(response.getWriter(), new ErrorResponse("You are not enrolled in the class for this competition."));
                        return;
                    }
                }

                // Lấy danh sách câu hỏi từ Quiz
                quiz quiz = quizDAO.getQuizById(competition.getQuizId());
                if (quiz == null) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    mapper.writeValue(response.getWriter(), new ErrorResponse("Associated Quiz not found."));
                    return;
                }

                List<Question> questions = quiz.getQuestions();

                // Giới hạn số lượng câu hỏi
                if (questions.size() > competition.getQuestionCount()) {
                    questions = questions.subList(0, competition.getQuestionCount());
                }

                // Chấm điểm
                int score = 0;
                for (int i = 0; i < questions.size(); i++) {
                    String userAnswer = submission.getUserAnswers().get(String.valueOf(i));
                    if (userAnswer != null && userAnswer.equalsIgnoreCase(questions.get(i).getCorrectAnswer())) {
                        score += 1;
                    }
                }

                // Tính điểm số
                float percentage = ((float) score / questions.size()) * 100;

                // Tạo CompetitionResult và lưu vào cơ sở dữ liệu nếu người dùng là học sinh
                if (currentUser.hasRole("student")) {
                    CompetitionResult competitionResult = new CompetitionResult();
                    competitionResult.setCompetitionId(competitionId);
                    competitionResult.setUserId(currentUser.getId());
                    competitionResult.setClassId(competition.getClassId());
                    competitionResult.setScore(percentage);
                    competitionResult.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                    competitionResultDAO.insertCompetitionResult(competitionResult);
                }

                // Trả về kết quả
                CompetitionResultResponse resultResponse = new CompetitionResultResponse();
                resultResponse.setScore(percentage);
                resultResponse.setTotalQuestions(questions.size());

                mapper.writeValue(response.getWriter(), resultResponse);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(response.getWriter(), new ErrorResponse("Invalid request data."));
                e.printStackTrace();
            }
        } else {
            // Phương thức HTTP không được hỗ trợ
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            mapper.writeValue(response.getWriter(), new ErrorResponse("Method not allowed."));
        }
    }

    /**
     * Các lớp hỗ trợ cho việc chuyển đổi JSON
     */
    static class CompetitionData {
        private int competitionId;
        private int totalQuestions;
        private int timeLimit;
        private List<Question> questions;

        // Getters and Setters
        public int getCompetitionId() {
            return competitionId;
        }

        public void setCompetitionId(int competitionId) {
            this.competitionId = competitionId;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }
    }

    static class CompetitionSubmission {
        private int competitionId;
        private Map<String, String> userAnswers; // { "0": "Option A", "1": "Option B", ... }
        private int timeTaken; // in seconds

        // Getters and Setters
        public int getCompetitionId() {
            return competitionId;
        }

        public void setCompetitionId(int competitionId) {
            this.competitionId = competitionId;
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

    static class CompetitionResultResponse {
        private float score;
        private int totalQuestions;

        // Getters and Setters
        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }
    }

    static class ErrorResponse {
        private String error;

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
