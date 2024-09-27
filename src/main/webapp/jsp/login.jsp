<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>
<div class="login-container">
    <h2>Login</h2>
    <form action="<%= request.getContextPath() %>/login" method="post">

        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
        <div class="forgot-password">
            <a href="lostpass.jsp">Forgot Password?</a>
        </div>
    </form>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error-message">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <!-- Add Back to Home Button -->
    <div class="back-home">
        <a href="<%= request.getContextPath() %>/index.jsp" class="btn-back">Back to Home</a>
    </div>
</div>
</body>
</html>
