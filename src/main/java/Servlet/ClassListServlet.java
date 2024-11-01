package Servlet;

import dao.ClassDAO;
import entity.classs;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClassListServlet", urlPatterns = {"/ClassListServlet"})
public class ClassListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users currentUser = null;
        if (session != null) {
            currentUser = (Users) session.getAttribute("user");
        }
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        System.out.println("currentUser: " + currentUser.getUsername());

        List<classs> classes = null;
        String search = request.getParameter("search");
        try {
            ClassDAO classDAO = new ClassDAO();
            if (search != null && !search.trim().isEmpty()) {
                classes = classDAO.searchClassesByTeacherId(search, currentUser.getId());
            } else {
                classes = classDAO.getClassesByTeacherId(currentUser.getId());
            }
            request.setAttribute("classes", classes);
            request.getRequestDispatcher(request.getContextPath() + "/jsp/classList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
