package Servlet;

import dao.CompetitionResultDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ResetCompetitionResultServlet", urlPatterns = {"/ResetCompetitionResultServlet"})
public class ResetCompetitionResultServlet extends HttpServlet {

    private CompetitionResultDAO competitionResultDAO;

    @Override
    public void init() throws ServletException {
        competitionResultDAO = new CompetitionResultDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int competitionId = Integer.parseInt(request.getParameter("competitionId"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");

        if (currentUser == null || !currentUser.hasRole("teacher")) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        try {
            boolean success = competitionResultDAO.deleteCompetitionResultByUserAndCompetition(userId, competitionId);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/CompetitionController?action=view&id=" + competitionId + "&message=resetSuccess");
            } else {
                response.sendRedirect(request.getContextPath() + "/CompetitionController?action=view&id=" + competitionId + "&message=resetFailed");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/CompetitionController?action=view&id=" + competitionId + "&message=resetFailed");
        }
    }
}
