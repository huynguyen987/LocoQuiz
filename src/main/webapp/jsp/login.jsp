<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>
<style>
    .error-message {
        color: #ff0026;
        font-size: 14px;
        position: absolute;
        top: 10px;
        right: 10px;
        background-color: #f8d7da;
        border-color: #f5c6cb;
        padding: 10px;
        border-radius: 5px;
    }
</style>
<div class="login-container">
    <h2>Login</h2>
    <form action="<%= request.getContextPath() %>/login" method="post">
        <% String success=(String) session.getAttribute("success"); if (success !=null) { %>
        <div class="error-message">
            <%= success %>
        </div>
        <% session.removeAttribute("success"); } %>
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
        <div class="forgot-password">
            <a href=<%= request.getContextPath() %>/jsp/lostpass.jsp>Forgot Password?</a>
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
