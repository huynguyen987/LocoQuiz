package Servlet;

import dao.UsersDAO;
import entity.Users;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EditProfileServlet")
public class EditProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại, không tạo session mới nếu không tồn tại
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");

        // Lấy các thông tin mới từ form
        String newUsername = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");

        UsersDAO userDAO = new UsersDAO();
        try {
            // Lấy người dùng hiện tại từ cơ sở dữ liệu
            Users user = userDAO.getUserByUsername(currentUser.getUsername());

            if (user != null) {
                // Cập nhật thông tin người dùng
                user.setUsername(newUsername);
                user.setEmail(email);
                user.setGender(gender);

                // Nếu bạn muốn cập nhật mật khẩu ở đây, thêm các bước xử lý mật khẩu mới

                // Cập nhật vào cơ sở dữ liệu
                boolean isUpdated = userDAO.updateUser(user);

                if (isUpdated) {
                    // Cập nhật lại đối tượng user trong session
                    session.setAttribute("user", user);

                    // Thiết lập thông báo thành công
                    request.setAttribute("success", "Profile updated successfully.");
                } else {
                    // Thiết lập thông báo lỗi
                    request.setAttribute("error", "Unable to update profile. Please try again.");
                }
            } else {
                // Nếu không tìm thấy người dùng
                request.setAttribute("error", "User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Thiết lập thông báo lỗi khi có ngoại lệ
            request.setAttribute("error", "Something went wrong.");
        }

        // Chuyển tiếp lại trang edit-profile.jsp để hiển thị thông báo và cập nhật thông tin
        request.getRequestDispatcher("/jsp/edit-profile.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra session và người dùng
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Chuyển hướng đến trang edit-profile.jsp
        response.sendRedirect(request.getContextPath() + "/jsp/edit-profile.jsp");
    }
}
