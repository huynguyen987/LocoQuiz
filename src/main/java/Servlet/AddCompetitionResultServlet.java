package Servlet;

import dao.CompetitionResultDAO;
import entity.CompetitionResult;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

@WebServlet(name = "AddCompetitionResultServlet", urlPatterns = {"/AddCompetitionResultServlet"})
public class AddCompetitionResultServlet extends HttpServlet {

    private CompetitionResultDAO competitionResultDAO;

    @Override
    public void init() throws ServletException {
        competitionResultDAO = new CompetitionResultDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form thêm kết quả
        // Đặt các thuộc tính cần thiết như danh sách cuộc thi, học sinh, lớp học để chọn lựa
        // Giả sử bạn đã có các DAO để lấy danh sách này
        // Ví dụ:
        // List<Competition> competitions = competitionDAO.getAllCompetitions();
        // List<Users> students = userDAO.getAllStudents();
        // List<Classs> classes = classDAO.getAllClasses();
        // request.setAttribute("competitions", competitions);
        // request.setAttribute("students", students);
        // request.setAttribute("classes", classes);
        // request.setAttribute("pageTitle", "Thêm Kết Quả Cuộc Thi");
        // request.setAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));

        // Chuyển tiếp đến trang JSP form thêm kết quả
        request.getRequestDispatcher("/jsp/addCompetitionResult.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý thêm kết quả mới
        // Lấy thông tin từ form
        String competitionIdParam = request.getParameter("competitionId");
        String userIdParam = request.getParameter("userId");
        String classIdParam = request.getParameter("classId");
        String scoreParam = request.getParameter("score");

        // Kiểm tra và chuyển đổi dữ liệu
        if (competitionIdParam == null || userIdParam == null || classIdParam == null || scoreParam == null ||
                competitionIdParam.isEmpty() || userIdParam.isEmpty() || classIdParam.isEmpty() || scoreParam.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            request.getRequestDispatcher("/jsp/addCompetitionResult.jsp").forward(request, response);
            return;
        }

        try {
            int competitionId = Integer.parseInt(competitionIdParam);
            int userId = Integer.parseInt(userIdParam);
            int classId = Integer.parseInt(classIdParam);
            float score = Float.parseFloat(scoreParam);
            int timeTaken = Integer.parseInt(request.getParameter("timeTaken"));

            // Tạo đối tượng CompetitionResult
            CompetitionResult cr = new CompetitionResult();
            cr.setCompetitionId(competitionId);
            cr.setUserId(userId);
            cr.setClassId(classId);
            cr.setScore(score);
            cr.setTimeTaken(timeTaken);
            cr.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // Thêm vào cơ sở dữ liệu
            boolean success = competitionResultDAO.insertCompetitionResult(cr);

            if (success) {
                // Redirect hoặc hiển thị thông báo thành công
                response.sendRedirect(request.getContextPath() + "/CompetitionResultsServlet?competitionId=" + competitionId);
            } else {
                request.setAttribute("errorMessage", "Đã xảy ra lỗi khi thêm kết quả.");
                request.getRequestDispatcher("/jsp/addCompetitionResult.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập không hợp lệ.");
            request.getRequestDispatcher("/jsp/addCompetitionResult.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi thêm kết quả.");
            request.getRequestDispatcher("/jsp/addCompetitionResult.jsp").forward(request, response);
        }
    }
}
