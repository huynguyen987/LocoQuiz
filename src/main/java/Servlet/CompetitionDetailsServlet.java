package Servlet;

import dao.CompetitionDAO;
import entity.Competition;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CompetitionDetailsServlet", urlPatterns = {"/CompetitionDetailsServlet"})
public class CompetitionDetailsServlet extends HttpServlet {

    private CompetitionDAO competitionDAO;

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số 'id' từ yêu cầu
        String competitionIdParam = request.getParameter("competitionId");
        System.out.println("competitionIdParam: " + competitionIdParam);
        if (competitionIdParam == null || competitionIdParam.isEmpty()) {
            // Nếu không có tham số 'id', chuyển hướng đến trang danh sách cuộc thi hoặc trang lỗi
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp");
            return;
        }

        try {
            int competitionId = Integer.parseInt(competitionIdParam);
            // Sử dụng CompetitionDAO để lấy thông tin cuộc thi theo ID
            Competition competition = competitionDAO.getCompetitionById(competitionId);

            if (competition == null) {
                // Nếu không tìm thấy cuộc thi, chuyển hướng hoặc hiển thị thông báo lỗi
                request.setAttribute("errorMessage", "Cuộc thi không tồn tại.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin người dùng hiện tại từ session (nếu cần thiết)
            HttpSession session = request.getSession(false);
            Users currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }

            // Đặt đối tượng Competition vào thuộc tính yêu cầu để JSP có thể truy cập
            request.setAttribute("competition", competition);
            // Có thể thêm thông tin người dùng vào nếu cần
            if (currentUser != null) {
                request.setAttribute("currentUser", currentUser);
            }
            // Chuyển tiếp yêu cầu đến trang JSP để hiển thị chi tiết cuộc thi
            request.getRequestDispatcher("/jsp/competitionDetails.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu tham số 'id' không phải là số
            request.setAttribute("errorMessage", "ID cuộc thi không hợp lệ.");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            // Xử lý lỗi kết nối cơ sở dữ liệu hoặc các lỗi khác
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi truy xuất dữ liệu.");
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
