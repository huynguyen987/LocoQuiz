<%@ page import="entity.Users" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<!-- Header Section -->
<header>
    <h1>Add New User</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
        </ul>
    </nav>
</header>

<!-- Hiển thị thông báo nếu có -->
<%
    String message = request.getParameter("message");
    String error = request.getParameter("error");
%>
<% if (message != null && !message.isEmpty()) { %>
<div class="success-message"><%= message %></div>
<% } %>
<% if (error != null && !error.isEmpty()) { %>
<div class="error-message"><%= error %></div>
<% } %>

<!-- Form Thêm Người Dùng Mới -->
<div class="add-user-form">
    <form action="${pageContext.request.contextPath}/UserServlet" method="post">
        <input type="hidden" name="action" value="addUser">

        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="role_id">Role:</label>
            <select id="role_id" name="role_id" required>
                <option value="">--Select Role--</option>
                <option value="1">Teacher</option>
                <option value="2">Student</option>
                <option value="3">Admin</option>
            </select>
        </div>

        <div class="form-group">
            <label for="gender">Gender:</label>
            <select id="gender" name="gender">
                <option value="">--Select Gender--</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
                <option value="Prefer not to say">Prefer not to say</option>
            </select>
        </div>

        <!-- Bạn có thể thêm các trường như avatar, feature_face nếu cần -->

        <div class="form-actions">
            <button type="submit">Add User</button>
            <a href="${pageContext.request.contextPath}/jsp/manage_users.jsp" class="button">Cancel</a>
        </div>
    </form>
</div>

<!-- Footer Section -->
<footer>
    <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
