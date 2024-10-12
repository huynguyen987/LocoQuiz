package Servlet;

import dao.ClassDAO;
import entity.classs;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "EditClassServlet", urlPatterns = {"/EditClassServlet"})
public class EditClassServlet extends HttpServlet {

    private static final int ROLE_TEACHER = 1; // Adjust based on your implementation

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
        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        int classId;
        try {
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        try {
            ClassDAO classDAO = new ClassDAO();
            classs classEntity = classDAO.getClassById(classId);

            if (classEntity == null) {
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
                return;
            }

            // Ensure the current user is the teacher of the class or an admin
            if (classEntity.getTeacher_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
                return;
            }

            // Set the class entity as a request attribute
            request.setAttribute("classEntity", classEntity);

            // Forward to the edit class JSP page
            request.getRequestDispatcher("/jsp/editClass.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
        }
    }

    // Handle POST requests to save changes
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Session and authentication check
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Retrieve form parameters
        String classIdStr = request.getParameter("classId");
        String className = request.getParameter("name");
        String classDescription = request.getParameter("description");

        // Validate parameters
        if (classIdStr == null || className == null || classDescription == null ||
                classIdStr.trim().isEmpty() || className.trim().isEmpty()) {
            // Missing parameters, redirect back with error
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        int classId;
        try {
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        // Update class in the database
        ClassDAO classDAO = new ClassDAO();
        try {
            classs classEntity = classDAO.getClassById(classId);

            if (classEntity == null || classEntity.getTeacher_id() != currentUser.getId()) {
                // Class not found or access denied
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
                return;
            }

            // Update class details
            classEntity.setName(className.trim());
            classEntity.setDescription(classDescription.trim());

            boolean updateSuccess = classDAO.updateClass(classEntity);

            if (updateSuccess) {
                // Redirect back to dashboard with success message
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=classEdited");
            } else {
                // Update failed, redirect back with error message
                response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
        }
    }
}
