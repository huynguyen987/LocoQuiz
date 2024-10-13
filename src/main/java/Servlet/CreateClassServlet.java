package Servlet;

import dao.ClassDAO;
import entity.classs;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CreateClassServlet", urlPatterns = {"/CreateClassServlet"})
public class CreateClassServlet extends HttpServlet {

    // Method to generate a unique class key
    private String generateClassKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Session and user authentication
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole_id() != Users.ROLE_TEACHER) {
            response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
            return;
        }

        // Input validation
        if (name == null || name.trim().isEmpty()) {
            // Redirect back to Create Class form with an error message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=createClass&message=createError");
            return;
        }

        // Create a new class entity
        classs newClass = new classs();
        newClass.setName(name.trim());
        newClass.setDescription(description != null ? description.trim() : "");
        newClass.setTeacher_id(currentUser.getId());
        newClass.setClass_key(generateClassKey());

        // Initialize the ClassDAO
        ClassDAO classDAO = new ClassDAO();

        try {
            // Save the new class to the database
            classDAO.createClass(newClass);

            // Redirect to the dashboard with a success message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=classCreated");
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect back to Create Class form with an error message
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?action=createClass&message=createError");
        }
    }
}
