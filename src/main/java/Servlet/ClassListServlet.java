package Servlet;

import dao.ClassDAO;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClassListServlet", value = "/ClassListServlet")
public class ClassListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String search = request.getParameter("search");

        ClassDAO classDAO = new ClassDAO();
        try {
            List<classs> classes;
            if (search != null && !search.trim().isEmpty()) {
                classes = classDAO.searchClasses(search);
            } else {
                classes = classDAO.getAllClass();
            }
            request.setAttribute("classes", classes);
            request.getRequestDispatcher("/jsp/classList.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}