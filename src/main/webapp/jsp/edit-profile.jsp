<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.SQLException" %>

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
    <style>
        /* Thêm một số kiểu cơ bản cho thông báo và nút */
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

        .btn-back-dashboard {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .btn-back-dashboard:hover {
            background-color: #0056b3;
        }
    </style>
</head>

<body>
<header>
    <div class="container">
        <h1><a href="<%= request.getContextPath() %>/index.jsp" class="logo">QuizLoco</a></h1>

        <div class="auth-links">
            <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn-logout">Logout</a>
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
                <form action="<%= request.getContextPath() %>/EditProfileServlet" method="post">
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
                        <input type="submit" value="Update Profile" class="btn-submit">
                    </div>
                </form>
            </div>
            <div class="profile-actions">
                <a href="<%= request.getContextPath() %>/jsp/change-password.jsp" class="btn-submit">Change Password</a>
                <a href="<%= request.getContextPath() %>/jsp/upload-avatar.jsp" class="btn-submit">Update Avatar</a>
            </div>
            <div class="back-to-dashboard" style="margin-top: 20px;">
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
                <form action="<%= dashboardURL %>" method="get">
                    <button type="submit" class="btn-back-dashboard">Back to Dashboard</button>
                </form>
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
