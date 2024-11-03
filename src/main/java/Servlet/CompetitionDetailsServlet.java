package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.QuizDAO;
import entity.Competition;
import entity.Users;
import entity.classs;
import entity.quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CompetitionDetailsServlet", urlPatterns = {"/CompetitionDetailsServlet"})
public class CompetitionDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CompetitionDAO competitionDAO;
    private ClassDAO classDAO;
    private QuizDAO quizDAO;

    @Override
    public void init() throws ServletException {
        competitionDAO = new CompetitionDAO();
        classDAO = new ClassDAO();
        quizDAO = new QuizDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số 'id' từ yêu cầu
        String competitionIdParam = request.getParameter("id");
        System.out.println("competitionIdParam: " + competitionIdParam);
        if (competitionIdParam == null || competitionIdParam.isEmpty()) {
            // Nếu không có tham số 'id', chuyển hướng đến trang danh sách cuộc thi hoặc trang lỗi
            response.sendRedirect(request.getContextPath() + "/CompetitionController?action=list");
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

            if (currentUser == null) {
                // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
                return;
            }

            // Đặt đối tượng Competition và Users vào thuộc tính yêu cầu để JSP có thể truy cập
            request.setAttribute("competition", competition);
            request.setAttribute("currentUser", currentUser);

            // Lấy thông tin lớp học và quiz liên quan
            classs classInfo = classDAO.getClassById(competition.getClassId());
            quiz quizInfo = quizDAO.getQuizById(competition.getQuizId());
            request.setAttribute("classInfo", classInfo);
            request.setAttribute("quizInfo", quizInfo);

            // Phân phối trang JSP dựa trên vai trò của người dùng
            if (currentUser.hasRole("teacher")) {
                request.getRequestDispatcher("/jsp/competition-details-teacher.jsp").forward(request, response);
            } else if (currentUser.hasRole("student")) {
                request.getRequestDispatcher("/jsp/competition-details-student.jsp").forward(request, response);
            } else {
                // Người dùng không có quyền, chuyển hướng về danh sách cuộc thi
                response.sendRedirect("CompetitionController?action=list");
            }

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
