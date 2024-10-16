package Servlet;

import dao.ClassDAO;
import dao.ClassUserDAO;
import dao.UsersDAO;
import entity.Users;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "EnrollStudentServlet", value = "/EnrollStudentServlet")
public class EnrollStudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve and validate classId and studentId from request parameters
        String classIdStr = request.getParameter("classId");
        String studentIdStr = request.getParameter("studentId");

        if (classIdStr == null || studentIdStr == null || classIdStr.trim().isEmpty() || studentIdStr.trim().isEmpty()) {
            // Missing parameters; redirect with enrollError message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=enrollStudents&classId=" + classIdStr + "&message=enrollError");
            return;
        }

        int classId;
        int studentId;
        try {
            classId = Integer.parseInt(classIdStr);
            studentId = Integer.parseInt(studentIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Invalid parameters; redirect with enrollError message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=enrollStudents&classId=" + classIdStr + "&message=enrollError");
            return;
        }

        ClassUserDAO classUserDAO = new ClassUserDAO();

        try {
            // Attempt to enroll the student to the class
            boolean isEnrolled = classUserDAO.enrollStudentToClass(classId, studentId);
            if (isEnrolled) {
                // **SUCCESS**: Redirect with message=studentEnrolled to trigger popup
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId + "&message=studentEnrolled");
            } else {
                // **FAILURE**: Redirect with message=enrollError
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=enrollStudents&classId=" + classId + "&message=enrollError");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // **EXCEPTION**: Redirect with message=enrollError
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=enrollStudents&classId=" + classId + "&message=enrollError");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        // Check if the user is not logged in or is not a teacher or admin
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get classId from the request parameter
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

        // Get class details
        try {
            ClassDAO classDAO = new ClassDAO();
            classs classEntity = classDAO.getClassById(classId);

            // Check if the class exists
            if (classEntity == null) {
                request.setAttribute("errorMessage", "Class not found.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Check if the current user is the teacher of the class or an admin
            if (classEntity.getTeacher_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                request.setAttribute("errorMessage", "You do not have permission to enroll students in this class.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Get all students not in the class
            UsersDAO usersDAO = new UsersDAO();
            List<Users> students = usersDAO.getAllStudentsNotInClass(classId);

            // Set attributes and forward to the enrollment JSP page
            request.setAttribute("classEntity", classEntity);
            request.setAttribute("students", students);

            // Forward to the enrollment JSP page
            request.getRequestDispatcher("/jsp/enrollStudent.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
