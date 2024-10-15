<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - QuizLoco</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <!-- Pass contextPath to JavaScript -->
    <script type="text/javascript">
        var contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
    <div class="dashboard-content">
        <%
            // Session and user authentication
            session = request.getSession(false);
            currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }
            if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
                response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
                return;
            }

            // Fetch latest user info
            Users user = null;
            try {
                user = new UsersDAO().getUserById(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                throw new ServletException(e);
            }

            // Message for notifications
            String message = request.getParameter("message");
        %>

        <!-- Student Dashboard -->
        <section id="dashboard" class="dashboard">
            <% if (message != null) { %>
            <div class="success-message"><%= message %></div>
            <% } %>
            <h1>Welcome, <%= user.getUsername() %>!</h1>
            <p>Access your classes, quizzes, and track your progress all in one place.</p>

            <!-- Grid of Cards -->
            <div class="grid">
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

                <!-- Take Quiz Card -->
                <div class="card">
                    <h2>Take Quiz</h2>
                    <p>Participate in quizzes to test your knowledge.</p>
                    <a href="<%= request.getContextPath() %>/AllQuizzesServlet" class="button">Take Quiz</a>
                </div>
            </div>
        </section>
    </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
<script src="<%= request.getContextPath() %>/js/student.js"></script>
</body>
</html>
