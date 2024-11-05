package Servlet;

import dao.UserLibraryDAO;
import entity.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AddToLibraryServlet")
public class AddToLibraryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users currentUser = (Users) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

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
        boolean added = userLibraryDAO.addQuizToLibrary(currentUser.getId(), quizId);

        if (added) {
            // Thêm thông báo thành công nếu cần
        } else {
            // Thêm thông báo lỗi nếu cần
        }

        response.sendRedirect(request.getContextPath() + "/LibraryServlet?quizId=" + quizId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
