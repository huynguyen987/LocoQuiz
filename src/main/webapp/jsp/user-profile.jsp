<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Base64" %>

<%
    // Kiểm tra nếu người dùng đã đăng nhập cho 3 role (admin, teacher, student)
    session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");
    if (currentUser == null
            || (currentUser.getRole_id() != Users.ROLE_STUDENT
            && currentUser.getRole_id() != Users.ROLE_TEACHER
            && currentUser.getRole_id() != Users.ROLE_ADMIN)) {
        response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
        return;
    }

    // Tạo một instance của UsersDAO
    UsersDAO userDAO = new UsersDAO();
    Users user = userDAO.getUserById(currentUser.getId());
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - QuizLoco</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap">
    <link rel="stylesheet" href="../css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<div class="wrapper">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <h2><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h2>
        </div>
        <nav class="sidebar-nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/jsp/profile.jsp" class="active"><i class="fas fa-user"></i> Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/dashboard.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/quizzes.jsp"><i class="fas fa-file-alt"></i> Quizzes</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/settings.jsp"><i class="fas fa-cog"></i> Settings</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/logout.jsp"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <div class="main-content">
        <header class="main-header">
            <h1>User Profile</h1>
        </header>
        <main>
            <div class="profile-container">
                <div class="profile-avatar-section">
                    <div class="avatar-wrapper">
                        <%
                            byte[] avatarBytes = user.getAvatar();
                            if (avatarBytes != null && avatarBytes.length > 0) {
                                String base64Image = Base64.getEncoder().encodeToString(avatarBytes);
                        %>
                        <img src="data:image/jpeg;base64,<%= base64Image %>" alt="User Avatar" class="avatar">
                        <% } else { %>
                        <img src="../img/avt.jpg" alt="Default Avatar" class="avatar">
                        <% } %>
                        <a href="upload-avatar.jsp" class="btn-update-avatar"><i class="fas fa-camera"></i> Update Avatar</a>
                    </div>
                </div>

                <div class="profile-details-section">
                    <div class="details-header">
                        <h2><%= user.getUsername() %></h2>
                        <p class="role"><%= user.getRoleName() %></p>
                    </div>
                    <div class="details-content">
                        <p><strong>Email:</strong> <%= user.getEmail() %></p>
                        <p><strong>Gender:</strong> <%= user.getGender() != null ? user.getGender() : "N/A" %></p>
                        <p><strong>Member Since:</strong> <%= user.getCreated_at() != null ? user.getCreated_at() : "N/A" %></p>
                    </div>
                    <div class="details-actions">
                        <a href="${pageContext.request.contextPath}/jsp/edit-profile.jsp" class="btn btn-edit"><i class="fas fa-edit"></i> Edit Profile</a>
                        <a href="${pageContext.request.contextPath}/jsp/change-password.jsp" class="btn btn-password"><i class="fas fa-key"></i> Change Password</a>
                    </div>
                </div>
            </div>

            <div class="activity-container">
                <h2>Recent Activity</h2>
                <ul class="activity-list">
                    <li>
                        <i class="fas fa-check-circle"></i>
                        Attempted <strong>"Math Quiz"</strong> on <span><%= new Timestamp(System.currentTimeMillis()) %></span>
                    </li>
                    <li>
                        <i class="fas fa-star"></i>
                        Scored <strong>85%</strong> on <strong>"Science Quiz"</strong> on <span><%= new Timestamp(System.currentTimeMillis() - 86400000) %></span>
                    </li>
                    <li>
                        <i class="fas fa-plus-circle"></i>
                        Created <strong>"History Quiz"</strong> on <span><%= new Timestamp(System.currentTimeMillis() - 172800000) %></span>
                    </li>
                </ul>
            </div>
        </main>
        <footer>
            <p>&copy; 2024 QuizLoco. All rights reserved.</p>
        </footer>
    </div>
</div>

<script src="../js/script.js"></script>
</body>
</html>
