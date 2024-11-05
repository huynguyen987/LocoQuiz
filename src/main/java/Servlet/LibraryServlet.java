package Servlet;

import dao.UserLibraryDAO;
import entity.Users;
import entity.quiz;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/LibraryServlet")
public class LibraryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users currentUser = (Users) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserLibraryDAO userLibraryDAO = new UserLibraryDAO();
        List<quiz> libraryQuizzes = userLibraryDAO.getUserLibrary(currentUser.getId());

        request.setAttribute("libraryQuizzes", libraryQuizzes);
        request.getRequestDispatcher("/jsp/library.jsp").forward(request, response);
    }
}
