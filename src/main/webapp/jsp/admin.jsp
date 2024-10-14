<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ page import="entity.Users, dao.UsersDAO" %>--%>
<%--<%@ page import="java.sql.SQLException" %>--%>

<%--<%--%>
<%--    // Kiểm tra nếu user không phải là admin--%>
<%--    session = request.getSession(false);--%>
<%--    Users currentUser = null;--%>
<%--    if (session != null) {--%>
<%--        currentUser = (Users) session.getAttribute("user");--%>
<%--    }--%>
<%--    if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRoleName())) {--%>
<%--        response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");--%>
<%--        return;--%>
<%--    }--%>

<%--    // Lấy thông tin người dùng hiện tại từ cơ sở dữ liệu để đảm bảo thông tin mới nhất--%>
<%--    Users user = null;--%>
<%--    try {--%>
<%--        user = new UsersDAO().getUserById(currentUser.getId());--%>
<%--    } catch (SQLException | ClassNotFoundException e) {--%>
<%--        throw new ServletException(e);--%>
<%--    }--%>

<%--    // Lấy tham số message từ URL để hiển thị thông báo--%>
<%--    String message = request.getParameter("message");--%>
<%--%>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <title>Admin Dashboard - QuizLoco</title>--%>
<%--    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">--%>
<%--    <!-- Font Awesome -->--%>
<%--    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">--%>
<%--</head>--%>
<%--<body>--%>
<%--<!-- Header -->--%>
<%--<header>--%>
<%--    <div class="container">--%>
<%--        <a href="<%= request.getContextPath() %>/jsp/admin.jsp" class="logo">QuizLoco</a>--%>
<%--        <nav>--%>
<%--            <ul>--%>
<%--                <li><a href="<%= request.getContextPath() %>/jsp/admin.jsp">Dashboard</a></li>--%>
<%--                <li><a href="<%= request.getContextPath() %>/jsp/admin.jsp?action=createAdmin">Create Admin</a></li>--%>
<%--                <li><a href="<%= request.getContextPath() %>/ManageUsersServlet"><i class="fas fa-users"></i> Manage Users</a></li>--%>
<%--                <li><a href="<%= request.getContextPath() %>/ManageClassesServlet"><i class="fas fa-chalkboard"></i> Manage Classes</a></li>--%>
<%--                <li><a href="<%= request.getContextPath() %>/ManageQuizzesServlet"><i class="fas fa-question-circle"></i> Manage Quizzes</a></li>--%>
<%--            </ul>--%>
<%--        </nav>--%>
<%--        <div class="user-info">--%>
<%--            <span>Welcome, <%= currentUser.getUsername() %></span>--%>
<%--            <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</header>--%>

<%--<!-- Sidebar -->--%>
<%--<aside class="sidebar">--%>
<%--    <ul>--%>
<%--        <li><a href="<%= request.getContextPath() %>/jsp/admin.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>--%>
<%--        <li><a href="<%= request.getContextPath() %>/jsp/admin.jsp?action=createAdmin"><i class="fas fa-plus"></i> Create Admin</a></li>--%>
<%--        <li><a href="<%= request.getContextPath() %>/ManageUsersServlet"><i class="fas fa-users"></i> Manage Users</a></li>--%>
<%--        <li><a href="<%= request.getContextPath() %>/ManageClassesServlet"><i class="fas fa-chalkboard"></i> Manage Classes</a></li>--%>
<%--        <li><a href="<%= request.getContextPath() %>/ManageQuizzesServlet"><i class="fas fa-question-circle"></i> Manage Quizzes</a></li>--%>
<%--    </ul>--%>
<%--</aside>--%>

<%--<!-- Main Content -->--%>
<%--<main>--%>
<%--    <div class="dashboard-content">--%>
<%--        <!-- Display success or error messages -->--%>
<%--        <% if (message != null) { %>--%>
<%--        <% if ("Profile updated successfully.".equals(message)) { %>--%>
<%--        <div class="success-message">--%>
<%--            <%= message %>--%>
<%--        </div>--%>
<%--        <% } else { %>--%>
<%--        <div class="info-message">--%>
<%--            <%= message %>--%>
<%--        </div>--%>
<%--        <% } %>--%>
<%--        <% } %>--%>

<%--        <% if ("dashboard".equalsIgnoreCase(action)) { %>--%>
<%--        <!-- Dashboard View -->--%>
<%--        <h1>Admin Dashboard</h1>--%>
<%--        <!-- Dashboard Statistics -->--%>
<%--        <div class="stats-container">--%>
<%--            <!-- Thêm các thẻ thống kê tùy thuộc vào yêu cầu của bạn -->--%>
<%--        </div>--%>

<%--        <!-- Action Links -->--%>
<%--        <div class="action-links">--%>
<%--            <a href="<%= request.getContextPath() %>/jsp/admin.jsp?action=createAdmin" class="action-btn">--%>
<%--                <i class="fas fa-plus"></i> Create New Admin--%>
<%--            </a>--%>
<%--        </div>--%>

<%--        <!-- Manage Users Section -->--%>
<%--        <section class="manage-section">--%>
<%--            <h2>Manage Users</h2>--%>
<%--            <!-- Nội dung quản lý người dùng -->--%>
<%--        </section>--%>

<%--        <!-- Manage Classes Section -->--%>
<%--        <section class="manage-section">--%>
<%--            <h2>Manage Classes</h2>--%>
<%--            <!-- Nội dung quản lý lớp học -->--%>
<%--        </section>--%>

<%--        <!-- Manage Quizzes Section -->--%>
<%--        <section class="manage-section">--%>
<%--            <h2>Manage Quizzes</h2>--%>
<%--            <!-- Nội dung quản lý quiz -->--%>
<%--        </section>--%>
<%--        <% } else if ("createAdmin".equalsIgnoreCase(action)) { %>--%>
<%--        <!-- Create Admin View -->--%>
<%--        <h1>Create New Admin</h1>--%>
<%--        <form action="<%=request.getContextPath()%>/CreateAdminServlet" method="post" class="form-container">--%>
<%--            <label for="username">Username:</label>--%>
<%--            <input type="text" id="username" name="username" required>--%>

<%--            <label for="email">Email:</label>--%>
<%--            <input type="email" id="email" name="email" required>--%>

<%--            <label for="password">Password:</label>--%>
<%--            <input type="password" id="password" name="password" required>--%>

<%--            <label for="gender">Gender:</label>--%>
<%--            <select id="gender" name="gender" required>--%>
<%--                <option value="male" <%= "male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>--%>
<%--                <option value="female" <%= "female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>--%>
<%--                <option value="other" <%= "other".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Other</option>--%>
<%--            </select>--%>

<%--            <button type="submit" class="submit-btn">--%>
<%--                <i class="fas fa-plus"></i> Create Admin--%>
<%--            </button>--%>
<%--        </form>--%>
<%--        <% if ("createAdminError".equalsIgnoreCase(message)) { %>--%>
<%--        <p class="error-message">An error occurred while creating the admin. Please try again.</p>--%>
<%--        <% } else if ("createAdminSuccess".equalsIgnoreCase(message)) { %>--%>
<%--        <p class="success-message">Admin created successfully.</p>--%>
<%--        <% } %>--%>

<%--        <!-- Back to Dashboard Link -->--%>
<%--        <a href="<%= request.getContextPath() %>/jsp/admin.jsp" class="action-btn back-btn">--%>
<%--            <i class="fas fa-arrow-left"></i> Back to Dashboard--%>
<%--        </a>--%>
<%--        <% } else { %>--%>
<%--        <!-- Default or Unknown Action -->--%>
<%--        <p>Invalid action specified.</p>--%>
<%--        <!-- Optionally redirect to dashboard -->--%>
<%--        <a href="<%= request.getContextPath() %>/jsp/admin.jsp" class="action-btn back-btn">--%>
<%--            <i class="fas fa-arrow-left"></i> Back to Dashboard--%>
<%--        </a>--%>
<%--        <% } %>--%>
<%--    </div>--%>
<%--</main>--%>

<%--<!-- Footer -->--%>
<%--<footer>--%>
<%--    <div class="container">--%>
<%--        <p>&copy; 2024 QuizLoco. All rights reserved.</p>--%>
<%--    </div>--%>
<%--</footer>--%>

<%--<!-- JavaScript -->--%>
<%--<script src="<%= request.getContextPath() %>/js/admin.js"></script>--%>
<%--</body>--%>
<%--</html>--%>
