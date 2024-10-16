package Servlet;

import dao.ClassDAO;
import dao.ClassQuizDAO;
import dao.QuizDAO;
import entity.classs;
import entity.quiz;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List; // Existing import

@WebServlet(name = "AssignQuizServlet", urlPatterns = {"/AssignQuizServlet"})
public class AssignQuizServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is a teacher or admin; if not, redirect to login
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Retrieve classId from request
        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Class ID is missing.");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }

        int classId;
        try {
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid Class ID.");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }

        classs classEntity = null;
        List<quiz> quizzes = null;

        // Verify access rights
        try {
            ClassDAO classDAO = new ClassDAO();
            classEntity = classDAO.getClassById(classId);
            if (classEntity == null || classEntity.getTeacher_id() != currentUser.getId()) {
                request.setAttribute("errorMessage", "Class not found or access denied.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Retrieve list of quizzes
            QuizDAO quizDAO = new QuizDAO();
            quizzes = quizDAO.getQuizzesByUserId(currentUser.getId());

            // Forward to assignQuiz.jsp with class and quizzes data
            request.setAttribute("classEntity", classEntity);
            request.setAttribute("quizzes", quizzes);
            request.getRequestDispatcher("/jsp/assignQuiz.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    // Handle POST requests to assign the quiz to the class
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is a teacher or admin; if not, redirect to login
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Retrieve classId and quizId from request
        String classIdStr = request.getParameter("classId");
        String quizIdStr = request.getParameter("quizId");

        if (classIdStr == null || quizIdStr == null || classIdStr.trim().isEmpty() || quizIdStr.trim().isEmpty()) {
            // Missing parameters; redirect with assignError message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=assignQuiz&classId=" + classIdStr + "&message=assignError");
            return;
        }

        int classId;
        int quizId;
        try {
            classId = Integer.parseInt(classIdStr);
            quizId = Integer.parseInt(quizIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Invalid parameters; redirect with assignError message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=assignQuiz&classId=" + classIdStr + "&message=assignError");
            return;
        }

        try {
            // Assign quiz to class
            ClassQuizDAO classQuizDAO = new ClassQuizDAO();
            boolean isAssigned = classQuizDAO.assignQuizToClass(classId, quizId);

            if (isAssigned) {
                // **UPDATED REDIRECT**: Include message=quizAssigned to trigger popup
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId + "&message=quizAssigned");
            } else {
                // Assignment failed; redirect with assignError message
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=assignQuiz&classId=" + classId + "&message=assignError");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Exception occurred; redirect with assignError message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=assignQuiz&classId=" + classId + "&message=assignError");
        }
    }
}
