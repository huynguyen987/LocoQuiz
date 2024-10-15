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
import java.util.List; // Add this import

@WebServlet(name = "AssignQuizServlet", urlPatterns = {"/AssignQuizServlet"})
public class AssignQuizServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra nếu không phải là giáo viên hoặc admin thì chuyển hướng về trang login
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
           //Lấy id của lớp và quiz từ request
        int classId = Integer.parseInt(request.getParameter("classId"));
        classs classEntity = null;
        List<quiz> quizzes = null;
                 
        // Kiểm tra quyền truy cập
        try {
            ClassDAO classDAO = new ClassDAO();
            classEntity = classDAO.getClassById(classId);
            if (classEntity == null || classEntity.getTeacher_id() != currentUser.getId()) {
                request.setAttribute("errorMessage", "Class not found or access denied.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách quiz
            QuizDAO quizDAO = new QuizDAO();
            quizzes = quizDAO.getQuizzesByUserId(currentUser.getId());

            // Chuyển hướng sang trang giao diện assignQuiz.jsp
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
        // Kiểm tra nếu không phải là giáo viên hoặc admin thì chuyển hướng về trang login
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy id của lớp và quiz từ request
        int classId = Integer.parseInt(request.getParameter("classId"));
        int quizId = Integer.parseInt(request.getParameter("quizId"));

        try {
            // lấy thông tin lớp học
            ClassQuizDAO classQuizDAO = new ClassQuizDAO();
            boolean isAssigned = classQuizDAO.assignQuizToClass(classId, quizId);

            if (isAssigned) {
                // Chuyển hướng về trang chi tiết lớp học
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId);
            } else {
                request.setAttribute("errorMessage", "Failed to assign quiz.");
                request.getRequestDispatcher("/jsp/teacher.jsp?action=assignQuiz&classId=" + classId).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
