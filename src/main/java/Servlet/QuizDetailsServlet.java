/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import dao.QuizDAO;
import entity.Users;
import entity.quiz;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hient
 */
@WebServlet(name = "QuizDetailsServlet", urlPatterns = {"/QuizDetailsServlet"})
public class QuizDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizIdParam = request.getParameter("quizId");
        if (quizIdParam == null || quizIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Invalid Quiz ID");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Invalid Quiz ID Format");
            return;
        }

        try {
            QuizDAO quizDAO = new QuizDAO();
            quiz quiz = quizDAO.getQuizById(quizId);

            if (quiz == null) {
                response.sendRedirect(request.getContextPath() + "/error.jsp?message=Quiz Not Found");
                return;
            }

            // Lấy người dùng từ session
            HttpSession session = request.getSession();
            Users currentUser = (Users) session.getAttribute("user");

            // Đặt đối tượng quiz vào request
            request.setAttribute("quiz", quiz);
            request.setAttribute("currentUser", currentUser);

            // Chuyển tiếp đến trang JSP
            request.getRequestDispatcher("/jsp/quiz-details.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Error Fetching Quiz Details");
        }
    }
}
