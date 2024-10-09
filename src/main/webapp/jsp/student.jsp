<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    // Redirect to login if not authenticated or not a student
    if (username == null || !"student".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
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
            <li><a href="<%= request.getContextPath() %>/jsp/student.jsp">Dashboard</a></li>
            <li><a href="<%= request.getContextPath() %>/jsp/my-classes.jsp">My Classes</a></li>
            <li><a href="<%= request.getContextPath() %>/jsp/recent-quizzes.jsp">Recent Quizzes</a></li>
            <li><a href="<%= request.getContextPath() %>/jsp/progress.jsp">Progress</a></li>
            <!-- Add other student-specific links here -->
        </ul>
    </div>
</div>

<!-- Header -->
<header>
    <div class="container">
        <!-- Logo -->
        <a href="<%= request.getContextPath() %>/jsp/student.jsp" class="logo">QuizLoco</a>
        <!-- Navigation Menu -->
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/jsp/student.jsp">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/jsp/my-classes.jsp">My Classes</a></li>
                <li><a href="<%= request.getContextPath() %>/jsp/recent-quizzes.jsp">Recent Quizzes</a></li>
                <li><a href="<%= request.getContextPath() %>/jsp/progress.jsp">Progress</a></li>
            </ul>
        </nav>

        <!-- Search Container -->
        <div class="search-container">
            <input type="text" id="liveSearch" placeholder="Search quizzes..." aria-label="Search quizzes">
            <div id="searchResults" class="search-results"></div>
        </div>

        <!-- User Info -->
        <div class="auth-links">
            <div class="user-info">
                <a href="<%= request.getContextPath() %>/jsp/user-profile.jsp" class="user-profile">
                    <i class="fas fa-user-circle"></i>
                    <span class="username">Welcome, <%= username %></span>
                </a>
                <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn-logout">Logout</a>
            </div>
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
</header>

<!-- Main Content -->
<main>
    <!-- Student Dashboard -->
    <section id="dashboard" class="dashboard">
        <div class="container">
            <h1>Welcome, <%= username %>!</h1>
            <p>Here's your student dashboard where you can access your classes, quizzes, and track your progress.</p>

            <!-- My Classes Section -->
            <section id="my-classes" class="my-classes">
                <h2>My Classes</h2>
                <!-- Fetch and display the classes the student is enrolled in -->
                <!-- Placeholder content -->
                <p>You are currently enrolled in the following classes:</p>
                <ul>
                    <li>Mathematics 101</li>
                    <li>Science 201</li>
                    <!-- Replace with dynamic content -->
                </ul>
            </section>

            <!-- Recent Quizzes Section -->
            <section id="recent-quizzes" class="recent-quizzes">
                <h2>Recent Quizzes</h2>
                <!-- Fetch and display recent quizzes for the student -->
                <!-- Placeholder content -->
                <div class="quiz-list">
                    <div class="quiz-item">
                        <h3>Algebra Basics</h3>
                        <p>Last Attempt: 85%</p>
                        <a href="<%= request.getContextPath() %>/jsp/quiz-details.jsp?id=1" class="btn-quiz">Retake Quiz</a>
                    </div>
                    <!-- Repeat for other quizzes -->
                </div>
            </section>

            <!-- Progress Tracking Section -->
            <section id="progress-tracking" class="progress-tracking">
                <h2>Your Progress</h2>
                <!-- Display charts or statistics about student's progress -->
                <!-- Placeholder content -->
                <p>Your overall average score is 88%</p>
                <!-- You can integrate chart libraries like Chart.js here -->
            </section>
        </div>
    </section>
</main>

<!-- Footer -->
<footer>
    <div class="container">
        <!-- Reuse the footer content from your index.jsp -->
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/student.js"></script>
</body>
</html>
