<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.User, model.UserDAO" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Base64" %>

<%
    // Check if the user is logged in
    String username = (String) session.getAttribute("username");
    if (username == null) {
        // Redirect to login page if not logged in
        response.sendRedirect("login.jsp");
        return;
    }

    // Create an instance of UserDAO
    UserDAO userDAO = new UserDAO();

    // Fetch user details from the database using username
    User user = null;
    try {
        user = userDAO.getUserByUsername(username);
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (user == null) {
        // Handle the case where user is not found
        out.println("<p>User not found.</p>");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
    <!-- Link to the profile.css file -->
    <link rel="stylesheet" href="../css/profile.css">
    <!-- Include any other head elements like meta tags, fonts, etc. -->
</head>
<body>
<header>
    <div class="container">
        <h1><a href="index.jsp" class="logo">QuizLoco</a></h1>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="index.jsp#about">About</a></li>
                <li><a href="index.jsp#services">Services</a></li>
                <li><a href="index.jsp#contact">Contact</a></li>
            </ul>
        </nav>
        <!-- Include user authentication links if needed -->
    </div>
</header>

<main>
    <div class="profile-container">
        <h1>User Profile</h1>
        <div class="profile-details">
            <p><strong>Username:</strong> <%= user.getUsername() %></p>
            <p><strong>Full Name:</strong> <%= user.getFullName() %></p>
            <p><strong>Email:</strong> <%= user.getEmail() %></p>
            <p><strong>Gender:</strong> <%= user.getGender() %></p>
            <p><strong>Status:</strong> <%= user.getStatus() %></p>
            <p><strong>Created At:</strong> <%= user.getCreatedAt() %></p>
            <p><strong>Updated At:</strong> <%= user.getUpdatedAt() %></p>
            <!-- Display avatar if available -->
            <%
                byte[] avatarBytes = user.getAvatar();
                if (avatarBytes != null && avatarBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(avatarBytes);
            %>
            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="User Avatar" />
            <%
            } else {
            %>
            <p>No avatar available.</p>
            <%
                }
            %>
        </div>
        <div class="profile-actions">
            <a href="edit-profile.jsp" class="btn btn-edit">Edit Profile</a>
            <!-- Add more actions like changing password, etc. -->
        </div>
    </div>
</main>

<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<!-- Include your JavaScript files if needed -->
<script src="../js/script.js"></script>
</body>
</html>
