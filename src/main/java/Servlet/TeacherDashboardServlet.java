package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import entity.classs;
import entity.Competition;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TeacherDashboardServlet", urlPatterns = {"/teacherDashboard"})
public class TeacherDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("update".equals(action)) { // Sử dụng "update".equals(action) để tránh NullPointerException
            try {
                ClassDAO classDAO = new ClassDAO();
                List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
                System.out.println(classes.size());
                request.setAttribute("classes", classes);

                CompetitionDAO competitionDAO = new CompetitionDAO();
                List<Competition> competitions = competitionDAO.getCompetitionsByTeacher(currentUser.getId());
                System.out.println(competitions.size());
                request.setAttribute("competitions", competitions);

                request.getRequestDispatcher("/jsp/teacher.jsp").forward(request, response);
                System.out.println("Update teacher dashboard!");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } else {
            // Nếu không có action hoặc action khác, cũng có thể chuyển hướng tới teacher.jsp với dữ liệu hiện tại
            try {
                ClassDAO classDAO = new ClassDAO();
                List<classs> classes = classDAO.getClassesByTeacherId(currentUser.getId());
                request.setAttribute("classes", classes);

                CompetitionDAO competitionDAO = new CompetitionDAO();
                List<Competition> competitions = competitionDAO.getCompetitionsByTeacher(currentUser.getId());
                request.setAttribute("competitions", competitions);

                request.getRequestDispatcher("/jsp/teacher.jsp").forward(request, response);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        }
    }
}
