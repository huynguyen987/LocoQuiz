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
public class EnrollStudentServlet extends   HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int classId = Integer.parseInt(request.getParameter("classId"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        ClassUserDAO classUserDAO = new ClassUserDAO();

        try {
            boolean isEnrolled = classUserDAO.enrollStudentToClass(classId, studentId);
            if (isEnrolled) {
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=classDetails&classId=" + classId);
            } else {
                request.setAttribute("errorMessage", "Không thể ghi danh học sinh.");
                request.getRequestDispatcher("/jsp/teacher.jsp?action=enrollStudents&classId=" + classId).forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("enrollStudent.jsp").forward(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        if (currentUser == null || (!currentUser.hasRole("teacher") && !currentUser.hasRole("admin"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get classId from the request parameter
        int classId = Integer.parseInt(request.getParameter("classId"));

        try {
            ClassDAO classDAO = new ClassDAO();
            classs classEntity = classDAO.getClassById(classId);

            if (classEntity == null) {
                request.setAttribute("errorMessage", "Class not found.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Ensure the current user is the teacher of the class or an admin
            if (classEntity.getTeacher_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                request.setAttribute("errorMessage", "You do not have permission to enroll students in this class.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Get the list of students not yet enrolled in the class
            UsersDAO usersDAO = new UsersDAO();
            List<Users> students = usersDAO.getAllStudentsNotInClass(classId);

            // Set attributes for the JSP page
            request.setAttribute("classEntity", classEntity);
            request.setAttribute("students", students);

            // Forward to the enrollment JSP page
            request.getRequestDispatcher("/jsp/enrollStudent.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
