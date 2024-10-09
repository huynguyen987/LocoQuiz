<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users" %>
<%
    session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");
    if (currentUser == null || !currentUser.hasRole("admin")) {
        response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
<h1>Welcome, <%= currentUser.getUsername() %>!</h1>
<!-- Admin-specific content goes here -->
<a href="<%= request.getContextPath() %>/logout">Logout</a>
</body>
</html>
