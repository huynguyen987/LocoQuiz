package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.UsersDAO;
import entity.Competition;
import entity.Users;
import entity.classs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClassDetailsStudentServlet", urlPatterns = {"/ClassDetailsStudentServlet"})
public class ClassDetailsStudentServlet extends HttpServlet {

    private ClassDAO classDAO;
    private CompetitionDAO competitionDAO;
    private UsersDAO usersDAO;

    @Override
    public void init() throws ServletException {
        classDAO = new ClassDAO();
        competitionDAO = new CompetitionDAO();
        usersDAO = new UsersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số classId từ request
        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?action=Classrooms&message=invalidClass");
            return;
        }

        int classId;
        try {
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?action=Classrooms&message=invalidClass");
            return;
        }

        // Lấy người dùng hiện tại từ session
        HttpSession session = request.getSession(false);
        Users currentUser = null;
        if (session != null) {
            currentUser = (Users) session.getAttribute("user");
        }

        if (currentUser == null) {
            // Người dùng chưa đăng nhập
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Kiểm tra xem sinh viên có thuộc lớp học này không
        try {
            boolean isEnrolled = classDAO.isStudentInClass(currentUser.getId(), classId);
            if (!isEnrolled) {
                request.setAttribute("message", "Bạn không thuộc lớp học này.");
                request.getRequestDispatcher("/jsp/message.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin lớp học
            classs classEntity = classDAO.getClassDetailsById(classId);
            if (classEntity == null) {
                request.setAttribute("message", "Lớp học không tồn tại.");
                request.getRequestDispatcher("/jsp/message.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách học sinh trong lớp
            List<Users> classmates = classDAO.getStudentsByClassId(classId);

            // Lấy danh sách các cuộc thi trong lớp
            List<Competition> assignedQuizzes = classDAO.getCompetitionsByClassId(classId);

            // Đặt các thuộc tính vào request
            request.setAttribute("classEntity", classEntity);
            request.setAttribute("classmates", classmates);
            request.setAttribute("assignedQuizzes", assignedQuizzes);

            // Chuyển tiếp tới trang JSP chi tiết lớp học
            System.out.println("currentUser who want to view class details: " + currentUser.getUsername());
            request.setAttribute("currentUser", currentUser);
            request.getRequestDispatcher("/jsp/classDetails.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi truy xuất dữ liệu.");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu cần xử lý POST, có thể triển khai ở đây
        doGet(request, response);
    }
}
