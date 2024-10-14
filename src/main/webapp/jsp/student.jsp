<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.SQLException" %>

<%
    // Kiểm tra nếu user không phải là student
    session = request.getSession(false);
    Users currentUser = null;
    if (session != null) {
        currentUser = (Users) session.getAttribute("user");
    }
    if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
        response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
        return;
    }

    // Lấy thông tin người dùng hiện tại từ cơ sở dữ liệu để đảm bảo thông tin mới nhất
    Users user = null;
    try {
        user = new UsersDAO().getUserById(currentUser.getId());
    } catch (SQLException | ClassNotFoundException e) {
        throw new ServletException(e);
    }

    // Lấy tham số message từ URL để hiển thị thông báo
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - QuizLoco</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/student.css">
    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
    <!-- Pass contextPath to JavaScript -->
    <script type="text/javascript">
        var contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<body id="body">
<!-- Sidebar -->
<div id="sidebar" class="sidebar">
    <div class="sidebar-content">
        <ul>
            <li>
                <a href="<%= request.getContextPath() %>/jsp/student.jsp">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/jsp/my-classes.jsp">
                    <i class="fas fa-chalkboard"></i>
                    <span>My Classes</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/jsp/progress.jsp">
                    <i class="fas fa-chart-line"></i>
                    <span>Progress</span>
                </a>
            </li>
            <!-- Logout Button -->
            <li>
                <a href="<%= request.getContextPath() %>/LogoutServlet">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<!-- Header -->
<header>
    <div class="container">
        <!-- Logo -->
        <a href="<%= request.getContextPath() %>/jsp/student.jsp" class="logo">QuizLoco</a>
        <!-- Search Container -->
        <div class="search-container">
            <input type="text" id="liveSearch" placeholder="Search quizzes..." aria-label="Search quizzes">
            <ul id="searchResults" class="search-results"></ul>
        </div>
        <!-- User Info -->
        <div class="auth-links">
            <!-- Notifications -->
            <div class="notifications">
                <i class="fas fa-bell"></i>
                <div class="notification-count">3</div>
                <!-- Notification Dropdown -->
                <div class="notification-dropdown">
                    <ul>
                        <li><a href="#">New assignment posted in Math</a></li>
                        <li><a href="#">Quiz results are out!</a></li>
                        <li><a href="#">Reminder: Science project due tomorrow</a></li>
                    </ul>
                </div>
            </div>
            <div class="user-profile">
                <i class="fas fa-user-circle"></i>
                <span class="username"><%= user.getUsername() %></span>
                <ul class="profile-menu">
                    <li><a href="<%= request.getContextPath() %>/jsp/user-profile.jsp">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/LogoutServlet">Logout</a></li>
                </ul>
            </div>
            <!-- Theme Toggle -->
            <div class="theme-toggle">
                <input type="checkbox" id="dark-mode-toggle" aria-label="Toggle dark mode">
                <label for="dark-mode-toggle">
                    <i class="fas fa-moon"></i>
                    <i class="fas fa-sun"></i>
                </label>
            </div>
        </div>
    </div>
</header>

<main>
    <!-- Student Dashboard -->
    <section id="dashboard" class="dashboard">
        <% if (message != null) { %>
        <div class="success-message"><%= message %></div>
        <% } %>
        <h1>Welcome, <%= user.getUsername() %>!</h1>
        <p>Access your classes, quizzes, and track your progress all in one place.</p>

        <!-- Grid of Cards -->
        <div class="grid grid-2">
            <!-- My Classes Card -->
            <div class="card">
                <h2>My Classes</h2>
                <p>View and manage your enrolled classes.</p>
                <a href="<%= request.getContextPath() %>/jsp/my-classes.jsp" class="button">Go to Classes</a>
            </div>

            <!-- Recent Quizzes Card -->
            <div class="card">
                <h2>Recent Quizzes</h2>
                <p>Continue where you left off.</p>
                <a href="<%= request.getContextPath() %>/jsp/recent-quizzes.jsp" class="button">View Quizzes</a>
            </div>

            <!-- Progress Card -->
            <div class="card">
                <h2>Your Progress</h2>
                <p>Check your learning progress and achievements.</p>
                <a href="<%= request.getContextPath() %>/jsp/progress.jsp" class="button">View Progress</a>
            </div>

            <!-- Announcements Card -->
            <div class="card">
                <h2>Announcements</h2>
                <p>Stay updated with the latest news.</p>
                <a href="<%= request.getContextPath() %>/jsp/announcements.jsp" class="button">View Announcements</a>
            </div>

            <!-- Take Quiz Card (Newly Added) -->
            <div class="card">
                <h2>Take Quiz</h2>
                <p>Participate in quizzes to test your knowledge.</p>
                <a href="<%= request.getContextPath() %>/AllQuizzesServlet" class="button">Take Quiz</a>
            </div>
        </div>
    </section>
</main>

<!-- Footer -->
<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/student.js"></script>
</body>
</html>
