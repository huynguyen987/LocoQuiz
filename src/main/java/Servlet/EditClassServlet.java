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

    // Handle GET requests to display the edit class form
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
                request.setAttribute("errorMessage", "You do not have permission to edit this class.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Set the class entity as a request attribute
            request.setAttribute("classEntity", classEntity);

            // Forward to the edit class JSP page
            request.getRequestDispatcher("/jsp/editClass.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    // Handle POST requests to save changes
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement the code to save the changes made to the class
        // Check permissions
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != Users.ROLE_TEACHER && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get data from the form
        int classId = Integer.parseInt(request.getParameter("classId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Update the class in the database
        ClassDAO classDAO = new ClassDAO();
        try {
            classs classEntity = classDAO.getClassById(classId);
            if (classEntity == null) {
                request.setAttribute("errorMessage", "Class not found.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Update class details
            classEntity.setName(name);
            classEntity.setDescription(description);

            // Save changes
            boolean isUpdated = classDAO.updateClass(classEntity);

            if (isUpdated) {
                // Redirect to class details page
                response.sendRedirect(request.getContextPath() + "/ClassDetailsServlet?classId=" + classId);
            } else {
                request.setAttribute("errorMessage", "Failed to update class. Please try again.");
                request.getRequestDispatcher("/jsp/editClass.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
