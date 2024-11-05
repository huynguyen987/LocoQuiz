package Servlet;

import dao.UserLibraryDAO;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/RemoveFromLibraryServlet")
public class RemoveFromLibraryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users currentUser = (Users) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String message = request.getParameter("message");
        String quizIdParam = request.getParameter("quizId");
        int quizId;
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        UserLibraryDAO userLibraryDAO = new UserLibraryDAO();
        boolean removed = userLibraryDAO.removeQuizFromLibrary(currentUser.getId(), quizId);

        if (removed) {
            // Thêm thông báo thành công nếu cần
            message = "Add to library successfully!";

        } else {
            // Thêm thông báo lỗi nếu cần
            message = "Add to library failed!";
        }

        response.sendRedirect(request.getContextPath() + "/LibraryServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
