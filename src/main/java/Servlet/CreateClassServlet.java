package Servlet;

import dao.ClassDAO;
import entity.Users;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CreateClassServlet", value = "/CreateClassServlet")
public class CreateClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check permissions (teacher or admin)
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (currentUser.getRole_id() != 2 && currentUser.getRole_id() != 3)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Get data from form
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Generate classKey
        String classKey = generateClassKey();

        // Create class object
        classs newClass = new classs();
        newClass.setName(name);
        newClass.setDescription(description);
        newClass.setClass_key(classKey);
        newClass.setTeacher_id(currentUser.getId());

        // Save to database
        ClassDAO classDAO = new ClassDAO();
        try {
            boolean isCreated = classDAO.createClass(newClass);
            if (isCreated) {
                // Redirect to class details page with the correct classId
                response.sendRedirect(request.getContextPath() + "/ClassDetailsServlet?classId=" + newClass.getId());
            } else {
                // Show error message
                request.setAttribute("errorMessage", "Không thể tạo lớp. Vui lòng thử lại.");
                request.getRequestDispatcher("/jsp/createClass.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi SQL: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi ClassNotFound: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi không xác định: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    // Method to generate a random classKey
    private String generateClassKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder classKey = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * characters.length());
            classKey.append(characters.charAt(index));
        }
        return classKey.toString();
    }
}
