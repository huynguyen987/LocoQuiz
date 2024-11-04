package Servlet;

import dao.UsersDAO;
import entity.Users;
import java.io.IOException;
import Module.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private UsersDAO usersDAO;

    @Override
    public void init() {
        usersDAO = new UsersDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");
        int userId = -1;

        // Kiểm tra và chuyển đổi userId nếu cần thiết
        if (userIdParam != null && !userIdParam.isEmpty()) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                // Log lỗi và chuyển hướng với thông báo lỗi
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?error=" + java.net.URLEncoder.encode("ID người dùng không hợp lệ.", "UTF-8"));
                return;
            }
        }

        try {
            switch (action) {
                case "addUser":
                    addUser(request, response);
                    break;
                case "updateRole":
                    updateRole(userId);
                    break;
                case "toggleStatus":
                    toggleRole(userId);
                    break;
                case "deleteUser":
                    deleteUser(userId);
                    break;
                default:
                    // Hành động không hợp lệ
                    response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?error=" + java.net.URLEncoder.encode("Hành động không hợp lệ.", "UTF-8"));
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?error=" + java.net.URLEncoder.encode("Đã xảy ra lỗi khi xử lý yêu cầu.", "UTF-8"));
            return;
        }

        // Chuyển hướng trở lại trang admin với thông báo thành công
        response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?message=" + java.net.URLEncoder.encode("Thao tác thành công.", "UTF-8"));
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String roleIdParam = request.getParameter("role_id");
        String gender = request.getParameter("gender");

        // Kiểm tra dữ liệu bắt buộc
        if (username == null || password == null || email == null || roleIdParam == null ||
                username.isEmpty() || password.isEmpty() || email.isEmpty() || roleIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?error=" + java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin.", "UTF-8"));
            return;
        }

        int role_id;
        try {
            role_id = Integer.parseInt(roleIdParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp?error=" + java.net.URLEncoder.encode("Vai trò không hợp lệ.", "UTF-8"));
            return;
        }

        // Hash mật khẩu trước khi lưu trữ
        String hashedPassword = HashPassword.hashPassword(password);

        // Tạo đối tượng Users
        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setEmail(email);
        newUser.setRole_id(role_id);
        newUser.setGender(gender);
        newUser.setAvatar(null); // Có thể cập nhật sau
        newUser.setFeature_face(null); // Có thể cập nhật sau

        // Thêm người dùng vào DB
        usersDAO.addUser(newUser);
    }

    private void updateRole(int userId) throws Exception {
        Users user = usersDAO.getUserById(userId);
        if (user != null) {
            int newRoleId;
            if (user.getRole_id() == Users.ROLE_ADMIN) {
                newRoleId = Users.ROLE_TEACHER;
            } else if (user.getRole_id() == Users.ROLE_TEACHER) {
                newRoleId = Users.ROLE_STUDENT;
            } else {
                newRoleId = Users.ROLE_ADMIN; // Mặc định thành Admin nếu không xác định
            }
            user.setRole_id(newRoleId);
            usersDAO.updateUser(user);
        } else {
            throw new Exception("Người dùng không tồn tại.");
        }
    }

    private void toggleRole(int userId) throws Exception {
        Users user = usersDAO.getUserById(userId);
        if (user != null) {
            int currentRole = user.getRole_id();
            int newRole;

            switch (currentRole) {
                case Users.ROLE_TEACHER:
                    newRole = Users.ROLE_STUDENT;
                    break;
                case Users.ROLE_STUDENT:
                    newRole = Users.ROLE_ADMIN;
                    break;
                case Users.ROLE_ADMIN:
                    newRole = Users.ROLE_TEACHER;
                    break;
                default:
                    newRole = Users.ROLE_STUDENT; // Mặc định thành Student nếu không xác định
            }

            user.setRole_id(newRole);
            usersDAO.updateUser(user);
        } else {
            throw new Exception("Người dùng không tồn tại.");
        }
    }

    private void deleteUser(int userId) throws Exception {
        usersDAO.deleteUser(userId);
    }

}