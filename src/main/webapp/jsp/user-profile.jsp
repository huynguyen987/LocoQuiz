<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Base64" %>

<%
    // Kiểm tra nếu người dùng đã đăng nhập
    String username = (String) session.getAttribute("username");
    if (username == null) {
        // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        response.sendRedirect("login.jsp");
        return;
    }

    // Tạo một instance của UsersDAO
    UsersDAO userDAO = new UsersDAO();

    // Lấy thông tin người dùng từ cơ sở dữ liệu bằng username
    Users user = null;
    try {
        user = userDAO.getUserByUsername(username);
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (user == null) {
        // Xử lý trường hợp không tìm thấy người dùng
        out.println("<p>User not found.</p>");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - QuizLoco</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap">
    <link rel="stylesheet" href="../css/profile.css">
</head>

<body>
<header>
    <div class="container">
        <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>

        <div class="auth-links">
            <a href="logout.jsp" class="btn-logout">Logout</a>
        </div>
    </div>
</header>

<main>
    <div class="profile-container">
        <div class="profile-header">
            <h1>User Profile</h1>
        </div>
        <div class="profile-content">
            <div class="profile-avatar">
                <%
                    byte[] avatarBytes = user.getAvatar();
                    if (avatarBytes != null && avatarBytes.length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(avatarBytes);
                %>
                <img src="data:image/jpeg;base64,<%= base64Image %>" alt="User Avatar" class="avatar">
                <% } else { %>
                <img src="../img/avt.jpg" alt="Default Avatar" class="avatar">
                <% } %>
                <a href="upload-avatar.jsp" class="btn-avatar">Update Avatar</a>
            </div>

            <div class="profile-actions">
                <a href="${pageContext.request.contextPath}/jsp/edit-profile.jsp" class="btn-edit">Edit Profile</a>
                <a href="${pageContext.request.contextPath}/jsp/change-password.jsp" class="btn-password">Change Password</a>
            </div>

            <div class="profile-details">
                <p><strong>Username:</strong> <%= user.getUsername() %></p>
                <p><strong>Email:</strong> <%= user.getEmail() %></p>
                <p><strong>Gender:</strong> <%= user.getGender() != null ? user.getGender() : "N/A" %></p>
                <p><strong>Created At:</strong> <%= user.getCreated_at() != null ? user.getCreated_at() : "N/A" %></p>
            </div>
        </div>

        <div class="profile-activity">
            <h2>Recent Activity</h2>
            <!-- Bạn có thể thêm logic ở đây để hiển thị hoạt động gần đây của người dùng -->
            <ul>
                <li>Attempted "Math Quiz" on <%= new Timestamp(System.currentTimeMillis()) %></li>
                <li>Scored 85% on "Science Quiz" on <%= new Timestamp(System.currentTimeMillis() - 86400000) %></li>
                <li>Created "History Quiz" on <%= new Timestamp(System.currentTimeMillis() - 172800000) %></li>
            </ul>
        </div>
    </div>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<script src="../js/script.js"></script>
</body>
</html>
