<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body>
<div class="container">
    <h1>An Error Occurred</h1>
    <% if (request.getAttribute("errorMessage") != null) { %>
    <p><%= request.getAttribute("errorMessage") %></p>
    <% } else { %>
    <p>Sorry, something went wrong. Please try again later.</p>
    <% } %>
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp">Return to Dashboard</a>
</div>
</body>
</html>
