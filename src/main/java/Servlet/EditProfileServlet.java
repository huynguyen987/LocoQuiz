package Servlet;

import dao.UsersDAO;
import entity.Users;
import java.io.IOException;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.util.Base64;

@WebServlet("/EditProfileServlet")
@MultipartConfig(maxFileSize = 16177215) // Giới hạn kích thước file upload: 16MB
public class EditProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public EditProfileServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra session và người dùng đã đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");

        // Thiết lập mã hóa để đọc được tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // Lấy các thông tin mới từ form
        String newUsername = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");

        // Lấy dữ liệu ảnh đã cắt từ input ẩn
        String croppedImageData = request.getParameter("croppedImageData");
        byte[] avatarBytes = null;

        if (croppedImageData != null && !croppedImageData.isEmpty()) {
            // Dữ liệu Base64 có dạng "data:image/png;base64,...."
            try {
                String base64Data = croppedImageData.split(",")[1];
                avatarBytes = Base64.getDecoder().decode(base64Data);
                System.out.println("Avatar bytes length: " + avatarBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Invalid image data.");
                request.getRequestDispatcher("/jsp/edit-profile.jsp").forward(request, response);
                return;
            }
        }

        UsersDAO usersDAO = new UsersDAO();
        try {
            // Tạo đối tượng Users mới với các thông tin cần cập nhật
            Users userToUpdate = new Users();
            userToUpdate.setId(currentUser.getId());
            userToUpdate.setUsername(newUsername);
            userToUpdate.setEmail(email);
            userToUpdate.setGender(gender);
            userToUpdate.setAvatar(avatarBytes); // Cập nhật avatar nếu có

            boolean isUpdated = usersDAO.updateUserForUploadImage(userToUpdate);

            if (isUpdated) {
                // Cập nhật lại đối tượng user trong session
                if (avatarBytes != null) {
                    currentUser.setAvatar(avatarBytes);
                }
                currentUser.setUsername(newUsername);
                currentUser.setEmail(email);
                currentUser.setGender(gender);
                session.setAttribute("user", currentUser);
                System.out.println("Profile updated successfully.");

                // Thiết lập thông báo thành công
                request.setAttribute("success", "Profile updated successfully.");
            } else {
                // Thiết lập thông báo lỗi
                request.setAttribute("error", "Unable to update profile. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Thiết lập thông báo lỗi khi có ngoại lệ
            request.setAttribute("error", "Error updating profile.");
        }

        // Chuyển tiếp lại trang edit-profile.jsp để hiển thị thông báo và cập nhật thông tin
        request.getRequestDispatcher("/jsp/edit-profile.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra session và người dùng đã đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // Chuyển hướng đến trang edit-profile.jsp
        response.sendRedirect(request.getContextPath() + "/jsp/edit-profile.jsp");
    }
}
