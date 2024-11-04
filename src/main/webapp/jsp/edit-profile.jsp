<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Base64" %>

<%
    // Kiểm tra người dùng đã đăng nhập chưa
    session = request.getSession(false);
    Users currentUser = null;
    if (session != null) {
        currentUser = (Users) session.getAttribute("user");
    }
    if (currentUser == null
            || (!"student".equalsIgnoreCase(currentUser.getRoleName())
            && !"teacher".equalsIgnoreCase(currentUser.getRoleName())
            && !"admin".equalsIgnoreCase(currentUser.getRoleName()))) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }

    // Lấy thông tin người dùng hiện tại từ cơ sở dữ liệu để đảm bảo thông tin mới nhất
    Users user = null;
    try {
        user = new UsersDAO().getUserById(currentUser.getId());
    } catch (SQLException | ClassNotFoundException e) {
        throw new ServletException(e);
    }

    // Kiểm tra nếu có thông báo lỗi hoặc thành công từ servlet
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - QuizLoco</title>
    <!-- Sử dụng context path để liên kết CSS đúng cách -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/edit-profile.css">

    <!-- Cropper.js CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.css" integrity="sha512-..." crossorigin="anonymous" referrerpolicy="no-referrer" />

    <!-- Custom CSS cho Cropper và cải thiện giao diện -->
    <style>
        /* Cấu trúc Flex container */
        .profile-container {
            display: flex;
            flex-wrap: wrap;
            gap: 30px;
            margin-top: 30px;
        }

        /* Phần thông tin người dùng */
        .edit-profile-form {
            flex: 1 1 400px;
            background-color: #f9f9f9;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        /* Phần hiển thị avatar */
        .profile-avatar {
            flex: 0 0 200px;
            text-align: center;
        }

        .profile-avatar img {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #ddd;
        }

        /* Thông báo lỗi và thành công */
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
            border-radius: 5px;
        }

        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
        }

        /* Nút hành động */
        .btn-back-dashboard,
        .btn-submit {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        .btn-back-dashboard {
            background-color: #6c757d;
        }

        .btn-back-dashboard:hover {
            background-color: #5a6268;
        }

        .btn-submit {
            background-color: #28a745;
        }

        .btn-submit:hover {
            background-color: #218838;
        }

        /* Các kiểu CSS khác */
        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="email"],
        select,
        input[type="file"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ced4da;
            border-radius: 4px;
            transition: border-color 0.3s ease;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        select:focus,
        input[type="file"]:focus {
            border-color: #80bdff;
            outline: none;
        }

        /* Vùng hiển thị ảnh xem trước */
        .img-container {
            max-width: 100%;
            max-height: 400px;
            margin-bottom: 20px;
            display: none; /* Ẩn đi cho đến khi có ảnh */
            border: 1px solid #ced4da;
            border-radius: 8px;
            overflow: hidden;
        }

        .img-container img {
            width: 100%;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .profile-container {
                flex-direction: column;
                align-items: center;
            }

            .profile-avatar {
                flex: 0 0 auto;
            }
        }
    </style>
</head>

<body>
<header>
    <div class="container">
        <h1><a href="<%= request.getContextPath() %>/index.jsp" class="logo">QuizLoco</a></h1>

        <div class="auth-links">
            <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn-back-dashboard">Logout</a>
        </div>
    </div>
</header>

<main>
    <div class="container">
        <div class="profile-container">
            <div class="edit-profile-form">
                <div class="profile-header">
                    <h2>Edit Profile</h2>
                </div>
                <% if (error != null) { %>
                <div class="error-message"><%= error %></div>
                <% } %>
                <% if (success != null) { %>
                <div class="success-message"><%= success %></div>
                <% } %>
                <form action="<%= request.getContextPath() %>/EditProfileServlet" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" value="<%= user.getUsername() %>" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="<%= user.getEmail() %>" required>
                    </div>
                    <div class="form-group">
                        <label for="gender">Gender:</label>
                        <select id="gender" name="gender" required>
                            <option value="male" <%= "male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>
                            <option value="female" <%= "female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>
                            <option value="other" <%= "other".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Other</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="avatar">Upload New Avatar (optional):</label>
                        <input type="file" id="avatar" name="avatar" accept="image/*">
                    </div>

                    <!-- Vùng hiển thị ảnh xem trước -->
                    <div class="img-container">
                        <img id="preview-image" src="#">
                    </div>

                    <div class="form-group">
                        <input type="hidden" id="croppedImageData" name="croppedImageData">
                        <button type="submit" class="btn-submit">Update Profile</button>
                    </div>
                </form>
            </div>

            <div class="profile-avatar">
                <%
                    if (user.getAvatar() != null && user.getAvatar().length > 0) {
                        String base64Avatar = Base64.getEncoder().encodeToString(user.getAvatar());
                        String mimeType = "image/jpeg"; // Bạn có thể xác định loại MIME thực tế nếu cần
                        System.out.println("Displaying avatar for user ID: " + user.getId());
                        System.out.println("Base64 Avatar Length: " + base64Avatar.length());
                %>
                <img src="data:<%= mimeType %>;base64,<%= base64Avatar %>" alt="Avatar">
                <% } else { %>
                <p>No avatar available.</p>
                <% } %>
            </div>
        </div>

        <div class="profile-actions" style="text-align: center;">
            <a href="<%= request.getContextPath() %>/jsp/change-password.jsp" class="btn-submit">Change Password</a>
            <!-- Nếu bạn muốn cho phép upload avatar trực tiếp từ đây, bạn có thể bỏ qua liên kết này -->
            <!-- <a href="<%= request.getContextPath() %>/jsp/upload-avatar.jsp" class="btn-submit">Update Avatar</a> -->
        </div>

        <div class="back-to-dashboard" style="text-align: center; margin-top: 20px;">
            <%
                String role = user.getRoleName();
                String dashboardURL = "";
                if ("teacher".equalsIgnoreCase(role)) {
                    dashboardURL = request.getContextPath() + "/jsp/teacher.jsp";
                } else if ("student".equalsIgnoreCase(role)) {
                    dashboardURL = request.getContextPath() + "/jsp/student.jsp";
                } else if ("admin".equalsIgnoreCase(role)) {
                    dashboardURL = request.getContextPath() + "/jsp/admin.jsp";
                }
            %>
            <form action="<%= dashboardURL %>" method="get" style="display: inline;">
                <button type="submit" class="btn-back-dashboard">Back to Dashboard</button>
            </form>
        </div>
    </div>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<!-- Cropper.js JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.js" integrity="sha512-..." crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<!-- Custom JS -->
<script>
    // Biến lưu trữ đối tượng Cropper
    let cropper;

    // Lắng nghe sự kiện khi người dùng chọn tệp ảnh
    document.getElementById('avatar').addEventListener('change', function (e) {
        const imgContainer = document.querySelector('.img-container');
        const previewImage = document.getElementById('preview-image');
        const file = e.target.files[0];

        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                previewImage.src = event.target.result;
                imgContainer.style.display = 'block';

                // Khởi tạo Cropper.js sau khi ảnh đã được load
                if (cropper) {
                    cropper.destroy(); // Hủy bỏ cropper cũ nếu có
                }
                cropper = new Cropper(previewImage, {
                    aspectRatio: 1, // Tỉ lệ khung hình 1:1 cho avatar
                    viewMode: 1,
                    movable: true,
                    zoomable: true,
                    rotatable: true,
                    scalable: true,
                });
            };
            reader.readAsDataURL(file);
        } else {
            imgContainer.style.display = 'none';
            if (cropper) {
                cropper.destroy();
                cropper = null;
            }
        }
    });

    // Xử lý sự kiện submit form
    document.querySelector('form').addEventListener('submit', function (e) {
        if (cropper) {
            // Lấy dữ liệu ảnh đã cắt
            cropper.getCroppedCanvas().toBlob(function (blob) {
                // Chuyển đổi blob thành base64 để gửi qua form
                const reader = new FileReader();
                reader.onloadend = function () {
                    const base64data = reader.result;
                    document.getElementById('croppedImageData').value = base64data;

                    // Sau khi đã thiết lập dữ liệu, submit form
                    e.target.submit();
                };
                reader.readAsDataURL(blob);
            });
            // Ngăn chặn việc submit form ngay lập tức
            e.preventDefault();
        }
    });
</script>
</body>
</html>
