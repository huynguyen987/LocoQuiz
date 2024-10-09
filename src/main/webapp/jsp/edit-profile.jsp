<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>

<%
    // Kiểm tra người dùng đã đăng nhập chưa
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath()+"/jsp/login.jsp");
        return;
    }

    // Lấy thông tin người dùng hiện tại
    UsersDAO userDAO = new UsersDAO();
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edit-profile.css">
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
    <div class="container">
        <div class="profile-container">
            <div class="profile-header">
                <h1>Edit Profile</h1>
            </div>
            <div class="edit-profile-form">
                <% if (error != null) { %>
                <div class="error-message"><%= error %></div>
                <% } %>
                <% if (success != null) { %>
                <div class="success-message"><%= success %></div>
                <% } %>
                <form action="${pageContext.request.contextPath}/EditProfileServlet" method="post">
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
                        <select id="gender" name="gender">
                            <option value="male" <%= "male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>
                            <option value="female" <%= "female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>
                            <option value="other" <%= "other".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Other</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Update Profile" class="btn-submit">
                    </div>
                </form>
            </div>
            <div class="profile-actions">
                <a href="change-password.jsp" class="btn-submit">Change Password</a>
                <a href="upload-avatar.jsp" class="btn-submit">Update Avatar</a>
            </div>
        </div>
    </div>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

</body>
</html>
