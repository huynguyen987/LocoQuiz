<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="entity.Users" %>
<%@ page import="entity.quiz" %>
<%@ page import="java.sql.SQLException" %>
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
        quizList = quizDAO.getAllQuizzes(0, 10);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    int totalQuizzes = quizList != null ? quizList.size() : 0;

    // Limiting to recent activities
    List<Users> recentUsers = usersList.subList(0, Math.min(5, totalUsers));
    List<quiz> recentQuizzes = quizList != null ? quizList.subList(0, Math.min(5, totalQuizzes)) : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Interface</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<!-- Header Section -->
<header>
    <h1>Admin Dashboard</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
            <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
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

<!-- Dashboard Section -->
<div class="dashboard">
    <h2>Overview</h2>
    <div class="stats">
        <div class="stat-item">
            <h3>Total Users</h3>
            <p><%= totalUsers %></p>
        </div>
        <div class="stat-item">
            <h3>Total Quizzes</h3>
            <p><%= totalQuizzes %></p>
        </div>
    </div>
    <h2>Recent Activities</h2>
    <div class="recent-activities">
        <h3>New Users</h3>
        <ul>
            <% for (Users user : recentUsers) { %>
            <li><%= user.getUsername() %> - Role:
                <%=
                user.getRole_id() == Users.ROLE_ADMIN ? "Admin" :
                        (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                %> - Status:
                <%=
               user.getRole_id() == Users.ROLE_ADMIN ? "Active" :
                        (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                %>
            </li>
            <% } %>
        </ul>
        <h3>New Quizzes</h3>
        <ul>
            <% if (recentQuizzes != null && !recentQuizzes.isEmpty()) { %>
            <% for (quiz q : recentQuizzes) { %>
            <li><%= q.getQuizTitle() %> by Creator ID: <%= q.getCreatorId() %></li>
            <% } %>
            <% } else { %>
            <li>No recent quizzes available.</li>
            <% } %>
        </ul>
    </div>
</div>

<!-- User Management Section -->
<div class="user-management">
    <h2>User Management</h2>
    <div class="user-filters">
        <label for="searchUser">Search Users:</label>
        <input type="text" id="searchUser" placeholder="Search by name or role">
        <button onclick="filterUsers()">Search</button>
        <button onclick="resetSearch()">Reset</button>
        <a href="${pageContext.request.contextPath}/jsp/add_user.jsp" class="button add-user-btn">Add New User</a>
    </div>
    <table>
        <thead>
        <tr>
            <th>User ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Status</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Users user : usersList) { %>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getUsername() %></td>
            <td>
                <%=
                user.getRole_id() == Users.ROLE_ADMIN ? "Admin" :
                        (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                %>
            </td>
            <td>
                <%=
                user.getRole_id() == Users.ROLE_ADMIN ? "Active" :
                        (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
                %>
            </td>
            <td><%= user.getEmail() %></td>
            <td>
                <form action="${pageContext.request.contextPath}/UserServlet" method="post" style="display:inline;">
                    <input type="hidden" name="userId" value="<%= user.getId() %>">
                    <button type="submit" name="action" value="updateRole">Update Role</button>
                    <button type="submit" name="action" value="toggleStatus">
                        <%= user.getRole_id() == Users.ROLE_ADMIN ? "Demote to Teacher" :
                                (user.getRole_id() == Users.ROLE_TEACHER ? "Promote to Student" : "Promote to Admin") %>
                    </button>
                    <button type="submit" name="action" value="deleteUser" onclick="return confirm('Are you sure you want to delete this user?');">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

<!-- Footer Section -->
<footer>
    <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
