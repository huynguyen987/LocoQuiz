package Servlet;

import dao.ClassDAO;
import dao.ClassQuizDAO;
import dao.ClassUserDAO;
import dao.UsersDAO;
import entity.Users;
import entity.classs;
import entity.quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ClassDetailsServlet", urlPatterns = {"/ClassDetailsServlet"})
public class ClassDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current user from the session
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Get classId from the request parameter
        int classId = Integer.parseInt(request.getParameter("classId"));

        try {
            ClassDAO classDAO = new ClassDAO();
            UsersDAO usersDAO = new UsersDAO();
            ClassUserDAO classUserDAO = new ClassUserDAO();
            ClassQuizDAO classQuizDAO = new ClassQuizDAO();

            classs classEntity = classDAO.getClassById(classId);
            if (classEntity == null) {
                request.setAttribute("errorMessage", "Class not found.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Access control
            if (currentUser.getRole_id() != Users.ROLE_ADMIN && currentUser.getRole_id() != Users.ROLE_TEACHER) {
                boolean isEnrolled = classUserDAO.isUserEnrolledInClass(currentUser.getId(), classId);
                if (!isEnrolled) {
                    request.setAttribute("errorMessage", "You do not have access to this class.");
                    request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                    return;
                }
            }

            Users teacher = usersDAO.getUserById(classEntity.getTeacher_id());
            List<Users> students = classUserDAO.getUsersByClassId(classId);
            List<quiz> quizzes = classQuizDAO.getQuizzesByClassId(classId);

            // Set attributes for the JSP page
            request.setAttribute("classEntity", classEntity);
            request.setAttribute("teacher", teacher);
            request.setAttribute("students", students);
            request.setAttribute("quizzes", quizzes);

            // Forward to the JSP page
            request.getRequestDispatcher("/jsp/classDetails.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}

