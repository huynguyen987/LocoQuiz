package Servlet;

import dao.CompetitionResultDAO;
import entity.CompetitionResult;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

@WebServlet(name = "CompetitionResultsServlet", urlPatterns = {"/CompetitionResultsServlet"})
public class CompetitionResultsServlet extends HttpServlet {

    private CompetitionResultDAO competitionResultDAO;

    @Override
    public void init() throws ServletException {
        competitionResultDAO = new CompetitionResultDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số 'competitionId' từ yêu cầu
        String competitionIdParam = request.getParameter("competitionId");
        if (competitionIdParam == null || competitionIdParam.isEmpty()) {
            // Nếu không có tham số 'competitionId', chuyển hướng đến trang danh sách cuộc thi
            response.sendRedirect(request.getContextPath() + "/CompetitionsServlet");
            return;
        }

        try {
            int competitionId = Integer.parseInt(competitionIdParam);
            // Lấy danh sách kết quả của cuộc thi này
            List<CompetitionResult> competitionResults = competitionResultDAO.getCompetitionResultsByCompetitionId(competitionId);

            // Lấy thông tin người dùng hiện tại từ session
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            // Đặt các thuộc tính cần thiết vào request
            request.setAttribute("competitionResults", competitionResults);
            request.setAttribute("competitionId", competitionId);
            if (currentUser != null) {
                request.setAttribute("currentUser", currentUser);
            }
            // Lấy năm hiện tại cho footer
            Calendar cal = Calendar.getInstance();
            request.setAttribute("currentYear", cal.get(Calendar.YEAR));

            // Đặt tiêu đề trang
            request.setAttribute("pageTitle", "Kết Quả Cuộc Thi");

            // Chuyển tiếp đến trang JSP để hiển thị kết quả
            request.getRequestDispatcher("/jsp/competitionResults.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu tham số 'competitionId' không phải là số
            request.setAttribute("errorMessage", "ID cuộc thi không hợp lệ.");
            // Đặt các thuộc tính cần thiết
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }
            if (currentUser != null) {
                request.setAttribute("currentUser", currentUser);
            }
            Calendar cal = Calendar.getInstance();
            request.setAttribute("currentYear", cal.get(Calendar.YEAR));
            request.setAttribute("pageTitle", "Lỗi");
            // Chuyển tiếp đến trang lỗi
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            // Xử lý lỗi cơ sở dữ liệu
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi truy xuất dữ liệu.");
            // Đặt các thuộc tính cần thiết
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }
            if (currentUser != null) {
                request.setAttribute("currentUser", currentUser);
            }
            Calendar cal = Calendar.getInstance();
            request.setAttribute("currentYear", cal.get(Calendar.YEAR));
            request.setAttribute("pageTitle", "Lỗi");
            // Chuyển tiếp đến trang lỗi
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu cần xử lý yêu cầu POST, có thể triển khai ở đây
        doGet(request, response);
    }
}
