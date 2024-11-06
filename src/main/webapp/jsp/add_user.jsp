<%@ page import="entity.Users" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New User</title>
    <style>
        /* Resetting margin and padding */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body Styling */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            color: #333;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* Container for Sidebar and Main Content */
        .container {
            display: flex;
            flex: 1;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 250px;
            background-color: #2c3e50;
            color: #ecf0f1;
            padding: 20px;
            min-height: 100vh;
            position: fixed;
        }

        .sidebar h2 {
            margin-bottom: 30px;
            text-align: center;
            font-size: 1.5rem;
        }

        .sidebar nav ul {
            list-style: none;
        }

        .sidebar nav ul li {
            margin: 20px 0;
        }

        .sidebar nav ul li a {
            color: #ecf0f1;
            text-decoration: none;
            font-size: 1.1rem;
            display: block;
            padding: 10px;
            border-radius: 4px;
            transition: background 0.3s;
        }

        .sidebar nav ul li a:hover,
        .sidebar nav ul li.active a {
            background-color: #34495e;
        }

        /* Main Content Styling */
        .main-content {
            margin-left: 250px;
            padding: 20px;
            flex: 1;
        }

        /* Header Section */
        header {
            margin-bottom: 20px;
        }

        header h1 {
            font-size: 2rem;
            color: #2c3e50;
        }

        /* Alerts */
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
            font-size: 0.95rem;
        }

        .alert.success {
            background-color: #d4edda;
            color: #155724;
            border-left: 5px solid #28a745;
        }

        .alert.error {
            background-color: #f8d7da;
            color: #721c24;
            border-left: 5px solid #dc3545;
        }

        /* Overview Cards */
        .overview {
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }

        .card {
            flex: 1;
            min-width: 200px;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .card h3 {
            margin-bottom: 10px;
            color: #2980b9;
            font-size: 1.2rem;
        }

        .card p {
            font-size: 1.5rem;
            color: #34495e;
        }

        /* Recent Activities */
        .recent-activities {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }

        .activity {
            flex: 1;
            min-width: 250px;
            background-color: #ffffff;
            padding: 15px 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .activity h3 {
            margin-bottom: 10px;
            color: #27ae60;
            font-size: 1.1rem;
        }

        .activity ul {
            list-style-type: none;
        }

        .activity ul li {
            margin-bottom: 8px;
            font-size: 0.95rem;
        }

        .activity ul li strong {
            color: #2c3e50;
        }

        /* User Filters */
        .user-filters {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 15px;
            align-items: center;
        }

        .user-filters input {
            flex: 1;
            padding: 8px 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .user-filters button {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            background-color: #3498db;
            color: #ffffff;
            cursor: pointer;
            transition: background 0.3s;
        }

        .user-filters button:hover {
            background-color: #2980b9;
        }

        .add-user-btn {
            background-color: #2ecc71;
            text-decoration: none;
            padding: 8px 12px;
            border-radius: 4px;
            color: #ffffff;
            transition: background 0.3s;
        }

        .add-user-btn:hover {
            background-color: #27ae60;
        }

        /* Form Styling */
        .add-user-form {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #2c3e50;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .form-actions button,
        .form-actions .button {
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            transition: background 0.3s;
        }

        .form-actions button {
            background-color: #3498db;
            color: #ffffff;
        }

        .form-actions button:hover {
            background-color: #2980b9;
        }

        .form-actions .button {
            background-color: #e74c3c;
            color: #ffffff;
            text-decoration: none;
            text-align: center;
        }

        .form-actions .button:hover {
            background-color: #c0392b;
        }

        /* Footer Styling */
        footer {
            background-color: #2c3e50;
            color: #ecf0f1;
            text-align: center;
            padding: 15px 0;
            position: relative;
            bottom: 0;
            width: 100%;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .sidebar {
                width: 200px;
            }

            .main-content {
                margin-left: 200px;
            }

            .overview, .recent-activities {
                flex-direction: column;
            }
        }

        @media (max-width: 576px) {
            .sidebar {
                position: relative;
                width: 100%;
                min-height: auto;
            }

            .main-content {
                margin-left: 0;
            }

            .overview, .recent-activities {
                flex-direction: column;
            }

            .user-filters {
                flex-direction: column;
                align-items: stretch;
            }

            .user-filters input, .user-filters button, .add-user-btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<!-- Sidebar -->
<div class="sidebar">
    <h2>Admin Dashboard</h2>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp" class="${pageContext.request.servletPath.endsWith("admin.jsp") ? "active" : ""}">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp" class="${pageContext.request.servletPath.endsWith("manage_users.jsp") ? "active" : ""}">Manage Users</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp" class="${pageContext.request.servletPath.endsWith("manage_quizzes.jsp") ? "active" : ""}">Manage Quizzes</a></li>
        </ul>
    </nav>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Header Section -->
    <header>
        <h1>Add New User</h1>
    </header>

    <!-- Hiển thị thông báo nếu có -->
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
            <div class="form-actions">
                <button type="submit">Add User</button>
                <a href="${pageContext.request.contextPath}/jsp/manage_users.jsp" class="button">Cancel</a>
            </div>
        </form>
    </div>
</div>

<!-- Footer Section -->
<footer>
    <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
