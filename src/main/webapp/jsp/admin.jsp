<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="entity.Users" %>
<%@ page import="entity.quiz" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%
    // Initialize DAOs
    UsersDAO usersDAO = new UsersDAO();
    QuizDAO quizDAO = new QuizDAO();

    // Fetch required data
    List<Users> usersList;
    try {
        usersList = usersDAO.getAllUsers();
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    int totalUsers = usersList.size();

    // Fetch quizzes with default parameters if needed
    List<quiz> quizList;
    try {
        quizList = quizDAO.getAllQuiz();
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    int totalQuizzes = quizList != null ? quizList.size() : 0;

    // Limiting to recent activities
    List<Users> recentUsers = usersList.subList(0, Math.min(5, totalUsers));
    List<quiz> recentQuizzes = quizList != null ? quizList.subList(0, Math.min(5, totalQuizzes)) : null;
%>

<div class="container">
    <!-- Sidebar Navigation -->
    <aside class="sidebar">
        <h2>Admin Panel</h2>
        <nav>
            <ul>
                <li class="active"><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
                <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Header Section -->
        <header>
            <h1>Welcome to the Admin Dashboard</h1>
        </header>

        <!-- Display Messages -->
        <%
            String message = request.getParameter("message");
            String error = request.getParameter("error");
        %>
        <% if (message != null && !message.isEmpty()) { %>
        <div class="alert success"><%= message %></div>
        <% } %>
        <% if (error != null && !error.isEmpty()) { %>
        <div class="alert error"><%= error %></div>
        <% } %>

        <!-- Dashboard Overview -->
        <section class="overview">
            <div class="card">
                <h3>Total Users</h3>
                <p><%= totalUsers %></p>
            </div>
            <div class="card">
                <h3>Total Quizzes</h3>
                <p><%= totalQuizzes %></p>
            </div>
        </section>

        <!-- Recent Activities -->
        <section class="recent-activities">
            <div class="activity">
                <h3>Recent Users</h3>
                <ul>
                    <% for (Users user : recentUsers) { %>
                    <li>
                        <strong><%= user.getUsername() %></strong> -
                        <span>Role:
                            <%=
                            user.getRole_id() == Users.ROLE_ADMIN ? "Admin" :
                                    (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                            %>
                        </span>
                        <span>Status:
                            <%=
                            user.getRole_id() == Users.ROLE_ADMIN ? "Active" :
                                    (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                            %>
                        </span>
                    </li>
                    <% } %>
                </ul>
            </div>
            <div class="activity">
                <h3>Recent Quizzes</h3>
                <ul>
                    <% if (recentQuizzes != null && !recentQuizzes.isEmpty()) { %>
                    <% for (quiz q : recentQuizzes) { %>
                    <li>
                        <strong><%= q.getQuizTitle() %></strong> by
                        <span>Creator ID: <%= q.getCreatorId() %></span>
                    </li>
                    <% } %>
                    <% } else { %>
                    <li>No recent quizzes available.</li>
                    <% } %>
                </ul>
            </div>
        </section>
    </main>
</div>

<!-- Footer Section -->
<footer>
    <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
