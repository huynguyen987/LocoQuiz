package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.QuizDAO;
import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@WebServlet(name = "TakeCompetitionServlet", urlPatterns = {"/TakeCompetitionServlet"})
public class TakeCompetitionServlet extends HttpServlet {

    // DateTimeFormatter để định dạng thời gian
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Kiểm tra xác thực và phân quyền
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
        if ("configure".equals(action)) {
            // Lấy danh sách các lớp mà giáo viên sở hữu
            ClassDAO classDAO = new ClassDAO();
            List<classs> classes = null;
            try {
                classes = classDAO.getClassesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error retrieving classes.");
            }

            // Lấy danh sách các quiz mà giáo viên đã tạo
            QuizDAO quizDAO = new QuizDAO();
            List<quiz> quizzes = null;
            try {
                quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error retrieving quizzes.");
            }

            // Đặt các thuộc tính cho JSP
            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);

            // Chuyển hướng đến trang cấu hình cuộc thi
            request.getRequestDispatcher("/jsp/configure-competition.jsp").forward(request, response);
            return;
        }

        // Tham gia cuộc thi (take a competition)
        if ("take".equals(action)) {
            // Lấy ID của cuộc thi từ tham số
            String competitionIdParam = request.getParameter("competitionId");
            if (competitionIdParam == null || competitionIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp");
                return;
            }

            try {
                int competitionId = Integer.parseInt(competitionIdParam);
                // Sử dụng CompetitionDAO để lấy thông tin cuộc thi theo ID
                CompetitionDAO competitionDAO = new CompetitionDAO();
                Competition competition = competitionDAO.getCompetitionById(competitionId);

                if (competition == null) {
                    request.setAttribute("errorMessage", "Competition not found.");
                    request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                    return;
                }

                // Lấy danh sách câu hỏi cho cuộc thi
                List<Question> questions = competitionDAO.getQuestionsForCompetition(competitionId);

                // Đặt các thuộc tính cho JSP
                request.setAttribute("competition", competition);
                request.setAttribute("questions", questions);

                // Chuyển hướng đến trang tham gia cuộc thi
                request.getRequestDispatcher("/jsp/take-competition.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid competition ID.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "An error occurred while retrieving competition data.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra xác thực và phân quyền
        HttpSession session = request.getSession(false);
        Users currentUser = null;
        if (session != null) {
            currentUser = (Users) session.getAttribute("user");
        }

        if (currentUser == null || !(currentUser.hasRole("teacher") || currentUser.hasRole("admin"))) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Đọc dữ liệu từ form cấu hình cuộc thi
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
                // Sử dụng LocalDateTime.parse để phân tích cú pháp chuỗi ngày giờ với thời gian
                LocalDateTime startLDT = LocalDateTime.parse(accessStartTimeStr, formatter);
                accessStartTime = Date.from(startLDT.atZone(ZoneId.systemDefault()).toInstant());

                LocalDateTime endLDT = LocalDateTime.parse(accessEndTimeStr, formatter);
                accessEndTime = Date.from(endLDT.atZone(ZoneId.systemDefault()).toInstant());

                if (accessStartTime.after(accessEndTime)) {
                    errorMessage = "Access start time must be before access end time.";
                }
            } catch (Exception e) {
                errorMessage = "Invalid date/time format.";
            }
        }

        if (errorMessage != null) {
            // Nếu có lỗi, tái hiện lại form với thông báo lỗi
            // Lấy lại danh sách lớp và quiz để hiển thị
            ClassDAO classDAO = new ClassDAO();
            List<classs> classes = null;
            try {
                classes = classDAO.getClassesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Lấy lại danh sách các quiz
            QuizDAO quizDAO = new QuizDAO();
            List<quiz> quizzes = null;
            try {
                quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("errorMessage", errorMessage);

            request.getRequestDispatcher("/jsp/configure-competition.jsp").forward(request, response);
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
        CompetitionDAO competitionDAO = new CompetitionDAO();
        ClassDAO classDAO = new ClassDAO();
        QuizDAO quizDAO = new QuizDAO();
        List<classs> classes = null;
        List<quiz> quizzes = null;
        try {
            boolean success = competitionDAO.createCompetition(competition);
            if (success) {
                // Chuyển hướng về dashboard với thông báo thành công
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=dashboard&message=competitionCreated");
            } else {
                request.setAttribute("errorMessage", "Failed to create competition.");
                // Lấy lại danh sách lớp và quiz để hiển thị
                try {
                    classes = classDAO.getClassesByTeacherId(currentUser.getId());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                request.setAttribute("classes", classes);
                request.setAttribute("quizzes", quizzes);
                request.getRequestDispatcher("/jsp/configure-competition.jsp").forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while creating the competition.");
            // Lấy lại danh sách lớp và quiz để hiển thị
            try {
                classes = classDAO.getClassesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            try {
                quizzes = quizDAO.getQuizzesByTeacherId(currentUser.getId());
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            request.setAttribute("classes", classes);
            request.setAttribute("quizzes", quizzes);
            request.getRequestDispatcher("/jsp/configure-competition.jsp").forward(request, response);
        }
    }
}
